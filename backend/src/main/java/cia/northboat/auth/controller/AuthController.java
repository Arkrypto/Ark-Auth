package cia.northboat.auth.controller;

import cia.northboat.auth.pojo.Image;
import cia.northboat.auth.pojo.Pair;
import cia.northboat.auth.service.AuthService;
import cia.northboat.auth.service.ImageService;
import cia.northboat.auth.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
public class AuthController {
    private final AuthService authService;
    private final ImageService imageService;

    @Autowired
    public AuthController(AuthService authService, ImageService imageService){
        this.authService = authService;
        this.imageService = imageService;
    }

    // 认证
    @RequestMapping("/auth")
    public String auth(@RequestParam Map<String, String> params, Model model){

        String algo = params.get("algo");

        Image image = imageService.findById(algo);
        if(Objects.isNull(image)){
            model.addAttribute("msg", "Protocol not found");
            return "auth";
        }
        StringBuilder detailStr = new StringBuilder();
        List<Pair> detail = authService.auth(params);
        for(Pair p: detail){
            System.out.println(p.fst + ": " + p.snd);
            detailStr.append(p.fst).append(" = ").append(p.snd).append("\n\n");
        }

        model.addAttribute("algo", algo);
        model.addAttribute("image", image.getPath());
        model.addAttribute("detail", detailStr.toString().trim());

        return "auth";
    }
}
