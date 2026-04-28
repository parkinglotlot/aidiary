//로그인
function login(loginId,password){
    $.ajax({
        type: "POST",
        url: "/user/login",
        body : {
            loginId : loginId,
            password : password
        },
        success: function(data){
            console.log(data);
            alert("로그인 완료");
            location.href = "diary/goHome"
        },
        fail:function(data){
            console.log(data);
        }
           })
}

let loginBtn = document.getElementById("loginBtn");

loginBtn.addEventListener("click",login);