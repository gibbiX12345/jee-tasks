function openTaskModal(data) {
    if (data.status === "success") {
        // Open the modal after the AJAX call is successful
        const modal = new bootstrap.Modal(document.getElementById('taskEditMaskModal'));
        modal.show();
    }
}

function openTaskListModal(data) {
    if (data.status === "success") {
        // Open the modal after the AJAX call is successful
        const modal = new bootstrap.Modal(document.getElementById('taskListEditMaskModal'));
        modal.show();
    }
}

function openCommentModal(data) {
    if (data.status === "success") {
        // Open the modal after the AJAX call is successful
        const modal = new bootstrap.Modal(document.getElementById('commentModal'));
        modal.show();
        setTimeout(() => {
            scrollToBottom('commentList');
            focusElement('commentForm:newCommentContent');
        }, 500);
    }
}

function onCommentSubmitCompleted(data) {
    if (data.status === "success") {
        // Scroll to the bottom of the comment list after the AJAX update completes
        scrollToBottom('commentList');
        focusElement('commentForm:newCommentContent');
    }
}

function scrollToBottom(containerId) {
    const container = document.getElementById(containerId);
    if (container) {
        container.scrollTop = container.scrollHeight;
    }
}

function focusElement(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.focus();
    }
}
