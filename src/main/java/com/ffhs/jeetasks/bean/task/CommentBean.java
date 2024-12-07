package com.ffhs.jeetasks.bean.task;

import com.ffhs.jeetasks.entity.Comment;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.service.CommentService;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Named
@SessionScoped
public class CommentBean implements Serializable {

    @Getter
    @Setter
    private List<Comment> commentsForTask;

    @Getter
    @Setter
    private Task currentTask;

    @Getter
    @Setter
    private String newCommentContent;

    @Inject
    private CommentService commentService;

    /**
     * Loads comments for a specific task and sets it as the current task.
     *
     * @param task The task whose comments are to be loaded.
     */
    public void loadCommentsForTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        this.currentTask = task;
        this.commentsForTask = commentService.findAllCommentsByTaskId(task.getTaskId());
    }

    /**
     * Adds a new comment to the current task if the content is not empty.
     * Updates the comments list after insertion.
     */
    public void addComment() {
        if (newCommentContent == null || newCommentContent.trim().isEmpty()) {
            return;
        }

        Comment newComment = createComment(newCommentContent);
        commentService.insertModel(newComment);

        loadCommentsForTask(currentTask);
        newCommentContent = "";
    }

    /**
     * Creates a new Comment entity with the given content.
     *
     * @param content The content of the comment.
     * @return A new Comment entity.
     */
    Comment createComment(String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTask(currentTask);
        comment.setUser(SessionUtils.getLoggedInUser());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return comment;
    }
}
