document.addEventListener('DOMContentLoaded', () => {
  const addToCartBtn = document.getElementById('addToCartBtn');
  const buyNowBtn = document.getElementById('buyNowBtn');
  const quantityInput = document.getElementById('quantity');
  const optionSelect = document.getElementById('detailOption');

  // 옵션 가격 배열을 안전하게 정수 배열로 변환
  let optionPriceList = Array.isArray(window.optionPriceList)
    ? window.optionPriceList.map(p => parseInt(p) || 0)
    : [];

  function addToCart(btn) {
    const productId = btn.dataset.id;
    const productName = btn.dataset.name;
    const basePrice = parseInt(btn.dataset.price) || 0;
    const productImage = btn.dataset.image;
    const quantity = parseInt(quantityInput.value) || 1;
    const option = optionSelect ? optionSelect.value : '';

    // 옵션 선택 안했을 때 경고
    if (optionSelect && !option) {
      alert('옵션을 선택해주세요.');
      return false;
    }

    let optionPrice = 0;

    if (optionSelect && optionPriceList.length > 0) {
      // value 있는 옵션 목록만 필터링
      const validOptions = Array.from(optionSelect.options).filter(opt => opt.value);
      const selectedIndex = validOptions.findIndex(opt => opt.value === option);

      if (selectedIndex !== -1 && selectedIndex < optionPriceList.length) {
        optionPrice = optionPriceList[selectedIndex];
      } else {
        console.warn('옵션 가격을 찾을 수 없습니다.');
      }
    }

    const unitPrice = basePrice + optionPrice;

    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    const existingIndex = cart.findIndex(
      item => item.productId === productId && item.option === option
    );

    if (existingIndex !== -1) {
      cart[existingIndex].quantity += quantity;
    } else {
      cart.push({
        productId,
        name: productName,
        image: productImage,
        basePrice,
        optionPrice,
        price: unitPrice,
        quantity,
        option
      });
    }

    localStorage.setItem('cart', JSON.stringify(cart));
    return true;
  }

  if (addToCartBtn) {
    addToCartBtn.addEventListener('click', () => {
      const success = addToCart(addToCartBtn);
      if (success && confirm('장바구니에 추가되었습니다! 장바구니로 가시겠습니까?')) {
        window.location.href = '/midnav/cart';
      }
    });
  }

  if (buyNowBtn) {
    buyNowBtn.addEventListener('click', () => {
      if (confirm('정말 구매하시겠습니까?')) {
        const success = addToCart(buyNowBtn);
        if (success) {
          window.location.href = '/midnav/cart';
        }
      }
    });
  }
});
