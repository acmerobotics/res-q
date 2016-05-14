package com.acmerobotics.library.mock.series;

public class Polynomial {

    private double[] coefficients;

    public Polynomial(double[] coeff) {
        coefficients = coeff;
    }

    public double get(double x) {
        double y = 0.0;
        for (int i = 0; i < coefficients.length; i++) {
            y += coefficients[i] * Math.pow(x, i);
        }
        return y;
    }

}
