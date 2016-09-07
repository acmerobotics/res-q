package com.acmerobotics.library.sensors.i2c;

import android.content.res.AssetManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class I2cChipJSONParser {

    public static I2cChipData parseJson(String contents) {
        I2cChipData data = new I2cChipData();

        // parse name and manufacturer
        JsonObject root = new JsonParser().parse(contents).getAsJsonObject();
        data.name = root.get("name").getAsString();
        data.manufacturer = root.get("manufacturer").getAsString();

        // parse registers
        Set<Map.Entry<String, JsonElement>> entries;
        data.registers = new HashMap<String, Integer>();
        JsonObject obj = root.get("registers").getAsJsonObject();
        entries = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            data.registers.put(entry.getKey(), entry.getValue().getAsInt());
        }

        // parse extras
        data.registers = new HashMap<String, Integer>();
        obj = root.get("registers").getAsJsonObject();
        entries = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            data.registers.put(entry.getKey(), entry.getValue().getAsInt());
        }

        // parse address list
        JsonArray addrList = root.get("addresses").getAsJsonArray();
        int addrSize = addrList.size();
        data.addresses = new int[addrSize];
        for (int i = 0; i < addrSize; i++) {
            data.addresses[i] = addrList.get(i).getAsInt();
        }

        return data;
    }

}
