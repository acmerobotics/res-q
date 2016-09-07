package com.acmerobotics.library.sensors.i2c;

import java.util.Map;

public class I2cChipData {

    public String name;
    public String manufacturer;
    public Map<String, String> extra;
    public int[] addresses;
    public Map<String, Integer> registers;
    
}
