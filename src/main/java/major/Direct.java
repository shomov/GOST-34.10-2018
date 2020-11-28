// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import signature.Point;
import signature.Sign;
import signature.Verify;
import stribog.Hash;

import java.math.BigInteger;

public class Direct {
    private final String fileOut;
    private final String fileSig;
    private final Point Q;
    private final BigInteger d;
    private final BigInteger hash;

    FileManager file = new FileManager();
    MessageManager msg = new MessageManager();

    public Direct(FlagManager flag) {
        var fileMessage = flag.fileMessage;
        this.fileOut = flag.outputFileName;
        this.fileSig = flag.fileSig;
        this.Q = flag.Q;
        this.d = flag.d;

        var message = file.messageReader(fileMessage);
        if (message.length == 0) msg.errorsIO(2, fileMessage);

        var stribog = new Hash(512);
        this.hash = stribog.getHash(message);
        if (Q.getX() == null) signing();
        else verification();

    }

    private void signing() {
        var sign = new Sign();

        var signature = sign.signing(hash, d);
        file.writeSignature(signature, fileOut);
    }

    private void verification() {
        var ver = new Verify();
        var sign = file.signReader(fileSig);

        if (sign.equals("")) msg.errorsIO(3, fileSig);

        var check = ver.check(sign, Q, hash);
        if (check)
            msg.status(2);
        else
            msg.status(3);
    }

}
