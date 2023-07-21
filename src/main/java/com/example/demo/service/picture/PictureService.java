package com.example.demo.service.picture;

import com.amazonaws.services.s3.AmazonS3;
import com.example.demo.model.Picture;
import com.example.demo.repository.PictureRepository;
import com.example.demo.s3.S3Bucket;
import com.example.demo.service.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PictureService implements IPictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private S3Bucket s3Bucket;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public List<Picture> findAll() {
        return pictureRepository.findAll();
    }

    @Override
    public Picture findById(Long id) {
        return pictureRepository.findById(id).orElse(null);
    }

    @Override
    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public void remove(Long id) {
        pictureRepository.deleteById(id);
    }

    @Override
    public Picture createPicture(List<MultipartFile> multipartFileList, Long pictureId) {
        Picture picture = null;
        if (pictureId != null) {
            picture = this.findById(pictureId);
        }
        if (picture == null) {
            picture = new Picture();
        }

        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            for (int i = 0; i < multipartFileList.size(); i++) {
                String fileName = multipartFileList.get(i).getOriginalFilename();
                try {
                    byte[] bytes = multipartFileList.get(i).getBytes();
                    s3Service.putS3Object(s3Bucket.getCustomer(), fileName, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (i) {
                    case 0:
                        picture.setMainPicture(fileName);
                        break;
                    case 1:
                        picture.setPicture1(fileName);
                        break;
                    case 2:
                        picture.setPicture2(fileName);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
            }
            this.save(picture);
        }

        return picture;
    }

}
