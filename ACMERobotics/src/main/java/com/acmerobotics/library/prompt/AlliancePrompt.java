package com.acmerobotics.library.prompt;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.acmerobotics.library.R;

public class AlliancePrompt {

    public enum AllianceColor {
        BLUE (R.color.allianceBlue),
        RED (R.color.allianceRed);

        public int colorId;

        AllianceColor(int color) {
            this.colorId = color;
        }
    }

    public interface AllianceSelectListener {

        public void onAllianceSelect(AllianceColor color);

    }

    public abstract class OnAllianceViewClickListener implements View.OnClickListener {

        protected AllianceColor color;

        public OnAllianceViewClickListener(AllianceColor color) {
            this.color = color;
        }

        public AllianceColor getColor() {
            return color;
        }
    }

    public double PADDING = 0.2;

    private AllianceSelectListener listener;
    private AllianceColor color;
    private Activity activity;
    private PopupWindow window;

    public AlliancePrompt(Activity a) {
        color = null;
        listener = null;

        activity = a;
    }

    public void setAllianceSelectListener(AllianceSelectListener selectListener) {
        listener = selectListener;
    }

    private View buildContentView(int height) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        AllianceColor[] colors = AllianceColor.values();
        for (AllianceColor color : colors) {
            SolidColorView view = new SolidColorView(activity, activity.getResources().getColor(color.colorId));

            view.setOnClickListener(new OnAllianceViewClickListener(color) {
                @Override
                public void onClick(View view) {
                    AlliancePrompt.this.color = getColor();
                    AlliancePrompt.this.listener.onAllianceSelect(getColor());
                    AlliancePrompt.this.window.dismiss();
                }
            });

            ViewGroup.LayoutParams params = view.getLayoutParams();
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            layout.addView(view);
        }
        return layout;
    }

    public void show() {
        final View decorView = activity.getWindow().getDecorView();
        int width = decorView.getWidth();
        int height = decorView.getHeight();
        final int offsetX = (int) Math.round(PADDING * width);
        final int offsetY = (int) Math.round(PADDING * height);
        final int popupWidth = width - 2 * offsetX;
        final int popupHeight = height - 2 * offsetY;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                window = new PopupWindow(activity);
                window.setContentView(buildContentView(popupHeight));
                window.setWidth(popupWidth);
                window.setHeight(popupHeight);
                window.showAtLocation(decorView, Gravity.NO_GRAVITY, offsetX, offsetY);
            }
        });
    }

}
