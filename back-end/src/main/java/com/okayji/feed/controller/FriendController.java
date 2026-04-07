package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.response.FriendReqResponse;
import com.okayji.feed.service.FriendService;
import com.okayji.identity.entity.User;
import com.okayji.utils.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@AllArgsConstructor
@Tag(name = "Friend Controller")
public class FriendController {

    private final FriendService friendService;


    @GetMapping("/received")
    @Operation(summary = "Get friend requests received")
    public ApiResponse<List<FriendReqResponse>> getFriendRequestsReceived(@CurrentUser User currentUser) {
        return ApiResponse.<List<FriendReqResponse>>builder()
                .success(true)
                .data(friendService.getFriendRequestReceived(currentUser.getId()))
                .build();
    }

    @GetMapping("/sent")
    @Operation(summary = "Get friend requests sent")
    public ApiResponse<List<FriendReqResponse>> getFriendRequestsSent(@CurrentUser User currentUser) {
        return ApiResponse.<List<FriendReqResponse>>builder()
                .success(true)
                .data(friendService.getFriendRequestSent(currentUser.getId()))
                .build();
    }

    @PostMapping("/request/{toUserIdOrUsername}")
    @Operation(summary = "Send friend request to another")
    public ApiResponse<?> sendFriendRequest(@PathVariable String toUserIdOrUsername,
                                            @CurrentUser User currentUser){
        friendService.createFriendRequest(currentUser.getId(), toUserIdOrUsername);
        return ApiResponse.builder()
                .success(true)
                .message("Friend request sent")
                .build();
    }
}
