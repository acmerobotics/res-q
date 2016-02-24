package com.acmerobotics.library.graph;

public class ValueNode<Value> extends Node<Value, Value> {

    private Value val;

    public ValueNode(Value def) {
        val = def;
    }

    public ValueNode() {
        this(null);
    }

    @Override
    public Value process(Value i) {
        val = i;
        return i;
    }

    public Value getLatestValue() {
        return val;
    }
}
