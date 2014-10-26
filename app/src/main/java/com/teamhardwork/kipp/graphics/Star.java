package com.teamhardwork.kipp.graphics;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Assists in rendering star graphics.
 *
 * @author Kevin Leong
 */
public class Star {
    private static final int INNER_ANGLE = 72;
    private static final int RIGHT_ANGLE = 90;

    private int centerX;
    private int centerY;
    private int longLength;
    private int shortLength;

    public Star(Point center, int longLength, int shortLength) {
        this.centerX = center.x;
        this.centerY = center.y;
        this.longLength = longLength;
        this.shortLength = shortLength;
    }

    public List<Point> getPoints() {
        List<Point> points = new ArrayList<Point>();

        // Start at 12 o'clock and move clockwise.
        points.add(new Point(centerX, centerY - longLength));

        int x = centerX + calculateOffset(Trig.SIN, INNER_ANGLE / 2, shortLength);
        int y = centerY - calculateOffset(Trig.COS, INNER_ANGLE / 2, shortLength);
        points.add(new Point(x, y));

        x = centerX + calculateOffset(Trig.SIN, INNER_ANGLE, longLength);
        y = centerY - calculateOffset(Trig.COS, INNER_ANGLE, longLength);
        points.add(new Point(x, y));

        int angle = INNER_ANGLE / 2 - (RIGHT_ANGLE - INNER_ANGLE);
        x = centerX + calculateOffset(Trig.COS, angle, shortLength);
        y = centerY + calculateOffset(Trig.SIN, angle, shortLength);
        points.add(new Point(x, y));

        angle = angle + INNER_ANGLE / 2;
        x = centerX + calculateOffset(Trig.COS, angle, longLength);
        y = centerY + calculateOffset(Trig.SIN, angle, longLength);
        points.add(new Point(x, y));

        points.add(new Point(centerX, centerY + shortLength));

        for (int i = 4; i >= 0; i--) {
            points.add(flipHorizontal(points.get(i)));
        }

        return points;
    }

    private int calculateOffset(Trig function, int degrees, int length) {
        double radians = Math.toRadians(degrees);
        double ratio = 0;

        switch (function) {
            case COS:
                ratio = Math.cos(radians);
                break;
            case SIN:
                ratio = Math.sin(radians);
                break;
        }
        return (int) (length * ratio);
    }

    private Point flipHorizontal(Point point) {
        return new Point(point.x - 2 * (point.x - centerX), point.y);
    }

    private enum Trig {
        COS,
        SIN;
    }
}
