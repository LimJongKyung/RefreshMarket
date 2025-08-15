document.addEventListener('DOMContentLoaded', () => {
  const cartItemsDiv = document.getElementById('cartItems');
  const totalQuantityEl = document.getElementById('totalQuantity');
  const totalPriceEl = document.getElementById('totalPrice');
  const purchaseTotalEl = document.getElementById('purchaseTotal');

  let cart = JSON.parse(localStorage.getItem('cart')) || [];

  if (!cartItemsDiv) return; // 이게 없으면 DOM에 없는 거임

  let totalQuantity = 0;
  let totalPrice = 0;

  cartItemsDiv.innerHTML = '';

  cart.forEach(item => {
    const itemHTML = `
      <div style="display:flex; align-items:center; margin-bottom:15px;">
        <img src="${item.image}" alt="${item.name}" style="width:60px; height:60px; object-fit:cover; margin-right:10px; border-radius:6px;">
        <div>
          <p><strong>${item.name}</strong> (${item.option || '옵션 없음'})</p>
          <p>수량: ${item.quantity}</p>
          <p>가격: ₩${(item.price * item.quantity).toLocaleString()}</p>
        </div>
      </div>
    `;
    cartItemsDiv.innerHTML += itemHTML;

    totalQuantity += item.quantity;
    totalPrice += item.price * item.quantity;
  });

  totalQuantityEl.textContent = `총 수량: ${totalQuantity}개`;
  totalPriceEl.textContent = `총 가격: ₩${totalPrice.toLocaleString()}`;
  purchaseTotalEl.textContent = `구매할 총 금액: ₩${totalPrice.toLocaleString()}`;
});

// public/js/postcode.js

function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      // 도로명 주소를 roadAddress 필드에 채워 넣음
      document.getElementById('roadAddress').value = data.roadAddress;
      document.getElementById('detailAddress').focus();
    }
  }).open();
}

document.getElementById('guestOrderForm').addEventListener('submit', function(e) {
  const cart = JSON.parse(localStorage.getItem('cart')) || [];
  if (cart.length === 0) {
    alert('장바구니가 비어있습니다.');
    e.preventDefault();
    return false;
  }

  const productIds = cart.map(item => item.productId).join(',');
  const quantities = cart.map(item => item.quantity).join(',');

  document.getElementById('cartData').value = JSON.stringify(cart);
  document.getElementById('quantities').value = quantities;

  // 주문 제출 시 장바구니 비우기
  localStorage.removeItem('cart');
});


document.addEventListener("DOMContentLoaded", () => {
  const loadInfoCheckbox = document.getElementById("loadExistingInfo");

  loadInfoCheckbox.addEventListener("change", async function () {
    if (this.checked) {
      try {
        const response = await fetch("/products/user-info");
        if (!response.ok) throw new Error("정보 불러오기 실패");

        const data = await response.json();

        document.getElementById("customerName").value = data.name || "";
        document.getElementById("email").value = data.email || "";
        document.getElementById("phoneNumber").value = data.phone || "";
        document.getElementById("roadAddress").value = data.address || "";
        document.getElementById("customerId").value = data.customerId || ""; // 수정

      } catch (error) {
        console.error(error);
        alert("기존 정보를 불러오지 못했습니다.");
        this.checked = false;
      }
    } else {
      // 체크 해제 시 비우기
      document.getElementById("customerName").value = "";
      document.getElementById("email").value = "";
      document.getElementById("phoneNumber").value = "";
      document.getElementById("roadAddress").value = "";
      document.getElementById("customerId").value = ""; // 수정
    }
  });
});

