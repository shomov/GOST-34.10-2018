// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package signature;

import java.math.BigInteger;
import java.util.Random;


public class Sign {

    BigInteger r;
    BigInteger s;

    public String signing (BigInteger hash, BigInteger d){

        var key = "";
//        var e = hash.mod(Constants.q);
//        if (e == BigInteger.ZERO)
//            e = BigInteger.ONE;

        var e = new BigInteger("20798893674476452017134061561508270130637142515379653289952617252661468872421");

        randK();

        key = r.toString();

        return key;
    }

    // Генерация псевдослучайного числа k
    private void randK(){
        var rand = new Random();
        var result = new BigInteger(Constants.q.bitLength(), rand);
        while(result.compareTo(Constants.q) >= 0 && result.compareTo(BigInteger.ZERO) != 1)
            result = new BigInteger(Constants.q.bitLength(), rand);
        genC(result);
    }

    private void genC (BigInteger k){
        var oper = new EllipticCurve();
        var temp = oper.scalar(k, Constants.P);
        setR(temp);

    }

    private void setR (Point C) {
        r = C.getX().mod(Constants.q);
        if (r.equals(BigInteger.ZERO)) {
            randK();
        }
    }




}
