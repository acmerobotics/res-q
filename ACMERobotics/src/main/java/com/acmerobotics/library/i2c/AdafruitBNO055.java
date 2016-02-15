package com.acmerobotics.library.i2c;

import com.acmerobotics.library.data.Acceleration;
import com.acmerobotics.library.data.AngularVelocity;
import com.acmerobotics.library.data.EulerAngle;
import com.acmerobotics.library.data.MagneticFlux;
import com.acmerobotics.library.data.Vector;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class AdaFruitBNO055 extends GyroSensor {

    public static final int BNO055ESS_A_ADDR = (0x28);
    public static final int BNO055ESS_B_ADDR = (0x29);
    public static final int BNO055_ID        = (0xA0);
    
    private I2cDeviceClient device; 

    private OperationMode mode;

    enum PowerMode {
        NORMAL (0x00),
        LOWPOWER (0x01),
        SUSPEND (0x02);

        public byte byteVal;

        PowerMode(int val) {
            byteVal = (byte) val;
        }
    }

    enum OperationMode {
        CONFIG (0x00),
        ACCONLY (0x01),
        MAGONLY (0x02),
        GYRONLY (0x03),
        ACCMAG (0x03),
        ACCGYRO (0x04),
        MAGGYRO (0x05),
        AMG (0x07),
        IMUPLUS (0x08),
        COMPASS (0x09),
        M4G (0x0A),
        NDOF_FMC_OFF (0x0B),
        NDOF (0x0C);

        public byte byteVal;

        OperationMode(int val) {
            byteVal = (byte) val;
        }
    }

    public class Registers {
        /* Page id register definition */
        public static final int BNO055_PAGE_ID = 0X07,

        /* PAGE0 REGISTER DEFINITION START*/
        BNO055_CHIP_ID = 0x00,
        BNO055_ACCEL_REV_ID = 0x01,
        BNO055_MAG_REV_ID = 0x02,
        BNO055_GYRO_REV_ID = 0x03  ,
        BNO055_SW_REV_ID_LSB = 0x04,
        BNO055_SW_REV_ID_MSB = 0x05,
        BNO055_BL_REV_ID = 0X06,

        /* Accel data register */
        BNO055_ACCEL_DATA_X_LSB = 0X08,
        BNO055_ACCEL_DATA_X_MSB = 0X09,
        BNO055_ACCEL_DATA_Y_LSB = 0X0A,
        BNO055_ACCEL_DATA_Y_MSB = 0X0B,
        BNO055_ACCEL_DATA_Z_LSB = 0X0C,
        BNO055_ACCEL_DATA_Z_MSB = 0X0D,

        /* Mag data register */
        BNO055_MAG_DATA_X_LSB = 0X0E,
        BNO055_MAG_DATA_X_MSB = 0X0F,
        BNO055_MAG_DATA_Y_LSB = 0X10,
        BNO055_MAG_DATA_Y_MSB = 0X11,
        BNO055_MAG_DATA_Z_LSB = 0X12,
        BNO055_MAG_DATA_Z_MSB = 0X13,

        /* Gyro data registers */
        BNO055_GYRO_DATA_X_LSB = 0X14,
        BNO055_GYRO_DATA_X_MSB = 0X15,
        BNO055_GYRO_DATA_Y_LSB = 0X16,
        BNO055_GYRO_DATA_Y_MSB = 0X17,
        BNO055_GYRO_DATA_Z_LSB = 0X18,
        BNO055_GYRO_DATA_Z_MSB = 0X19,

        /* Euler data registers */
        BNO055_EULER_H_LSB = 0X1A,
        BNO055_EULER_H_MSB = 0X1B,
        BNO055_EULER_R_LSB = 0X1C,
        BNO055_EULER_R_MSB = 0X1D,
        BNO055_EULER_P_LSB = 0X1E,
        BNO055_EULER_P_MSB = 0X1F,

        /* Quaternion data registers */
        BNO055_QUATERNION_DATA_W_LSB = 0X20,
        BNO055_QUATERNION_DATA_W_MSB = 0X21,
        BNO055_QUATERNION_DATA_X_LSB = 0X22,
        BNO055_QUATERNION_DATA_X_MSB = 0X23,
        BNO055_QUATERNION_DATA_Y_LSB = 0X24,
        BNO055_QUATERNION_DATA_Y_MSB = 0X25,
        BNO055_QUATERNION_DATA_Z_LSB = 0X26,
        BNO055_QUATERNION_DATA_Z_MSB = 0X27,

        /* Linear acceleration data registers */
        BNO055_LINEAR_ACCEL_DATA_X_LSB = 0X28,
        BNO055_LINEAR_ACCEL_DATA_X_MSB = 0X29,
        BNO055_LINEAR_ACCEL_DATA_Y_LSB = 0X2A,
        BNO055_LINEAR_ACCEL_DATA_Y_MSB = 0X2B,
        BNO055_LINEAR_ACCEL_DATA_Z_LSB = 0X2C,
        BNO055_LINEAR_ACCEL_DATA_Z_MSB = 0X2D,

        /* Gravity data registers */
        BNO055_GRAVITY_DATA_X_LSB = 0X2E,
        BNO055_GRAVITY_DATA_X_MSB = 0X2F,
        BNO055_GRAVITY_DATA_Y_LSB = 0X30,
        BNO055_GRAVITY_DATA_Y_MSB = 0X31,
        BNO055_GRAVITY_DATA_Z_LSB = 0X32,
        BNO055_GRAVITY_DATA_Z_MSB = 0X33,

        /* Temperature data register */
        BNO055_TEMP = 0X34,

        /* Status registers */
        BNO055_CALIB_STAT = 0X35,
        BNO055_SELFTEST_RESULT = 0X36,
        BNO055_INTR_STAT = 0X37,

        BNO055_SYS_CLK_STAT = 0X38,
        BNO055_SYS_STAT = 0X39,
        BNO055_SYS_ERR = 0X3A,

        /* Unit selection register */
        BNO055_UNIT_SEL = 0X3B,
        BNO055_DATA_SELECT = 0X3C,

        /* Mode registers */
        BNO055_OPR_MODE = 0X3D,
        BNO055_PWR_MODE = 0X3E,

        BNO055_SYS_TRIGGER = 0X3F,
        BNO055_TEMP_SOURCE = 0X40,

        /* Axis remap registers */
        BNO055_AXIS_MAP_CONFIG = 0X41,
        BNO055_AXIS_MAP_SIGN = 0X42,

        /* SIC registers */
        BNO055_SIC_MATRIX_0_LSB = 0X43,
        BNO055_SIC_MATRIX_0_MSB = 0X44,
        BNO055_SIC_MATRIX_1_LSB = 0X45,
        BNO055_SIC_MATRIX_1_MSB = 0X46,
        BNO055_SIC_MATRIX_2_LSB = 0X47,
        BNO055_SIC_MATRIX_2_MSB = 0X48,
        BNO055_SIC_MATRIX_3_LSB = 0X49,
        BNO055_SIC_MATRIX_3_MSB = 0X4A,
        BNO055_SIC_MATRIX_4_LSB = 0X4B,
        BNO055_SIC_MATRIX_4_MSB = 0X4C,
        BNO055_SIC_MATRIX_5_LSB = 0X4D,
        BNO055_SIC_MATRIX_5_MSB = 0X4E,
        BNO055_SIC_MATRIX_6_LSB = 0X4F,
        BNO055_SIC_MATRIX_6_MSB = 0X50,
        BNO055_SIC_MATRIX_7_LSB = 0X51,
        BNO055_SIC_MATRIX_7_MSB = 0X52,
        BNO055_SIC_MATRIX_8_LSB = 0X53,
        BNO055_SIC_MATRIX_8_MSB = 0X54,

        /* Accelerometer Offset registers */
        ACCEL_OFFSET_X_LSB = 0X55,
        ACCEL_OFFSET_X_MSB = 0X56,
        ACCEL_OFFSET_Y_LSB = 0X57,
        ACCEL_OFFSET_Y_MSB = 0X58,
        ACCEL_OFFSET_Z_LSB = 0X59,
        ACCEL_OFFSET_Z_MSB = 0X5A,

        /* Magnetometer Offset registers */
        MAG_OFFSET_X_LSB = 0X5B,
        MAG_OFFSET_X_MSB = 0X5C,
        MAG_OFFSET_Y_LSB = 0X5D,
        MAG_OFFSET_Y_MSB = 0X5E,
        MAG_OFFSET_Z_LSB = 0X5F,
        MAG_OFFSET_Z_MSB = 0X60,

        /* Gyroscope Offset register s*/
        GYRO_OFFSET_X_LSB = 0X61,
        GYRO_OFFSET_X_MSB = 0X62,
        GYRO_OFFSET_Y_LSB = 0X63,
        GYRO_OFFSET_Y_MSB = 0X64,
        GYRO_OFFSET_Z_LSB = 0X65,
        GYRO_OFFSET_Z_MSB = 0X66,

        /* Radius registers */
        ACCEL_RADIUS_LSB = 0X67,
        ACCEL_RADIUS_MSB = 0X68,
        MAG_RADIUS_LSB = 0X69,
        MAG_RADIUS_MSB = 0X6A;
    }

    public AdaFruitBNO055(I2cDeviceClient device) {
        this.device = device;
    }

    // TODO
    public void delay(int ms) {
        device.delay(ms);
    }

    public boolean begin() {
        /* Make sure we have the right device */
        int id = (byte) device.read8(Registers.BNO055_CHIP_ID);
        if(id != BNO055_ID)
        {
            delay(1000); // hold on for boot
            id = device.read8(Registers.BNO055_CHIP_ID);
            if(id != BNO055_ID) {
                return false;  // still not? ok bail
            }
        }

        /* Switch to config mode (just in case since this is the default) */
        setMode(OperationMode.CONFIG);

        /* Reset */
        device.write8(Registers.BNO055_SYS_TRIGGER, 0x20);
        while (device.read8(Registers.BNO055_CHIP_ID) != BNO055_ID)
        {
            delay(10);
        }
        delay(50);

        /* Set to normal power mode */
        setPowerMode(PowerMode.NORMAL);

        device.write8(Registers.BNO055_PAGE_ID, 0);

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

        device.write8(Registers.BNO055_SYS_TRIGGER, (byte) 0x0);
        delay(10);
        /* Set the requested operating mode (see section 3.3) */
        setMode(mode);
        delay(20);

        return true;
    }

    public void setExtCrystalUse(boolean usextal) {
        OperationMode lastMode = this.mode;

        /* Switch to config mode (just in case since this is the default) */
        setMode(OperationMode.CONFIG);
        delay(25);
        device.write8(Registers.BNO055_PAGE_ID, 0);
        if (usextal) {
            device.write8(Registers.BNO055_SYS_TRIGGER, 0x80);
        } else {
            device.write8(Registers.BNO055_SYS_TRIGGER, 0x00);
        }
        delay(10);
        /* Set the requested operating mode (see section 3.3) */
        setMode(lastMode);
        delay(20);
    }

    public int getTemp() {
        return device.readInt(Registers.BNO055_TEMP, 1);
    }

    public Vector getVector(int startRegister, double scale) {
        double x = device.readInt(startRegister, 2),
                y = device.readInt(startRegister + 2, 2),
                z = device.readInt(startRegister + 4, 2);
        return new Vector(x / scale, y / scale, z / scale);
    }

    /** @returns micro Teslas */
    public MagneticFlux getMagneticFlux() {
        return (MagneticFlux) getVector(Registers.BNO055_MAG_DATA_X_LSB, 16);
    }

    /** @returns degrees per second */
    public AngularVelocity getAngularVelocity() {
        return (AngularVelocity) getVector(Registers.BNO055_ACCEL_DATA_X_LSB, 2.5);
    }

    /** @returns degrees */
    public EulerAngle getEulerAngles() {
        return (EulerAngle) getVector(Registers.BNO055_EULER_H_LSB, 16);
    }

    /** @returns meters per second^2 */
    public Acceleration getAcceleration() {
        return (Acceleration) getVector(Registers.BNO055_ACCEL_DATA_X_LSB, 100);
    }

    public void setPowerMode(PowerMode mode) {
        device.write8(Registers.BNO055_PWR_MODE, mode.byteVal);
        delay(10);
    }

    public void setMode(OperationMode mode) {
        this.mode = mode;
        device.write8(Registers.BNO055_OPR_MODE, mode.byteVal);
        delay(30);
    }
    
    @Override
    public void calibrate() {

    }

    @Override
    public boolean isCalibrating() {
        return false;
    }

    @Override
    public int getHeading() {
        return (int) getEulerAngles().x;
    }

    @Override
    public double getRotation() {
        return getEulerAngles().x;
    }

    @Override
    public int rawX() {
        return (int) getAngularVelocity().x;
    }

    @Override
    public int rawY() {
        return (int) getAngularVelocity().y;
    }

    @Override
    public int rawZ() {
        return (int) getAngularVelocity().z;
    }

    @Override
    public void resetZAxisIntegrator() {
        throw new RuntimeException("unimplemented: z-axis integrator");
    }

    @Override
    public String status() {
        return "";
    }

    @Override
    public String getDeviceName() {
        return "BNO055 Bosch IMU";
    }

    @Override
    public String getConnectionInfo() {
        return device.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void close() {

    }
}
