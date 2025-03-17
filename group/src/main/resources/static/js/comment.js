document.addEventListener("DOMContentLoaded", function () {
    let submitBtn = document.getElementById("commentSubmitBtn");

    if (submitBtn) {
        submitBtn.addEventListener("click", addComment);
    } else {
        console.error("Error: 등록 버튼(commentSubmitBtn)을 찾을 수 없습니다.");
    }
});

function addComment() {
    let commentInput = document.getElementById("commentInput");
    let commentList = document.getElementById("commentList");

    if (commentInput.value.trim() === "") {
        alert("댓글을 입력하세요.");
        return;
    }

    let commentCard = document.createElement("div");
    commentCard.classList.add("card", "mt-2", "p-2");

    let cardBody = document.createElement("div");
    cardBody.classList.add("card-body");

    let commentText = document.createElement("p");
    commentText.textContent = commentInput.value;
    commentText.classList.add("mb-1");

    let btnContainer = document.createElement("div");
    btnContainer.classList.add("d-flex", "align-items-center");

    let editButton = document.createElement("button");
    editButton.textContent = "수정";
    editButton.classList.add("btn", "btn-sm", "btn-warning", "mr-2");
    editButton.onclick = function () {
        editComment(commentText, editButton);
    };

    let replyButton = document.createElement("button");
    replyButton.textContent = "답글";
    replyButton.classList.add("btn", "btn-sm", "btn-secondary", "mr-2");
    replyButton.onclick = function () {
        addReplyInput(commentCard);
    };

    let deleteButton = document.createElement("button");
    deleteButton.textContent = "삭제";
    deleteButton.classList.add("btn", "btn-sm", "btn-danger");
    deleteButton.onclick = function () {
        commentCard.remove();
    };

    btnContainer.appendChild(editButton);
    btnContainer.appendChild(replyButton);
    btnContainer.appendChild(deleteButton);

    cardBody.appendChild(commentText);
    cardBody.appendChild(btnContainer);
    commentCard.appendChild(cardBody);
    commentList.appendChild(commentCard);

    commentInput.value = "";
}

function editComment(commentText, editButton) {
    let editInput = document.createElement("input");
    editInput.type = "text";
    editInput.value = commentText.textContent;
    editInput.classList.add("form-control", "mb-2");

    let saveButton = document.createElement("button");
    saveButton.textContent = "저장";
    saveButton.classList.add("btn", "btn-sm", "btn-success", "ml-2");
    saveButton.onclick = function () {
        commentText.textContent = editInput.value;
        commentText.style.display = "block";
        editInput.remove();
        saveButton.remove();
        editButton.style.display = "inline-block";
    };

    commentText.style.display = "none";
    editButton.style.display = "none";
    commentText.parentNode.insertBefore(editInput, commentText);
    commentText.parentNode.insertBefore(saveButton, editButton);
}

function addReplyInput(parentDiv) {
    if (parentDiv.querySelector(".replyInput")) {
        return;
    }

    let replyInputDiv = document.createElement("div");
    replyInputDiv.classList.add("mt-2", "ml-4");

    let replyInput = document.createElement("input");
    replyInput.type = "text";
    replyInput.classList.add("form-control", "replyInput");
    replyInput.placeholder = "대댓글을 입력하세요";

    let replySubmit = document.createElement("button");
    replySubmit.textContent = "등록";
    replySubmit.classList.add("btn", "btn-sm", "btn-primary", "ml-2");
    replySubmit.onclick = function () {
        addReply(parentDiv, replyInput.value);
        replyInputDiv.remove();
    };

    replyInputDiv.appendChild(replyInput);
    replyInputDiv.appendChild(replySubmit);
    parentDiv.appendChild(replyInputDiv);
}

function addReply(parentDiv, replyText) {
    if (replyText.trim() === "") {
        alert("대댓글을 입력하세요.");
        return;
    }

    let replyCard = document.createElement("div");
    replyCard.classList.add("card", "mt-2", "ml-4", "p-2", "border-left");

    let replyBody = document.createElement("div");
    replyBody.classList.add("card-body");

    let replyTextP = document.createElement("p");
    replyTextP.textContent = replyText;

    let editReplyButton = document.createElement("button");
    editReplyButton.textContent = "수정";
    editReplyButton.classList.add("btn", "btn-sm", "btn-warning", "mr-2");
    editReplyButton.onclick = function () {
        editComment(replyTextP, editReplyButton);
    };

    let deleteReplyButton = document.createElement("button");
    deleteReplyButton.textContent = "삭제";
    deleteReplyButton.classList.add("btn", "btn-sm", "btn-danger", "ml-2");
    deleteReplyButton.onclick = function () {
        replyCard.remove();
    };

    replyBody.appendChild(replyTextP);
    replyBody.appendChild(editReplyButton);
    replyBody.appendChild(deleteReplyButton);
    replyCard.appendChild(replyBody);
    parentDiv.appendChild(replyCard);
}
