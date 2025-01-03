document.addEventListener("DOMContentLoaded", function() {
    const hamburger = document.getElementById("hamburger");
    const sidebar = document.getElementById("sidebar");
    const closeBtn = document.getElementById("close-btn");

    // 햄버거 버튼 클릭 시 사이드바 열기
    hamburger.addEventListener("click", function() {
        sidebar.classList.toggle("open");
    });

    // 닫기 버튼 클릭 시 사이드바 닫기
    closeBtn.addEventListener("click", function() {
        sidebar.classList.remove("open");
    });
});

let timeLeft = 3600; // 10분 = 600초

// 타이머 업데이트 함수
function updateTimer() {
    const timerElement = document.getElementById("logout-timer");

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
const countdown = setInterval(updateTimer, 1000);

// 페이지가 로드되면 타이머 업데이트 시작
updateTimer();
