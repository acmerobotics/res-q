package com.acmerobotics.library.image;

import android.graphics.Bitmap;

import com.acmerobotics.library.tree.Node;

public class MirrorNode extends Node<Bitmap, Bitmap> {

    public MirrorNode(Node<?, Bitmap> parent) {
        super(parent);
    }

    @Override
    public Bitmap process(Bitmap input) {
        return input;
    }
}
