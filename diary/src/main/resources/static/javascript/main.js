
// 실제 실행 시점
document.addEventListener("DOMContentLoaded",function(){

   let diaryList =  document.getElementById("diaryList");

   let realDiaryList = document.createElement("ul");
   // realDiaryList.InnerHTML = "<>"

    //다이어리 리스트 가져오기
    axios.get("/diary/readCustom",{
      method: "GET",
      headers: {
          "Content-Type": "application/json"
      },
        params: {
            curPage : 1,
            pageSize : 10
        }
    }).then(function(response){
        if(!response.ok){
            swal.fire({
                title: "에러 발생",
                text: "데이터를 불러올 수 없습니다.",
                type: "error"
                      })
        }
        console.log(response);
    }).then(function(response){
        console.log(response);

    })
})
