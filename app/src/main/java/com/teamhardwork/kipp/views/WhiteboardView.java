package com.teamhardwork.kipp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.teamhardwork.kipp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardView extends View {
    List<PaintPath> paintPathList;
    Context context;
    int paintPathIndex = 0;

    public WhiteboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setFocusable(true);
        setFocusableInTouchMode(true);

        this.context = context;
        setupPaint();
    }

    void setupPaint() {
        paintPathList = new ArrayList<PaintPath>();

        List<Integer> colorList = new ArrayList<Integer>();
        colorList.add(context.getResources().getColor(R.color.DodgerBlue));
        colorList.add(context.getResources().getColor(R.color.Red));
        colorList.add(context.getResources().getColor(R.color.SchoolBusYellow));

        for(Integer color : colorList) {
            PaintPath paintPath = new PaintPath();
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

            WhiteboardPath path = new WhiteboardPath();
            paintPath.paint = paint;
            paintPath.path = path;

            paintPathList.add(paintPath);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(PaintPath paintPath : paintPathList) {
            canvas.drawPath(paintPath.path, paintPath.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PaintPath paintPath = paintPathList.get(paintPathIndex % paintPathList.size());

        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                paintPath.path.moveTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                paintPath.path.lineTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
                paintPathIndex++;
                break;
        }
        postInvalidate();
        return true;
    }

    class PaintPath {
        public Paint paint;
        public WhiteboardPath path;
    }

    class WhiteboardPath extends Path implements Serializable {
        private static final long serialVersionUID = -63167299461009205L;
    }
}
