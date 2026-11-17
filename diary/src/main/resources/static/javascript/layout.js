let logoutBtn; //로그아웃 버튼
let diaryMenus; //다이어리 메뉴
let userAuth; //유저권한 메뉴 버튼
let currentPath; //현재 메뉴 url
let menuList; // menulist ul

// 메뉴 활성화(선택된 메뉴)
function activeMenu() {
  document.querySelectorAll(".menu-item").forEach(function (menu) {
    if (menu.dataset["menu"] === currentPath) {
      menu.classList.add("active");
    } else {
      menu.classList.remove("active");
    }
  });
}

function getMenu() {
  window
    .fetch("/getMenu", {
      method: "GET",
    })
    .then(function (response) {
      return response.json();
    })
    .then(function (result) {
      console.log("result", result);
      menuList.innerHTML = "";
      let itemList = result.data;
      itemList.forEach(function (item) {
        let li = document.createElement("li");
        li.classList.add("menu-item");
        li.classList.add("diaryMenu");
        li.dataset.menu = item.url;
        li.innerHTML = item.name;
        menuList.append(li);
      });
      activeMenu(); // 메뉴 활성화
    });
}

// 실제 실행 시점
document.addEventListener("DOMContentLoaded", function () {
  logoutBtn = document.getElementById("logOutBtn");

  menuList = document.getElementsByClassName("menu-list")[0]; // menulist ul

  diaryMenus = document.querySelectorAll(".diaryMenu");

  userAuth = document.getElementById("userAuth");

  currentPath = window.location.pathname;

  getMenu();

  logoutBtn.addEventListener("click", function () {
    window
      .fetch("/user/logOut", {
        method: "POST",
      })
      .then(function (data) {
        console.log("Logged Out! " + data);
        location.href = "/";
      })
      .catch(function (data) {
        console.log(data);
        Swal.fire({
          title: "로그아웃 실패",
          icon: "error",
        });
      });
  });

  //메뉴이동
  document.addEventListener("click", function (e) {
    let menuItem = e.target.classList.contains("menu-item") ? e.target : null;

    if (menuItem !== null) {
      e.preventDefault();
      let url = menuItem.dataset.menu;
      window
        .fetch(url, {
          method: "GET",
        })
        .then(function (data) {
          if (!data.ok) {
            throw new Error("권한이 없습니다.");
          }
          window.location.href = url;
        })
        .catch(function (error) {
          swal.fire({
            title: "권한이 없습니다.",
            icon: "error",
          });
        });
      // location.href = menuItem.dataset.menu;
    }
  });
});
