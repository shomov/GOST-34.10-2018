// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import java.math.BigInteger;

public class Point {

    private BigInteger x;
    private BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public static final Point POINT_INFINITY = new Point();


    public Point() {
        this.x = null;
        this.y = null;
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public boolean equals(Point pnt) {
        return x == pnt.getX() && y == pnt.getY();
    }
}
