package com.getyasa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by maxim.vasilkov@gmail.com  on 22/12/15.
 */
public class InverterLayout extends FrameLayout  {

    // structure to hold our color filter
    private Paint paint = new Paint();

    // the color filter itself
    private ColorFilter cf;

    public InverterLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
        construct();
    }

    public InverterLayout(Context context) {
        super(context);
        construct();
    }

    void construct() {
        // construct the inversion color matrix
        float[] mat = new float[]
                {
                        0,   0,  0,  0,  128,
                        0,   0,  0,  0,  128,
                        0,   0,  0,  0,  128,
                        0,   0,  0,  -0.5f, 128
                };
        cf = new ColorMatrixColorFilter(new ColorMatrix(mat));
    }



    @Override
    protected void dispatchDraw(Canvas c) {
        // create a temporary bitmap to draw the child views
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cc = new Canvas(b); // draw them to that temporary bitmap

        super.dispatchDraw(cc); // copy the temporary bitmap to screen without the inversion filter

        paint.setColorFilter(cf);
        c.drawBitmap(b, 0, 0, paint);

        // copy the inverted rectangle
        paint.setColorFilter(cf);


        // the rectangle we want to invert
        Rect inversion_rect = new Rect(0, 0, getWidth(), getHeight());
        c.drawBitmap(b, inversion_rect, inversion_rect, paint);
    }
}