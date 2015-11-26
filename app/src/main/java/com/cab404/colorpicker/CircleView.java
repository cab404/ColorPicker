package com.cab404.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cab404 on 24.11.15.
 */
public class CircleView extends View {
    private int color = Color.RED;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    RectF sizerect = new RectF();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float side = Math.min(getWidth(), getHeight());

        sizerect.set(0, 0, side, side);
        sizerect.left += getPaddingLeft();
        sizerect.right -= getPaddingRight();
        sizerect.top += getPaddingTop();
        sizerect.bottom -= getPaddingBottom();


        float dp = getContext().getResources().getDisplayMetrics().density;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(sizerect.centerX(), sizerect.centerY(), side / 2, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(dp);
        canvas.drawCircle(sizerect.centerX(), sizerect.centerY(), side / 2, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawCircle(sizerect.centerX(), sizerect.centerY(), side / 2 - 6 * dp, paint);

    }

    void setColor(int color){
        this.color = color;
        invalidate();
    }


}
