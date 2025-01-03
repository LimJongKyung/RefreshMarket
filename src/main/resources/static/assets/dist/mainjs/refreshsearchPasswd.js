function toggleInputFields() {
    const option = document.getElementById("passwordFindOption").value;
    const emailInputField = document.getElementById("emailInputField");
    const phoneInputField = document.getElementById("phoneInputField");

    // 선택한 옵션에 따라 필드의 표시 여부 결정
    emailInputField.style.display = option === "email" ? "block" : "none"; 
    phoneInputField.style.display = option === "phone" ? "block" : "none";
}

function createCode() {
    const name = document.getElementById("userFullName").value; // userName을 userFullName으로 수정
    const option = document.getElementById("passwordFindOption").value;
    let identifier = option === "email" ? document.getElementById("userEmail").value : document.getElementById("userPhone").value;

    // 비밀번호 찾기 인증 코드 요청
    fetch('/member/login/send-verification-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            name: name,
            [option]: identifier // email 또는 phone
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(data => {
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

    // 인증 코드 전송 요청
    fetch("/api/sendVerificationCode?email=" + encodeURIComponent(userEmail))
        .then(response => {
            if (response.ok) {
                alert("인증 코드가 이메일로 전송되었습니다.");
            } else {
                alert("코드 전송 실패. 다시 시도해 주세요.");
            }
        });
}

function findPassword() {
    const userInputCode = document.getElementById("yourCode").value;
    const userEmail = document.getElementById("userEmail").value;
    const userName = document.getElementById("userFullName").value;

    // 필드 유효성 검사
    if (!userInputCode || !userEmail || !userName) {
        alert("모든 필드를 입력해 주세요.");
        return;
    }

    // 비밀번호 찾기 요청
    fetch("/member/login/findPasswd?email=" + encodeURIComponent(userEmail) + "&name=" + encodeURIComponent(userName) + "&code=" + encodeURIComponent(userInputCode), {
        method: 'POST'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(message => {
        const resultMessageElement = document.getElementById("resultMessage");
        resultMessageElement.textContent = message;
        resultMessageElement.style.color = message.includes("비밀번호가 발송되었습니다.") ? "green" : "red";
    })
    .catch(error => {
        const resultMessageElement = document.getElementById("resultMessage");
        resultMessageElement.textContent = "비밀번호 찾기 요청 중 오류 발생: " + error.message;
        resultMessageElement.style.color = "red";
    });
}