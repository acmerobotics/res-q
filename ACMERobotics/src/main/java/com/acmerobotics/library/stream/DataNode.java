package com.acmerobotics.library.stream;

public abstract class DataNode<Input, Output> {

    public DataNode() {

    }

    public abstract Data<Output> process(Data<Input> d);

}
