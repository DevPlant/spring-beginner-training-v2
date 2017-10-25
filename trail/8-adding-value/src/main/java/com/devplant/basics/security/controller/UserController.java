package com.devplant.basics.security.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.security.controller.error.InvalidRequestException;
import com.devplant.basics.security.controller.error.PasswordDoesNotMatchException;
import com.devplant.basics.security.controller.error.UserAlreadyExistsException;
import com.devplant.basics.security.controller.model.ChangePasswordRequest;
import com.devplant.basics.security.controller.model.ErrorResponse;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.model.User;
import com.devplant.basics.security.repository.UserRepository;
import com.google.common.collect.Lists;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public User register(Principal principal) {
        User me = userRepository.findOne(principal.getName());
        me.setPassword(null);
        return me;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        User existingUser = userRepository.findOne(user.getUsername());
        if (existingUser == null) {
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Lists.newArrayList("ROLE_USER"));
            userRepository.save(user);
        } else {
            throw new UserAlreadyExistsException("User " + user.getUsername() + " is already registered");
        }
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
        User currentUser = userRepository.findOne(principal.getName());
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(currentUser);
        } else {
            throw new PasswordDoesNotMatchException("Old password does not match current one!");
        }
    }

    @DeleteMapping("/delete-my-account")
    public void deleteAccount(HttpServletRequest request, Principal principal) {
        if (userRepository.findOne(principal.getName()).getReservedBooks().stream().anyMatch(BookStock::isPickedUp)) {
            throw new InvalidRequestException("Your account cannot be deleted before you return all books");
        }
        userRepository.delete(principal.getName());
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            SecurityContextHolder.clearContext();
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            SecurityContextHolder.clearContext();
        }
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseBody
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = PasswordDoesNotMatchException.class)
    @ResponseBody
    public ErrorResponse handlePasswordDoesNotMatchException(PasswordDoesNotMatchException e) {
        return new ErrorResponse(e.getMessage());
    }
}
