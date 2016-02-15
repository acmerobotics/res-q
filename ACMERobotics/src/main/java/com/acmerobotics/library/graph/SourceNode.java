package com.acmerobotics.library.graph;

public class SourceNode<Output> extends Node<Output, Output> {
    @Override
    public void update(Output i) {
        for (Node node : getChildren()) {
            node.update(i);
        }
    }
}
