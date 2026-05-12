
// 실제 실행 시점
document.addEventListener("DOMContentLoaded", function(){
    let logoutBtn = document.getElementById("logOutBtn");

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

})
