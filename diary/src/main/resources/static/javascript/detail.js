const id = window.location.href.split("/").pop(); //다이어리 아이디

//다이어리 제목
let diaryTitle;

//다이어리 날짜
let diaryDate;

//다이어리 안에 들어갈 내용
let diaryContent;

//다이어리 작성자
let diaryWriter;

//다이어리 기분 상태
let diaryMood;

// 수정 버튼
let btnEdit;

// 저장 버튼
let btnSave;

// 삭제 버튼
let btnDelete;

//모드설정
let mode;

//수정버튼 클래스 리스트
let editClassList;

//저장버튼 클래스 리스트
let saveClassList;

// ai 감정분석 버튼
let btnAnalyzeEmotion;

// ai 감정분석 결과
let aiComment;

//URL에서 모드 추출
function getModeFromUrl() {
  let path = location.pathname;
  if (path.includes("create")) {
    mode = "CREATE";
  } else {
    mode = "READ";
    // EDIT -> 항상 READ  후에 EDIT 버튼을 누르면 변함
  }
}

//Read일때 가져올 데이터
function readFunction(id) {
  window
    .fetch(`/diary/readDetail/${id}`, {
      method: "GET",
    })
    .then((response) => response.json())
    .then(function (result) {
      console.log(result);

      diaryTitle.innerText = result.data.title;
      diaryDate.innerText = result.data.date;
      diaryContent.innerText = result.data.content;
      diaryWriter.innerText = result.data.loginId; //작성자 아이디 세팅
      saveClassList.add("hidden"); //저장버튼 비활성화
      editClassList.remove("hidden"); //수정버튼 활성화
      diaryTitle.setAttribute("contenteditable", false); // editable false
      diaryContent.setAttribute("contenteditable", false); // editable false
      aiComment.innerText = result.data.aiAnalysis; //감정분석 결과 세팅
    })
    .catch(function (error) {
      console.log(error);
    });
}

//CREATE일때 모드 변경
function createModeFUnction() {
  diaryTitle.innerText = ""; // 제목 초기화
  diaryContent.innerText = ""; // 내용 초기화

  diaryTitle.setAttribute("contenteditable", true); // editable true
  diaryContent.setAttribute("contenteditable", true); // editable true

  editClassList.add("hidden"); // 수정버튼 비활성화
  saveClassList.remove("hidden"); //저장버튼 활성화
}

//CREATE일때 가져올 데이터
function createFunction() {
  window
    .fetch(`/diary/create`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: diaryTitle.innerText,
        content: diaryContent.innerText,
      }),
    })
    .then(function (response) {
      console.log(response);
      return response.json();
    })
    .then(function (result) {
      console.log("result:", result);
      swal
        .fire({
          title: "등록 성공",
          text: "등록되었습니다",
          type: "success",
          icon: "success",
          timer: 2000,
        })
        .then(function () {
          //등록 후 상세페이지 모드 변경
          mode = "READ";
          location.href = `/diary/detail/read/${result.data}`; //등록 후 상세페이지 이동
        });
    })
    .catch(function (error) {
      swal.fire({
        title: "등록 실패",
        text: "등록에 실패했습니다.",
        type: "error",
        icon: "error",
      });
    });
}

//EDIT
function editFunction() {
  //   alert("!");
  diaryTitle.setAttribute("contenteditable", true); // editable true
  diaryContent.setAttribute("contenteditable", true); // editable true
  editClassList.add("hidden"); // 수정버튼 비활성화
  saveClassList.remove("hidden"); //저장버튼 활성화
}

// // Delete 버튼
function deleteFunction(id) {
  console.log(diaryWriter.innerText);
  window
    .fetch(`/diary/delete/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id: id,
        loginId: diaryWriter.innerText,
      }),
    })
    .then(function (response) {
      console.log(response.json());
    })
    .then(function (result) {
      console.log(result);
      swal
        .fire({
          title: "삭제 성공",
          text: "삭제되었습니다",
          type: "success",
          timer: 2000,
        })
        .then(function () {
          // 삭제 후 다이어리 목록 페이지로 이동
          window.location.href = "/diary/goHome";
        });
    })
    .catch(function (error) {
      console.log(error);
      swal.fire({
        title: "삭제 실패",
        text: "삭제에 실패했습니다.",
        type: "error",
        icon: "error",
      });
    });
}

function saveFunction(id) {
  window
    .fetch(`/diary/modify/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: diaryTitle.innerText,
        content: diaryContent.innerText,
      }),
    })
    .then(function (response) {
      return response.json();
    })
    .then(function (result) {
      console.log(result);
      swal.fire({
        title: "수정 성공",
        text: "수정되었습니다",
        type: "success",
        icon: "success",
      });
      // 수정 후 READ 모드로 돌아가기
      mode = "READ";
      readFunction(id);
    })
    .catch(function (error) {
      console.log(error);
      swal.fire({
        title: "수정 실패",
        text: "수정에 실패했습니다.",
        type: "error",
        icon: "error",
      });
    });
}

function analyzeEmtion() {
  window
    .fetch("/api/gpt/call", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        content: diaryContent.innerText,
      }),
    })
    .then(function (response) {
      return response.json();
    })
    .then(function (result) {
      console.log(result);
      aiComment.innerText = result.data;
    })
    .catch(function (error) {
      console.log(error);
      swal.fire({
        title: "분석 실패",
        text: "분석에 실패했습니다.",
        type: "error",
        icon: "error",
      });
    });
}

document.addEventListener("DOMContentLoaded", function () {
  console.log(id);

  diaryTitle = document.getElementById("diaryTitle");
  diaryDate = document.getElementById("diaryCreatedAt");
  diaryContent = document.getElementById("diaryContent");
  diaryWriter = document.getElementById("diaryWriter");
  diaryMood = document.getElementById("diaryMood");
  btnEdit = document.getElementById("btnEdit");
  btnSave = document.getElementById("btnSave");
  btnDelete = document.getElementById("btnDelete");
  editClassList = btnEdit.classList;
  saveClassList = btnSave.classList;
  btnAnalyzeEmotion = document.getElementById("btnAnalyzeEmotion");
  aiComment = document.getElementById("aiComment");

  // 모드 설정
  getModeFromUrl();

  // 읽기 모드이면 해당되는 데이터 가져오기
  if (mode === "READ") {
    readFunction(id);
  }

  // 생성 모드이면 저장버튼 활성화
  if (mode === "CREATE") {
    createModeFUnction();
  }

  // 수정 버튼 누르면 EDIT 모드로
  btnEdit.addEventListener("click", function (e) {
    if (e.target && e.target.id === "btnEdit") {
      mode = "EDIT";
      // edit mode로 변하기
      editFunction();
    }
  });

  //저장버튼 누르면 저장
  btnSave.addEventListener("click", function () {
    if (mode === "CREATE") {
      createFunction();
    } else {
      saveFunction(id);
    }
  });

  //삭제버튼 누르면 저장
  btnDelete.addEventListener("click", function (e) {
    e.preventDefault();
    deleteFunction(id);
  });
  // document.getElementById("btnEdit")

  // ai 감정분석 버튼 클릭 이벤트
  btnAnalyzeEmotion.addEventListener("click", function () {
    // ai 감정분석 로직 구현
    analyzeEmtion();
  });
});
