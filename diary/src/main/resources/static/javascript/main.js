
// 다이어리 리스트 불러오기
let diaryList =  document.getElementById("diaryList");
let realDiaryList = document.createElement("table");
let header = '<thead>\n' +
             '<tr>\n' +
             '  <th>제목</th>\n' +
             '  <th>작성자</th>\n' +
             '  <th>날짜</th>\n' +
             '</tr>\n' +
             '</thead>\n' +
             '\n';

//페이지네이션 선언
let pagination = document.getElementById("pagination");

//현재 페이지 (초기화)
let curPage = 1;

//페이지 사이즈
const pageSize = 10;

//전체 게시글 수
let totalCnt = 0;

function getDiariesFromBack(array,realDiaryList){
    // let list = '';
    let tBody = document.createElement("tbody");
    tBody.id = "tBody";

    array.forEach(function(item){
        let trTag = document.createElement("tr");
        tBody.append(trTag);
        console.log(item.title);
        let td1 = document.createElement("td");
        td1.innerHTML = item.title;
        let td2 = document.createElement("td");
        td2.innerHTML = item.writer;
        // console.log(trTag);
        let td3 = document.createElement("td");
        td3.innerHTML = item.date;
        trTag.appendChild(td1); // 제목추가
        trTag.appendChild(td2); // writer 추가
        trTag.appendChild(td3); // date 추가

    })
    // console.log(list);
    realDiaryList.appendChild(tBody);
    return realDiaryList;
}

// 버튼 리스트 가져오기
function getButtonlist(startPage, endPage, pagination){

    // 가장 왼쪽 버튼(가장 처음 이동)
    let firstButton = document.createElement("button");
    firstButton.id = "firstButton";
    firstButton.innerHTML = "<<"
    pagination.appendChild(firstButton);

    //이전 버튼(한칸 이전 이동)
    let beforeButton = document.createElement("button");
    beforeButton.id = "beforeButton";
    beforeButton.innerHTML = "<"
    pagination.appendChild(beforeButton);

    //페이지네이션 버튼
    for(let i = startPage; i <= endPage; i++){
        let buttonTag = document.createElement("button");
        buttonTag.innerHTML = i;
        buttonTag.className = "buttonTag";
        buttonTag.id = "buttonTag" + i;
        if(i === curPage){
            buttonTag.classList.add("active");
        }else{
            buttonTag.classList.remove("active");
        }

        pagination.appendChild(buttonTag);
    }

    //다음 버튼(한칸 이후 이동)
    let afterButton = document.createElement("button");
    afterButton.id = "afterButton";
    afterButton.innerHTML = ">"
    pagination.appendChild(afterButton);

    // 가장 끝 이동
    let endButton = document.createElement("button");
    endButton.id = "endButton";
    endButton.innerHTML = ">>"
    pagination.appendChild(endButton);

    //조건 별로 클래스 넣어주기
    //1. curPage == 1
    if(curPage === 1){
        firstButton.classList.add("disabled");
        beforeButton.classList.add("disabled");
    }else {
        firstButton.classList.remove("disabled");
        beforeButton.classList.remove("disabled");
    }

    //2. curPage == Math.ceil(totalCnt / pageSize)
    if(curPage === Math.ceil(totalCnt / pageSize)){
        afterButton.classList.add("disabled");
        endButton.classList.add("disabled");
    }else{
        afterButton.classList.remove("disabled");
        endButton.classList.remove("disabled");
    }

    return pagination;
}

//다이어리 목록 불러오기
function readDiaries(curPage,pageSize) {
    axios.get("/diary/readCustom", {
        headers: {
            "Content-Type": "application/json"
        },
        params : {
            curPage : curPage,
            pageSize: pageSize
        }
    }).then(function (response) {
        if (response.status !== 200) {
            swal.fire({
                          title: "에러 발생",
                          text : "데이터를 불러올 수 없습니다.",
                          type : "error"
                      });
        }
        console.log(response);
        console.log("response.data", response.data);
        let arrayList = response.data.data.list;

        realDiaryList = getDiariesFromBack(arrayList, realDiaryList);
        // console.log("realDiaryList",realDiaryList);
        diaryList.appendChild(realDiaryList);

        //startPage ~ endPage 버튼 리스트
        // 페이지네이션 불러오기
        let paginationObj = response.data.data.pagination;
        getButtonlist(paginationObj.startPage,
                                          paginationObj.endPage,
                                          pagination);

        totalCnt = paginationObj.totalCnt;
    });
}

// 실제 실행 시점
document.addEventListener("DOMContentLoaded",function(){

    realDiaryList.className = "diary-table";

   // realDiaryList.InnerHTML = "<>"

    realDiaryList.insertAdjacentHTML("beforeend",header);

    //다이어리 리스트 가져오기 (초기화)
    readDiaries(curPage,pageSize);

    //해당 버튼 클릭 시
    pagination.addEventListener("click",function(e){

        if(e.target.querySelectorAll("button")){

            realDiaryList.innerHTML = ""; //다이어리 리스트 초기화
            pagination.innerHTML = ""; //페이지네이션 초기화
            realDiaryList.insertAdjacentHTML("beforeend",header); // 테이블 헤더만 넣기
            alert("button");
            //1. 숫자 버튼일때
            if(e.target.classList.contains("buttonTag")){

                let id = e.target.id; // 클릭한 대상객체 id
                curPage = Number(id.replace(/\D/g, ""));
            } else if (e.target.id === "firstButton"){
                //2. 가장 처음 이동 버튼일 때
                curPage = 1;
            } else if (e.target.id === "endButton"){
                //3. 가장 끝 이동 버튼일때
                curPage = Math.ceil(totalCnt/10);
            }else if(e.target.id === "afterButton"){
                //한칸 이동 버튼 일때
                // 가장 끝 페이지 아니면 그대로

                curPage = curPage !== Math.ceil(totalCnt/10)? curPage + 1 : curPage;
            }else if (e.target.id === "beforeButton"){
                //한칸 이동 버튼 일때(이전)
                //첫페이지 아니면 그대로
                curPage = curPage !== 1? curPage - 1 : curPage;
            }
            console.log("curPage",curPage);
            readDiaries(curPage,pageSize);
        }

    })



})
