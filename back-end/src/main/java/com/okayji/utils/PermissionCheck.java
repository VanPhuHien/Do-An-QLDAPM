package com.okayji.utils;

import com.okayji.chat.repository.ChatMemberRepository;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.Comment;
import com.okayji.feed.entity.FriendRequest;
import com.okayji.feed.entity.Post;
import com.okayji.feed.repository.CommentRepository;
import com.okayji.feed.repository.FriendRepository;
import com.okayji.feed.repository.FriendRequestRepository;
import com.okayji.feed.repository.PostRepository;
import com.okayji.identity.entity.Profile;
import com.okayji.identity.entity.User;
import com.okayji.identity.repository.UserRepository;
import com.okayji.notification.entity.Notification;
import com.okayji.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.okayji.feed.entity.PostStatus.PUBLISHED;
import static com.okayji.identity.entity.ProfileVisibility.PUBLIC;

@Service("permissionCheck")
@AllArgsConstructor
public class PermissionCheck {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public boolean isFriend(String userId, String friendId) {
        return userId.compareTo(friendId) < 0
                ? friendRepository.existsByUserLowIdAndUserHighId(userId, friendId)
                : friendRepository.existsByUserLowIdAndUserHighId(friendId, userId);
    }

    /**
     *
     * @param userId
     * @param friendRequestId
     * @param action "ACCEPT", "DECLINE", "CANCEL"
     */
    public boolean canAlterFriendRequest(String userId,
                                         String friendRequestId,
                                         String action) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new AppException(AppError.FRIEND_REQUEST_NOT_FOUND));

        return switch (action) {
            case "ACCEPT", "DECLINE" -> friendRequest.getReceiver().getId().equals(userId);
            case "CANCEL" -> friendRequest.getSender().getId().equals(userId);
            default -> false;
        };
    }
}
