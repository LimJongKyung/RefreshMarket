document.addEventListener('DOMContentLoaded', () => {
  const cartItemsDiv = document.getElementById('cartItems');
  const totalQuantityEl = document.getElementById('totalQuantity');
  const totalPriceEl = document.getElementById('totalPrice');
  const purchaseTotalEl = document.getElementById('purchaseTotal');
  const deliveryChargesEI = document.getElementById('deliveryCharges');
  const benefitSelector = document.getElementById('benefitSelector');
  const warningEl = document.getElementById('benefitWarning');
  const selectedBenefitsList = document.getElementById('selectedBenefitsList');
  const usedBenefitsInput = document.getElementById('usedBenefits'); 

  let cart = JSON.parse(localStorage.getItem('cart')) || [];
  let deliveryFee = 4000;
  let selectedBenefits = [];
  let totalDiscount = 0;

  if (!cartItemsDiv) return;

  function applyBenefit() {
    deliveryFee = 4000;
    totalDiscount = 0;
    if (warningEl) warningEl.textContent = '';

    if (selectedBenefits.length === 0 || cart.length === 0) return;

    selectedBenefits.forEach(benefit => {
      if (benefit.includes("전체 상품")) {
        if (benefit.includes("금액 할인")) {
          totalDiscount += parseInt(benefit.match(/\d+/)[0]);
        } else if (benefit.includes("퍼센트 할인")) {
          const percent = parseInt(benefit.match(/\d+/)[0]);
          const totalPrice = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
          totalDiscount += Math.floor(totalPrice * (percent / 100));
        }
      } else if (benefit.includes("특정 상품")) {
        const targetName = benefit.split(":")[1]?.trim();
        const targetItem = cart.find(item => item.name === targetName);

        if (!targetItem) {
          warningEl.textContent += `"${targetName}"만 할인 가능! 해당 상품이 장바구니에 없습니다.\n`;
          return;
        }

        if (benefit.includes("금액 할인")) {
          totalDiscount += parseInt(benefit.match(/\d+/)[0]);
        } else if (benefit.includes("퍼센트 할인")) {
          const percent = parseInt(benefit.match(/\d+/)[0]);
          totalDiscount += Math.floor(targetItem.price * targetItem.quantity * (percent / 100));
        }
      } else if (benefit.includes("택배비")) {
        if (benefit.includes("금액 할인")) {
          deliveryFee -= parseInt(benefit.match(/\d+/)[0]);
          if (deliveryFee < 0) deliveryFee = 0;
        }
      }
    });
  }

  function updateCouponOptions() {
    const options = benefitSelector?.options;
    if (!options || cart.length === 0) return;

    for (let i = 0; i < options.length; i++) {
      const option = options[i];
      option.disabled = false;

      if (option.value.includes("특정 상품")) {
        const targetName = option.value.split(":")[1]?.trim();
        const exists = cart.some(item => item.name === targetName);
        if (!exists) option.disabled = true;
      }
    }
  }

  function renderSelectedBenefits() {
    selectedBenefitsList.innerHTML = '';
    selectedBenefits.forEach((benefit, index) => {
      const li = document.createElement('li');
      li.textContent = benefit;

      const removeBtn = document.createElement('button');
      removeBtn.textContent = '❌';
      removeBtn.style.marginLeft = '10px';
      removeBtn.onclick = () => {
        selectedBenefits.splice(index, 1);
        renderSelectedBenefits();
        renderCart();
      };

      li.appendChild(removeBtn);
      selectedBenefitsList.appendChild(li);
    });
  }

  function renderCart() {
    cartItemsDiv.innerHTML = '';
    let totalQuantity = 0;
    let totalPrice = 0;

    applyBenefit();
    updateCouponOptions();

    cart.forEach(item => {
      cartItemsDiv.innerHTML += `
        <div style="display:flex; align-items:center; margin-bottom:15px;">
          <img src="/products/main-image/${item.productId}" alt="${item.name}" style="width:100px; height:100px; object-fit:cover; border-radius:8px; margin-right:15px;">
          <div>
            <p><strong>${item.name}</strong> (${item.option || '옵션 없음'})</p>
            <p>수량: ${item.quantity}</p>
            <p>가격: ₩${(item.price * item.quantity).toLocaleString()}</p>
          </div>
        </div>`;
      totalQuantity += item.quantity;
      totalPrice += item.price * item.quantity;
    });

    totalPrice = totalPrice - totalDiscount + deliveryFee;

    deliveryChargesEI.textContent = `택배비: ${deliveryFee.toLocaleString()}원`;
    totalQuantityEl.textContent = `총 수량: ${totalQuantity}개`;
    totalPriceEl.textContent = `총 가격: ₩${totalPrice.toLocaleString()}`;
    purchaseTotalEl.textContent = `구매할 총 금액: ₩${totalPrice.toLocaleString()}`;
  }

  renderCart();

  if (benefitSelector) {
    benefitSelector.addEventListener('change', function () {
      const selected = this.value;
      if (!selected || selectedBenefits.includes(selected)) return;
      selectedBenefits.push(selected);
      renderSelectedBenefits();
      renderCart();
    });
  }

  // ✅ 구매 확정 시 쿠폰 + 합친 주소 전송
  document.getElementById('guestOrderForm').addEventListener('submit', function(e) {
    if (cart.length === 0) {
      alert('장바구니가 비어있습니다.');
      e.preventDefault();
      return false;
    }

    document.getElementById('cartData').value = JSON.stringify(cart);
    document.getElementById('quantities').value = cart.map(item => item.quantity).join(',');
    usedBenefitsInput.value = JSON.stringify(selectedBenefits);

    // 🚀 도로명 + 상세주소 합치기
    const road = document.getElementById('roadAddress').value || '';
    const detail = document.getElementById('detailAddress').value || '';
    document.getElementById('shippingAddress').value = road + ' ' + detail;

    localStorage.removeItem('cart');
  });
});

// 다음 주소 검색
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      document.getElementById('roadAddress').value = data.roadAddress;
      document.getElementById('detailAddress').focus();
    }
  }).open();
}

// 기존 정보 불러오기
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
        document.getElementById("customerId").value = data.customerId || "";

      } catch (error) {
        console.error(error);
        alert("기존 정보를 불러오지 못했습니다.");
        this.checked = false;
      }
    } else {
      document.getElementById("customerName").value = "";
      document.getElementById("email").value = "";
      document.getElementById("phoneNumber").value = "";
      document.getElementById("roadAddress").value = "";
      document.getElementById("customerId").value = "";
    }
  });
});
