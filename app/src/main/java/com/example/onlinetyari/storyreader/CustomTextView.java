package com.example.onlinetyari.storyreader;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Siddharth Verma on 10/6/16.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b){
        return super.setFrame(l, t, l+(b-t), t+(r-l));
    }

    @Override
    public void draw(Canvas canvas){

        super.draw(canvas);
    }
}
