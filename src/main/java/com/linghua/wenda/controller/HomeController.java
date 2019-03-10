package com.linghua.wenda.controller;

import com.linghua.wenda.model.Question;
import com.linghua.wenda.model.ViewObject;
import com.linghua.wenda.service.QuestionService;
import com.linghua.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos", getQuestions(0, 0, 20));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "index";

    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> list = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : list) {
            ViewObject vo = new ViewObject();
            question.setContent(question.getContent().substring(0,100));
            vo.set("question", question);
            vo.set("user", userService.getUserById(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
