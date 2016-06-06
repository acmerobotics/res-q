package com.acmerobotics.library.sensors.drivers;

import com.acmerobotics.library.inject.core.Inject;
import com.acmerobotics.library.inject.hardware.Hardware;
import com.acmerobotics.library.sensors.i2c.Chip;
import com.acmerobotics.library.sensors.i2c.I2cChip;
import com.acmerobotics.library.sensors.types.OrientationSensor;
import com.acmerobotics.library.vector.Vector;
import com.acmerobotics.library.sensors.types.GyroSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.util.RobotLog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Chip("BNO055")
public class BNO055 extends I2cChip implements GyroSensor, OrientationSensor {

    private boolean initialized;

    private AngleUnits angleUnits;
    private TemperatureUnits tempUnits;
    private OperationMode mode;

    @Override
    public double getAngularVelocityYaw() {
        return getAngularVelocity().z;
    }

    @Override
    public double getAngularVelocityPitch() {
        return getAngularVelocity().y;
    }

    @Override
    public double getAngularVelocityRoll() {
        return getAngularVelocity().x;
    }

    @Override
    public double getOrientationYaw() {
        return getOrientation().z;
    }

    @Override
    public double getOrientationPitch() {
        return getOrientation().y;
    }

    @Override
    public double getOrientationRoll() {
        return getOrientation().x;
    }

    @Override
    public Vector getOrientation() {
        return getEulerAngles();
    }

    public enum AngleUnits {
        RADIANS,
        DEGREES
    }

    public enum TemperatureUnits {
        CELSIUS,
        FAHRENHEIT
    }

    public enum PowerMode {
        NORMAL,
        LOWPOWER,
        SUSPEND
    }

    public enum OperationMode {
        CONFIG,
        ACCONLY,
        MAGONLY,
        GYRONLY,
        ACCMAG,
        ACCGYRO,
        MAGGYRO,
        AMG,
        IMUPLUS,
        COMPASS,
        M4G,
        NDOF_FMC_OFF,
        NDOF
    }

    @Inject
    public BNO055(OpMode mode, @Hardware I2cDevice device) {
        this(mode, device, OperationMode.NDOF, AngleUnits.DEGREES, TemperatureUnits.FAHRENHEIT);
    }

    public BNO055(OpMode mode, I2cDevice device, OperationMode operationMode, AngleUnits angle, TemperatureUnits temp) {
        super(mode, device);

        initialized = false;

        begin();

        setAngleUnits(angle);
        setTemperatureUnits(temp);
        setMode(operationMode);
    }

    public boolean chipIdMatches() {
        int val = device.read8(registers.get("BNO055_CHIP_ID_ADDR")) & 0xff;
        return val == Integer.parseInt(getExtra("id"));
    }

    public boolean begin() {
        if (initialized) return false;

        RobotLog.i("Begin");

        /* Make sure we have the right device */
        while (!chipIdMatches()) {
            delay(1000);
        }

        /* Switch to config mode (just in case since this is the default) */
        setMode(OperationMode.CONFIG);

        /* Reset */
        device.write8(registers.get("BNO055_SYS_TRIGGER_ADDR"), (byte) 0x20);
        while (!chipIdMatches()) {
            delay(10);
        }
        delay(50);

        /* Set to normal power mode */
        setPowerMode(PowerMode.NORMAL);

        device.write8(registers.get("BNO055_PAGE_ID_ADDR"), (byte) 0);

        /* Set the output units */
        /*
        uint8_t unitsel = (0 << 7) | // Orientation = Android
                        (0 << 4) | // Temperature = Celsius
                        (0 << 2) | // Euler = Degrees
                        (1 << 1) | // Gyro = Rads
                        (0 << 0);  // Accelerometer = m/s^2
        device.write8(Registers.BNO055_UNIT_SEL, unitsel);
        */

        /* Configure axis mapping (see section 3.4) */
        /*
        device.write8(Registers.BNO055_AXIS_MAP_CONFIG, REMAP_CONFIG_P2); // P0-P7, Default is P1
        delay(10);
        device.write8(Registers.BNO055_AXIS_MAP_SIGN, REMAP_SIGN_P2); // P0-P7, Default is P1
        delay(10);
        */

        device.write8(registers.get("BNO055_SYS_TRIGGER_ADDR"), (byte) 0x0);
        delay(10);

        RobotLog.i("End");

        return true;
    }

    public void setPowerMode(PowerMode mode) {
        int byteVal = registers.get("POWER_MODE_" + mode.toString());
        device.write8(registers.get("BNO055_PWR_MODE_ADDR"), byteVal);
        delay(10);
    }

    public void setMode(OperationMode mode) {
        this.mode = mode;
        int byteVal = registers.get("OPERATION_MODE_" + mode.toString());
        device.write8(registers.get("BNO055_OPR_MODE_ADDR"), byteVal);
        delay(30);
    }

    public void setTemperatureUnits(TemperatureUnits tempUnits) {
        this.tempUnits = tempUnits;
        int val = device.read8(registers.get("BNO055_UNIT_SEL_ADDR"));
        device.write8(registers.get("BNO055_UNIT_SEL_ADDR"), (byte) ((val & 0b11101111) | (tempUnits.equals(TemperatureUnits.CELSIUS) ? 0 : 1) << 4));
        delay(10);
    }

    public TemperatureUnits getTemperatureUnits() {
        return this.tempUnits;
    }

    public void setAngleUnits(AngleUnits angleUnits) {
        this.angleUnits = angleUnits;
        int val = device.read8(registers.get("BNO055_UNIT_SEL_ADDR"));
        device.write8(registers.get("BNO055_UNIT_SEL_ADDR"), (byte) ((val & 0b11111101) | (angleUnits.equals(AngleUnits.RADIANS) ? 0b11 : 0b00) << 1));
    }

    public AngleUnits getAngleUnits() {
        return this.angleUnits;
    }

    public void setExtCrystalUse(boolean usextal) {
        OperationMode lastMode = this.mode;

        /* Switch to config mode (just in case since this is the default) */
        setMode(OperationMode.CONFIG);
        delay(25);
        device.write8(registers.get("BNO055_PAGE_ID_ADDR"), (byte) 0);
        if (usextal) {
            device.write8(registers.get("BNO055_SYS_TRIGGER_ADDR"), (byte) 0x80);
        } else {
            device.write8(registers.get("BNO055_SYS_TRIGGER_ADDR"), (byte) 0x00);
        }
        delay(10);
        /* Set the requested operating mode (see section 3.3) */
        setMode(lastMode);
        delay(20);
    }

    public int getTemperature() {
        return device.read8(registers.get("BNO055_TEMP_ADDR")) * (getTemperatureUnits().equals(TemperatureUnits.FAHRENHEIT) ? 2 : 1);
    }

    public Vector getVector(int startRegister, double scale) {
        ByteBuffer buf = ByteBuffer.wrap(device.read(startRegister, 6));
        buf.order(ByteOrder.LITTLE_ENDIAN);
//        RobotLog.i("Bytes (" + System.nanoTime() + ")");
//        displayBytes(buf);
        double x = (double) buf.getShort(0),
                y = (double) buf.getShort(2),
                z = (double) buf.getShort(4);
        return new Vector(x / scale, y / scale, z / scale);
    }

    /** @return micro Teslas */
    public Vector getMagneticFlux() {
        return getVector(registers.get("BNO055_MAG_DATA_X_LSB_ADDR"), 16);
    }

    /** @return angle units per second */
    public Vector getAngularVelocity() {
        return getVector(registers.get("BNO055_GYRO_DATA_X_LSB_ADDR"),
                getAngleUnits().equals(AngleUnits.RADIANS) ? 900 : 16
        );
    }

    /** @return angle units */
    public Vector getEulerAngles() {
        return getVector(registers.get("BNO055_EULER_H_LSB_ADDR"),
                getAngleUnits().equals(AngleUnits.RADIANS) ? 900 : 16
        );
    }

    /** @return meters per second^2 */
    public Vector getAcceleration() {
        return getVector(registers.get("BNO055_ACCEL_DATA_X_LSB_ADDR"), 100);
    }

}
