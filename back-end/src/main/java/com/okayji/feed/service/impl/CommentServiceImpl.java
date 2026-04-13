package com.okayji.feed.service.impl;


import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.request.CommentUpdateRequest;
import com.okayji.feed.dto.response.CommentResponse;

import com.okayji.feed.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {


    @Override
    public CommentResponse createComment(String userId, CommentCreationRequest request) {
        return null;
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentUpdateRequest request) {
        return null;
    }

    @Override
    public void deleteComment(String commentId) {

    }

    @Override
    public Page<CommentResponse> getCommentsByPostId(String postId, int page, int size) {
        return null;
    }
}
