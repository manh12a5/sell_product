package com.example.demo.service.picture;

import com.example.demo.model.Picture;
import com.example.demo.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPictureService extends IService<Picture> {

    byte[] getPictures(Long id) throws IOException;

    Picture createPicture(List<MultipartFile> multipartFileList, Long pictureId);
}
