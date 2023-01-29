package com.logicgate.image.repository;


import com.logicgate.image.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture,Long> {
}
