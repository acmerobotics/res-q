package com.acmerobotics.library.prompt;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class Prompter {

    public static double COVERAGE = 0.7;

    private Activity activity;
    private PopupWindow window;

    public Prompter(final Activity a) {
        activity = a;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                window = new PopupWindow(a);
            }
        });
    }

    public void setContentView(final View view) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                window.setContentView(view);
            }
        });
    }

    public void show() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View decorView = activity.getWindow().getDecorView();
                int windowWidth = decorView.getWidth();
                int windowHeight = decorView.getHeight();
                int width = (int) Math.round(COVERAGE * windowWidth);
                int height = (int) Math.round(COVERAGE * windowHeight);
                double offset = (1.0 - COVERAGE) / 2.0;
                int offsetX = (int) Math.round(offset * windowWidth);
                int offsetY = (int) Math.round(offset * windowHeight);
                window.setWidth(width);
                window.setHeight(height);
                window.showAtLocation(decorView, Gravity.NO_GRAVITY, offsetX, offsetY);
            }
        });
    }

}
