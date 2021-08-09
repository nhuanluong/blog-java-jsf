package com.coursevm.entity.blog.entity;

import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "metaKey")
    @Column(name = "metaValue")
    @CollectionTable(name = BlogSchema.postMeta, schema = BlogSchema.name, joinColumns = @JoinColumn(name = "postId"))
    private Map<String, String> attributes = new HashMap<>();
}