package com.acmerobotics.library.image;

import android.graphics.Bitmap;

import com.acmerobotics.library.tree.Node;

public class StreamNode extends Node<Bitmap, Bitmap> {

    private StreamListener callback;

    public interface StreamListener {
        public void onFrame(Bitmap bitmap);
    }

    public StreamNode() {
    }

    public void setCallback(StreamListener listener) {
        callback = listener;
    }

    @Override
    public Bitmap process(Bitmap input) {
        if (callback != null) {
            callback.onFrame(input);
        }
        return input;
    }
}
