package cia.northboat.auth.dao;

import cia.northboat.auth.pojo.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findById(int id);

}
