// table body element (표 body 요소)
let userTableBody;

let curPage = 1; // 현재 페이지 (초기화)
const pageSize = 10; // 페이지당 표시할 항목 수 (초기화)

let pagination; // 페이지네이션

let startPage; // 현재 블럭(범위)에서 startPage
let endPage; // 현재 블럭(범위)에서 endPage

let resultData; // fetch로 가져온 데이터 저장

let searchKeyword; // 검색 키워드 input element

let filter = ""; // 검색 키워드 (초기화)

let userStatus = ""; // 사용자 상태 (초기화)

let userList; //사용자 목록

let detailButton; // 사용자 상세 페이지 버튼

let pageBtnList; // 버튼 리스트(페이지 버튼)

let paginationFirst;

let paginationPrev;

let paginationLast;

let paginationNext;

let btnSearch; // 조회버튼

let totalUserCount; //총 사용자 수

let btnReset; //리셋버튼

// 검색 필터 설정
function setSearchFilter() {
  filter = document.getElementById("searchKeyword").value;
  userStatus = document.getElementById("userStatus").value;
}

//검색 필터 초기화
function resetSearchFilter() {
  document.getElementById("searchKeyword").value = "";
  document.getElementById("userStatus").value = "";
  filter = "";
  userStatus = "";
}

// 페이지 번호 버튼 제외한 버튼들의 상태 설정 (맨처음 / 맨끝 / 이전 / 다음 )
function getUtilButtonStatus() {
  if (curPage === 1) {
    paginationFirst.disabled = true;
    paginationPrev.disabled = true;
  } else {
    paginationFirst.disabled = false;
    paginationPrev.disabled = false;
  }

  if (curPage === pagination.totalPage) {
    paginationLast.disabled = true;
    paginationNext.disabled = true;
  } else {
    paginationLast.disabled = false;
    paginationNext.disabled = false;
  }
}

function getButtonList(startPage, endPage, paginationPrev) {
  //버튼 갱신
  document.querySelectorAll("button[data-page]").forEach((btn) => btn.remove());
  let fragment = document.createDocumentFragment();
  // console.log("paginationPrev: ", paginationPrev);
  // console.log("fragment : ", fragment);

  for (let i = startPage; i <= endPage; i++) {
    let pageBtn = document.createElement("button");
    pageBtn.classList.add("pagination-btn");
    pageBtn.dataset.page = i; // 페이지 번호 설정
    pageBtn.textContent = i;

    if (i === Number(curPage)) {
      pageBtn.classList.add("active"); // 현재 페이지 버튼에 active 클래스 추가
      console.log("pageBtn:", pageBtn);
    } else {
      pageBtn.classList.remove("active"); // 현재 페이지 버튼이 아니면 active 클래스 제거
    }

    fragment.appendChild(pageBtn);
  }

  paginationPrev.after(fragment);
}

// 사용자 목록 가져오기
function getUserList(curPage, pageSize, filter, userStatus) {
  //기존 tbody 내용 제거
  userTableBody.innerHTML = "";
  //페이지 번호 버튼 제거

  let query = new URLSearchParams({
    curPage: curPage,
    pageSize: pageSize,
    filter: filter,
    filter2: userStatus,
  });

  window
    .fetch(`/userAuth/userAuthUser?${query.toString()}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
    .then(function (data) {
      resultData = data.json();
      //   console.log(curPage, pageSize, filter, userStatus);

      console.log(resultData);
      return resultData;
    })
    .then(function (result) {
      console.log("result", result);
      //   let tr = document.createElement("tr");
      console.log("result.data", result.data);

      userList = result.data.list; // 사용자 목록
      pagination = result.data.pagination; // 페이지네이션

      for (const element of userList) {
        //로그인 아이디로 세팅
        let tr = document.createElement("tr");
        tr.id = element.id; // tr의 id를 사용자 id로 설정
        tr.innerHTML = `
          <td>${element.ser}</td>
          <td>${element.loginId}</td>
          <td>${element.fullName}</td>
          <td>${element.email}</td>
          <td>${element.birthday}</td>
          <td>${element.state}</td>
          <td>
            <button class="table-action-btn btn-user-detail" data-user-id="${element.id}">상세</button>
          </td>
        `;
        userTableBody.appendChild(tr);
      }
      // 총 사용자 수 표시
      totalUserCount.innerHTML = pagination.totalCnt;

      // 페이징 버튼 추가
      startPage = pagination.startPage;
      endPage = pagination.endPage;
      getButtonList(startPage, endPage, paginationPrev);
      getUtilButtonStatus(); // 페이지 번호 버튼 제외한 버튼들의 상태 설정 (맨처음 / 맨끝 / 이전 / 다음 )
    });
}

document.addEventListener("DOMContentLoaded", function () {
  userTableBody = document.getElementById("userTableBody");
  filter = document.getElementById("searchKeyword").value;
  userStatus = document.getElementById("userStatus").value;
  paginationFirst = document.getElementsByClassName("pagination-first")[0];
  paginationPrev = document.getElementsByClassName("pagination-prev")[0];
  paginationNext = document.getElementsByClassName("pagination-next")[0];
  paginationLast = document.getElementsByClassName("pagination-last")[0];
  searchKeyword = document.getElementById("searchKeyword");
  totalUserCount = document.getElementById("totalUserCount");
  btnReset = document.getElementById("btnReset");

  btnSearch = document.getElementById("btnSearch");

  // 사용자 목록 가져오기(초기화)
  getUserList(curPage, pageSize, filter, userStatus);

  // console.log("pageBtnList:", pageBtnList);

  //조회 버튼 클릭 -> 사용자 목록 가져오기
  btnSearch.addEventListener("click", function (e) {
    e.preventDefault();
    curPage = 1; // 조회 버튼 클릭 시 현재 페이지를 1로 초기화
    setSearchFilter(); // 검색 필터 설정
    getUserList(curPage, pageSize, filter, userStatus);
    console.log("filter:", filter);
    console.log("userStatus:", userStatus);
  });

  //enter key 입력 시 조회 버튼 클릭 이벤트 발생
  searchKeyword.addEventListener("keypress", function (e) {
    if (e.key == "Enter") {
      // e.preventDefault();
      btnSearch.click();
    }
  });

  //리셋 버튼 클릭 -> 검색 필터 초기화 및 사용자 목록 가져오기
  btnReset.addEventListener("click", function (e) {
    e.preventDefault();
    resetSearchFilter(); // 검색 필터 초기화
    curPage = 1; // 현재 페이지를 1로 초기화
    getUserList(curPage, pageSize, filter, userStatus); // 사용자 목록 가져오기
  });

  // pageBtnList 클릭 (번호 버튼) -> 사용자 목록 업데이트
  document.addEventListener("click", function (e) {
    // e.preventDefault();
    let btn = e.target.matches("button[data-page]") ? e.target : null;

    if (btn !== null) {
      curPage = Number(btn.dataset.page); //클릭한 버튼의 페이지 번호를 curPage로 설정
      setSearchFilter(); // 검색 필터 설정
      getUserList(curPage, pageSize, filter, userStatus);
    }
  });

  //paginationPrev 클릭 -> 이전 페이지 블록으로 이동
  paginationPrev.addEventListener("click", function (e) {
    e.preventDefault();
    if (curPage > 1) {
      curPage = curPage - 1; // 이전 페이지로 이동
      setSearchFilter(); // 검색 필터 설정
      getUserList(curPage, pageSize, filter, userStatus); // 사용자 목록 업데이트
    }
  });

  //paginationNext 클릭 -> 다음 페이지 블록으로 이동
  paginationNext.addEventListener("click", function (e) {
    e.preventDefault();
    if (curPage < pagination.totalPage) {
      curPage = curPage + 1; // 다음 페이지로 이동
      setSearchFilter(); // 검색 필터 설정
      getUserList(curPage, pageSize, filter, userStatus); // 사용자 목록 업데이트
    }
  });

  //paginationFirst 클릭 -> 첫 페이지로 이동
  paginationFirst.addEventListener("click", function (e) {
    e.preventDefault();
    if (curPage > 1) {
      curPage = 1; // 첫 페이지로 이동
      setSearchFilter(); // 검색 필터 설정
      getUserList(curPage, pageSize, filter, userStatus); // 사용자 목록 업데이트
    }
  });

  //paginationLast 클릭 -> 마지막 페이지로 이동
  paginationLast.addEventListener("click", function (e) {
    e.preventDefault();
    if (curPage < pagination.totalPage) {
      curPage = pagination.totalPage; // 마지막 페이지로 이동
      setSearchFilter(); // 검색 필터 설정
      getUserList(curPage, pageSize, filter, userStatus); // 사용자 목록 업데이트
    }
  });
});
