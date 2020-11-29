// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import java.math.BigInteger;


/**
 * Базовые параметры эллиптической кривой
 * http://protect.gost.ru/v.aspx?control=8&baseC=6&page=0&month=1&year=2019&search=&RegNum=1&DocOnPageCount=15&id=224247&pageK=8E69DFE1-1A5C-4CF4-A10C-9C35E90341F2
 */
public class SignatureParameters {
    // Разрядность (256 false; 512 true)
    public boolean digit;
    // Модуль эллиптической кривой
    public BigInteger p;
    // Коэффициенты эллиптичекой кривой
    public BigInteger a;
    public BigInteger b;
    // Порядок группы точек эллиптической кривой
    public BigInteger m;
    // Порядок циклической подгруппы группы точек эллиптической кривой
    public BigInteger q;
    // Коэффициенты точки эллиптической кривой
    public BigInteger xp;
    public BigInteger yp;
    public Point P;

    public SignatureParameters(){
        digit = false;
        p = BigInteger.ZERO;
        a = BigInteger.ZERO;
        b = BigInteger.ZERO;
        m = BigInteger.ZERO;
        q = BigInteger.ZERO;
        xp = BigInteger.ZERO;
        yp = BigInteger.ZERO;
    }

    public void setConstants (boolean digit, BigInteger p, BigInteger a,
                         BigInteger b, BigInteger m, BigInteger q,
                         BigInteger xp, BigInteger yp) {
        this.digit = digit;
        this.p = p;
        this.a = a;
        this.b = b;
        this.m = m;
        this.q = q;
        this.xp = xp;
        this.yp = yp;
        this.P = new Point(xp, yp);
    }



}