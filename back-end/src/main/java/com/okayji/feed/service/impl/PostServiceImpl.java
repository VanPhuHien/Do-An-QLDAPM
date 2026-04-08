package com.okayji.feed.service.impl;

import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.request.PostUpdateRequest;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {


    @Override
    public PostResponse getPostById(String viewerId, String id) {
        return null;
    }

    @Override
    public PostResponse createPost(String userId, PostCreationRequest postCreationRequest) {
        return null;
    }

    @Override
    public PostResponse updatePost(String postId, PostUpdateRequest postUpdateRequest) {
        return null;
    }

    @Override
    public void deletePostById(String id) {

    }

    @Override
    public Page<PostResponse> getPostsByUser(String viewerId, String userIdOrUsername, int page, int size) {
        return null;
    }

    @Override
    public Page<PostResponse> searchPosts(String viewerId, String keyword, int page, int size) {
        return null;
    }
}
