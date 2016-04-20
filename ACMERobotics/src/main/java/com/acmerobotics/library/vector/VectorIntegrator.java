package com.acmerobotics.library.vector;

import java.util.concurrent.TimeUnit;

public class VectorIntegrator {

    private Vector sum, last;

    public VectorIntegrator() {
        sum = new Vector();
        last = null;
    }

    public void add(Vector in) {
        if (last == null) {
            last = in;
            return;
        }

        double timeDiff = ((double) (in.time - last.time)) / Math.pow(10.0, 9.0);
        sum.x = (last.x + in.x) * timeDiff / 2.0;
        sum.y = (last.y + in.y) * timeDiff / 2.0;
        sum.z = (last.z + in.z) * timeDiff / 2.0;
    }

    public Vector getSum() {
        return sum;
    }

}
