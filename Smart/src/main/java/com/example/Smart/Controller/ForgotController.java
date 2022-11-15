package com.example.Smart.Controller;

import com.example.Smart.Dao.UserRepository;
import com.example.Smart.Entity.User;
import com.example.Smart.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Random;

@Controller
public class ForgotController {

    @Autowired
    private UserRepository userRepository;

    Random random = new Random(1000);

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot_password")
    public ModelAndView forgot() {
        return new ModelAndView("forgot");
    }

    @PostMapping("/send-otp")
    public ModelAndView sendOTP(@RequestParam("email") String email, HttpSession session, Principal principal) throws MessagingException {
        System.out.println("email: " + email);


        User user = this.userRepository.getUserByUserName(email);

        //generating otp

        int otp = random.nextInt(99999);
        System.out.println("OTP: " + otp);

        String subject = "OTP from D-Caller";
        String message = "Hello "+user.getName()+",\n"+" Your OTP is "+otp+"\n Kindly register quickly.";
        String from = "phppeerbits@gmail.com";



        boolean flag = this.emailService.sendEmail(subject, message, email,from);
        if (flag) {
            return new ModelAndView("verify-otp");
        } else {
            session.setAttribute("message","Check Your Email");
            return new ModelAndView("forgot");
        }
    }
}
