// 서버에서 상품 정보 가져오기
// 장바구니에 추가하는 함수
function addToCart(product) {
    // 로컬 스토리지에서 장바구니 항목을 가져옴 (없으면 빈 배열로 초기화)
    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    // 수량 선택과 선택사항 추가
    const quantity = document.getElementById('quantity').value;
    const selectedOption = document.getElementById('p-select').value;

    // 상품 객체에 수량과 선택사항 추가
    const cartItem = {
        id: product.id,
        name: product.name,
        price: product.price,
        imageUrl: product.imageUrl,
        quantity: quantity,
        selectedOption: selectedOption
    };

    // 장바구니에 상품 추가
    cart.push(cartItem);

    // 로컬 스토리지에 장바구니 업데이트
    localStorage.setItem('cart', JSON.stringify(cart));

    // 장바구니에 추가된 상품 확인
    console.log('Item added to cart:', cartItem);
}

// 장바구니 버튼 클릭 이벤트 처리
document.getElementById('addToCartBtn').addEventListener('click', function() {
	// 수량 선택과 선택사항 추가
	
	
	// 서버에서 상품 정보를 받아오기
    fetch('/products/otherController')
        .then(response => response.json())  // 서버로부터 JSON 데이터 받아오기
        .then(product => {
            // 장바구니에 상품 추가
            addToCart(product);
        })
        .catch(error => {
            console.error('Error fetching product data:', error);
        });
		
		const userChoice = confirm("장바구니에 추가되었습니다! 장바구니로 이동하시겠습니까?");

		if (userChoice) {
		        // 사용자가 '예'를 클릭한 경우 장바구니 페이지로 이동
		        window.location.href = "/PNC/cart";  // 실제 장바구니 페이지 URL로 변경
		}
});
