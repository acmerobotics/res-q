package com.acmerobotics.library.tree;

import com.acmerobotics.library.module.core.Inject;

import java.util.ArrayList;
import java.util.List;

public abstract class Node<I, O> {

    private Node<?, I> parentNode;
    private List<Node<O, ?>> childNodes;

    @Inject
    public Node(@Parent Node<?, I> parent) {
        parentNode = parent;
        childNodes = new ArrayList<>();
    }

    public void addChild(Node<O, ?> child) {
        childNodes.add(child);
    }

    public void removeChild(Node<O, ?> child) {
        childNodes.remove(child);
    }

    public List<Node<O, ?>> getChildNodes() {
        return childNodes;
    }

    public Node<?, I> getParentNode() {
        return parentNode;
    }

    public abstract O process(I input);

}
