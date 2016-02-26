package com.qualcomm.ftcrobotcontroller.hardware.sensors;

import android.provider.ContactsContract;

import com.acmerobotics.library.file.DataLogger;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareInterface;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Ryan on 1/27/2016.
 */
public class UltrasonicHardware extends HardwareInterface {

    public static final int SMOOTHING_SIZE = 10;

    private UltrasonicSensor usSensor;
    private DataLogger logger;
    private String name;
    private double[] data;
    private double sum;
    private int numSpikes;

    public UltrasonicHardware(String name) {
        this.name = name;
    }

    @Override
    public void init(OpMode mode) {
        usSensor = mode.hardwareMap.ultrasonicSensor.get(name);

        logger = new DataLogger(mode, this.name + "_data.csv");
        logger.writeLine("timestamp,raw,smooth");

        this.data = new double[SMOOTHING_SIZE];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = 0;
        }
        this.sum = 0;
        this.numSpikes = 0;
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);

        double next = usSensor.getUltrasonicLevel();

        if (next != 0 && next != 255) {
            numSpikes = 0;
            sum -= data[0];
            for (int i = 1; i < data.length; i++) {
                data[i - 1] = data[i];
            }
            data[data.length-1] = next;
            sum += next;
        } else {
            numSpikes++;
            if (numSpikes > 15) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = next;
                }
                this.sum = next * data.length;
            }
        }

        logger.writeLine(System.nanoTime() + "," + usSensor.getUltrasonicLevel() + "," + this.sum / SMOOTHING_SIZE);
    }

    @Override
    public String getStatusString() {
        return "smoothed: " + getDistance() + "  raw: " + data[data.length-1];
    }

    public double getDistance() {
        return sum / ((double) SMOOTHING_SIZE);
    }
}
