function getDiariesFromBack(array,realDiaryList){
    // let list = '';
    array.forEach(function(item){
        let liTag = document.createElement("li");
        console.log(item.title);
        liTag.innerHTML = item.title;
        console.log(liTag);
        realDiaryList.appendChild(liTag);
    })
    // console.log(list);
    return realDiaryList;
}

// 실제 실행 시점
document.addEventListener("DOMContentLoaded",function(){

   let diaryList =  document.getElementById("diaryList");

   let realDiaryList = document.createElement("ul");
   // realDiaryList.InnerHTML = "<>"

    //다이어리 리스트 가져오기
    axios.get("/diary/readCustom",{
      headers: {
          "Content-Type": "application/json"
      },
        params: {
            curPage : 1,
            pageSize : 10
        }
    }).then(function(response){
        if(response.status !== 200){
            swal.fire({
                title: "에러 발생",
                text: "데이터를 불러올 수 없습니다.",
                type: "error"
                      })
        }
        console.log(response);
        console.log("response.data",response.data);
        let arrayList = response.data.data.list;

        realDiaryList = getDiariesFromBack(arrayList,realDiaryList);
        // console.log("realDiaryList",realDiaryList);
        diaryList.appendChild(realDiaryList);
    })
})
