package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Comment;
import com.ffhs.jeetasks.service.CommentService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class CommentBean {

    @Inject
    private CommentService commentService;

    public List<Comment> getComments() {
        return commentService.findAllComments();
    }
}