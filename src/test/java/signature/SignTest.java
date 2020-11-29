//// This is a personal academic project. Dear PVS-Studio, please check it.
//
//// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
//
//package signature;
//
//import org.junit.jupiter.api.Test;
//import stribog.Hash;
//
//import java.math.BigInteger;
//import java.util.Random;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
///**
// * Тестирование корректности создания и проверки ЭЦП
// * Важным моментом нижеприведённых тестов является то, что все входные значения (сообщение и ключ подписи) генерируются автоматически.
// */
//class SignTest {
//
//    private final int testIterations = 1000;
//
//
//    @Test
//    void success() {
//        var curveOperation = new EllipticCurve();
//        var stribog512_1 = new Hash(512);
//        var check = new Verify();
//
//        var test = true;
//
//        for (var i = 0; i < testIterations; i++){
//            var ar512 = stribog512_1.getHash(randomMessage());
//            // подписание
//            var sign = new Sign();
//            var d = randomKey();
//            var key = sign.signing(ar512, d);
//            var Q = curveOperation.scalar(d, SignatureConstants.P);
//
//            // верификация
//            var ver = check.check(key, Q, ar512);
//            if (!ver){
//                test = false;
//                break;
//            }
//
//        }
//        assertTrue(test);
//
//    }
//
//    @Test
//    void wrongMessage() {
//        var curveOperation = new EllipticCurve();
//        var stribog512_1 = new Hash(512);
//        var check = new Verify();
//        var message = randomMessage();
//        var ar512 = stribog512_1.getHash(message);
//        var sign = new Sign();
//        var d = randomKey();
//        var key = sign.signing(ar512, d);
//        var Q = curveOperation.scalar(d, SignatureConstants.P);
//
//        var test = false;
//
//        for (var i = 0; i < testIterations; i++) {
//            var rand = new Random();
//            message[rand.nextInt(message.length)] = rand.nextInt(256);
//
//            var ar512W = stribog512_1.getHash(message);
//
//            var ver = check.check(key, Q,ar512W);
//            if (ver){
//                test = true;
//                break;
//            }
//        }
//
//        assertFalse(test);
//
//    }
//
//    @Test
//    void wrongSign() {
//        var curveOperation = new EllipticCurve();
//        var stribog512_1 = new Hash(512);
//        var check = new Verify();
//        var ar512 = stribog512_1.getHash(randomMessage());
//        var d = randomKey();
//        var Q = curveOperation.scalar(d, SignatureConstants.P);
//        var sign = new Sign();
//        var test = false;
//
//        for (var i = 0; i < testIterations; i++) {
//            var key = new StringBuilder(sign.signing(ar512, d));
//
//            var rand = new Random();
//            int r;
//            String ch;
//            do {
//                r = rand.nextInt(key.length());
//                ch = new BigInteger(String.valueOf(rand.nextInt(16))).toString(16);
//            }
//            while (key.charAt(r) == ch.charAt(0));
//
//            key.setCharAt(r, ch.charAt(0));
//
//
//            var ver = check.check(key.toString(), Q, ar512);
//            if (ver){
//                test = true;
//                break;
//            }
//        }
//
//        assertFalse(test);
//
//    }
//
//    @Test
//    void wrongVerificationKeySign() {
//        var curveOperation = new EllipticCurve();
//        var stribog512_1 = new Hash(512);
//        var check = new Verify();
//        var d = randomKey();
//        var ar512 = stribog512_1.getHash(randomMessage());
//        var sign = new Sign();
//        var key = sign.signing(ar512, d);
//        var Q = curveOperation.scalar(d, SignatureConstants.P);
//
//        var test = false;
//
//        for (var i = 0; i < testIterations; i++) {
//
//            var rand = new Random();
//            var rx = rand.nextInt();
//            var ry = rand.nextInt();
//            var x = (Q.getX().add(BigInteger.valueOf(rx)));
//            var y = (Q.getY().add(BigInteger.valueOf(ry)));
//
//            var ver = check.check(key, new Point(x, y), ar512);
//            if (ver){
//                test = true;
//                break;
//            }
//        }
//
//        assertFalse(test);
//
//    }
//
//    private int[] randomMessage() {
//        var rand = new Random();
//        var size = Math.abs(rand.nextInt(1000));
//        var message = new int[size];
//        for (var i = 0; i < size; i++)
//            message[i] = Math.abs(rand.nextInt(256));
//        return message;
//    }
//
//    private BigInteger randomKey() {
//        var rand = new Random();
//        return new BigInteger(SignatureConstants.q.bitLength(), rand);
//    }
//
//
//
//}