package cia.arkrypto.auth.dao;

import cia.arkrypto.auth.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
}
