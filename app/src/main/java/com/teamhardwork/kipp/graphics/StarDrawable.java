package com.teamhardwork.kipp.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.utilities.GraphicsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarDrawable extends Drawable {
    private Paint.Style paintStyle = Paint.Style.STROKE;
    private int fillColor = Color.BLACK;
    private int strokeColor = Color.BLACK;
    private float strokeWidth = GraphicsUtils.dpToPx(3);
    private int longLength;
    private int shortLength;

    public StarDrawable(int longLength, int shortLength) {

        this.longLength = longLength;
        this.shortLength = shortLength;
    }

    public static List<StarDrawable> createUniverse(Context context, int count) {
        List<StarDrawable> starDrawableList = new ArrayList<StarDrawable>();

        Resources resources = context.getResources();
        int colorBlue = resources.getColor(R.color.BlueZircon);
        int colorRed = resources.getColor(R.color.WatermelonPink);
        int colorYellow = resources.getColor(R.color.Mustard);
        int colorWhite = resources.getColor(R.color.Pearl);
        int colorGreen = resources.getColor(R.color.GreenThumb);
        int colorOrange = resources.getColor(R.color.PumpkinOrange);

        List<Integer> colorList = new ArrayList<Integer>();
        colorList.add(colorBlue);
        colorList.add(colorRed);
        colorList.add(colorYellow);
        colorList.add(colorWhite);
        colorList.add(colorGreen);
        colorList.add(colorOrange);

        List<Length> lengthList = new ArrayList<Length>();
        lengthList.add(new Length(GraphicsUtils.dpToPx(20), GraphicsUtils.dpToPx(10)));
        lengthList.add(new Length(GraphicsUtils.dpToPx(40), GraphicsUtils.dpToPx(20)));
        lengthList.add(new Length(GraphicsUtils.dpToPx(30), GraphicsUtils.dpToPx(10)));
        lengthList.add(new Length(GraphicsUtils.dpToPx(50), GraphicsUtils.dpToPx(20)));

        for (int i = 0; i < count; i++) {
            List<StarDrawable> subList = new ArrayList<StarDrawable>();
            for (Length length : lengthList) {
                StarDrawable star = new StarDrawable(length.longLength, length.shortLength);

                Paint.Style[] styleList = {Paint.Style.STROKE, Paint.Style.FILL_AND_STROKE, Paint.Style.STROKE,
                        Paint.Style.FILL_AND_STROKE, Paint.Style.STROKE};
                Paint.Style style = styleList[new Random().nextInt(styleList.length)];

                switch (style) {
                    case FILL_AND_STROKE:
                        star.setPaintStyle(Paint.Style.FILL_AND_STROKE);
                        star.setFillColor(randomColor(colorList));
                        star.setStrokeColor(randomColor(colorList));
                        break;
                    case STROKE:
                        star.setPaintStyle(Paint.Style.STROKE);
                        star.setStrokeColor(randomColor(colorList));
                        break;
                }
                subList.add(star);
            }
            starDrawableList.addAll(subList);
        }
        return starDrawableList;
    }

    static int randomColor(List<Integer> colorList) {
        return colorList.get(new Random().nextInt(colorList.size()));
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

    static class Length {
        public int longLength;
        public int shortLength;

        public Length(int longLength, int shortLength) {
            this.longLength = longLength;
            this.shortLength = shortLength;
        }
    }
}
