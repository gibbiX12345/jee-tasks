package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Comment;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.service.CommentService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Named
@SessionScoped
public class CommentBean implements Serializable {

    private List<Comment> commentsForTask;
    private Task currentTask;
    private String newCommentContent;

    @Inject
    private CommentService commentService;

    @Inject
    private LoginBean loginBean;

    public void loadCommentsForTask(Task task) {
        this.currentTask = task;
        commentsForTask = commentService.findAllCommentsByTaskId(currentTask.getTaskId());
    }

    public List<Comment> getComments() {
        return commentService.findAllComments();
    }

    public void addComment() {
        if (newCommentContent != null && !newCommentContent.isEmpty()) {
            Comment newComment = new Comment();
            newComment.setContent(newCommentContent);
            newComment.setTask(currentTask);
            newComment.setUser(loginBean.getUser());
            newComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            commentService.insertModel(newComment);

            loadCommentsForTask(currentTask);
            newCommentContent = "";
        }
    }
}