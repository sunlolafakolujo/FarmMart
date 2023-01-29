package com.logicgate.image.service;


import com.logicgate.image.exception.PictureNotFoundException;
import com.logicgate.image.model.Picture;
import com.logicgate.image.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PictureServiceImpl implements PictureService{
    @Autowired
    private PictureRepository pictureRepository;

    @Override
    public Picture addPicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Picture fetchPictureById(Long id) throws PictureNotFoundException {
        return pictureRepository.findById(id).orElseThrow(()->new PictureNotFoundException("Picture Not Found"));
    }

    @Override
    public List<Picture> fetchAllPictures(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return pictureRepository.findAll(pageable).toList();
    }

    @Override
    public Picture updatePicture(Picture picture, Long id) throws PictureNotFoundException {
        Picture savedPicture=pictureRepository.findById(id)
                .orElseThrow(()->new PictureNotFoundException("Picture Not Found"));
        if (Objects.nonNull(picture.getName()) && !"".equalsIgnoreCase(picture.getName())){
            savedPicture.setName(picture.getName());
        }if (Objects.nonNull(picture.getImageType()) && !"".equalsIgnoreCase(picture.getImageType())){
            savedPicture.setImageType(picture.getImageType());
        }if (Objects.nonNull(picture.getPicByte()) && !"".equals(picture.getPicByte())){
            savedPicture.setPicByte(picture.getPicByte());
        }
        return pictureRepository.save(savedPicture);
    }

    @Override
    public void deletePicture(Long id) {
        if (pictureRepository.existsById(id)){
            pictureRepository.deleteById(id);
        }
    }

    @Override
    public void deleteAllPictures() {
        pictureRepository.deleteAll();
    }
}
