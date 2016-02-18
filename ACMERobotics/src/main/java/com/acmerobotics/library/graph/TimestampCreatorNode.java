package com.acmerobotics.library.graph;

import com.acmerobotics.library.data.TimestampedData;

public class TimestampCreatorNode<T> extends Node<T, TimestampedData<T>> {
    @Override
    public TimestampedData<T> process(T i) {
        return TimestampedData.wrap(i);
    }
}
