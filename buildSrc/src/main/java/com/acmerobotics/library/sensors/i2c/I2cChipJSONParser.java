package com.acmerobotics.library.sensors.i2c;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        data.extra = new HashMap<String, String>();
        obj = root.get("extra").getAsJsonObject();
        entries = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            data.extra.put(entry.getKey(), entry.getValue().getAsString());
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
