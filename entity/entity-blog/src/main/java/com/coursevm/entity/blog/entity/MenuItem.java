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
@DiscriminatorValue("nav_menu_item")
@ToString(callSuper = true)
public class MenuItem extends Node {

    public MenuItem() {
        setPostCreatedDate(LocalDateTime.now());
    }

    @JoinTable(
            name = BlogSchema.menuPostMap,
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "menuId"))
    @ManyToOne(fetch = FetchType.EAGER)
    private Menu menu;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "metaKey")
    @Column(name = "metaValue")
    @CollectionTable(name = BlogSchema.postMeta, schema = BlogSchema.name, joinColumns = @JoinColumn(name = "postId"))
    private Map<String, String> attributes = new HashMap<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postParentId", insertable = false, updatable = false)
    private MenuItem parentMenu;

    @OneToMany(mappedBy = "parentMenu", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItem> subMenu = new HashSet<>();
}