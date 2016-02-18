package com.acmerobotics.library.graph;

public class ValueNode<Value> extends Node<Value, Value> {

    private Value val;

    public ValueNode() {
        val = null;
    }

    @Override
    public Value process(Value i) {
        val = i;

        return super.process(i);
    }

    public Value getLatestValue() {
        return val;
    }
}
