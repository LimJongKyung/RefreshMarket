// 주소 검색 버튼 클릭 시 주소 입력란에 값 채우기
document.getElementById('btnSearchAddress').addEventListener('click', function() {
    new daum.Postcode({
        oncomplete: function(data) {
            var fullAddress = data.address;
            if (data.addressType === "R") {
                fullAddress = data.roadAddress;
            }
            document.getElementById('address').value = fullAddress;
        }
    }).open();
});

// 폼 전송 시 주소와 상세 주소를 합쳐서 하나의 'address' 필드에 저장
function submitForm(event) {
    event.preventDefault();  // 폼의 기본 제출 동작을 막음

    // 주소와 상세 주소 가져오기
    var address = document.getElementById('address').value;
    var detailAddress = document.getElementById('detailAddress').value;

    // 두 값을 합쳐서 address 필드에 저장
    var fullAddress = address + " " + detailAddress;

    // 합쳐진 주소를 address 필드에 넣기
    document.getElementById('address').value = fullAddress;

    // 폼 제출
    document.getElementById('infoForm').submit();
}
