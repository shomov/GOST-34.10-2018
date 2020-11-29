// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import signature.Point;
import signature.Sign;
import signature.SignatureParameters;
import signature.Verify;
import stribog.Hash;

import java.math.BigInteger;

public class Direct {
    private final String fileOut;
    private final String fileSig;
    private final Point Q;
    private final BigInteger d;
    private final BigInteger hash;
    private final SignatureParameters parameters;

    FileManager file = new FileManager();
    MessageManager msg = new MessageManager();

    public Direct(FlagManager flag) {
        this.parameters = flag.parameters;
        var fileMessage = flag.fileMessage;
        this.fileOut = flag.outputFileName;
        this.fileSig = flag.fileSig;
        this.Q = flag.Q;
        this.d = flag.d;

        var message = file.messageReader(fileMessage);
        if (message.length == 0) msg.errorsIO(2, fileMessage);

        var digit = 256;
        if (parameters.digit) digit = 512;

        var stribog = new Hash(digit);
        this.hash = stribog.getHash(message);
        if (Q.getX() == null) signing();
        else verification();

    }

    private void signing() {
        var sign = new Sign();

        var signature = sign.signing(hash, d, parameters);
        file.writeSignature(signature, fileOut);
    }

    private void verification() {
        var ver = new Verify();
        var sign = file.signReader(fileSig);

        if (sign.equals("")) msg.errorsIO(3, fileSig);

        var check = ver.check(sign, Q, hash, parameters);
        if (check)
            msg.status(2);
        else
            msg.status(3);
    }

}
