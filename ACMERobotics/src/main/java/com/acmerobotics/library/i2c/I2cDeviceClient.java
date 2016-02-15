package com.acmerobotics.library.i2c;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class I2cDeviceClient implements HardwareDevice {

    public final int msPortReadyDelay = 1;

    private final DeviceInterfaceModule dim;

    private Lock readCacheLock;
    private Lock writeCacheLock;
    private byte[] readCache;
    private byte[] writeCache;

    private final int port;
    private int address;

    public I2cDeviceClient(DeviceInterfaceModule dim, int address, int port) {
        this.dim = dim;
        this.port = port;
        this.address = address;

        readCache = dim.getI2cReadCache(port);
        writeCache = dim.getI2cWriteCache(port);
        readCacheLock = dim.getI2cReadCacheLock(port);
        writeCacheLock = dim.getI2cWriteCacheLock(port);
    }

    public synchronized void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO exception handling
        }
    }

    public synchronized void waitForReady() {
        while (!dim.isI2cPortReady(port)) {
            delay(msPortReadyDelay);
        }
    }

    public byte[] read(int start, int len) {
        waitForReady();
        synchronized(readCacheLock) {
            dim.enableI2cReadMode(port, address, start, len);
            dim.writeI2cCacheToController(port);
            waitForReady();
            return Arrays.copyOfRange(readCache, 0, len);
        }
    }

    public void write(int start, byte[] data, int len) {
        waitForReady();
        synchronized (writeCacheLock) {
            dim.enableI2cWriteMode(port, address, start, len);
            dim.copyBufferIntoWriteBuffer(port, data);
            dim.writeI2cCacheToController(port);
            waitForReady();
        }
    }

    public byte read8(int register) {
        return this.read(register, 1)[0];
    }

    public void write8(int register, byte data) {
        this.write(register, new byte[]{data}, 1);
    }

    public void write8(int register, int data) {
        this.write8(register, ((byte) data) & 0xFF);
    }

    public int readInt(int register, int len, ByteOrder bo) {
        byte[] result = read(register, len);
        if (bo.equals(ByteOrder.LITTLE_ENDIAN)) {
            for (int i = 0; i < result.length / 2; i++) {
                byte temp = result[i];
                result[i] = result[result.length - 1 - i];
                result[result.length - 1 - i] = temp;
            }
        }
        int num = result[0] & 0xff;
        for (int i = 1; i < len; i++) {
            num <<= 8;
            num |= result[i] & 0xff;
        }
        return num;
    }

    public int readInt(int register, int len) {
        return readInt(register, len, ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void close() {

    }
}
