package cia.arkrypto.auth.service;


import cia.arkrypto.auth.dao.ImageRepository;
import cia.arkrypto.auth.dto.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private static final Map<String, Integer> algoImage;

    static{
        algoImage = new HashMap<>();
        algoImage.put("Schnorr", 4);
    }

    private final Random random = new Random();

    public Image findById(int id){
        return imageRepository.findById(id);
    }

    public Image findById(String algo){
        int id = algoImage.getOrDefault(algo, -1);
        return findById(id);
    }

    public long count(){
        return imageRepository.count();
    }


    public String getRandomPath(){
        long count = count();
        if (count == 0) {
            throw new IllegalStateException("No records found in the database.");
        }
        int id = random.nextInt((int) count) + 1;
        return findById(id).getPath();
    }


    public byte[] getRandomImage() throws IOException {
        String path = getRandomPath();
        BufferedImage bufferedImage = ImageIO.read(Files.newInputStream(new File(path).toPath()));

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", bs);
        return bs.toByteArray();
    }

}
