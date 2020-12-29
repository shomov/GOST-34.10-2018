// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package gost.signature;

import java.math.BigInteger;


/**
 * Базовые параметры эллиптической кривой
 * http://protect.gost.ru/v.aspx?control=8&baseC=6&page=0&month=1&year=2019&search=&RegNum=1&DocOnPageCount=15&id=224247&pageK=8E69DFE1-1A5C-4CF4-A10C-9C35E90341F2
 */
    public record SignatureParameters (Integer digit, BigInteger p, BigInteger a,
                                       BigInteger b, BigInteger m, BigInteger q, Point P) {
    @Override
    public String toString() {
        return "Параметры эллиптической кривой\n" +
                "p = " + p + " (модуль эллиптической кривой)\n" +
                "a = " + a + " (коэффициент эллиптичекой кривой)\n" +
                "b = " + b + " (коэффициент эллиптичекой кривой)\n" +
                "m = " + m + " (порядок группы точек эллиптической кривой)\n" +
                "q = " + q + " (порядок циклической подгруппы группы точек эллиптической кривой)\n" +
                P.toString(); //Р - точка эллиптической кривой
    }
}
