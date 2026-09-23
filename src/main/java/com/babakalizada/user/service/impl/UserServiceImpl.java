package com.babakalizada.user.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.BusinessException;
import com.babakalizada.common.exception.ForbiddenException;
import com.babakalizada.common.exception.NullRequestException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.user.dto.request.DtoUpdateUserRequest;
import com.babakalizada.user.dto.response.DtoUserResponse;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.enums.UserRole;
import com.babakalizada.user.enums.UserStatus;
import com.babakalizada.user.repository.IUserRepository;
import com.babakalizada.user.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<DtoUserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public DtoUserResponse getUsersByUsername(String username) {
        User currentUser = getCurrentUser();

        if (username == null || username.isBlank()) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Username cannot be empty"
                    )
            );
        }

        boolean isOwner = username.equals(currentUser.getUsername());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "You are not allowed to perform this action!"
                    )
            );
        }

        User user = userRepository.findUsersByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "User not found"
                        )
                ));

        return mapToResponse(user);
    }

    @Override
    public DtoUserResponse getUserById(Long id) {
        User currentUser = getCurrentUser();

        boolean isOwner = id.equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "You are not allowed to perform this action!"
                    )
            );
        }

        return mapToResponse(findUserOrThrow(id));
    }

    @Transactional
    @Override
    public boolean deleteUserById(Long id) {
        User currentUser = getCurrentUser();

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.USER_NOT_FOUND,
                                "User not found"
                        )
                ));

        boolean isOwner = currentUser.getId().equals(targetUser.getId());

        boolean isAdmin = UserRole.ADMIN == (currentUser.getRole());

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "You cannot delete another user's account"
                    )
            );
        }

        targetUser.setStatus(UserStatus.INACTIVE);

        userRepository.save(targetUser);
        return true;
    }

    @Transactional
    @Override
    public boolean updateUser(Long id, DtoUpdateUserRequest request) {
        if (request == null) {
            throw new NullRequestException(
                    new ErrorMessage(
                            ErrorCode.NULL_REQUEST,
                            "Request cannot be null"
                    )
            );
        }
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "User not found"
                        )
                ));
        User currentUser = getCurrentUser();

        boolean isOwner = currentUser.getId().equals(targetUser.getId());

        boolean isAdmin = UserRole.ADMIN == currentUser.getRole();

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "You cannot update another user's account"
                    )
            );
        }
        if (request.getFirstName() != null && request.getLastName() != null) {
            targetUser.setFirstName(request.getFirstName());
            targetUser.setLastName(request.getLastName());
        }
        if (request.getFirstName() != null && request.getLastName() == null) {
            targetUser.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && request.getFirstName() == null) {
            targetUser.setLastName(request.getLastName());
        }
        userRepository.save(targetUser);
        return true;
    }

    private User findUserOrThrow(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "User ID is required and must be greater than zero"
                    )
            );
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "User not found"
                        )
                ));
    }

    private DtoUserResponse mapToResponse(User user) {
        DtoUserResponse response = new DtoUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    private User getCurrentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findUsersByUsername(name);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(
                    new ErrorMessage(
                            ErrorCode.USER_NOT_FOUND,
                            "User not found"
                    )
            );
        }
        return user.get();
    }
}