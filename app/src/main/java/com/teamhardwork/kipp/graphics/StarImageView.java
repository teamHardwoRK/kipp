package com.teamhardwork.kipp.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.widget.ImageView;

import java.util.List;

public class StarImageView extends ImageView {
    private Paint.Style paintStyle = Paint.Style.STROKE;
    private int fillColor = Color.RED;
    private int strokeColor = Color.RED;
    private float strokeWidth = 5;
    private int longLength;
    private int shortLength;

    public StarImageView(Context context) {
        super(context);
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

    public void setLongLength(int longLength) {
        this.longLength = longLength;
    }

    public void setShortLength(int shortLength) {
        this.shortLength = shortLength;
    }
}
