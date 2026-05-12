//로그인
function login(loginId,password){
    // $.ajax({
    //            type: "POST",
    //            url: "/user/login",
    //            contentType: "application/json; charset=utf-8", // 요청본문타입
    //            data : JSON.stringify({
    //                                      loginId : loginId,
    //                                      password : password
    //                                  }),
    //            processData: false, // 중요 : jQuery가 data를 건드리지 못하게
    //            success : function(data){
    //                console.log(data);
    //                alert("로그인 완료");
    //                location.href = "/diary/goHome";
    //            },
    //            error : function(dasdta){
    //                console.log(data);
    //            }
    //        })
    window.fetch("/user/login",{
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            loginId:loginId,
            password:password
                             })
    }).then(function(res){
        if(!res.ok) throw new Error("서버 응답 에러:"+res.status);
        return res.json();
    }).then(function(response){
        console.log(response);
        // alert("로그인 완료");
        Swal.fire({
            title: "로그인 완료",
            text: "다이어리 페이지로 이동합니다.",
            icon: "success"
                  })
        setTimeout(function(){
            location.href = "/diary/goHome"
        },1500)

    }).catch(function(error){
        console.log(error);
        Swal.fire({
                      title: "로그인 실패",
                      text: "다시 시도해주세요.",
                      icon: "error"
                  })

    })
    return null;
}



document.addEventListener("DOMContentLoaded",function(){
    let loginBtn = document.getElementById("loginBtn");
    let loginId = document.getElementById("loginId");
    let password = document.getElementById("password");

    loginBtn.addEventListener("click",function(){
        login(loginId.value,password.value);
    })

})
