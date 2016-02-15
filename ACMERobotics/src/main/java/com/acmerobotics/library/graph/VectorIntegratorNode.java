package com.acmerobotics.library.graph;

import com.acmerobotics.library.data.TimestampedData;
import com.acmerobotics.library.data.Vector;

public class VectorIntegratorNode<A extends Vector, B extends Vector> extends Node<TimestampedData<A>, B> {

    private B base;
    private TimestampedData<A> last;

    public VectorIntegratorNode(B b) {
        base = b;
        last = null;
    }

    @Override
    public B process(TimestampedData<A> i) {
        if (last == null) {
            last = i;
        } else {
            double dt = ((double) i.getTimeSince(last)) * Math.pow(10, -9);
            A data = i.getData();
            base.x += data.x * dt;
            base.y += data.y * dt;
            base.z += data.z * dt;
        }
        return base;
    }
}
