package com.coursevm.entity.blog.entity;

import com.coursevm.entity.blog.schema.BlogSchema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("menu")
@ToString(callSuper = true)
public class MenuItem extends Node {

    public MenuItem() {
        setPostCreatedDate(LocalDateTime.now());
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "metaKey")
    @Column(name = "metaValue")
    @CollectionTable(name = BlogSchema.postMeta, schema = BlogSchema.name, joinColumns = @JoinColumn(name = "postId"))
    private Map<String, String> attributes = new HashMap<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postParentId")
    private MenuItem menuParent;

    @OneToMany(mappedBy="menuParent", fetch = FetchType.EAGER)
    private Set<MenuItem> subMenu = new HashSet<>();
}