package com.qualcomm.ftcrobotcontroller.hardware;

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

    public I2cReadCallback callback = new I2cReadCallback() {
        @Override
        public void onReadFinished(int address, byte[] result, int length) {
            if (address == EUL_DATA_X_ADDRESS) {
                byte lsb = result[0];
                byte msb = result[1];
                int val =  (lsb & 0xff) | ((msb & 0xff) << 8); // the '& 0xff' converts from signed to unsigned byte
                double heading = ((double) val) / 16.0; // 16 bytes = 1 degree
                lastHeading = heading;
                RobotLog.d("Heading: " + heading);
            }
        }
    };

    public I2cGyroHardware() {
        this.port = 0;
        this.address = 0x28 * 2;
    }

    @Override
    public void init(OpMode mode) {
        super.init(mode);

        writeRegisterSync(OPR_MODE_ADDRESS, OPR_MODE_DATA);
        writeRegisterSync(UNIT_SEL_ADDRESS, UNIT_SEL_DATA);
    }

    @Override
    public void loop(double timeSinceLastLoop) {
        super.loop(timeSinceLastLoop);

        if (!isReading()) readRegister(EUL_DATA_X_ADDRESS, 2, callback);
    }

    public double getHeading() {
        return lastHeading;
    }
}
