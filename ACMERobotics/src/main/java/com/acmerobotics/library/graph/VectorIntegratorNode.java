package com.acmerobotics.library.graph;

import com.acmerobotics.library.data.TimestampedData;
import com.acmerobotics.library.data.Vector;

public class VectorIntegratorNode<A extends Vector, B extends Vector> extends Node<TimestampedData<A>, TimestampedData<B>> {

    private B base;
    private TimestampedData<A> last;

    private IntegrationAlgorithm algorithm;

    public enum IntegrationAlgorithm {
        LEFT,
        RIGHT,
        TRAPEZOIDAL
    }

    public VectorIntegratorNode(B b) {
        base = b;
        algorithm = IntegrationAlgorithm.LEFT;
        last = null;
    }

    public void setIntegrationAlgorithm(IntegrationAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public TimestampedData<B> process(TimestampedData<A> i) {
        if (last == null) {
            last = i;
        } else {
            double dt = ((double) i.getTimeSince(last)) * Math.pow(10, -9);
            A currentData = i.getData();
            A lastData = last.getData();
            switch(algorithm) {
                case LEFT:
                    base.x += lastData.x * dt;
                    base.y += lastData.y * dt;
                    base.z += lastData.z * dt;
                    break;
                case RIGHT:
                    base.x += currentData.x * dt;
                    base.y += currentData.y * dt;
                    base.z += currentData.z * dt;
                    break;
                case TRAPEZOIDAL:
                    base.x += 0.5 * (currentData.x + lastData.x) * dt;
                    base.y += 0.5 * (currentData.y + lastData.y) * dt;
                    base.z += 0.5 * (currentData.z + lastData.z) * dt;
                    break;
            }
        }
        return TimestampedData.wrap(base, i.getTimestamp());
    }
}
