// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import org.junit.jupiter.api.Test;
import stribog.Hash;

import java.math.BigInteger;
import java.util.Random;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.When;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitQuickcheck.class)
public class QuickTest {

    @Property(trials=10)
    public void simple(int num) {
        System.out.println("simple:" + num);
        assertTrue(num>0);
    }

    @Property(trials = 10)
    public void assume(int num) {
        System.out.print(" | Before:" + num);
        assumeThat(num, greaterThan(0));
        System.out.println(" | Afer:" + num);
        assertTrue(num > 0);
    }

    @Property(trials=5)
    public void inRange(@InRange(minInt = 0, maxInt = 100) int num) {
        System.out.println("InRange: " + num);
        assertTrue(num>0);
    }

    @Property(trials = 5)
    public void when(@When(satisfies = "#_ > 1000 && #_ < 1000000000") int num) {
        System.out.println("when: " + num);
        assertTrue(num > 0);
    }




}
