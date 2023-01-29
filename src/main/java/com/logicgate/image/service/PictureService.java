package com.logicgate.image.service;


import com.logicgate.image.exception.PictureNotFoundException;
import com.logicgate.image.model.Picture;

import java.util.List;

public interface PictureService {
    Picture addPicture(Picture picture);
    Picture fetchPictureById(Long id) throws PictureNotFoundException;
    List<Picture> fetchAllPictures(Integer pageNumber);
    Picture updatePicture(Picture picture, Long id) throws PictureNotFoundException;
    void deletePicture(Long id);
    void deleteAllPictures();
}
