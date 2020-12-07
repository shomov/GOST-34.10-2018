// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package Main.signature;

import java.math.BigInteger;
import java.util.Random;

/**
 * Класс реализует процесс формирования ЭЦП и ключа
 * Алгоритмы, реализованные данным классом, описываются на страницах 6-7 настоящего Стандарта
 * http://protect.gost.ru/v.aspx?control=8&baseC=6&page=0&month=1&year=2019&search=&RegNum=1&DocOnPageCount=15&id=224247&pageK=B45A6C4D-DAD9-46F1-829E-9A8C682166D4
 */
public class Sign {
    private SignatureParameters parameters;
    private BigInteger k;
    private BigInteger e;
    private BigInteger d;
    private BigInteger r;
    private BigInteger s;

    private EllipticCurve curveOperation;

    public String signing (BigInteger hash, BigInteger d, SignatureParameters parameters) throws Exception {
        this.parameters = parameters;
        curveOperation = new EllipticCurve(parameters);
        this.d = d;
        calcE(hash);
        randK();

        //проверка
//        var curveOperation = new EllipticCurve(parameters);
//        var Q = curveOperation.scalar(d, parameters.P);
//        var ver = new Verify();
        var signature = concatenation();
//        if (!ver.check(signature, Q, hash, parameters))
//            signing(hash, d, parameters);

        return signature;
    }

    private void calcE(BigInteger hash) {
        e = hash.mod(parameters.q);
        if (e.equals(BigInteger.ZERO))
            e = BigInteger.ONE;
    }

    // Генерация псевдослучайного числа k
    // см. (16) Стандарта
    private void randK() throws Exception {
        var rand = new Random();
        k = new BigInteger(parameters.q.bitLength(), rand);
        while (k.compareTo(parameters.q) >= 0 || k.compareTo(BigInteger.ZERO) < 1)
            k = new BigInteger(parameters.q.bitLength(), rand);
        genC();
    }

    // Вычисление точки эллиптической кривой
    // см. Шаг 4
    private void genC() throws Exception {
        var pnt = curveOperation.scalar(k, parameters.P);
        calcR(pnt);
    }

    // см. (17)
    private void calcR(Point C) throws Exception {
        r = C.getX().mod(parameters.q);
        if (r.equals(BigInteger.ZERO) || r.toString(16).length() > parameters.p.bitLength() / 4)
            randK();
        calcS();
    }

    // см. (18)
    private void calcS() throws Exception {
        s = ((r.multiply(d)).add(k.multiply(e))).mod(parameters.q);
        if (s.equals(BigInteger.ZERO) || s.toString(16).length() > parameters.p.bitLength() / 4)
            randK();
    }

    // Дополнение векторов до определённой длины (длина модуля элллиптической кривой), что в дальнейшем позволит восстановить r и s
    private String completion (BigInteger num) throws Exception {
        var str = new StringBuilder(num.toString(16));
        while (str.length() < parameters.p.bitLength() / 4) //1 цифра кодируется 4 битами
            str.insert(0, "0");
        if (str.length() > parameters.p.bitLength() / 4)
            randK();
        return str.toString();
    }

    // Конкатенация (объединение) векторов
    // см. Шаг 6
    private String concatenation() throws Exception {
        return completion(r) + completion(s);
    }

}
