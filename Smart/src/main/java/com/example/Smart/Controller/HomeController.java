package com.example.Smart.Controller;

import com.example.Smart.Dao.UserRepository;
import com.example.Smart.Entity.User;
import com.example.Smart.Helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult r,

                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               Model model, HttpSession session) {
        try {

            if (!agreement) {
                System.out.println("You must agree Terms and Conditions");
                throw new Exception("You must agree Terms and Conditions");
            }
            if (r.hasErrors()) {
                System.out.println("Error " + r.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnable(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Agreement" + agreement);
            System.out.println("USER" + user);
            User result = this.userRepository.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered..!"
                    , "alert-success"));
            return "signup";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong..!  " + e.getMessage()
                    , "alert-danger"));
            return "signup";
        }

    }

    @GetMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "LogIn Page");
        return "login";
    }

}
