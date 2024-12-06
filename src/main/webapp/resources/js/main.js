
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