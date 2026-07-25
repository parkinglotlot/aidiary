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

//모드설정
let mode;

//수정버튼 클래스 리스트
let editClassList;

//저장버튼 클래스 리스트
let saveClassList;

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
      diaryWriter.innerText = result.data.writer;
      saveClassList.add("hidden"); //저장버튼 비활성화
      editClassList.remove("hidden"); //수정버튼 활성화
      diaryTitle.setAttribute("contenteditable", false); // editable false
      diaryContent.setAttribute("contenteditable", false); // editable false
    })
    .catch(function (error) {
      console.log(error);
    });
}

//EDIT일 때
function editFunction() {
  //   alert("!");
  diaryTitle.setAttribute("contenteditable", true); // editable true
  diaryContent.setAttribute("contenteditable", true); // editable true
  editClassList.add("hidden"); // 수정버튼 비활성화
  saveClassList.remove("hidden"); //저장버튼 활성화
}

document.addEventListener("DOMContentLoaded", function () {
  const id = window.location.href.split("/").pop(); //다이어리 아이디
  console.log(id);

  diaryTitle = document.getElementById("diaryTitle");
  diaryDate = document.getElementById("diaryCreatedAt");
  diaryContent = document.getElementById("diaryContent");
  diaryWriter = document.getElementById("diaryWriter");
  diaryMood = document.getElementById("diaryMood");
  btnEdit = document.getElementById("btnEdit");
  btnSave = document.getElementById("btnSave");
  editClassList = btnEdit.classList;
  saveClassList = btnSave.classList;

  // 모드 설정
  getModeFromUrl();

  // 읽기 모드이면 해당되는 데이터 가져오기
  if (mode === "READ") {
    readFunction(id);
  }

  // 수정 버튼 누르면 EDIT 모드로
  btnEdit.addEventListener("click", function (e) {
    if (e.target && e.target.id === "btnEdit") {
      mode = "EDIT";
      // edit mode로 변하기
      editFunction();
    }
  });

  // document.getElementById("btnEdit")
});
