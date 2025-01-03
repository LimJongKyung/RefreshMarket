$(document).ready(function () {
    // ID 입력 시 최소 및 최대 길이 체크
    $('#id').on('input', function () {
        const userId = $(this).val().trim(); // ID를 trim하여 불필요한 공백 제거
        if (userId.length < 5 || userId.length > 15) {
            $('#idLengthResult').text('아이디는 5자 이상, 15자 이하이어야 합니다.').css('color', 'red');
        } else {
            $('#idLengthResult').text('아이디 길이 적합.').css('color', 'green');
        }
    });

    // 비밀번호 복잡성 체크
    $('#passwd').on('input', function () {
        const passwd = $(this).val();
        const passwordComplexity = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/; // 최소 8자, 대문자, 소문자, 숫자, 특수문자 포함
        if (!passwordComplexity.test(passwd)) {
            $('#passwordComplexityResult').text('비밀번호는 최소 8자 이상이어야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.').css('color', 'red');
        } else {
            $('#passwordComplexityResult').text('비밀번호 형식 적합.').css('color', 'green');
        }
    });

    // 비밀번호 확인
    $('#confirmPasswd').on('keyup', function () {
        const passwd = $('#passwd').val();
        const confirmPasswd = $(this).val();

        if (passwd !== confirmPasswd) {
            $('#passwordCheckResult').text('비밀번호가 일치하지 않습니다.').css('color', 'red');
        } else {
            $('#passwordCheckResult').text('비밀번호가 일치합니다.').css('color', 'green');
        }
    });

    // ID 중복 확인
    $('#btnCheckDuplicate').click(function () {
        const userId = $('#id').val().trim();
        if (userId === "") {
            alert("아이디를 입력해주세요.");
            return;
        }

        $.ajax({
            url: '/member/check-id', // ID 중복 확인 URL
            type: 'GET',
            data: { id: userId },
            success: function (response) {
                // 서버에서 boolean 값을 반환하므로, response 자체가 true/false
                if (response) {
                    $('#idCheckResult').text('이미 사용 중인 ID입니다.').css('color', 'red');
                } else {
                    $('#idCheckResult').text('사용 가능한 ID입니다.').css('color', 'green');
                }
            },
            error: function () {
                alert("중복 확인 중 오류가 발생했습니다.");
            }
        });
    });

    // 회원가입 폼 제출 시 유효성 검사 및 Ajax 전송
    $('#signupForm').on('submit', function (event) {
        event.preventDefault(); // 기본 제출 방지

        const passwd = $('#passwd').val();
        const confirmPasswd = $('#confirmPasswd').val();

        // 비밀번호 일치 여부 확인
        if (passwd !== confirmPasswd) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        // ID 글자 수 체크
        const userId = $('#id').val().trim();
        if (userId.length < 5 || userId.length > 15) {
            alert('아이디는 5자 이상, 15자 이하이어야 합니다.');
            return;
        }

        // 비밀번호 복잡성 체크
        const passwordComplexity = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/; // 최소 8자, 대문자, 소문자, 숫자, 특수문자 포함
        if (!passwordComplexity.test(passwd)) {
            alert('비밀번호는 최소 8자 이상이어야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.');
            return;
        }
		 // Ajax 요청으로 폼 데이터 전송
		const formData = $(this).serialize(); // 폼 데이터를 직렬화

		$.ajax({
				url: '/member/register', // 회원가입 등록 URL
				type: 'POST',
				data: formData,
				success: function (response) {
				if (response === "success") {
					alert("회원가입이 완료되었습니다.");
					window.location.href = '/'; // 메인 화면으로 리디렉션
		            } else {
		            	alert("회원가입 중 오류가 발생했습니다."); // 서버 오류 처리
		            }
		        },
		        error: function () {
				alert("회원가입 중 오류가 발생했습니다.");
			}
		});
	});
});

$('#btnSearchAddress').on('click', function () {
    new daum.Postcode({
        oncomplete: function(data) {
            var roadAddr = data.roadAddress;
            var extraAddr = '';
            
            if(data.bname !== '' && data.bname !== null) {
                extraAddr += data.bname;
            }
            if(data.buildingName !== '' && data.buildingName !== null) {
                extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            if(extraAddr !== '') {
                extraAddr = ' (' + extraAddr + ')';
            }
            
            $('#address').val(roadAddr + extraAddr);
        }
    }).open();
});