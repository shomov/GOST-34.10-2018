// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import org.junit.jupiter.api.Test;
import stribog.Hash;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Тестирование корректности создания и проверки ЭЦП
 * Важным моментом нижеприведённых тестов является то, что все входные значения (сообщение и ключ подписи) генерируются автоматически.
 */
class SignTest {

    int testIterations = 1000;


    @Test
    void success() {
        var curveOperation = new EllipticCurve();
        var stribog512_1 = new Hash(512);
        var check = new Verify();
        var ar512 = stribog512_1.getHash(randomMessage());

        var test = true;

        for (var i = 0; i < testIterations; i++){
            // подписание
            var sign = new Sign();
            var d = randomKey();
            var key = sign.signing(new BigInteger(int2byte(ar512)), d);
            var Q = curveOperation.scalar(d, Constants.P);

            // верификация
            var ver = check.check(key, Q, new BigInteger(int2byte(ar512)));
            if (!ver){
                test = false;
                break;
            }

        }
        assertTrue(test);

    }

    @Test
    void wrongMessage() {
        var curveOperation = new EllipticCurve();
        var stribog512_1 = new Hash(512);
        var check = new Verify();
        var message = randomMessage();
        var ar512 = stribog512_1.getHash(message);
        var sign = new Sign();
        var d = randomKey();
        var key = sign.signing(new BigInteger(int2byte(ar512)), d);
        var Q = curveOperation.scalar(d, Constants.P);

        var test = false;

        for (var i = 0; i < testIterations; i++) {
            var rand = new Random();
            message[rand.nextInt(message.length)] = rand.nextInt(256);

            var ar512W = stribog512_1.getHash(message);

            var ver = check.check(key, Q, new BigInteger(int2byte(ar512W)));
            if (ver){
                test = true;
                break;
            }
        }

        assertFalse(test);

    }

    @Test
    void wrongSign() {
        var curveOperation = new EllipticCurve();
        var stribog512_1 = new Hash(512);
        var check = new Verify();
        var ar512 = stribog512_1.getHash(randomMessage());
        var d = randomKey();
        var Q = curveOperation.scalar(d, Constants.P);
        var sign = new Sign();
        var test = false;

        for (var i = 0; i < testIterations; i++) {
            var key = new StringBuilder(sign.signing(new BigInteger(int2byte(ar512)), d));

            var rand = new Random();
            int r;
            String ch;
            do {
                r = rand.nextInt(key.length());
                ch = new BigInteger(String.valueOf(rand.nextInt(16))).toString(16);
            }
            while (key.charAt(r) == ch.charAt(0));

            key.setCharAt(r, ch.charAt(0));


            var ver = check.check(key.toString(), Q, new BigInteger(int2byte(ar512)));
            if (ver){
                test = true;
                break;
            }
        }

        assertFalse(test);

    }

    @Test
    void wrongVerificationKeySign() {
        var curveOperation = new EllipticCurve();
        var stribog512_1 = new Hash(512);
        var check = new Verify();
        var d = randomKey();
        var ar512 = stribog512_1.getHash(randomMessage());
        var sign = new Sign();
        var key = sign.signing(new BigInteger(int2byte(ar512)), d);
        var Q = curveOperation.scalar(d, Constants.P);

        var test = false;

        for (var i = 0; i < testIterations; i++) {

            var rand = new Random();
            var rx = rand.nextInt();
            var ry = rand.nextInt();
            Q.setX(Q.getX().add(BigInteger.valueOf(rx)));
            Q.setY(Q.getY().add(BigInteger.valueOf(ry)));

            var ver = check.check(key, Q, new BigInteger(int2byte(ar512)));
            if (ver){
                test = true;
                break;
            }
        }

        assertFalse(test);

    }

    private int[] randomMessage() {
        var rand = new Random();
        var size = Math.abs(rand.nextInt(1000));
        var message = new int[size];
        for (var i = 0; i< size; i++)
            message[i] = Math.abs(rand.nextInt(256));
        return message;
    }

    private BigInteger randomKey() {
        var rand = new Random();
        return new BigInteger(Constants.q.bitLength(), rand);
    }


    // https://stackoverflow.com/questions/2183240/java-integer-to-byte-array
    public static byte[] int2byte(int[] src) {
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