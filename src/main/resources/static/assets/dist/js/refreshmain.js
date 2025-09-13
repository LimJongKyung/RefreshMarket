let currentSlide = 0;
const slides = document.querySelectorAll('.slide');
const totalSlides = slides.length;

// 슬라이드를 표시하는 함수
function showSlide(index) {
    if (index >= totalSlides) {
        currentSlide = 0;
    } else if (index < 0) {
        currentSlide = totalSlides - 1;
    } else {
        currentSlide = index;
    }
    const slidesContainer = document.querySelector('.slides');
    slidesContainer.style.transform = 'translateX(' + (-currentSlide * 100) + '%)';
}

// 슬라이드를 변경하는 함수
function changeSlide(direction) {
    showSlide(currentSlide + direction);
}

// 자동 슬라이드 기능 추가
function autoSlide() {
    setInterval(() => {
        changeSlide(1);
    }, 3000); // 3초 간격으로 슬라이드 전환
}

// 처음 슬라이드 표시
showSlide(currentSlide);
autoSlide(); // 자동 슬라이드 시작

function startCountdown(timerId, endDate) {
    const timerElement = document.getElementById(timerId);
    
    function updateCountdown() {
        const now = new Date().getTime(); // 현재 시간
        const countdownDate = new Date(endDate).getTime(); // 종료 시간
        const timeRemaining = countdownDate - now; // 남은 시간 (밀리초 단위)

        if (timeRemaining <= 0) {
            clearInterval(countdownInterval); // 타이머 정지
            timerElement.innerHTML = "Sale Ended"; // "Sale Ended"로 변경
            const button = timerElement.nextElementSibling;
            button.disabled = true; // 버튼 비활성화
            button.innerHTML = "Sold Out"; // 버튼 텍스트 변경
            return;
        }

        // 시간 계산
        const days = Math.floor(timeRemaining / (1000 * 60 * 60 * 24));
        const hours = Math.floor((timeRemaining % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((timeRemaining % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((timeRemaining % (1000 * 60)) / 1000);

        // 남은 시간 출력
        timerElement.innerHTML = `${days}일 ${hours}시 ${minutes}분 ${seconds < 10 ? '0' : ''}${seconds}초 남음`;
    }

    // 1초마다 타이머 업데이트
    const countdownInterval = setInterval(updateCountdown, 1000);

    // 초기 호출
    updateCountdown();
}

// 각 제품에 대해 타이머 시작 (예시: 2024년 10월 31일 종료)
startCountdown('timer1', '2024-10-31 23:59:59');
startCountdown('timer2', '2024-11-29 23:59:59');
startCountdown('timer3', '2024-12-02 23:59:59');
startCountdown('timer4', '2024-11-20 23:59:59');
startCountdown('timer5', '2024-10-31 23:59:59');

// 공구 좋아요
function toggleLike(button) {
    const heartIcon = button.querySelector('i');
    const likeCountSpan = button.nextElementSibling; // 좋아요 수 span 요소
    let likeCount = parseInt(likeCountSpan.textContent); // 현재 좋아요 수 가져오기
    
    if (heartIcon.classList.contains('far')) {
        // 빈 하트를 채워진 하트로 변경
        heartIcon.classList.remove('far');
        heartIcon.classList.add('fas');
        likeCount += 1; // 좋아요 수 증가
    } else {
        // 채워진 하트를 빈 하트로 변경
        heartIcon.classList.remove('fas');
        heartIcon.classList.add('far');
        likeCount -= 1; // 좋아요 수 감소 (0 이하로는 설정하지 않음)
        if (likeCount < 0) likeCount = 0;
    }
    
    likeCountSpan.textContent = likeCount; // 좋아요 수 업데이트
}

function handleCancel(orderStatus) {
    const blockedStatuses = ['입금확인', '배송중', '배송완료'];

    if (blockedStatuses.includes(orderStatus)) {
        alert('주문 작업이 시작되어 취소가 불가능합니다!');
        return false;
    }

    if (orderStatus === '주문취소') {
        alert('이미 주문 취소 처리가 되었습니다.');
        return false;
    }

    return confirm('정말 주문을 취소하시겠습니까?');
}

function openReturnModal(modalId) {
    const wrapper = document.getElementById(modalId);
    if (wrapper) {
        wrapper.classList.add('show');
    }
}

function closeReturnModal(modalId) {
    const wrapper = document.getElementById(modalId);
    if (wrapper) {
        wrapper.classList.remove('show');
    }
}

function openReturnStatusModal(id) {
    document.getElementById(id).classList.add('show');
}
function closeReturnModal(id) {
    document.getElementById(id).classList.remove('show');
}

