// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import major.MessageManager;

import java.math.BigInteger;

public class EllipticCurve {
    SignatureParameters parameters;

    private final MessageManager msg = new MessageManager();

    public EllipticCurve (SignatureParameters parameters) {
        this.parameters = parameters;
    }


    /**
     * Реализация скалярного произведения точки эллиптической кривой на число
     * Алгоритм удвоения-сложения
     * Выполняется чтение бита, если он равен 1 - выполняется сложение, иначе удвоение, затем сдвиг бит
     * https://habr.com/ru/post/335906/
     */
    public Point scalar (BigInteger k, Point point) throws Exception {
        var result = Point.POINT_INFINITY;
        var bits = new int[k.bitLength()];

        var c = k;
        var i = 0;
        while (c.getLowestSetBit() >= 0) {
            bits[i] = 0;
            if (c.getLowestSetBit() == 0)
                bits[i] = 1;
            i++;
            c = c.shiftRight(1);
        }
        i = k.bitLength() - 1;
        while (i != -1){
            result = sum(result, result);
            if (bits[i] == 1)
                result = sum(result, point);
            i--;
        }
        return result;
    }

    /**
     * Функция реализует операцию сложения двух точек эллиптической кривой
     * https://stackoverflow.com/questions/15727147/scalar-multiplication-of-point-over-elliptic-curve
     */
    public Point sum (Point a, Point b) throws Exception {
        if (b.equals(Point.POINT_INFINITY))
            return a;
        else if (a.equals(Point.POINT_INFINITY))
            return b;
        var x = BigInteger.ZERO;
        var y = BigInteger.ZERO;
        try {
            if (b.equals(a)) {
                var lambda = (((a.getX().pow(2)).multiply(BigInteger.valueOf(3))).add(parameters.a)).multiply((a.getY().multiply(BigInteger.TWO)).modInverse(parameters.p));
                x = ((lambda.pow(2).subtract(a.getX().multiply(BigInteger.TWO))).mod(parameters.p));
                y = (a.getY().negate()).add(lambda.multiply(a.getX().subtract(x))).mod(parameters.p);
            } else {
                var lambda = (b.getY().subtract(a.getY())).multiply(b.getX().subtract(a.getX()).modInverse(parameters.p));
                x = (lambda.modPow(BigInteger.TWO, parameters.p).subtract(b.getX()).subtract(a.getX()).mod(parameters.p));
                y = a.getY().negate().mod(parameters.p).add(lambda.multiply(a.getX().subtract(x))).mod(parameters.p);
            }
        } catch (Exception e) {
            msg.basicErrors(3);
        }
        return new Point(x, y);
    }

}
