package cia.northboat.auth.controller;


import cia.northboat.auth.pojo.Friend;
import cia.northboat.auth.service.FriendService;
import cia.northboat.auth.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class FriendController {


    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService){
        this.friendService = friendService;
    }

    @RequestMapping("/record")
    public String record(@RequestParam Map<String, String> params, Model model){
        String name = params.get("name");
        String pin = params.get("pin");

        int flag = friendService.record(name, pin);
        String msg = flag == -1 ? "NAME 和 PIN 不能为空" : flag == 0 ? "PIN码错误" : flag == 1 ? "加入游戏成功" : "更新成功";
        model.addAttribute("msg", msg);

        List<Friend> friends = friendService.getAllFriendsByLastTime();
        model.addAttribute("friends", friends);

        return "index";
    }
}
