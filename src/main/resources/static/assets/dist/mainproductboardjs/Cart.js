// 로컬 스토리지에서 장바구니 데이터 가져오기
const cart = JSON.parse(localStorage.getItem("cart")) || [];
const cartContainer = document.getElementById("cartItems");
const emptyMessage = document.getElementById("emptyMessage"); // "장바구니가 비었습니다" 메시지를 표시할 요소
const totalQuantityContainer = document.getElementById("totalQuantity");
const totalPriceContainer = document.getElementById("totalPrice");

// 장바구니 데이터 렌더링
function renderCart() {
    cartContainer.innerHTML = ""; // 기존 항목 초기화

    let totalQuantity = 0;
    let totalPrice = 0;

    if (cart.length === 0) {
        // 장바구니가 비었을 때 메시지 표시
        emptyMessage.style.display = "block"; // 메시지 보이기
    } else {
        // 장바구니에 항목이 있을 때는 항목 렌더링
        emptyMessage.style.display = "none"; // 메시지 숨기기
        cart.forEach((item, index) => {
            const cartItemDiv = document.createElement("div");
            cartItemDiv.className = "cart-item border p-3 mb-3 rounded";
            cartItemDiv.innerHTML = `
                <h4>${item.name}</h4>
                <img src="${item.imageUrl}" alt="${item.name}" class="cart-item-image" style="width: 100px; height: auto;"/>
                <p>가격: ₩${item.price}</p>
                <p>선택사항: ${item.selectedOption || '없음'}</p> <!-- 선택사항 출력 -->
                <label for="quantity-${index}">수량:</label>
                <input type="number" id="quantity-${index}" value="${item.quantity}" min="1" class="quantity-input" data-index="${index}">
                <button class="btn btn-danger btn-sm remove-btn" data-index="${index}">삭제</button>
            `;
            cartContainer.appendChild(cartItemDiv);

            // 수량과 가격 합계 계산
            totalQuantity += parseInt(item.quantity);
            totalPrice += item.price * parseInt(item.quantity);
        });

        // 삭제 버튼 이벤트 리스너 추가
        const removeButtons = document.querySelectorAll(".remove-btn");
        removeButtons.forEach(button => {
            button.addEventListener("click", function () {
                const index = this.getAttribute("data-index");
                removeCartItem(index);
            });
        });

        // 수량 입력 필드에 이벤트 리스너 추가
        const quantityInputs = document.querySelectorAll(".quantity-input");
        quantityInputs.forEach(input => {
            input.addEventListener("change", function () {
                const index = this.getAttribute("data-index");
                const newQuantity = parseInt(this.value);
                updateQuantity(index, newQuantity);
            });
        });
    }

    // 합계 표시
    totalQuantityContainer.innerText = `총 수량: ${totalQuantity}개`;
    totalPriceContainer.innerText = `총 가격: ₩${totalPrice.toLocaleString()}`;
}

// 수량 업데이트
function updateQuantity(index, newQuantity) {
    if (newQuantity < 1) {
        alert("수량은 1개 이상이어야 합니다.");
        return;
    }

    // 장바구니 항목의 수량 업데이트
    cart[index].quantity = newQuantity;

    // 로컬 스토리지에 업데이트
    localStorage.setItem("cart", JSON.stringify(cart));

    // UI 업데이트
    renderCart();
}

// 특정 상품 삭제
function removeCartItem(index) {
    // 장바구니에서 항목 제거
    cart.splice(index, 1);

    // 로컬 스토리지에 업데이트
    localStorage.setItem("cart", JSON.stringify(cart));

    // UI 업데이트
    renderCart();
}

// 초기 렌더링
renderCart();

// 구매하기 버튼 클릭 시 동작
document.getElementById("buyButton").addEventListener("click", function() {
    if (cart.length === 0) {
        alert("장바구니에 아이템이 없습니다.");
    } else {
        // 실제 구매 페이지로 이동하는 예시
        alert("구매 페이지로 이동합니다.");
        // window.location.href = "구매페이지URL";  // 실제 구매 페이지 URL로 이동
    }
});

// 모두 취소하기 버튼 클릭 시 동작
document.getElementById("clearCartButton").addEventListener("click", function() {
    if (cart.length === 0) {
        alert("장바구니가 비어 있습니다.");
    } else {
        // 장바구니 초기화
        cart.length = 0;
        localStorage.setItem("cart", JSON.stringify(cart)); // 로컬 스토리지 업데이트
        renderCart(); // 장바구니 화면 갱신
        alert("장바구니가 비워졌습니다.");
    }
});
