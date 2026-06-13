let logoutBtn; //로그아웃 버튼
let diaryMenus; //다이어리 메뉴



// 실제 실행 시점
document.addEventListener("DOMContentLoaded", function(){
    logoutBtn = document.getElementById("logOutBtn");

    diaryMenus = document.querySelectorAll(".diaryMenu");



    logoutBtn.addEventListener("click",function(){
        window.fetch("/user/logOut",{
            method: "POST"
        }).then(function(data){
            console.log("Logged Out! " + data);
            location.href = "/";
        }).catch(function(data){
            console.log(data);
            Swal.fire({
                          title : "로그아웃 실패",
                          icon: "error"
                      })
        })
    })

    //다이어리 목록 이동
    diaryMenus.forEach(function(diaryMenu){
        diaryMenu.addEventListener("click",function(){
            window.location.href = "/diary/goHome";
        })
    })



})
