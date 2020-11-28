// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import major.MessageManager;

import java.math.BigInteger;

/**
 * Класс реализует функцию проверки ЭЦП
 * http://protect.gost.ru/v.aspx?control=8&baseC=6&page=0&month=1&year=2019&search=&RegNum=1&DocOnPageCount=15&id=224247&pageK=56ACFF06-585B-4C86-AFBB-70BAC6EE97D2
 */
public class Verify {
    private String sign;
    private Point Q;
    private BigInteger hash;
    private BigInteger r;
    private BigInteger s;
    private BigInteger e;

    private final MessageManager msg = new MessageManager();

    public boolean check (String sign, Point Q, BigInteger hash) {
        this.sign = sign;
        this.Q = Q;
        this.hash = hash;

        extraction();

        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(Constants.q) >= 0 || s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(Constants.q) >= 0 )
            return false;

        calcE();

        var R = calcC().getX().mod(Constants.q);

        return R.compareTo(r) == 0;
    }

    /**
     * Вычисление r и s по полученной подписи
     * см. Шаг 1
     */
    private void extraction() {
        try {
            r = new BigInteger(sign.substring(0, Constants.p.bitLength() / 4), 16);
            s = new BigInteger(sign.substring(Constants.p.bitLength() / 4), 16);
        }
        catch (NumberFormatException e) {
            msg.basicErrors(2);
        }
    }

    /**
     * Определение значения e по хэшу сообщения
     * см. Шаг 2-3
     */
    private void calcE() {
        var alpha = hash;
        e = alpha.mod(Constants.q);
        if (e.compareTo(BigInteger.ZERO) == 0)
            e = BigInteger.ONE;
    }

    /**
     * Вычисление точки C эллиптической кривой
     * см. Шаг 5-6
     */
    private Point calcC() {
        var v = e.modInverse(Constants.q);
        var z1 = (s.multiply(v)).mod(Constants.q);
        var z2 = (BigInteger.valueOf(-1).multiply(r.multiply(v))).mod(Constants.q);

        var curveOperation = new EllipticCurve();
        return curveOperation.sum(curveOperation.scalar(z1, Constants.P), curveOperation.scalar(z2, Q));
    }

}
