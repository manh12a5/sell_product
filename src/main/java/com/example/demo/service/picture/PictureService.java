package com.example.demo.service.picture;

import com.example.demo.model.Picture;
import com.example.demo.repository.PictureRepository;
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
    private Environment environment;

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
                String fileUpload = environment.getProperty("upload.path");
                String newFile = fileUpload + fileName;
                try {
                    FileCopyUtils.copy(multipartFileList.get(i).getBytes(), new File(newFile));
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
