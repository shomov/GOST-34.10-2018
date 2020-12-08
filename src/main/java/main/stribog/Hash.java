// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package main.stribog;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * В данном классе реализуется одна из составляющих алгоритма генерации электронной цифровой подписи - хэш функция
 * Описывает данный алгоритм ГОСТ 34.11-2018 или Стрибог
 *  "http://protect.gost.ru/v.aspx?control=7&id=232143"
 */
public class Hash {
    private boolean method = true;
    private final int[] iv = new int[64];
    private int[] N = new int[64];
    private int[] sig = new int[64];

    private final StribogConstants stribogConstants = new StribogConstants();

    /**
     * Инициализация хэш-функции, установка начальных значений
     * @param length Для реализации с ключом 512 бит хранит значение true, в противном случае false
     * 1 этап согласно документации ГОСТ 34.11-2018
     * http://protect.gost.ru/v.aspx?control=8&baseC=-1&page=0&month=-1&year=-1&search=&RegNum=1&DocOnPageCount=15&id=224241&pageK=6C6D6BF2-DDD4-4BD9-8037-943B58998298
     */
    public Hash(int length) {
        var a = 0x00;
        if (length == 256) {
            this.method = false;
            a = 0x01;
        }
        Arrays.fill(N, 0x00);
        Arrays.fill(sig, 0x00);
        Arrays.fill(iv, a);       
    }

    public BigInteger getHash (int[] message){
        var m = new int[64];
        var h = iv;
        var l = message.length;

        //Разбиение сообщения на блоки 64 байт
        while (l > 64) {
            System.arraycopy(message, l - 64, m, 0, 64);
            h = gN(h, m, N);
            N = ringAdd(N, stribogConstants.numByte(512 / 8));
            sig = ringAdd(sig, m);
            l -= 64;
        }
        m = Arrays.copyOfRange(message, message.length - l, message.length);

        //выполняется сдвиг массива вправо, а оставшееся пространство заполняется нулями, а перед началом оставшегося массива - единицей
        //описывается шагом 3.1 настоящего стандарта
        if (l != 64){
            var shift = new int[64];
            Arrays.fill(shift, 0x00);
            System.arraycopy(message, 0, shift, 64 - l, l);
            shift[64 - l - 1] = 0x01;
            m = shift;
        }

        h = gN(h, m, N);
        N = ringAdd(N, stribogConstants.numByte(l));
        sig = ringAdd(sig, m);
        h = gN(h, N, stribogConstants.numByte(0));
        h = gN(h, sig, stribogConstants.numByte(0));

        if (method)
            return new BigInteger(int2byte(h));
        else {
            var h256 = new int[32];
            System.arraycopy(h, 0, h256, 0, 32);
            return new BigInteger(int2byte(h256));
        }
    }

    /**
     * Функция реализует операцию сложения в кольце, т.е. с переносом в следующий байт. Переполнение отбрасывается
     */
    private int[] ringAdd(int[] a, int[] b) {
        var c = new int[64];
        var internal = 0;
        for (var i = 63; i >= 0; i--) {
            internal = a[i] + b[i] + (internal >> 8);            //выполняется сложение двух чисел, а затем прибавляется то, что оказалось перенесённым из предыдущей операции сложения за 1 байт
            c[i] = internal & 0xff;                              //поскольку в Java тип int хранит значения от -2147483648 до 2147483647 (первый бит отводится под знак), мы вынуждены интерпретировать значения в положительный диапазон https://habr.com/ru/post/225901/
        }
        return c;
    }

    /**
     * Функция сжатия
     * http://protect.gost.ru/v.aspx?control=8&baseC=-1&page=0&month=-1&year=-1&search=&RegNum=1&DocOnPageCount=15&id=224241&pageK=6C6D6BF2-DDD4-4BD9-8037-943B58998298
     */
    private int[] gN(int[] h, int[] m, int[] val) {
        var LPS = L(P(S(xFun(h, val))));
        return xFun(xFun(E(LPS, m), h), m);
    }

    /**
     * Часть функции сжатия. Задача - XOR временного ключа с итерационными константами, указанными в настоящем ГОСТ.
     * По сути надёжность алгоритма хэширования достигается именно ей.
     * http://protect.gost.ru/v.aspx?control=8&baseC=-1&page=0&month=-1&year=-1&search=&RegNum=1&DocOnPageCount=15&id=224241&pageK=6C6D6BF2-DDD4-4BD9-8037-943B58998298
     */
    private int[] E(int[] k, int[] m) {
        var result = xFun(k, m);
        var tempKey = k;
        for (var i = 0; i < 12; i++) {
            tempKey = L(P(S(xFun(tempKey, stribogConstants.C[i]))));
            result = xFun(L(P(S(result))), tempKey);
        }
        return result;
    }


    /**
     * @return XOR двух значений
     */
    private int[] xFun(int[] k, int[] a) {
        var result = new int[k.length];
        for (var i = 0; i < k.length; i++) result[i] = (k[i] ^ a[i]);
        return result;
    }


    /**
     * Функция подстановки. Каждому элементу массива лежащему в диапазоне от 0 до 255 соотвествует значение таблицы замещения с соответствующем значению порядковым номером http://protect.gost.ru/v.aspx?control=8&baseC=-1&page=0&month=-1&year=-1&search=&RegNum=1&DocOnPageCount=15&id=224241&pageK=8A5E965D-A695-4C6C-9DC0-76EFE1C804D5
     * @see Main.stribog.StribogConstants#substitution
     * http://protect.gost.ru/v.aspx?control=8&baseC=-1&page=0&month=-1&year=-1&search=&RegNum=1&DocOnPageCount=15&id=224241&pageK=6C6D6BF2-DDD4-4BD9-8037-943B58998298
     */
    private int[] S (int[] val) {
        var result = new int[64];
        for (var i = 0; i < 64; i++) 
            result[i] = stribogConstants.substitution[val[i]];
        return result;
    }

    /**
     * Функция перестановки. Входные значения возвращаются функцией в порядке, установленном таблицей перестановок
     * @see Main.stribog.StribogConstants#t
     * http://protect.gost.ru/v.aspx?control=8&baseC=-1&page=0&month=-1&year=-1&search=&RegNum=1&DocOnPageCount=15&id=224241&pageK=6C6D6BF2-DDD4-4BD9-8037-943B58998298
     */
    private int[] P (int[] val) {
        var result = new int[64];
        for (var i = 0; i < 64; i++) result[i] = val[stribogConstants.t[i]];
        return result;
    }

    /**
     * Функция линейного преобразования
     * @param val разбивается на блоки по 8 байт, каждому биту каждого байта соотносится строка из A
     * @see Main.stribog.StribogConstants#A
     * Затем проверяется значение бита, если 1, то выполняется операция XOR над результирующей строкой и строкой массива A
     */
    private int[] L (int[] val) {
        var result = new int[64];
        for(var i = 0; i < 8; i++)
            for(var j = 0; j < 8; j++)
                for(var k = 0; k < 8; k++)
                    if ((val[i * 8 + j] & (1 << Math.abs(k - 7))) != 0)
                        for (var l = 0; l < 8; l++)
                            result[8 * i + l] ^= stribogConstants.A[j * 8 + k][l];
        return result;
    }

    // https://stackoverflow.com/questions/2183240/java-integer-to-byte-array
    private byte[] int2byte(int[] src) {
        var srcLength = src.length;
        var dst = new byte[srcLength << 2];
        for (var i = 0; i < srcLength; i++) {
            var x = src[i];
            var j = i << 2;
            dst[j] = (byte) ((x) & 0xff);
            j++;
            dst[j] = (byte) ((x >>> 8) & 0xff);
            dst[j] = (byte) ((x >>> 16) & 0xff);
            dst[j] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }

}