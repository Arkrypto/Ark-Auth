package cia.arkrypto.auth.dao;

import cia.arkrypto.auth.dto.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findById(int id);

}
