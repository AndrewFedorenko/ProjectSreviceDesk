package ua.servisedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.servisedesk.dao.UserRepository;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public String home() {
        return "redirect:/requests";
    }

}
