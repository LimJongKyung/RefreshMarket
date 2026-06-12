let currentSlide = 0;
const slides = document.querySelectorAll(".slide");
const slidesContainer = document.querySelector(".slides");
let slideInterval;

function showSlide(index) {
    if (!slides.length || !slidesContainer) {
        return;
    }

    currentSlide = (index + slides.length) % slides.length;
    slidesContainer.style.transform = `translateX(-${currentSlide * 100}%)`;

    slides.forEach((slide, slideIndex) => {
        slide.classList.toggle("active", slideIndex === currentSlide);
    });
}

function changeSlide(direction) {
    showSlide(currentSlide + direction);
    restartAutoSlide();
}

function restartAutoSlide() {
    window.clearInterval(slideInterval);
    if (slides.length > 1) {
        slideInterval = window.setInterval(() => showSlide(currentSlide + 1), 5000);
    }
}

function toggleLike(button) {
    const heartIcon = button.querySelector("i");
    const likeCountSpan = button.querySelector(".like-count");

    if (!heartIcon || !likeCountSpan) {
        return;
    }

    const isLiked = heartIcon.classList.contains("fas");
    heartIcon.classList.toggle("fas", !isLiked);
    heartIcon.classList.toggle("far", isLiked);
    likeCountSpan.textContent = isLiked ? "0" : "1";
    button.setAttribute("aria-label", isLiked ? "관심 상품 등록" : "관심 상품 해제");
}

function handleCancel(orderStatus) {
    const blockedStatuses = ["입금확인", "배송중", "배송완료"];

    if (blockedStatuses.includes(orderStatus)) {
        alert("주문 작업이 시작되어 취소가 불가능합니다!");
        return false;
    }

    if (orderStatus === "주문취소") {
        alert("이미 주문 취소 처리가 되었습니다.");
        return false;
    }

    return confirm("정말 주문을 취소하시겠습니까?");
}

function openReturnModal(modalId) {
    const wrapper = document.getElementById(modalId);
    if (wrapper) {
        wrapper.classList.add("show");
    }
}

function closeReturnModal(modalId) {
    const wrapper = document.getElementById(modalId);
    if (wrapper) {
        wrapper.classList.remove("show");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    showSlide(0);
    restartAutoSlide();
});
