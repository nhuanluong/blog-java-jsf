/*
 * Created on 2021.07.18 (y.M.d) 19:18
 *
 * Copyright(c) 2021 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.web.blog.util;

import com.coursevm.core.component.TermTree;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nhuan Luong
 */
@UtilityClass
public class TreeNodeUtil {

    @SneakyThrows
    public <T extends TermTree<T>> TreeNode createTree(List<T> categories, boolean expanded, Class<T> clazz) {

        if (CollectionUtils.isEmpty(categories)) return new DefaultTreeNode(clazz.getDeclaredConstructor().newInstance(), null);

        TreeNode root = new DefaultTreeNode(clazz.getDeclaredConstructor().newInstance(), null);
        root.setExpanded(expanded);

        List<T> parents = categories
                .stream()
                .filter(item -> item.getParent() == null)
                .collect(Collectors.toList());

        parents.forEach(p -> {
            TreeNode parentNode = new DefaultTreeNode(p, root);
            parentNode.setExpanded(expanded);
            children(p.getChildren(), parentNode, expanded);
        });
        return root;
    }

    private <T extends TermTree<T>> void children(Set<T> children, TreeNode node, boolean expanded) {

        if (children.isEmpty()) return;

        children.forEach(item -> {

            TreeNode subMenu = new DefaultTreeNode(item, node);
            subMenu.setExpanded(true);

            children(item.getChildren(), subMenu, expanded);
        });
    }

    public  <T extends TermTree<T>> void removeChild(List<T> parent, Set<T> children) {
        parent.removeAll(children);
        children.forEach(item -> {
            if (CollectionUtils.isNotEmpty(item.getChildren())) {
                removeChild(parent, item.getChildren());
            }
        });
    }
}
