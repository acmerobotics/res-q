package com.acmerobotics.library.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class Node<I, O> {

    private Node<?, I> parentNode;
    private List<Node<O, ?>> childNodes;

    public Node() {
        childNodes = new ArrayList<>();
    }

    public void setParentNode(Node<?, I> parent) {
        parentNode = parent;
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

    public boolean isRoot() {
        return parentNode == null;
    }

    public boolean isLeaf() {
        return childNodes.size() == 0;
    }

    public abstract O process(I input);

}
