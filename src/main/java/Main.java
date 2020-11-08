// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

import stribog.Hash;
import java.math.BigInteger;

public class Main {

    static int[] message1 = {
            0x32, 0x31, 0x30, 0x39, 0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x30, 0x39, 0x38, 0x37,
            0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x30, 0x39, 0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31,
            0x30, 0x39, 0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x30, 0x39, 0x38, 0x37, 0x36, 0x35,
            0x34, 0x33, 0x32, 0x31, 0x30, 0x39, 0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x30
    };

    public static void main(String[] args) {
        var d = new BigInteger("55441196065363246126355624130324183196576709222340016572108097750006097525544");



        var stribog512_1 = new Hash(512);
        var ar512 = stribog512_1.getHash(message1);
        var hash = new BigInteger(int2byte(ar512));
        System.out.println(hash);





    }

    public static byte[] int2byte(int[]src) {
        var srcLength = src.length;
        var dst = new byte[srcLength << 2];
        for (var i = 0; i < srcLength; i++) {
            var x = src[i];
            var j = i << 2;
            dst[j++] = (byte) ((x >>> 0) & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j++] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }
}
