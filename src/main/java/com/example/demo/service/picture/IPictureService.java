package com.example.demo.service.picture;

import com.example.demo.model.Picture;
import com.example.demo.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPictureService extends IService<Picture> {

    Picture createPicture(List<MultipartFile> multipartFileList, Long pictureId);
}
