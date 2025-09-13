$(document).ready(function() {
    $('#trackingForm').on('submit', function(e) {
        e.preventDefault();
        const invoiceNumber = $('#trackingNumber').val();

        $('#resultContainer').removeClass('d-none');
        $('#resultMessage').text('조회 중...');

        fetch(`/api/tracking/${invoiceNumber}`)
            .then(res => {
                if (!res.ok) throw new Error("조회 실패");
                return res.json();
            })
            .then(data => {
                $('#resultMessage').html(
                    `<strong>상태:</strong> ${data.status}<br/>
                     <strong>현재 위치:</strong> ${data.location}<br/>
                     <strong>업데이트 시간:</strong> ${new Date(data.updatedAt).toLocaleString()}`
                );
            })
            .catch(err => {
                $('#resultMessage').text("운송장 번호를 다시 확인해주세요.");
            });
    });
});