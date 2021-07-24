package com.coursevm.entity.blog.entity;

import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("post")
@ToString(callSuper = true)
public class Post extends Node {

    public Post() {
        setPostCreatedDate(LocalDateTime.now());
    }

    @JoinTable(
            name = BlogSchema.categoryPostMap,
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId"))
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Category> categories;


    @JoinTable(
            name = BlogSchema.tagPostMap,
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "tagId"))
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @Transient
    private List<String> tagsStr;
}