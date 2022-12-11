package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.ArticleService;
import com.nellshark.springbootblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class MainController {
    private final ArticleService articleService;
    private final UserService userService;

    @RequestMapping(value = {"/", "/index", "/main", "/home"}, method = RequestMethod.GET)
    public String getIndexPage(@RequestParam(value = "search", required = false) String search,
                               Model model,
                               @AuthenticationPrincipal User user) {
        List<Article> articles = StringUtils.isEmpty(search)
                ? articleService.getAllArticles()
                : articleService.searchArticle(search);

        if (user != null) model.addAttribute("user", user);
        model.addAttribute("search", search);
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/contact-us")
    public String getContactUsPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) model.addAttribute("user", user);
        List<User> admins = userService.getAllAdmins();
        List<User> moderators = userService.getAllModerators();

        model.addAttribute("users", ListUtils.union(admins, moderators));
        return "contact-us";
    }
}
