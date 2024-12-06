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

function showNotificationPopup() {
    const notificationPopup = document.getElementById("notificationPopup");
    notificationPopup.style.display = "block";
}

function triggerNotificationButtonAction() {
    const button = document.querySelector("[id$=':notificationButton']");
    if (button) {
        button.click();
    }
}

function onNotificationDismissCompleted(data) {
    if (data.status === "success") {
        showNotificationPopup();
    }
}

document.addEventListener("DOMContentLoaded", function () {
    document.addEventListener("click", function (event) {
        const notificationPopup = document.getElementById("notificationPopup");
        if (notificationPopup.style.display === "block" && !notificationPopup.contains(event.target)) {
            document.getElementById("notificationPopup").style.display = "none";
        }
    });
});


window.addEventListener('load', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const taskId = urlParams.get('taskId');
    if (taskId) {
        const hiddenForm = document.getElementById('hiddenTaskForm');
        if (hiddenForm) {
            const hiddenButton = document.getElementById('hiddenTaskForm:prepareForEditButton');
            if (hiddenButton) {
                // Add the taskId parameter dynamically
                hiddenButton.form.action += `?taskId=${taskId}`;
                // Trigger the button click programmatically to load the task details
                hiddenButton.click();
            }
        }
    }
});
