
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

document.addEventListener("DOMContentLoaded",function(){

    const id = window.location.href.split("/").pop(); //다이어리 아이디
    // console.log(id);

    diaryTitle = document.getElementById("diaryTitle");
    diaryDate = document.getElementById("diaryCreatedAt");
    diaryContent = document.getElementById("diaryContent");
    diaryWriter = document.getElementById("diaryWriter");
    diaryMood = document.getElementById("diaryMood");

    window.fetch(`/diary/returnDetail/${id}`, {
        method: "GET"
    })
    .then(response=> response.json())
          .then(function(result){
              console.log(result);

              diaryTitle.innerText = result.data.title;
              diaryDate.innerText = result.data.date;
              diaryContent.innerText = result.data.content;
              diaryWriter.innerText = result.data.content;
    })
          .catch(function(error){
        console.log(error);
    })
})