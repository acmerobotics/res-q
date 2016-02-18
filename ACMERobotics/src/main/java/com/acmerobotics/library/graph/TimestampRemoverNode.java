package com.acmerobotics.library.graph;

import com.acmerobotics.library.data.TimestampedData;

public class TimestampRemoverNode<T> extends Node<TimestampedData<T>, T> {
    @Override
    public T process(TimestampedData<T> i) {
        return i.getData();
    }
}
