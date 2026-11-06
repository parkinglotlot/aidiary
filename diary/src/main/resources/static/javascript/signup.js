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

let loginIdMessage; // 로그인 아이디 중복 메세지
let loginIdMessage2; // 로그인 아이디 길이 제한 메세지
let fullNameMessage; // 이름 입력 메세지
let passwordMessage; // 비밀번호 입력 메세지
let passwordMessage2; // 비밀번호 길이 제한 메세지
let verifyPasswordMessage; // 비밀번호 확인 입력 메세지
let emailMessage; // 이메일 입력 메세지
let emailCodeMessage; // 이메일 인증코드 입력 메세지
let phoneMessage; // 전화번호 입력 메세지
let birthdayMessage; // 생년월일 입력 메세지

let isEmailVerified;

const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
const loginIdRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{4,12}$/; // 로그인 아이디 정규식

let btnSendEmail; //인증번호 발송 버튼
let btnVerifyEmail; // 이메일 인증 확인 버튼

//인풋란
let loginIdInput;
let fullNameInput;
let passwordInput;
let verifyPasswordInput;
let emailInput;
let phoneInput;
let birthdayInput;

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
      if (result === "인증번호가 발송되었습니다.") {
        swal.fire({
          title: result,
          text: "해당 주소에서 인증번호를 확인해주세요.",
          type: "success",
        });

        setTime(); // 타이머 시작
      } else if (result === "이미 사용중인 이메일입니다.") {
        swal.fire({
          title: "이메일 전송 실패",
          text: "이미 사용중인 이메일입니다.",
          type: "error",
        });
      } else {
        swal.fire({
          title: "이메일 전송 실패",
          text: "이메일 주소를 다시 확인해주세요",
          type: "error",
        });
      }
    })
    .catch(function (error) {
      console.log("error", error);
      swal.fire({
        title: "이메일 전송 실패",
        text: "다시 시도해주세요.",
        type: "error",
      });
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
        emailCode: emailCode,
      }),
    })
    .then(function (response) {
      return response.text();
    })
    .then(function (result) {
      console.log("result", result);
      if (result === "인증번호가 일치합니다.") {
        swal.fire({
          title: "인증 요청이 완료되었습니다.",
          type: "success",
        });
        endTimer(timerFunc); //타이머 end
        isEmailVerified = true; // 전역 이메일 확인여부 true로
      } else {
        swal.fire({
          title: "인증 실패",
          text: "인증번호를 다시 확인해주세요.",
          type: "error",
        });
      }
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
  let isValid = false; // 유효성 검사 결과
  let loginIdValid = false;
  let fullNameValid = false;
  let passwordValid = false;
  let verifyPasswordValid = false;
  let emailValid = false;
  let phoneValid = false;
  let birthdayValid = false;

  loginId = document.getElementById("loginId").value.trim();
  fullName = document.getElementById("fullName").value.trim();
  password = document.getElementById("password").value.trim();
  verifyPassword = document.getElementById("verifyPassword").value.trim();
  email = document.getElementById("email").value.trim();
  emailCodeInput = document.getElementById("emailCode"); // 이메일코드 인풋요소
  emailCode = document.getElementById("emailCode").value.trim(); //이메일 코드값
  phone = document.getElementById("phone").value.trim();
  birthday = document.getElementById("birthday").value.trim();
  emailTimer = document.getElementById("emailTimer"); //span
  btnVerifyEmail = document.getElementById("btnVerifyEmail");
  btnSignup = document.getElementById("btnSignup"); // 회원가입 버튼

  if (loginId == null || loginId === "") {
    loginIdMessage.style.display = "block";
    loginIdMessage2.style.display = "none";
    loginIdValid = false;
  } else if (!loginIdRegex.test(loginId)) {
    loginIdMessage2.style.display = "block";
    loginIdMessage.style.display = "none";
    loginIdValid = false;
  } else {
    loginIdMessage2.style.display = "none";
    loginIdMessage.style.display = "none";
    loginIdValid = true;
  }

  if (fullName == null || fullName === "") {
    fullNameMessage.style.display = "block";
    fullNameValid = false;
  } else {
    fullNameMessage.style.display = "none";
    fullNameValid = true;
  }
  if (password == null || password === "") {
    passwordMessage.style.display = "block";
    passwordMessage2.style.display = "none";
    passwordValid = false;
  } else if (password.length < 4 || password.length > 8) {
    passwordMessage.style.display = "none";
    passwordMessage2.style.display = "block";
    passwordValid = false;
  } else {
    passwordMessage.style.display = "none";
    passwordMessage2.style.display = "none";
    passwordValid = true;
  }

  if (password !== verifyPassword) {
    verifyPasswordMessage.style.display = "block";
    verifyPasswordValid = false;
  } else {
    verifyPasswordMessage.style.display = "none";
    verifyPasswordValid = true;
  }
  if (email == null || email === "") {
    emailMessage.style.display = "block";
    emailValid = false;
  } else {
    emailMessage.style.display = "none";
    emailValid = true;
  }

  if (birthday == null || birthday === "") {
    birthdayMessage.style.display = "block";
    birthdayValid = false;
  } else {
    birthdayMessage.style.display = "none ";
    birthdayValid = true;
  }

  if (!isEmailVerified) {
    emailCodeMessage.style.display = "block";
  } else {
    emailCodeMessage.style.display = "none";
  }

  if (
    loginIdValid &&
    fullNameValid &&
    passwordValid &&
    verifyPasswordValid &&
    emailValid &&
    phoneValid &&
    birthdayValid &&
    isEmailVerified
  ) {
    isValid = true;
  } else {
    isValid = false;
  }
  if (isValid) {
    btnSignup.disabled = true;
    return true;
  }
  return false;
}

// 회원가입 요청
function insertMembership() {
  loginId = document.getElementById("loginId").value.trim();
  password = document.getElementById("password").value.trim();
  verifyPassword = document.getElementById("verifyPassword").value.trim();
  email = document.getElementById("email").value.trim();
  phone = document.getElementById("phone").value.trim();
  birthday = document.getElementById("birthday").value.trim();
  fullName = document.getElementById("fullName").value.trim();

  // console.log("회원가입 성공");
  window
    .fetch("/login/valMembership", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        loginId: loginId,
        fullName: fullName,
        password: password,
        verifyPassword: verifyPassword,
        email: email,
        phone: phone,
        birthday: birthday,
      }),
    })
    .then(function (response) {
      return response.json();
    })
    .then(function (result) {
      swal.fire({
        title: result.code === 200 ? "회원가입 성공" : "회원가입 실패",
        text:
          result.code === 200
            ? "회원가입이 완료되었습니다. 로그인 창으로 이동합니다"
            : "다시 시도해주세요",
        type: result.success ? "success" : "error",
      });
      if (result.code === 200) {
        setTimeOut(function () {
          location.href = "/";
        }, 2000);
      }
    })
    .catch(function (error) {
      swal.fire({
        title: "회원가입 실패",
        text: "다시 시도해주세요",
        type: "error",
      });
    });
  swal
    .fire({
      title: "회원가입 성공",
      text: "로그인 페이지로 이동합니다.",
      type: "success",
    })
    .then(function () {
      document.getElementById("signupForm").submit();
    });
}

document.addEventListener("DOMContentLoaded", function () {
  loginId = document.getElementById("loginId").value.trim();
  fullName = document.getElementById("fullName").value.trim();
  password = document.getElementById("password").value.trim();
  verifyPassword = document.getElementById("verifyPassword").value.trim();
  email = document.getElementById("email").value.trim();
  emailCodeInput = document.getElementById("emailCode"); // 이메일코드 인풋요소
  emailCode = document.getElementById("emailCode").value.trim(); //이메일 코드값
  phone = document.getElementById("phone").value.trim();
  birthday = document.getElementById("birthday").value.trim();
  emailTimer = document.getElementById("emailTimer"); //span
  btnVerifyEmail = document.getElementById("btnVerifyEmail");
  btnSignup = document.getElementById("btnSignup"); // 회원가입 버튼
  isEmailVerified = false; // 초기화

  btnSendEmail = document.getElementById("btnSendEmail");

  loginIdMessage = document.getElementById("loginIdMessage"); // 로그인 아이디 중복 메세지
  loginIdMessage2 = document.getElementById("loginIdMessage2"); // 로그인 아이디 길이 제한 메세지
  fullNameMessage = document.getElementById("fullNameMessage"); // 이름 입력 메세지
  passwordMessage = document.getElementById("passwordMessage"); // 비밀번호 입력 메세지
  passwordMessage2 = document.getElementById("passwordMessage2"); // 비밀번호 길이 제한 메세지
  verifyPasswordMessage = document.getElementById("verifyPasswordMessage"); // 비밀번호 확인 입력 메세지
  emailMessage = document.getElementById("emailMessage"); // 이메일 입력 메세지
  emailCodeMessage = document.getElementById("emailCodeMessage"); // 이메일 인증코드 입력 메세지
  phoneMessage = document.getElementById("phoneMessage"); // 전화번호 입력 메세지
  birthdayMessage = document.getElementById("birthdayMessage"); // 생년월일 입력 메세지

  //인풋란
  loginIdInput = document.getElementById("loginId");
  fullNameInput = document.getElementById("fullName");
  passwordInput = document.getElementById("password");
  verifyPasswordInput = document.getElementById("verifyPassword");
  emailInput = document.getElementById("email");
  phoneInput = document.getElementById("phone");
  birthdayInput = document.getElementById("birthday");

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

  // 인풋창 변경 시 유효성 검사
  document.querySelectorAll("input").forEach(function (input) {
    input.addEventListener("change", function () {
      validateSignUp();
    });
  });

  //회원가입
  btnSignup.addEventListener("click", function (e) {
    e.preventDefault();
    insertMembership();
  });
});
