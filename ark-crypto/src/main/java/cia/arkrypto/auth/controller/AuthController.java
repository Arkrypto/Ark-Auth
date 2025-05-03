package cia.arkrypto.auth.controller;

import cia.arkrypto.auth.dto.Key;
import cia.arkrypto.auth.dto.Signature;
import cia.arkrypto.auth.service.AuthService;
import cia.arkrypto.auth.utils.ResultCode;
import cia.arkrypto.auth.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Controller
//@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }


    @GetMapping("/test")
    public String test(@RequestParam("algo") String algo, Model model) {
        Key key = authService.keygen(algo);
        if(Objects.isNull(key)){
            model.addAttribute("result", Map.of("error", "algo invalid"));
            return "auth";
        }

        Signature signature = authService.sign(algo, key);
        Boolean flag = authService.verify(algo, key, signature);


        model.addAttribute("algo", algo);
        model.addAttribute("result", Map.of(
                "key", key,
                "signature", signature,
                "flag", flag
        ));

        return "auth";
    }

    // 密钥生成
    @RequestMapping("/keygen")
    @ResponseBody
    public ResultUtil keygen(@RequestParam Map<String, String> params){
        String algo = params.get("algo");
        Key key = authService.keygen(algo);
        if(key == null){
            return ResultUtil.failure(ResultCode.PARAM_IS_INVALID);
        }
        return ResultUtil.success(key);
    }

    // 签名
    @RequestMapping("/sign")
    @ResponseBody
    public ResultUtil sign(@RequestParam Map<String, Object> params){
        String algo = (String)params.get("algo");
        Key key = (Key)params.get("key");

        Signature signature = authService.sign(algo, key);
        if(signature == null){
            return ResultUtil.failure(ResultCode.INTERNAL_SERVER_ERROR);
        }

        return ResultUtil.success(signature);
    }


    // 认证
    @RequestMapping("/verify")
    @ResponseBody
    public ResultUtil verify(@RequestParam Map<String, Object> params){
        String algo = (String)params.get("algo");
        Key key = (Key)params.get("key");
        Signature signature = (Signature)params.get("signature");


        return ResultUtil.success(authService.verify(algo, key, signature));
    }


}
