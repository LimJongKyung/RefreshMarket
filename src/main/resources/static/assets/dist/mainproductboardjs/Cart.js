document.addEventListener('DOMContentLoaded', () => {
  const cartItemsDiv = document.getElementById('cartItems');
  const emptyMessage = document.getElementById('emptyMessage');
  const totalQuantityEl = document.getElementById('totalQuantity');
  const totalPriceEl = document.getElementById('totalPrice');
  const clearCartButton = document.getElementById('clearCartButton');

  // 로컬스토리지에서 cart 불러오기 (없으면 빈 배열)
  let cart = JSON.parse(localStorage.getItem('cart')) || [];

  function renderCart() {
    cartItemsDiv.innerHTML = '';
    let totalQuantity = 0;
    let totalPrice = 0;

    if (cart.length === 0) {
      if (emptyMessage) emptyMessage.style.display = 'block';
      totalQuantityEl.textContent = '총 수량: 0개';
      totalPriceEl.textContent = '총 가격: ₩0';
      return;
    }

    if (emptyMessage) emptyMessage.style.display = 'none';

    cart.forEach((item, index) => {
      const unitPrice = parseInt(item.price) || 0; // 옵션 포함 단가
      const optionPrice = parseInt(item.optionPrice) || 0;
      const totalItemPrice = unitPrice * item.quantity;

      const itemContent = `
        <img src="${item.image}" alt="${item.name}" style="width:100px; height:100px; object-fit:cover; border-radius:8px; margin-right:15px;">
        <div style="flex-grow:1;">
          <p><strong>상품명:</strong> <a href="/products/detail/${item.productId}" style="color:#0d6efd; text-decoration:none;">${item.name}</a></p>
          <p><strong>옵션:</strong> ${item.option || '없음'} ${optionPrice ? `( +₩${optionPrice.toLocaleString()} )` : ''}</p>
          <p><strong>단가:</strong> ₩${unitPrice.toLocaleString()}</p>
          <p>
            <strong>수량:</strong>
            <input type="number" min="1" value="${item.quantity}" data-index="${index}" class="quantity-input" style="width:60px;"/>
          </p>
          <p><strong>총 가격:</strong> ₩${totalItemPrice.toLocaleString()}</p>
          <button class="remove-btn" data-index="${index}" style="background:#dc3545; color:white; border:none; padding:5px 10px; border-radius:5px; cursor:pointer;">삭제</button>
          <hr style="margin-top:10px;" />
        </div>
      `;

      const itemDiv = document.createElement('div');
      itemDiv.classList.add('cart-item');
      itemDiv.style.display = 'flex';
      itemDiv.style.alignItems = 'center';
      itemDiv.style.marginBottom = '15px';

      itemDiv.innerHTML = itemContent;
      cartItemsDiv.appendChild(itemDiv);

      totalQuantity += parseInt(item.quantity);
      totalPrice += totalItemPrice;
    });

    totalQuantityEl.textContent = `총 수량: ${totalQuantity}개`;
    totalPriceEl.textContent = `총 가격: ₩${totalPrice.toLocaleString()}`;
  }

  // 수량 변경 처리 (이벤트 위임)
  cartItemsDiv.addEventListener('input', e => {
    if (e.target.classList.contains('quantity-input')) {
      const index = e.target.dataset.index;
      let newQty = parseInt(e.target.value);
      if (isNaN(newQty) || newQty < 1) {
        newQty = 1;
        e.target.value = 1;
      }
      cart[index].quantity = newQty;
      localStorage.setItem('cart', JSON.stringify(cart));
      renderCart();
    }
  });

  // 삭제 버튼 클릭 처리 (이벤트 위임)
  cartItemsDiv.addEventListener('click', e => {
    if (e.target.classList.contains('remove-btn')) {
      const index = e.target.dataset.index;
      cart.splice(index, 1);
      localStorage.setItem('cart', JSON.stringify(cart));
      renderCart();
    }
  });

  // 장바구니 전체 비우기 버튼
  if (clearCartButton) {
    clearCartButton.addEventListener('click', () => {
      if (confirm('장바구니를 모두 비우시겠습니까?')) {
        localStorage.removeItem('cart');
        cart = [];
        renderCart();
      }
    });
  }

  renderCart();
});

document.addEventListener('DOMContentLoaded', () => {
  const buyButton = document.getElementById('buyButton');

  if (buyButton) {
    buyButton.addEventListener('click', () => {
      const cart = JSON.parse(localStorage.getItem('cart')) || [];

      if (cart.length === 0) {
        alert('장바구니가 비어있습니다!');
        return;
      }

      window.location.href = '/products/purchase';
    });
  }
});
