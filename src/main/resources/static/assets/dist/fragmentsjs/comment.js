document.addEventListener("DOMContentLoaded", function() {
	   const postId = new URLSearchParams(window.location.search).get("postId");
	   if (postId) {
	       document.querySelector("input[name='postId']").value = postId;
	   }
});
	    
function increaseLike(commentId) {
	   fetch(`/comment/like/${commentId}`, {
	       method: "POST",
	   })
	   .then(response => response.json())
	   .then(data => {
	       if (data.success) {
	           document.getElementById("likeCount").innerText = data.likeCount;
	       } else {
	           alert("좋아요 반영 실패");
	       }
	   })
	   .catch(error => console.error("Error:", error));
}

function enableEdit(commentId) {
    const contentEl = document.getElementById('content-' + commentId);
    const inputEl = document.getElementById('editInput-' + commentId);

    if (contentEl && inputEl) {
        contentEl.style.display = 'none';
        inputEl.style.display = 'inline-block';
    } else {
        console.warn('요소를 찾을 수 없습니다:', commentId);
    }
}

function submitEdit(commentId) {
    const newContent = document.getElementById('editInput-' + commentId).value;

    fetch('/ALPBoard/updateComment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ commentId: commentId, content: newContent })
    })
    .then(response => {
        if (response.ok) {
            location.reload();
        } else {
            alert('수정 실패!');
        }
    });
}