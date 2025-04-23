package cia.arkrypto.auth.dao;

import cia.arkrypto.auth.pojo.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findById(int id);

}
