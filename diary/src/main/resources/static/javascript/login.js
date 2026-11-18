//로그인
function login(loginId, password) {
  axios
    .post("/user/login", {
      loginId: loginId,
      password: password,
    })
    .then(function (response) {
      console.log(response);
      // alert("로그인 완료");
      if (response.status === 200) {
        Swal.fire({
          title: "로그인 완료",
          text: "다이어리 페이지로 이동합니다.",
          icon: "success",
        });

        setTimeout(function () {
          window.location.href = "/goHome";
        }, 1500);
      }
    })
    .catch(function (error) {
      console.log(error);
      Swal.fire({
        title: "로그인 실패",
        text: "다시 시도해주세요.",
        icon: "error",
      });
    });
  return null;
}

document.addEventListener("DOMContentLoaded", function () {
  let loginBtn = document.getElementById("loginBtn");
  let loginId = document.getElementById("loginId");
  let password = document.getElementById("password");

  loginBtn.addEventListener("click", function () {
    login(loginId.value, password.value);
  });
});
