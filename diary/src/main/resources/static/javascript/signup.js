let loginId; //  로그인 아이디
let password; // password
let verifyPassword; //password  확인
let email;
let emailCode;
let emailCodeResponse;
let phone;
let birthday;

let isEmailVerified;

const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;

let btnSendEmail; //인증번호 발송 버튼
let btnVerifyEmail; // 이메일 인증 확인 버튼

// 이메일 인증 요청
function sendEmail(email) {
  // console.log(email);
  if (
    email == null ||
    email == "undefined" ||
    email.trim() == "" ||
    !emailRegex.test(email)
  ) {
    swal.fire({
      title: "이메일 전송 실패",
      text: "이메일 주소를 다시 확인해주세요",
      type: "error",
    });
    return;
  }
  window
    .fetch("/sendMail", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: email,
      }),
    })
    .then(function (response) {
      return response.text();
    })
    .then(function (result) {
      // console.log("result", result);
      swal.fire({
        title: result,
        text: "해당 주소에서 인증번호를 확인해주세요.",
        type: "success",
      });
    })
    .catch(function (error) {
      console.log("error", error);
    });
}

//이메일 인증요청 완료
function verifyEmail(emailCode) {
  if (emailCode == null || emailCode == "undefined" || emailCode.trim() == "") {
    swal.fire({
      title: "코드를 다시 입력해주세요",
      type: "error",
    });
    return;
  }
  window
    .fetch("/verifyEmail", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        verifyNumber: emailCode,
      }),
    })
    .then(function (response) {
      return response.text();
    })
    .then(function (result) {
      swal.fire({
        title: "result",
        type: "Success",
      });
      isEmailVerified = true; // 전역 이메일 확인여부 true로
    })
    .catch(function (error) {
      swal.fire({
        title: "error",
        type: "Error",
      });
    });
}

document.addEventListener("DOMContentLoaded", function () {
  loginId = document.getElementById("loginId").value;
  password = document.getElementById("password").value;
  verifyPassword = document.getElementById("verifyPassword").value;
  email = document.getElementById("email").value;
  emailCode = document.getElementById("emailCode").value;
  phone = document.getElementById("phone").value;
  birthday = document.getElementById("birthday").value;
  isEmailVerified = false; // 초기화

  btnSendEmail = document.getElementById("btnSendEmail");

  // 이메일 전송 버튼 클릭
  btnSendEmail.addEventListener("click", function (e) {
    e.preventDefault();
    email = document.getElementById("email").value; // 다시 값 세팅
    sendEmail(email.trim()); // 이메일 전송
  });

  btnVerifyEmail.addEventListener("click", function (e) {
    e.preventDefault();
    emailCode = document.getElementById("emailCode").value; // 다시 값 세팅
    verifyEmail(emailCode);
  });
});
