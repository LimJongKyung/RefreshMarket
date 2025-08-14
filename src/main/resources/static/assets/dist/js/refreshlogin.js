$(document).ready(function() {
    // 로그인 폼 제출 이벤트
    $('form').on('submit', function(event) {
        event.preventDefault(); // 폼의 기본 동작(새로고침) 방지

        const id = $('#username').val().trim();  // 아이디 값 가져오기
        const passwd = $('#password').val().trim();  // 비밀번호 값 가져오기

        // 입력값 검증
        if (!id || !passwd) {
            alert('아이디와 비밀번호를 모두 입력해주세요.');
            return;
        }

        // AJAX 요청을 통해 로그인 처리
        $.ajax({
            type: 'POST',
            url: '/member/check', // 로그인 요청 처리하는 URL
            data: {
                id: id,
                passwd: passwd
            },
            success: function(response) {
                if (response.status === 'success') {
                    alert(response.message); // 로그인 성공 메시지

                    // 사용자 이름을 가져와서 HTML에 업데이트
                    $('#username').text(response.username); // 사용자 이름을 업데이트
                    $('#welcomeMessage').show(); // 환영 메시지 표시

                    // 로그인 성공 시 /login 페이지로 리다이렉트
                    window.location.href = '/home';
                } else {
                    alert(response.message); // 로그인 실패 메시지
                }
            },
            error: function(xhr, status, error) {
                console.error('로그인 요청 중 오류 발생:', error);
                alert('로그인 중 문제가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });
});
