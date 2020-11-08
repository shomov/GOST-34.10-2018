package signature;

import java.math.BigInteger;

public class EllipticCurve {

    /**
     * Реализация скалярного произведения точки эллиптической кривой на число
     */
    public Point scalar (BigInteger k, Point P) {
        var result = P;
        while (k.equals(BigInteger.ZERO)) {
            result = sum(result, P);
            k = k.subtract(BigInteger.ONE);
        }

        return result;
    }

    /**
     * Функция реализует операцию сложения двух точек эллиптической кривой
     * Расчётные ыормулы предствлены в соответствующей части ГОСТ-34.10-2018
     * http://protect.gost.ru/v.aspx?control=8&baseC=6&page=0&month=1&year=2019&search=&RegNum=1&DocOnPageCount=15&id=224247&pageK=E8B73425-BBD6-4AD6-9202-39793648B123
     */
    public Point sum (Point a, Point b) {
        var result = new Point();
        // Формула 4
        if (!a.getX().equals(b.getX())) {
            var lambda = ((b.getY().subtract(a.getY())).divide(b.getX().subtract(a.getX()))).mod(Constants.p);
            result.setX(lambda.pow(2).subtract(a.getX()).subtract(b.getX().mod(Constants.p)));
            result.setY(lambda.multiply(a.getX().subtract(result.getX())).subtract(a.getX().mod(Constants.p)));
        }
        // Формула 5
        else if (a.getX().equals(b.getX()) && a.getY().equals(b.getY()) && !a.getY().equals(BigInteger.ZERO)) {
            var lambda = ((BigInteger.valueOf(3).multiply(a.getX().pow(2)).add(Constants.a)).divide(BigInteger.TWO.multiply(a.getY()))).mod(Constants.p);
            result.setX(lambda.pow(2).subtract(BigInteger.TWO).multiply(a.getX().mod(Constants.p)));
            result.setY(lambda.multiply(a.getX().subtract(result.getX())).subtract(a.getY().mod(Constants.p)));
        }

        return result;
    }



}
