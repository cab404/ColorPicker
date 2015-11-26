package com.cab404.colorpicker;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cab404 on 24.11.15.
 */
public class ColorPickerFragment extends Fragment {

    private ColorCircleView circle;
    private CircleView color;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ccolpicker_palette, container, false);

        circle = (ColorCircleView) view.findViewById(R.id.ccp_hue_circle);
        color = (CircleView) view.findViewById(R.id.ccp_color_circle);

        return view;

    }

    int csel = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circle.setScaleX(0);
        circle.setScaleY(0);
        circle.setAlpha(1);

        circle.animate().scaleX(1).scaleY(1).alpha(1).setStartDelay(1000).start();
        circle.setColorIterationHandler(ColorIterators.generateHueIterator(0xffdf1281, 0));
        circle.setColorChangedListener(new ColorCircleView.OnColorChangedListener() {
            @Override
            public void onColorChange(int new_color) {
                color.setColor(new_color);
            }
        });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csel = (csel + 1) % 3;
                ColorCircleView.ColorIterationHandler cih;
                switch (csel) {
                    case 0:
                        cih = ColorIterators.generateHueIterator(circle.getColor(), (int) -circle.getKnobAngle());
                        break;
                    case 1:
                        cih = ColorIterators.generateSaturationIterator(circle.getColor(), (int) -circle.getKnobAngle());
                        break;
                    case 2:
                        cih = ColorIterators.generateValueIterator(circle.getColor(), (int) -circle.getKnobAngle());
                        break;
                    default:
                        return;
                }
                switchTo(cih);
            }
        });
    }


    protected void switchTo(final ColorCircleView.ColorIterationHandler cih){
        circle.animate().setStartDelay(0).scaleX(0).scaleY(0).alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        circle.setColorIterationHandler(cih);
                        circle.animate().scaleX(1).scaleY(1).alpha(1).setStartDelay(0).setListener(null).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();

    }


}
