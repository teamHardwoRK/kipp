package com.teamhardwork.kipp.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

public class StarSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Paint.Style paintStyle = Paint.Style.STROKE;
    private int fillColor = Color.RED;
    private int strokeColor = Color.RED;
    private float strokeWidth = 5;
    private int longLength;
    private int shortLength;

    public StarSurfaceView(Context context, int longLength, int shortLength) {
        super(context);
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);

        this.longLength = longLength;
        this.shortLength = shortLength;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas(null);
        draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        int y = getHeight() / 2;
        int x = getWidth() / 2;

        Point center = new Point(x, y);
        Paint paint;

        switch (paintStyle) {
            case FILL:
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(fillColor);
                drawStar(canvas, paint, center);
                break;
            case FILL_AND_STROKE:
                paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(fillColor);
                drawStar(canvas, paint, center);
                paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(strokeColor);
                drawStar(canvas, paint, center);
            case STROKE:
                paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(strokeColor);
                drawStar(canvas, paint, center);
                break;
        }
        invalidate();
    }

    private void drawStar(Canvas canvas, Paint paint, Point center) {
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);

        Path path = new Path();

        Star star = new Star(center, longLength, shortLength);
        List<Point> pointList = star.getPoints();

        Point startingPoint = pointList.get(0);
        path.moveTo(startingPoint.x, startingPoint.y);

        pointList.remove(startingPoint);

        for (Point point : pointList) {
            path.lineTo(point.x, point.y);
        }
        canvas.drawPath(path, paint);
    }

    public void setPaintStyle(Paint.Style paintStyle) {
        this.paintStyle = paintStyle;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}
