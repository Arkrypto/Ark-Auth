package cia.arkrypto.auth.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BasicController {

    @GetMapping({"/home", "/"})
    public String home(Model model) {
        return "index";  // 返回 templates/index.html 页面
    }

    @GetMapping("/toAuth")
    public String toAuth() {
        return "auth";  // 返回 templates/login.html 页面
    }

    @RequestMapping(value = "/toAuth/{algo}", method = RequestMethod.GET)
    public String toAuth(@PathVariable("algo") String algo, Model model) {
//        System.out.println(algo);
        model.addAttribute("algo", algo);
        return "auth";
    }
}

