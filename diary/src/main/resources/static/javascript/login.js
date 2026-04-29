//로그인
function login(loginId,password){
    $.ajax({
        type: "POST",
        url: "/user/login",
        data : {
            loginId : loginId,
            password : password
        },
        success : function(data){
            console.log(data);
            alert("로그인 완료");
            location.href = "/diary/goHome"
        },
        error : function(data){
            console.log(data);
        }
           })
    return null;
}

let loginBtn = document.getElementById("loginBtn");
let loginId = document.getElementById("loginId");
let password = document.getElementById("password");

loginBtn.addEventListener("click",login(loginId,password));