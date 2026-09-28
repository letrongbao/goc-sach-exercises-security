package vn.edu.hcmute.example1;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller public class WebController {
  private final UserRepository users; public WebController(UserRepository users){this.users=users;}
  @GetMapping("/login") String login(){return "login";}
  @GetMapping("/") String home(Authentication auth, Model model){ users.findByEmail(auth.getName()).ifPresent(u -> model.addAttribute("currentUser",u)); return "home"; }
}
