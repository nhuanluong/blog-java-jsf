package com.coursevm.entity.blog.entity;

import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@DiscriminatorValue("page")
public class Page extends Node {
}