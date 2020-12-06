// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package Main.signature;

import java.math.BigInteger;

public class Point {

    final private BigInteger x;
    final private BigInteger y;

    public static Point POINT_INFINITY = new Point(null , null);

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public boolean equals(Point pnt) {
        if (this == pnt) return true;
        if (this == POINT_INFINITY) return false;
        if (x != null && y != null && pnt.getX() != null && pnt.getY() != null)
            return ((x.equals(pnt.getX())) && (y.equals(pnt.getY())));
        return false;
    }

    public String toString() {
        return "Xp = " + getX() + " (коэффициент точки эллиптической кривой)\n" +
        "Yp = " + getY() + " (коэффициент точки эллиптичекой кривой)\n";
    }
}