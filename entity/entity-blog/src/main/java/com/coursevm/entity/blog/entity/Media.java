package com.coursevm.entity.blog.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@DiscriminatorValue("media")
public class Media extends Node {
    public Media() {
        setPostCreatedDate(LocalDateTime.now());
    }
}