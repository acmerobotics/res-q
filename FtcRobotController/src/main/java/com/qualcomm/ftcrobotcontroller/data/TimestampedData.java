package com.qualcomm.ftcrobotcontroller.data;

/**
 * Created by Admin on 2/4/2016.
 */
public class TimestampedData<T> {

    private long timestamp;
    private T data;

    public TimestampedData(T data) {
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimeSince(TimestampedData timestampedData) {
        return getTimestamp() - timestampedData.getTimestamp();
    }
}
