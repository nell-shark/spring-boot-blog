package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.service.CommentService;
import com.nellshark.springbootblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final CommentService commentService;
    private final String TEMPLATES_FOLDER = "users" + File.separator;

    @GetMapping("/sign-up")
    public String getSignUpPage() {
        return TEMPLATES_FOLDER + "sign-up";
    }

    @PostMapping("/sign-up")
    public String createNewUser(@RequestParam String email,
                                @RequestParam String password,
                                HttpServletRequest request) {
        User user = userService.saveUser(new User(email, password));
        authenticateUserAndSetSession(user.getUsername(), request);
        return "redirect:/";
    }

    @GetMapping("/sign-in")
    public String getSignInPage() {
        return TEMPLATES_FOLDER + "sign-in";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/";
    }


    @GetMapping("/{id}")
    public String getUserPage(@PathVariable("id") Long id, Model model) {
        User userById = userService.getUserById(id);
        model.addAttribute("userById", userById);
        model.addAttribute("comments", commentService.getAllCommentsByUser(userById));
        return TEMPLATES_FOLDER + "id";
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getListOfUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", UserRole.values());
        return TEMPLATES_FOLDER + "list";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String getEditPage(@PathVariable("id") Long id) {
        return TEMPLATES_FOLDER + "edit";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String updateEmailAndPassword(@PathVariable("id") Long id,
                                         @RequestParam("email") String email,
                                         @RequestParam("password") String password,
                                         @AuthenticationPrincipal User user) {
        userService.updateEmail(email, user);
        userService.updatePassword(password, user);
        return "redirect:/" + "users/" + id;
    }

    @PostMapping("/{id}/edit/avatar")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String uploadUserAvatar(@PathVariable("id") Long id,
                                   @RequestParam("image") MultipartFile image,
                                   @AuthenticationPrincipal User user) throws IOException {
        userService.updateAvatar(image, user);
        return "redirect:/" + "users/" + id;
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") Long id,
                             @AuthenticationPrincipal User user,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        // TODO: check admin

        userService.deleteUserById(id);

        if (user.getRole().equals(UserRole.ROLE_ADMIN)) return "redirect:/users/list";
        return "redirect:/";
    }

    private void authenticateUserAndSetSession(String username, HttpServletRequest request) {
        request.getSession();
        UserDetails user = userService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
