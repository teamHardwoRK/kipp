package com.teamhardwork.kipp.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.utilities.GraphicsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SadFaceDrawable extends Drawable {
    Paint.Style paintStyle = Paint.Style.STROKE;
    int headColor = Color.RED;
    int featuresColor = Color.RED;
    int alpha = 255;
    float strokeWidth = GraphicsUtils.dpToPx(3);
    float radius;
    Paint headPaint;
    Paint featuresPaint;

    public SadFaceDrawable(float radius) {
        this.radius = radius;

        headPaint = new Paint();
        headPaint.setAntiAlias(true);

        featuresPaint = new Paint();
        featuresPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        Point center = new Point(getBounds().width()/2, getBounds().height()/2);

        headPaint.setColor(headColor);
        headPaint.setAlpha(alpha);
        headPaint.setStrokeWidth(strokeWidth);

        featuresPaint.setStyle(Paint.Style.STROKE);
        featuresPaint.setColor(featuresColor);
        featuresPaint.setAlpha(alpha);
        featuresPaint.setStrokeWidth(strokeWidth);

        // Paint head
        if(paintStyle == Paint.Style.FILL) {
            headPaint.setStyle(Paint.Style.FILL);
        }
        else {
            headPaint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawCircle(center.x, center.y, radius - GraphicsUtils.dpToPx(5), headPaint);

        // Paint mouth
        float mouthTop = center.y + radius / 2;
        float mouthLeft = center.x - radius / 2;

        RectF mouthBounds = new RectF(mouthLeft, mouthTop, mouthLeft + radius, mouthTop + radius);
        canvas.drawArc(mouthBounds, 220, 100, false, featuresPaint);

        // Paint eyes
        featuresPaint.setStyle(Paint.Style.FILL);

        float leftTop = center.y - radius/2;
        float leftLeft = center.x - radius/2;

        RectF leftEye = new RectF(leftLeft, leftTop, leftLeft + radius/4, center.y);
        canvas.drawOval(leftEye, featuresPaint);

        float rightLeft = center.x + radius/4;
        RectF rightEye = new RectF(rightLeft, leftTop, rightLeft + radius/4, center.y);
        canvas.drawOval(rightEye, featuresPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public static List<SadFaceDrawable> createPopulation(Context context, int count) {
        List<SadFaceDrawable> people = new ArrayList<SadFaceDrawable>();

        Resources resources = context.getResources();
        int red = resources.getColor(R.color.Red);
        int lightRed = resources.getColor(R.color.VioletRed);
        int darkRed = resources.getColor(R.color.Burgundy);
        int lightOrange = resources.getColor(R.color.PumpkinOrange);
        int orange = resources.getColor(R.color.ShockingOrange);
        int yellow = resources.getColor(R.color.RubberDuckyYellow);
        int white = resources.getColor(R.color.GrayCloud);
        int black = resources.getColor(R.color.Black);

        List<FaceColoring> coloringList = new ArrayList<FaceColoring>();
        coloringList.add(new FaceColoring(red, red, Paint.Style.STROKE));
        coloringList.add(new FaceColoring(white, red, Paint.Style.STROKE));
        coloringList.add(new FaceColoring(lightOrange, lightOrange, Paint.Style.STROKE));
        coloringList.add(new FaceColoring(yellow, black, Paint.Style.FILL));
        coloringList.add(new FaceColoring(darkRed, black, Paint.Style.FILL));
        coloringList.add(new FaceColoring(red, black, Paint.Style.FILL));
        coloringList.add(new FaceColoring(black, orange, Paint.Style.FILL));
        coloringList.add(new FaceColoring(lightRed, white, Paint.Style.FILL));
        coloringList.add(new FaceColoring(red, white, Paint.Style.FILL));
        coloringList.add(new FaceColoring(darkRed, yellow, Paint.Style.FILL));

        for (int i = 0; i < count; i++) {
            float radius = (new Random().nextInt(100) / 60) * GraphicsUtils.dpToPx(40);
            for(FaceColoring coloring : coloringList) {
                SadFaceDrawable sadFace = new SadFaceDrawable(radius);
                sadFace.setAlpha(new Random().nextInt(155) + 90);
                sadFace.paintStyle = coloring.paintStyle;
                sadFace.headColor = coloring.headColor;
                sadFace.featuresColor = coloring.featuresColor;
                sadFace.strokeWidth = GraphicsUtils.dpToPx(new Random().nextInt(3)) + GraphicsUtils.dpToPx(2);

                people.add(sadFace);
            }
        }
        return people;
    }

    static class FaceColoring {
        public int headColor;
        public int featuresColor;
        public Paint.Style paintStyle;

        FaceColoring(int headColor, int featuresColor, Paint.Style paintStyle) {
            this.headColor = headColor;
            this.featuresColor = featuresColor;
            this.paintStyle = paintStyle;
        }
    }

}
