// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import major.MessageManager;

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
    private final MessageManager msg = new MessageManager();

    public String signing (BigInteger hash, BigInteger d, SignatureParameters parameters){
        this.parameters = parameters;
        curveOperation = new EllipticCurve(parameters);
        this.d = d;
        e = hash.mod(parameters.q);
        if (e.equals(BigInteger.ZERO))
            e = BigInteger.ONE;
        randK();
        calcS();

        //проверка
        var curveOperation = new EllipticCurve(parameters);
        var Q = curveOperation.scalar(d, parameters.P);
        var ver = new Verify();
        if (!ver.check(concatenation(), Q, hash, parameters))
            signing(hash, d, parameters);

        return concatenation();
    }

    // Генерация псевдослучайного числа k
    // см. (16) Стандарта
    private void randK() {
        var rand = new Random();
        k = new BigInteger(parameters.q.bitLength(), rand);
        while (k.compareTo(parameters.q) >= 0 || k.compareTo(BigInteger.ZERO) < 1)
            k = new BigInteger(parameters.q.bitLength(), rand);
        genC();
    }

    // Вычисление точки эллиптической кривой
    // см. Шаг 4
    private void genC (){
        var pnt = curveOperation.scalar(k, parameters.P);
        setR(pnt);
    }

    // см. (17)
    private void setR (Point C) {
        r = C.getX().mod(parameters.q);
        if (r.equals(BigInteger.ZERO))
            randK();
    }

    // см. (18)
    private void calcS() {
        s = ((r.multiply(d)).add(k.multiply(e))).mod(parameters.q);
        if (s.equals(BigInteger.ZERO))
            randK();
    }

    // Дополнение векторов до определённой длины (длина модуля элллиптической кривой), что в дальнейшем позволит восстановить r и s
    private String completion (BigInteger num) {
        var str = new StringBuilder(num.toString(16));
        if (str.length() > parameters.p.bitLength())
            msg.basicErrors(3);
        while (str.length() < parameters.p.bitLength() / 4) //1 цифра кодируется 4 битами
            str.insert(0, "0");
        return str.toString();
    }

    // Конкатенация (объединение) векторов
    // см. Шаг 6
    private String concatenation() {
        return completion(r) + completion(s);
    }

}
