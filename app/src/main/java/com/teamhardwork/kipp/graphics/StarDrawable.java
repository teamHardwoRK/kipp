package com.teamhardwork.kipp.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import java.util.List;

public class StarDrawable extends Drawable {
    private Paint.Style paintStyle = Paint.Style.STROKE;
    private int fillColor = Color.BLACK;
    private int strokeColor = Color.BLACK;
    private float strokeWidth = 0;
    private int longLength;
    private int shortLength;

    public StarDrawable(int longLength, int shortLength) {

        this.longLength = longLength;
        this.shortLength = shortLength;
    }

    @Override
    public void draw(Canvas canvas) {
        int y = getBounds().height() / 2;
        int x = getBounds().width() / 2;
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

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return 0;
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
