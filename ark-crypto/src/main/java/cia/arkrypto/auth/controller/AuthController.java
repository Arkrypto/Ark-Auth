package cia.arkrypto.auth.controller;

import cia.arkrypto.auth.dto.KeyPair;
import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.service.AuthService;
import cia.arkrypto.auth.utils.ResultCode;
import cia.arkrypto.auth.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        KeyPair keyPair = authService.keygen(algo);
        if(Objects.isNull(keyPair)){
            model.addAttribute("result", Map.of("error", "algo invalid"));
            return "auth";
        }

        CryptoMap signature = authService.sign(algo, "test", keyPair.sk);
        Boolean flag = authService.verify(algo, "test", keyPair.pk, signature);


        model.addAttribute("algo", algo);
        model.addAttribute("result", Map.of(
                "pk", keyPair.pk,
                "sk", keyPair.sk,
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
        KeyPair keyPair = authService.keygen(algo);
        if(keyPair == null){
            return ResultUtil.failure(ResultCode.PARAM_IS_INVALID);
        }
        return ResultUtil.success(keyPair);
    }

    // 签名
    @RequestMapping("/sign")
    @ResponseBody
    public ResultUtil sign(@RequestParam Map<String, Object> params){
        String algo = (String)params.get("algo");
        String message = (String)params.get("message");
        CryptoMap sk = (CryptoMap) params.get("sk");

        CryptoMap signature = authService.sign(algo, message, sk);
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
        String message = (String)params.get("message");
        CryptoMap pk = (CryptoMap)params.get("pk");
        CryptoMap signature = (CryptoMap)params.get("signature");


        return ResultUtil.success(authService.verify(algo, message, pk, signature));
    }


}
