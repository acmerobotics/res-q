package com.acmerobotics.library.robot;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@SuppressWarnings("ResourceType")
public class RobotUILayout {

    public interface Callback {
        public void layoutCreated(RelativeLayout layout);
        public void layoutDestroyed(RelativeLayout layout);
    }

    private Activity activity;
    private RelativeLayout parentLayout;
    private RelativeLayout robotLayout;
    private Callback callback;

    public RobotUILayout(OpMode mode) {
        activity = (Activity) mode.hardwareMap.appContext;

        callback = null;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parentLayout = (RelativeLayout) activity.findViewById(0x7f0b001a);

                robotLayout = new RelativeLayout(activity);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.BELOW, 0x7f0b001e);
                layoutParams.addRule(RelativeLayout.ABOVE, 0x7f0b001f);
                parentLayout.addView(robotLayout, layoutParams);
                if (callback != null) {
                    callback.layoutCreated(robotLayout);
                }
            }
        });
    }

    public void stop() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parentLayout.removeView(robotLayout);
                if (callback != null) {
                    callback.layoutDestroyed(robotLayout);
                }
            }
        });
    }

}
