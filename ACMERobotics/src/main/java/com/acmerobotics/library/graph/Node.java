package com.acmerobotics.library.graph;

import java.util.ArrayList;

public class Node<Input, Output> {

    private ArrayList<Node<Output, ?>> children;

    public Node() {
        children = new ArrayList<>();
    }

    public void connect(Node<Output, ?> node) {
        children.add(node);
    }

    public Output process(Input i) {
        return null;
    }

    public void update(Input i) {
        for (Node node : children) {
            node.update(process(i));
        }
    }
}
