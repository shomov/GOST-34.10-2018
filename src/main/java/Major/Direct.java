package Major;

import signature.*;
import stribog.Hash;

import java.io.IOException;
import java.math.BigInteger;

public class Direct {
    String fileMessage;
    Point Q;
    BigInteger d;
    BigInteger hash;

    FileManager file = new FileManager();

    public Direct(FlagManager flag) throws IOException {
        this.fileMessage = flag.fileMessage;
        this.Q = flag.Q;
        this.d = flag.d;
        this.d = new BigInteger("55441196065363246126355624130324183196576709222340016572108097750006097525544");


        var stribog = new Hash(512);
        this.hash = stribog.getHash(file.messageReader(fileMessage));
        if (Q.getX() == null) signing();
        else verification();

    }

    private void signing() {
        var sign = new Sign();

        var signature = sign.signing(hash, d);
        file.writer(signature);
    }

    private void verification() throws IOException {
        var ver = new Verify();
        var sign = file.signReader("signature.sgn");

        var check = ver.check(sign, Q, hash);
        if (check)
            System.out.println("Подпись успешно прошла проверку");
        else
            System.out.println("Подпись не верна!");
    }

}
