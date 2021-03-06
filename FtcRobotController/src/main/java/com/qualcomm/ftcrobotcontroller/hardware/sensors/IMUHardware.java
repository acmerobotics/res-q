package com.qualcomm.ftcrobotcontroller.hardware.sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 11/29/2015.
 */
public class IMUHardware extends I2cHardware {

    public static final int OPR_MODE_ADDRESS = 0x3d;
    public static final byte OPR_MODE_DATA = 8;
    public static final int UNIT_SEL_ADDRESS = 0x80;
    public static final byte UNIT_SEL_DATA = 0;
    public static final int EUL_DATA_X_ADDRESS = 0x1a;

    private boolean calibrated = false;
    private double lastRawHeading = 0.0;
    private double heading = 0.0;

    public I2cReadCallback callback = new I2cReadCallback() {
        @Override
        public void onReadFinished(int address, byte[] result, int length) {
            if (address == EUL_DATA_X_ADDRESS) {
                int val = I2cHardware.assembleWord(result[0], result[1]);
                double rawHeading = ((double) val) / 16.0; // 16 bytes = 1 degree
                double delta = rawHeading - lastRawHeading;
                if (!calibrated && (Math.abs(delta) < 1.0)) {
                    calibrated = true;
                }
                if (calibrated) {
                    if (delta > 180) {
                        delta = -lastRawHeading - (360 - rawHeading);
                    } else if (delta < -180) {
                        delta = (360 - lastRawHeading) + rawHeading;
                    }
                    RobotLog.d("Delta: " + delta);
                    heading += delta;
                    RobotLog.d("Heading: " + heading);
                }
                lastRawHeading = rawHeading;
            }
        }
    };

    public IMUHardware() {

    }

    @Override
    public void init(OpMode mode) {
        super.init(mode);

        writeRegisterSync(OPR_MODE_ADDRESS, OPR_MODE_DATA);
        writeRegisterSync(UNIT_SEL_ADDRESS, UNIT_SEL_DATA);
//        byte[] offsetBytes = readRegisterSync(EUL_DATA_X_ADDRESS, 2);
//        offset = ((double) assembleWord(offsetBytes[0], offsetBytes[1])) / 16.0;
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);

        if (!isReading()) readRegister(EUL_DATA_X_ADDRESS, 2, callback);
    }

    @Override
    public String getStatusString() {
        return "raw: " + getRawHeading() + "  heading: " + getHeading() + "  calibrated: " + calibrated + "  " + super.getStatusString();
    }

    @Override
    public int getI2cPort() {
        return 0;
    }

    @Override
    public int getI2cAddress() {
        return 0x28 * 2;
    }

    public double getHeading() {
        return heading;
    }

    public double getRawHeading() {
        return lastRawHeading;
    }
}
