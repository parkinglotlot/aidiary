
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

function getDiariesFromBack(array,realDiaryList){
    // let list = '';
    let tBody = document.createElement("tbody");
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

function getButtonlist(startPage, endPage, pagination){

    for(let i = startPage; i <= endPage; i++){
        let buttonTag = document.createElement("button");
        buttonTag.innerHTML = i;
        pagination.appendChild(buttonTag);
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
        pagination        = getButtonlist(paginationObj.startPage,
                                          paginationObj.endPage,
                                          pagination);
    });
}

// 실제 실행 시점
document.addEventListener("DOMContentLoaded",function(){


   realDiaryList.className = "diary-table";

   // realDiaryList.InnerHTML = "<>"

    realDiaryList.insertAdjacentHTML("beforeend",header);





    //다이어리 리스트 가져오기 (초기화)
    readDiaries(1,10);

})
