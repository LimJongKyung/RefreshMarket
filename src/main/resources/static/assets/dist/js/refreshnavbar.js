document.addEventListener("DOMContentLoaded", function() {
    const hamburger = document.getElementById("hamburger");
    const sidebar = document.getElementById("sidebar");
    const closeBtn = document.getElementById("close-btn");

    if (!hamburger || !sidebar) {
        return;
    }

    const closeSidebar = function() {
        sidebar.classList.remove("open");
        hamburger.setAttribute("aria-expanded", "false");
    };

    hamburger.setAttribute("role", "button");
    hamburger.setAttribute("tabindex", "0");
    hamburger.setAttribute("aria-controls", "sidebar");
    hamburger.setAttribute("aria-expanded", "false");

    hamburger.addEventListener("click", function(event) {
        event.stopPropagation();
        const isOpen = sidebar.classList.toggle("open");
        hamburger.setAttribute("aria-expanded", String(isOpen));
    });

    hamburger.addEventListener("keydown", function(event) {
        if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            hamburger.click();
        }
    });

    sidebar.addEventListener("click", function(event) {
        event.stopPropagation();
    });

    if (closeBtn) {
        closeBtn.addEventListener("click", closeSidebar);
    }

    document.addEventListener("click", closeSidebar);
    document.addEventListener("keydown", function(event) {
        if (event.key === "Escape") {
            closeSidebar();
        }
    });
});

let timeLeft = 3600; // 10분 = 600초

// 타이머 업데이트 함수
function updateTimer() {
    const timerElement = document.getElementById("logout-timer");
    if (!timerElement) {
        return;
    }

    const minutes = Math.floor(timeLeft / 60); // 남은 분 계산
    const seconds = timeLeft % 60; // 남은 초 계산

    timerElement.innerText = `${minutes}분 ${seconds}초 후 자동 로그아웃 됩니다!`;

    if (timeLeft > 0) {
        timeLeft -= 1; // 남은 시간 감소
    } else {
        clearInterval(countdown);
        timerElement.innerText = "자동 로그아웃 중...";
        alert("자동 로그아웃되었습니다.");
        // 여기에 로그아웃 처리 코드 추가
        window.location.href = '/'; // 로그아웃 처리 URL
    }
}

// 1초마다 updateTimer 호출
const countdown = document.getElementById("logout-timer")
    ? setInterval(updateTimer, 1000)
    : null;

if (countdown) {
    updateTimer();
}
