package cia.arkrypto.auth.dao;

import cia.arkrypto.auth.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
}
