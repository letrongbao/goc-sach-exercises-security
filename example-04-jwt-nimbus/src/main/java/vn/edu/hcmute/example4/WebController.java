package vn.edu.hcmute.example4;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WebController {
    @GetMapping("/")
    String login() { return "login"; }

    @GetMapping("/profile")
    String profile() { return "profile"; }
}
