// table body element (표 body 요소)
let userTableBody;

let curPage = 1; // 현재 페이지 (초기화)
const pageSize = 10; // 페이지당 표시할 항목 수 (초기화)

let pagination; // 페이지네이션

let startPage; // 현재 블럭(범위)에서 startPage
let endPage; // 현재 블럭(범위)에서 endPage

let resultData; // fetch로 가져온 데이터 저장

let searchKeyword = ""; // 검색 키워드 (초기화)

let userStatus = ""; // 사용자 상태 (초기화)

let userList; //사용자 목록

let detailButton; // 사용자 상세 페이지 버튼

let pageBtnList; // 버튼 리스트(페이지 버튼)

let paginationFirst;

let paginationPrev;

let paginationLast;

let paginationNext;

let btnSearch; // 조회버튼

function getButtonList(startPage, endPage, paginationPrev) {
  //버튼 갱신
  document.querySelectorAll("button[data-page]").forEach((btn) => btn.remove());
  let fragment = document.createDocumentFragment();
  // console.log("paginationPrev: ", paginationPrev);
  // console.log("fragment : ", fragment);

  for (let i = startPage; i <= endPage; i++) {
    let pageBtn = document.createElement("button");
    pageBtn.classList.add("table-action-btn", "btn-user-detail");
    pageBtn.dataset.page = i; // 페이지 번호 설정
    pageBtn.textContent = i;
    fragment.appendChild(pageBtn);
  }

  paginationPrev.after(fragment);
}

// 사용자 목록 가져오기
function getUserList(curPage, pageSize, searchKeyword, userStatus) {
  //기존 tbody 내용 제거
  userTableBody.innerHTML = "";
  //페이지 번호 버튼 제거

  let query = new URLSearchParams({
    curPage: curPage,
    pageSize: pageSize,
    filter: searchKeyword,
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
      //   console.log(curPage, pageSize, searchKeyword, userStatus);

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
      // 페이징 버튼 추가
      startPage = pagination.startPage;
      endPage = pagination.endPage;
      getButtonList(startPage, endPage, paginationPrev);
    });
}

document.addEventListener("DOMContentLoaded", function () {
  userTableBody = document.getElementById("userTableBody");
  searchKeyword = document.getElementById("searchKeyword").value;
  userStatus = document.getElementById("userStatus").value;
  paginationFirst = document.getElementsByClassName("pagination-first")[0];
  paginationPrev = document.getElementsByClassName("pagination-prev")[0];
  paginationNext = document.getElementsByClassName("pagination-next")[0];
  paginationLast = document.getElementsByClassName("pagination-last")[0];
  pageBtnList = document.querySelectorAll("button[data-page]");
  btnSearch = document.getElementById("btnSearch");

  // 사용자 목록 가져오기(초기화)
  getUserList(curPage, pageSize, searchKeyword, userStatus);

  //조회 버튼 클릭 -> 사용자 목록 가져오기
  btnSearch.addEventListener("click", function (e) {
    e.preventDefault();
    searchKeyword = document.getElementById("searchKeyword").value;
    userStatus = document.getElementById("userStatus").value;
    getUserList(curPage, pageSize, searchKeyword, userStatus);
    console.log("searchKeyword:", searchKeyword);
    console.log("userStatus:", userStatus);
  });

  // pageBtnList 클릭 -> 사용자 목록 업데이트
  pageBtnList.forEach(function (btn) {
    btn.addEventListener("click", function (e) {
      getUserList(curPage, pageSize, searchKeyword, userStatus);
    });
  });
});
