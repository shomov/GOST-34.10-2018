package Major;

import signature.*;
import stribog.Hash;

import java.io.IOException;
import java.math.BigInteger;

public class Direct {
    String fileMessage;
    String fileOut;
    String fileSig;
    Point Q;
    BigInteger d;
    BigInteger hash;

    FileManager file = new FileManager();

    public Direct(FlagManager flag) throws IOException {
        this.fileMessage = flag.fileMessage;
        this.fileOut = flag.outputFileName;
        this.fileSig = flag.fileSig;
        this.Q = flag.Q;
        this.d = flag.d;


        var stribog = new Hash(512);
        this.hash = stribog.getHash(file.messageReader(fileMessage));
        if (Q.getX() == null) signing();
        else verification();

    }

    private void signing() {
        var sign = new Sign();

        var signature = sign.signing(hash, d);
        file.writeSignature(signature, fileOut);
    }

    private void verification() throws IOException {
        var ver = new Verify();
        var sign = file.signReader(fileSig);

        var check = ver.check(sign, Q, hash);
        if (check)
            System.out.println("Подпись успешно прошла проверку");
        else
            System.out.println("Подпись не верна!");
    }

}
