package com.acmerobotics.library.sensors.drivers;

import com.acmerobotics.library.sensors.types.ColorSensor;
import com.acmerobotics.library.sensors.i2c.Chip;
import com.acmerobotics.library.sensors.i2c.I2cChip;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.RobotLog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Chip("TCS34725")
public class TCS34725 extends I2cChip implements ColorSensor {

    private I2cDeviceSynch device;
    private boolean initialized = false;

    private Gain gain;
    private IntegrationTime integrationTime;

    public enum IntegrationTime {
        INTEGRATIONTIME_2_4_MS,
        INTEGRATIONTIME_24MS,
        INTEGRATIONTIME_50MS,
        INTEGRATIONTIME_101MS,
        INTEGRATIONTIME_154MS,
        INTEGRATIONTIME_700MS
    }

    public enum Gain {
        GAIN_1X,
        GAIN_4X,
        GAIN_16X,
        GAIN_60X
    }

    public TCS34725(OpMode mode, I2cDevice device, IntegrationTime time, Gain g) {
        super(mode, device);

        initialized = false;
        integrationTime = time;
        gain = g;
    }

    public TCS34725(OpMode mode, I2cDevice device) {
        this(mode, device, IntegrationTime.INTEGRATIONTIME_50MS, Gain.GAIN_1X);
    }

    private byte[] read(int ireg, int creg) {
        return device.read(ireg | registers.get("TCS34725_COMMAND_BIT"), creg);
    }

    private byte read8(int ireg) {
        return read(ireg, 1)[0];
    }

    private int readShort(int reg) {
        byte[] data = read(reg, 2);
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort() & 0xffff;
    }

    public int getClear() {
        return readShort(registers.get("TCS34725_CDATAL"));
    }

    @Override
    public int getRed() {
        return readShort(registers.get("TCS34725_RDATAL"));
    }

    @Override
    public int getGreen() {
        return readShort(registers.get("TCS34725_GDATAL"));
    }

    @Override
    public int getBlue() {
        return readShort(registers.get("TCS34725_BDATAL"));
    }

    public void enable() {
        device.write8(registers.get("TCS34725_ENABLE"), registers.get("TCS34725_ENABLE_PON"));
        delay(3);
        device.write8(registers.get("TCS34725_ENABLE"),
                registers.get("TCS34725_ENABLE_PON") |
                registers.get("TCS34725_ENABLE_AEN")
        );
    }

    public void disable() {
        int reg = read8(registers.get("TCS34725_ENABLE"));
        device.write8(registers.get("TCS34725_ENABLE"),
                reg & ~(registers.get("TCS34725_ENABLE_PON") | registers.get("TCS34725_ENABLE_AEN"))
        );
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean begin() {
        // FIXME: 4/22/2016
//        int x = read8(registers.get("TCS34725_ID"));
//        if ((x != 0x44) && (x != 0x10)) {
//            RobotLog.e("Didn't initialize");
//            return false;
//        }
        RobotLog.i("Begin");
        initialized = true;

        setIntegrationTime(integrationTime);
        setGain(gain);

        enable();

        RobotLog.i("End");

        return true;
    }

    public void setIntegrationTime(IntegrationTime time) {
        if (!isInitialized()) begin();

        device.write8(registers.get("TCS34725_ATIME"), registers.get("TCS34725_" + time.toString()));

        integrationTime = time;
    }

    public void setGain(Gain gain) {
        if (!isInitialized()) begin();

        device.write8(registers.get("TCS34725_CONTROL"), registers.get("TCS34725_" + gain.toString()));

        this.gain = gain;
    }

    public IntegrationTime getIntegrationTime() {
        return integrationTime;
    }

    public Gain getGain() {
        return gain;
    }

}
