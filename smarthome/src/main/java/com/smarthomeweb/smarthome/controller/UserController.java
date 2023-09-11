package com.smarthomeweb.smarthome.controller;


import com.smarthomeweb.smarthome.model.User;
import com.smarthomeweb.smarthome.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {//возращает страничцу для вход на сайт
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")//получает страничку ля регистрации
    public String showRegistrationForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")//записать пользовотеля которого форму заполнил
    public String registration(@ModelAttribute("user") User user,
                               BindingResult result,
                               Model model){
        User existing = userService.findUserByUsername(user.getUsername());
        if (existing != null) {//что уже есть такой пользователь
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register"; //Таким образом, предполагается, что если в процессе регистрации были допущены ошибки, пользователь будет перенаправлен обратно на страницу "register",
        }
        userService.saveUser(user);//сохранения пользователя через базу данных
        return "redirect:/register?success";
    }

    @GetMapping("/users")//по пути user мы получаем список всех зарегестрированых
    public String listRegisteredUsers(Model model){
        List<User> users = userService.allUsers();
        model.addAttribute("users", users);
        return "users";
    }
}