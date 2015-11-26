package com.cab404.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cab404 on 22.11.15.
 */
public class ColorCircleView extends View {

    public ColorCircleView(Context context) {
        super(context);
    }

    public ColorCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    float thickness = 1/8f;

    float current = 0;

    /* We're using this to draw color circle.*/
    Paint cpaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /* We're using this to make holes.*/
    Paint tpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap temporary;
    Canvas src;

    {
        tpaint.setColor(Color.BLACK);
        tpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    RectF sizerect = new RectF();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        temporary = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        src = new Canvas(temporary);

        remakeCircle(w, h);
    }

    public int getColor(){
        return colorIterationHandler.getIterationColor((int) (Math.toDegrees(current) + 360) % 360);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawBitmap(temporary, 0, 0, cpaint);

        float side = Math.min(sizerect.width(), sizerect.height());
        cpaint.setColor(Color.WHITE);


        // The part where we draw a bar between detector circle and centre
//        canvas.save();
//        canvas.translate(sizerect.centerX(), sizerect.centerY());
//        canvas.rotate((float) Math.toDegrees(current));
//        canvas.drawRect(0, -10, side / 2, 10, cpaint);
//        canvas.restore();

        float circleRadius = side / 2;
        float circleCenter = circleRadius * (1f - thickness);
        float knobRadius = thickness * circleRadius * 0.8f;
        canvas.drawCircle(
                sizerect.centerX() + (float) Math.cos(current) * circleCenter,
                sizerect.centerY() + (float) Math.sin(current) * circleCenter,
                knobRadius,
                cpaint
        );

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        if (widthMeasureSpec > 0 && heightMeasureSpec == ViewGroup.LayoutParams.WRAP_CONTENT)
            heightMeasureSpec = widthMeasureSpec;
        if (heightMeasureSpec > 0 && widthMeasureSpec == ViewGroup.LayoutParams.WRAP_CONTENT)
            widthMeasureSpec = heightMeasureSpec;

//        if (heightMeasureSpec < 0 && widthMeasureSpec < 0)
//            widthMeasureSpec = heightMeasureSpec = 400;

        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }


    public double getKnobAngle(){
        return Math.toDegrees(current);
    }

    void remakeCircle(float w, float h) {
        float side = Math.min(w, h);
        cpaint.setXfermode(null);

        sizerect.set(0, 0, side, side);
        sizerect.left += getPaddingLeft();
        sizerect.right -= getPaddingRight();
        sizerect.top += getPaddingTop();
        sizerect.bottom -= getPaddingBottom();

        remakeCircle();
    }

    void remakeCircle() {
        if (src == null) return;

        for (int i = 0; i < 360; i++) {
            cpaint.setColor(colorIterationHandler.getIterationColor(i));
            /* We can't do exactly one degree section - gaps will be visible. */
            src.drawArc(sizerect, i, 1.5f, true, cpaint);
        }

        float side = Math.min(sizerect.width(), sizerect.height());
        src.drawCircle(sizerect.centerX(), sizerect.centerY(), side / 2 * (1f - thickness * 2), tpaint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float side = Math.min(sizerect.width(), sizerect.height()) / 2;
        float x = event.getX() - sizerect.centerX();
        float y = event.getY() - sizerect.centerY();
        x /= side;
        y /= side;

        float quad = x * x + y * y;
        float cstart = 1f - thickness * 2;
        cstart *= cstart;

        if (quad < cstart | quad > 1)
            return false;

        current = (float) Math.atan2(y, x);

        if (colorChangedListener != null)
            colorChangedListener.onColorChange(getColor());

        invalidate();
        return true;
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    ColorIterationHandler def = new ColorIterationHandler() {
        float[] hsv = new float[3];

        @Override
        public int getIterationColor(int i) {
            hsv[0] = i;
            hsv[1] = 1;
            hsv[2] = 1;
            return Color.HSVToColor(hsv);
        }
    };

    ColorIterationHandler colorIterationHandler = def;

    public interface ColorIterationHandler {
        int getIterationColor(int i);
    }

    public void setColorIterationHandler(ColorIterationHandler colorIterationHandler) {
        if (colorIterationHandler == null) colorIterationHandler = def;
        this.colorIterationHandler = colorIterationHandler;
        remakeCircle();
        invalidate();
    }

    OnColorChangedListener colorChangedListener;

    public interface OnColorChangedListener {
        void onColorChange(int color);
    }

    public void setColorChangedListener(OnColorChangedListener colorChangedListener) {
        this.colorChangedListener = colorChangedListener;
    }

}
