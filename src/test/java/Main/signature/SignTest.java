// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package Main.signature;

import Main.major.FileManager;
import Main.stribog.Hash;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.Random;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assume.assumeThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Тестирование корректности создания и проверки ЭЦП
 * Важным моментом нижеприведённых тестов является то, что все входные значения (сообщение и ключ подписи) генерируются автоматически.
 */
@RunWith(JUnitQuickcheck.class)
public class SignTest {

    private final int testIterations = 10;

    SignatureParameters parameters = SignatureParameters.PARAMETERS_INFINITY;
    private final FileManager file = new FileManager();

    @Disabled
    @Test
    @Property(trials = testIterations)
    public void success(@InRange(minInt = 0, maxInt = 255) int[] message, BigInteger d) throws Exception {
        assumeThat(message.length, greaterThan(0));
        parameters = file.setConstants("Parameters/Signature512");
        var curveOperation = new EllipticCurve(parameters);
        var stribog512_1 = new Hash(512);
        var check = new Verify();
        var test = true;
        var ar512 = stribog512_1.getHash(message);
        // подписание
        var sign = new Sign();
        var key = sign.signing(ar512, d, parameters);
        var Q = curveOperation.scalar(d, parameters.P);
        // верификация
        var ver = check.check(key, Q, ar512, parameters);
        if (!ver)
            test = false;
        assertTrue(test);
    }

    @Disabled
    @Test
    @Property(trials = testIterations)
    public void wrongMsgQCh(@InRange(minInt = 0, maxInt = 255) int[] message, BigInteger d) throws Exception {
        assumeThat(message.length, greaterThan(0));
        parameters = file.setConstants("Parameters/Signature512");
        var curveOperation = new EllipticCurve(parameters);
        var stribog512_1 = new Hash(512);
        var check = new Verify();
        var ar512 = stribog512_1.getHash(message);
        var sign = new Sign();
        var key = sign.signing(ar512, d, parameters);
        var Q = curveOperation.scalar(d, parameters.P);
        var test = false;
        var rand = new Random();
        message[rand.nextInt(message.length)] = rand.nextInt(256);
        var ar512W = stribog512_1.getHash(message);
        var ver = check.check(key, Q, ar512W, parameters);
        if (ver)
            test = true;
        assertFalse(test);
    }

    @Disabled
    @Test
    @Property(trials = testIterations)
    public void wrongSignQCh(@InRange(minInt = 0, maxInt = 255) int[] message, BigInteger d) throws Exception {
        assumeThat(message.length, greaterThan(0));
        parameters = file.setConstants("Parameters/Signature512");
        var curveOperation = new EllipticCurve(parameters);
        var stribog512_1 = new Hash(512);
        var check = new Verify();
        var ar512 = stribog512_1.getHash(message);
        var Q = curveOperation.scalar(d, parameters.P);
        var sign = new Sign();
        var key = new StringBuilder(sign.signing(ar512, d, parameters));
        var rand = new Random();
        int r;
        String ch;
        do {
            r = rand.nextInt(key.length());
            ch = new BigInteger(String.valueOf(rand.nextInt(16))).toString(16);
        } while (key.charAt(r) == ch.charAt(0));
        key.setCharAt(r, ch.charAt(0));
        var ver = check.check(key.toString(), Q, ar512, parameters);
        var test = false;
        if (ver)
            test = true;
        assertFalse(test);
    }

    @Disabled
    @Test
    @Property(trials = testIterations)
    public void wrongVerificationKeySign(@InRange(minInt = 0, maxInt = 255) int[] message, BigInteger d) throws Exception {
        assumeThat(message.length, greaterThan(0));
        parameters = file.setConstants("Parameters/Signature256");
        var stribog512_1 = new Hash(256);
        var check = new Verify();
        var ar512 = stribog512_1.getHash(message);
        var sign = new Sign();
        var key = sign.signing(ar512, d, parameters);
        var rand = new Random();
        var x = new BigInteger(parameters.q.bitLength(), rand);
        var y = new BigInteger(parameters.q.bitLength(), rand);
        assertFalse(check.check(key, new Point(x, y), ar512, parameters));
    }

}
