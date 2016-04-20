package com.acmerobotics.library.i2c;

import android.content.res.AssetManager;
import android.os.SystemClock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class I2cChip {

    private String chipFile;

    protected String name;
    protected String manufacturer;
    protected Map<String, String> extra;
    protected int[] addresses;
    protected Map<String, Integer> registers;

    public I2cChip(OpMode mode) {
        Chip chip = this.getClass().getAnnotation(Chip.class);
        chipFile = chip.value().toLowerCase() + ".json";
        AssetManager manager = mode.hardwareMap.appContext.getAssets();
        try {
            InputStream inputStream = manager.open("chips/" + chipFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            RobotLog.i(content.toString());
            JsonObject root = new JsonParser().parse(content.toString()).getAsJsonObject();
            name = root.get("chip").getAsString();
            manufacturer = root.get("manufacturer").getAsString();
            JsonArray addrList = root.get("addresses").getAsJsonArray();
            int addrSize = addrList.size();
            addresses = new int[addrSize];
            for (int i = 0; i < addrSize; i++) {
                addresses[i] = addrList.get(i).getAsInt();
            }
            extra = new HashMap<String, String>();
            JsonObject obj = root.get("extra").getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries) {
                extra.put(entry.getKey(), entry.getValue().getAsString());
            }
            registers = new HashMap<String, Integer>();
            obj = root.get("registers").getAsJsonObject();
            entries = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries) {
                registers.put(entry.getKey(), entry.getValue().getAsInt());
            }
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
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

    protected int[] normalizeByteArray(byte[] buf) {
        int[] b = new int[buf.length];
        for (int i = 0; i < buf.length; i++) {
            b[i] = ((int) buf[i]) & 0xff;
        }
        return b;
    }

    protected int assembleByteArray(int[] arr) {
        String s = "";
        for (int i = 0; i < arr.length; i++) {
            s += arr[i] + " ";
        }
        int n = 0;
        for (int i = 0; i < arr.length; i++) {
            n <<= 8;
            n |= arr[i];
        }
        RobotLog.e(s + "-> " + n);
        return n;
    }

    protected void displayBytes(ByteBuffer buffer) {
        byte[] arr = buffer.array();
        for (int i = 0; i < arr.length; i++) {
            RobotLog.i(String.format("0x%2s", arr[i]));
        }
    }

}