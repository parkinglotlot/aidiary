let loginId; //  로그인 아이디
let fullName; // 이름
let password; // password
let verifyPassword; //password  확인
let email;
let emailCode;
let emailCodeInput;
let phone;
let birthday;
let emailTimer; //이메일 타이머
let timerFunc; //타이머 실행 함수
let btnSignup; // 회원가입 버튼

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
      isEmailVerified = false; // 전역 이메일 확인여부 false로
      setTime(); // 타이머 시작
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
        title: "인증 요청이 완료되었습니다.",
        type: "success",
      });
      endTimer(timerFunc); //타이머 end
    })
    .catch(function (error) {
      swal.fire({
        title: "인증 실패",
        text: "인증번호를 다시 확인해주세요.",
        type: "error",
      });
    });
}

// 타이머 작동
function setTime() {
  isEmailVerified = false; // 전역 이메일 확인여부 false로
  emailCodeInput.disabled = false;
  btnVerifyEmail.disabled = false;
  clearInterval(timerFunc); // w재발송일 경우 초기화
  let time = 180; // 3분
  timerFunc = setInterval(() => {
    // console.log(time);
    let minutes = Math.floor(time / 60); // 5분
    let seconds = time % 60; // 초

    emailTimer.innerHTML =
      String(minutes).padStart(2, "0") + ":" + String(seconds).padStart(2, "0");

    if (time <= 0) {
      endTimer(timerFunc); // 시간 0:00 시, 초기화
    }

    time--; // 시간 줄어들기
  }, 1000);
}

function endTimer(timerFunc) {
  clearInterval(timerFunc);
  emailTimer.innerHTML = "03:00"; // 다시 초기화
  emailCodeInput.disabled = true;
  btnVerifyEmail.disabled = true;
}

function validateSignUp() {
  if (
    loginId == null ||
    password == null ||
    verifyPassword == null ||
    email == null ||
    phone == null ||
    birthday == null ||
    emailCode == null ||
    loginId.trim() === "" ||
    password.trim() === "" ||
    verifyPassword.trim() === "" ||
    email.trim() === "" ||
    phone.trim() === "" ||
    birthday.trim() === "" ||
    emailCode.trim() === ""
  ) {
    swal.fire({
      title: "회원가입 실패",
      text: "모든 항목을 입력해주세요.",
      type: "error",
    });
    return false;
  }
  if (password !== verifyPassword) {
    swal.fire({
      title: "회원가입 실패",
      text: "비밀번호가 일치하지 않습니다.",
      type: "error",
    });
    return false;
  }
  if (!isEmailVerified) {
    swal.fire({
      title: "회원가입 실패",
      text: "이메일 인증을 완료해주세요.",
      type: "error",
    });
    return false;
  }
  return true;
}

document.addEventListener("DOMContentLoaded", function () {
  loginId = document.getElementById("loginId").value;
  fullName = document.getElementById("fullName").value;
  password = document.getElementById("password").value;
  verifyPassword = document.getElementById("verifyPassword").value;
  email = document.getElementById("email").value;
  emailCodeInput = document.getElementById("emailCode"); // 이메일코드 인풋요소
  emailCode = document.getElementById("emailCode").value; //이메일 코드값
  phone = document.getElementById("phone").value;
  birthday = document.getElementById("birthday").value;
  emailTimer = document.getElementById("emailTimer"); //span
  btnVerifyEmail = document.getElementById("btnVerifyEmail");
  btnSignup = document.getElementById("btnSignup"); // 회원가입 버튼
  isEmailVerified = false; // 초기화

  btnSendEmail = document.getElementById("btnSendEmail");

  // setTime();

  // 이메일 전송 버튼 클릭
  btnSendEmail.addEventListener("click", function (e) {
    e.preventDefault();
    email = document.getElementById("email").value; // 다시 값 세팅
    sendEmail(email.trim()); // 이메일 전송
  });

  btnVerifyEmail.addEventListener("click", function (e) {
    e.preventDefault();
    emailCode = document.getElementById("emailCode").value; // 다시 값 세팅

    verifyEmail(String(emailCode));
  });

  //회원가입
  btnSignup.addEventListener("click", function (e) {
    e.preventDefault();
    loginId = document.getElementById("loginId").value;
    password = document.getElementById("password").value;
    verifyPassword = document.getElementById("verifyPassword").value;
    email = document.getElementById("email").value;
    phone = document.getElementById("phone").value;
    birthday = document.getElementById("birthday").value;
    fullName = document.getElementById("fullName").value;

    if (validateSignUp()) {
      // console.log("회원가입 성공");
      window.fetch("/login/valMembership", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          loginId: loginId,
          fullName: fullName,
          password: password,
          email: email,
          phone: phone,
          birthday: birthday,
        }),
      });
      swal
        .fire({
          title: "회원가입 성공",
          text: "로그인 페이지로 이동합니다.",
          type: "success",
        })
        .then(function () {
          document.getElementById("signupForm").submit(); // 폼 제출
        });
    } else {
      return; // 회원가입 실패 시, 폼 제출하지 않음
    }
  });
});
