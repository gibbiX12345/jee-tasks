/**
 * Opens the task editing modal if the request was successful.
 *
 * @param {Object} data - The response data from the AJAX request.
 */
function openTaskModal(data) {
    if (data.status === "success") {
        const modal = new bootstrap.Modal(document.getElementById('taskEditMaskModal'));
        modal.show();
    }
}

/**
 * Opens the task list editing modal if the request was successful.
 *
 * @param {Object} data - The response data from the AJAX request.
 */
function openTaskListModal(data) {
    if (data.status === "success") {
        const modal = new bootstrap.Modal(document.getElementById('taskListEditMaskModal'));
        modal.show();
    }
}

/**
 * Opens the comment modal, scrolls to the bottom of the comment list,
 * and focuses on the input field if the request was successful.
 *
 * @param {Object} data - The response data from the AJAX request.
 */
function openCommentModal(data) {
    if (data.status === "success") {
        const modal = new bootstrap.Modal(document.getElementById('commentModal'));
        modal.show();
        setTimeout(() => {
            scrollToBottom('commentList');
            focusElement('commentForm:newCommentContent');
        }, 500);
    }
}

/**
 * Handles the completion of comment submission by scrolling to the bottom
 * of the comment list and focusing on the input field.
 *
 * @param {Object} data - The response data from the AJAX request.
 */
function onCommentSubmitCompleted(data) {
    if (data.status === "success") {
        scrollToBottom('commentList');
        focusElement('commentForm:newCommentContent');
    }
}

/**
 * Scrolls a container to the bottom.
 *
 * @param {string} containerId - The ID of the container element to scroll.
 */
function scrollToBottom(containerId) {
    const container = document.getElementById(containerId);
    if (container) {
        container.scrollTop = container.scrollHeight;
    }
}

/**
 * Sets focus to a specific DOM element by its ID.
 *
 * @param {string} elementId - The ID of the element to focus.
 */
function focusElement(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.focus();
    }
}

/**
 * Displays the notification popup.
 */
function showNotificationPopup() {
    const notificationPopup = document.getElementById("notificationPopup");
    notificationPopup.style.display = "block";
}

/**
 * Triggers the notification button action programmatically.
 */
function triggerNotificationButtonAction() {
    const button = document.querySelector("[id$=':notificationButton']");
    if (button) {
        button.click();
    }
}

/**
 * Triggers the reload notifications button action programmatically.
 */
function triggerNotificationReloadButtonAction() {
    const button = document.querySelector("[id$=':reloadNotificationsButton']");
    if (button) {
        button.click();
    }
}

/**
 * Handles the completion of notification dismissal by showing the notification popup.
 *
 * @param {Object} data - The response data from the AJAX request.
 */
function onNotificationDismissCompleted(data) {
    if (data.status === "success") {
        showNotificationPopup();
    }
}

/**
 * Adds an event listener to close the notification popup when clicking outside of it.
 */
document.addEventListener("DOMContentLoaded", function () {
    document.addEventListener("click", function (event) {
        const notificationPopup = document.getElementById("notificationPopup");
        if (notificationPopup.style.display === "block" && !notificationPopup.contains(event.target)) {
            notificationPopup.style.display = "none";
        }
    });
});

/**
 * Automatically opens a task in the editing modal based on the `taskId` query parameter in the URL.
 */
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
