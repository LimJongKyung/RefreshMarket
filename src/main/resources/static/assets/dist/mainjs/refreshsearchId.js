function toggleInputFields() {
    const option = document.getElementById("idFindOption").value;
    const emailInputField = document.getElementById("emailInputField");
    const phoneInputField = document.getElementById("phoneInputField");

    emailInputField.style.display = option === "email" ? "block" : "none";
    phoneInputField.style.display = option === "phone" ? "block" : "none";
}

function CreatCode() {
    const name = document.getElementById("userName").value;
    const option = document.getElementById("idFindOption").value;
    let identifier = option === "email" ? document.getElementById("userEmail").value : document.getElementById("userPhone").value;

    // AJAX 요청
    fetch('/member/login/send-verification-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            name: name,
            [option]: identifier // 선택한 방법에 따라 email 또는 phone 사용
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text(); // 서버 응답을 텍스트로 변환
    })
    .then(data => {
        // 결과 메시지 출력
        document.getElementById('resultMessage').innerHTML = `<div class="alert alert-success">${data}</div>`;
    })
    .catch(error => {
        document.getElementById('resultMessage').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
    });
}

function sendVerificationCode() {
    const userEmail = document.getElementById("userEmail").value;
    const userName = document.getElementById("userName").value;

    if (!userEmail || !userName) {
        alert("이메일과 이름을 입력해 주세요.");
        return;
    }

    // API 호출
    fetch("/api/sendVerificationCode?email=" + encodeURIComponent(userEmail))
        .then(response => {
            if (response.ok) {
                alert("인증 코드가 이메일로 전송되었습니다.");
            } else {
                alert("코드 전송 실패. 다시 시도해 주세요.");
            }
        });
}

function findId() {
    const userInputCode = document.getElementById("yourCode").value;
    const userEmail = document.getElementById("userEmail").value;
    const userName = document.getElementById("userName").value;

    // API 호출
    fetch("/member/login/findId?email=" + encodeURIComponent(userEmail) + "&name=" + encodeURIComponent(userName) + "&code=" + encodeURIComponent(userInputCode), {
        method: 'POST'
    })
    .then(response => response.text())
    .then(message => {
        const resultMessageElement = document.getElementById("resultMessage");
        resultMessageElement.textContent = message;
        resultMessageElement.style.color = message.includes("전송되었습니다.") ? "green" : "red";
    });
}
