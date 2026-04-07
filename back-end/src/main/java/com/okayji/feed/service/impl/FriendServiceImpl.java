package com.okayji.feed.service.impl;

import com.okayji.chat.service.ChatService;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.dto.response.FriendReqResponse;
import com.okayji.feed.entity.Friend;
import com.okayji.feed.entity.FriendRequest;
import com.okayji.feed.repository.FriendRepository;
import com.okayji.feed.repository.FriendRequestRepository;
import com.okayji.feed.service.FriendService;
import com.okayji.identity.dto.response.ProfileBasicResponse;
import com.okayji.identity.entity.User;
import com.okayji.identity.repository.UserRepository;
import com.okayji.mapper.FriendRequestMapper;
import com.okayji.mapper.ProfileMapper;
import com.okayji.notification.service.NotificationFactory;
import com.okayji.notification.service.NotificationService;
import com.okayji.utils.PairUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final FriendRequestMapper friendRequestMapper;
    private final ChatService chatService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void createFriendRequest(String fromUserId, String toUserIdOrUsername) {
        User sender = userRepository.findById(fromUserId)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        User receiver = userRepository.findUserByIdOrUsername(toUserIdOrUsername, toUserIdOrUsername)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        try {
            var pair = PairUser.canonical(sender, receiver);

            if (friendRepository.existsByUserLowIdAndUserHighId(
                    pair.getLow().getId(),
                    pair.getHigh().getId()))
                throw new AppException(AppError.FRIEND_ALREADY);

            if (friendRequestRepository.existsBySenderIdAndReceiverId(
                    receiver.getId(),
                    sender.getId()))
                throw new AppException(AppError.FRIEND_REQUEST_EXISTS);

            FriendRequest friendRequest = FriendRequest.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();

            friendRequestRepository.save(friendRequest);

            // Ping noti to other user
            notificationService.sendNotification(NotificationFactory.friendRequest(friendRequest));
        } catch (IllegalArgumentException e) {
            throw new AppException(AppError.FRIEND_YOURSELF);
        }
    }

    @Override
    public List<FriendReqResponse> getFriendRequestSent(String userId) {
        return friendRequestRepository
                .findBySenderId(userId).stream()
                .map(friendRequest -> friendRequestMapper.toFriendReqResponse(
                        friendRequest,
                        profileMapper.toProfileBasicResponse(friendRequest.getSender().getProfile()),
                        profileMapper.toProfileBasicResponse(friendRequest.getReceiver().getProfile())
                ))
                .toList();
    }

    @Override
    public List<FriendReqResponse> getFriendRequestReceived(String userId) {
        return friendRequestRepository
                .findByReceiverId(userId).stream()
                .map(friendRequest -> friendRequestMapper.toFriendReqResponse(
                        friendRequest,
                        profileMapper.toProfileBasicResponse(friendRequest.getSender().getProfile()),
                        profileMapper.toProfileBasicResponse(friendRequest.getReceiver().getProfile())
                ))
                .toList();
    }
}
