package cia.northboat.auth.dao;

import cia.northboat.auth.pojo.Friend;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend findByName(String name);

    // 方法 1：使用命名规则自动解析的查询方法
    List<Friend> findAllByOrderByLastTimeAsc();

//    @Query("SELECT FUNCTION('DATE_FORMAT', f.lastTime, '%Y-%m-%d %H:%i:%s') FROM Friend f ORDER BY f.lastTime ASC")
//    List<String> findAllFormattedLastTime();
}
