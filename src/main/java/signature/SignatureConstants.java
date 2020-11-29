// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import java.math.BigInteger;


/**
 * Базовые параметры эллиптической кривой
 * http://protect.gost.ru/v.aspx?control=8&baseC=6&page=0&month=1&year=2019&search=&RegNum=1&DocOnPageCount=15&id=224247&pageK=8E69DFE1-1A5C-4CF4-A10C-9C35E90341F2
 */
public class SignatureConstants {
    // Модуль эллиптической кривой
    public static final BigInteger p = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564821041");
    // Коэффициенты эллиптичекой кривой
    public static final BigInteger a = new BigInteger("7");
    public static final BigInteger b = new BigInteger("43308876546767276905765904595650931995942111794451039583252968842033849580414");
    // Порядок группы точек эллиптической кривой
    public static final BigInteger m = new BigInteger("57896044618658097711785492504343953927082934583725450622380973592137631069619");
    // Порядок циклической подгруппы группы точек эллиптической кривой
    public static final BigInteger q = new BigInteger("57896044618658097711785492504343953927082934583725450622380973592137631069619");
    // Коэффициенты точки эллиптической кривой
    public static final BigInteger xp = new BigInteger("2");
    public static final BigInteger yp = new BigInteger("4018974056539037503335449422937059775635739389905545080690979365213431566280");
    public static Point P = new Point(xp, yp);

}
