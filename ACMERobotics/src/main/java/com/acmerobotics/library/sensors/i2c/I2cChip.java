package com.acmerobotics.library.sensors.i2c;

import android.os.SystemClock;

import com.acmerobotics.library.util.FileUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class I2cChip implements HardwareDevice {

    private String chipFile;

    protected I2cDeviceSynch device;

    protected String name;
    protected String manufacturer;
    protected Map<String, String> extra;
    protected int[] addresses;
    protected Map<String, Integer> registers;

    public I2cChip(OpMode mode, I2cDeviceSynch device) {
        this.device = device;
        this.device.engage();

        Chip chip = this.getClass().getAnnotation(Chip.class);
        chipFile = chip.value().toLowerCase() + ".json";
        String contents = FileUtils.getAssetFileContents(mode.hardwareMap.appContext, "chips/" + chipFile);
        parseChipJson(contents);
    }

    private void parseChipJson(String contents) {
        JsonObject root = new JsonParser().parse(contents).getAsJsonObject();
        name = root.get("chip").getAsString();
        manufacturer = root.get("manufacturer").getAsString();
        parseAddressList(root);
        parseExtras(root);
        parseRegisters(root);
    }

    private void parseRegisters(JsonObject root) {
        JsonObject obj;
        Set<Map.Entry<String, JsonElement>> entries;
        registers = new HashMap<String, Integer>();
        obj = root.get("registers").getAsJsonObject();
        entries = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            registers.put(entry.getKey(), entry.getValue().getAsInt());
        }
    }

    private void parseExtras(JsonObject root) {
        extra = new HashMap<String, String>();
        JsonObject obj = root.get("extra").getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            extra.put(entry.getKey(), entry.getValue().getAsString());
        }
    }

    private void parseAddressList(JsonObject root) {
        JsonArray addrList = root.get("addresses").getAsJsonArray();
        int addrSize = addrList.size();
        addresses = new int[addrSize];
        for (int i = 0; i < addrSize; i++) {
            addresses[i] = addrList.get(i).getAsInt();
        }
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getExtra(String name) {
        return extra.get(name);
    }

    public Map<String, Integer> getRegisters() {
        return registers;
    }

    public int getRegister(String name) {
        return registers.get(name);
    }

    public void delay(int ms) {
        SystemClock.sleep(ms);
    }

    @Override
    public String getDeviceName() {
        return getManufacturer() + " " + getName();
    }

    @Override
    public String getConnectionInfo() {
        return getDeviceName() + "\n" + device.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void close() {
        device.close();
    }
}
