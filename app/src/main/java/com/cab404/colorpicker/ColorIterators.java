package com.cab404.colorpicker;

import android.graphics.Color;

/**
 * Created by cab404 on 26.11.15.
 */
public class ColorIterators {

    public static ColorCircleView.ColorIterationHandler generateSaturationIterator(int color, final int rotation){
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return new ColorCircleView.ColorIterationHandler() {
            @Override
            public int getIterationColor(int i) {
                i += rotation + 360;
                i %= 360;

                hsv[1] = i / 180f;
                hsv[1] -= 1;
                hsv[1] = Math.abs(hsv[1]);

                return Color.HSVToColor(hsv);
            }
        };
    }

    public static ColorCircleView.ColorIterationHandler generateValueIterator(int color, final int rotation){
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return new ColorCircleView.ColorIterationHandler() {
            @Override
            public int getIterationColor(int i) {

                i += rotation + 360;
                i %= 360;

                hsv[2] = i / 180f;
                hsv[2] -= 1;
                hsv[2] = Math.abs(hsv[2]);

                return Color.HSVToColor(hsv);
            }
        };
    }

    public static ColorCircleView.ColorIterationHandler generateHueIterator(int color, final int rotation){
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return new ColorCircleView.ColorIterationHandler() {
            @Override
            public int getIterationColor(int i) {
                hsv[0] = i + rotation + 360;
                hsv[0] %= 360;
                return Color.HSVToColor(hsv);
            }
        };
    }
}
