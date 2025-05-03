package cia.arkrypto.auth.service;

import cia.arkrypto.auth.dao.FriendRepository;
import cia.arkrypto.auth.dto.Friend;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FriendService {
    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }


    public Friend find(String name){
        return friendRepository.findByName(name);
    }

    // 获取所有按 lastTime 升序的数据
    public List<Friend> getAllFriendsByLastTime() {
        return friendRepository.findAllByOrderByLastTimeAsc();
    }


    // 保存 Friend 实体
    public void saveFriend(String name, String pin) {
        Friend friend = new Friend(name, pin);
        friendRepository.save(friend);
    }


    public int record(String name, String pin){
        if(Objects.isNull(name) || Objects.isNull(pin) || name.equals("") || pin.equals("")){
            return -1;
        }
        Friend friend = friendRepository.findByName(name);
        if(Objects.isNull(friend)){
            saveFriend(name, pin);
            return 1;
        }
        if(!friend.getPin().equals(pin)){
            return 0;
        }
        Date now = new Date();
        System.out.println(now);
        friend.setLongest();
        friend.setLastTime(now);
        friendRepository.save(friend);
        return 2;
    }
}
