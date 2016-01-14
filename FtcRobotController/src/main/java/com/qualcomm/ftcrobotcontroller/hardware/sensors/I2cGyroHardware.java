package com.qualcomm.ftcrobotcontroller.hardware.sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by Ryan on 11/29/2015.
 */
public class I2cGyroHardware extends I2cHardware {

    public static final int OPR_MODE_ADDRESS = 0x3d;
    public static final byte OPR_MODE_DATA = 8;
    public static final int UNIT_SEL_ADDRESS = 0x80;
    public static final byte UNIT_SEL_DATA = 0;
    public static final int EUL_DATA_X_ADDRESS = 0x1a;

    private double lastHeading = 0.0;
    private double offset = 0;

    public I2cReadCallback callback = new I2cReadCallback() {
        @Override
        public void onReadFinished(int address, byte[] result, int length) {
            RobotLog.d("Bytes: " + I2cHardware.byteArrayToString(result));
            if (address == EUL_DATA_X_ADDRESS) {
                int val = I2cHardware.assembleWord(result[0], result[1]);
                double heading = ((double) val) / 16.0; // 16 bytes = 1 degree
                lastHeading = heading;// - offset;
                if (lastHeading > 180) {
                    lastHeading = lastHeading - 360;
                }
                RobotLog.d("Heading: " + heading);
            }
        }
    };

    public I2cGyroHardware() {

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
    public int getI2cPort() {
        return 0;
    }

    @Override
    public int getI2cAddress() {
        return 0x28 * 2;
    }

    public double getHeading() {
        return lastHeading;
    }

    public double getNormalizedHeading() {
        double raw = getHeading();
        raw -= offset;
        if (Math.abs(raw) > 180) {
            raw = 180 - raw;
        }
        return raw;
    }

    public void resetHeading() {
        offset = lastHeading;
    }
}
