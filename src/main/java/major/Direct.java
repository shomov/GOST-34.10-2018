// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import signature.*;
import stribog.Hash;

import java.math.BigInteger;

public class Direct {
    private final String fileOut;
    private final String fileSig;
    private final Point Q;
    private final BigInteger d;
    private BigInteger hash;
    private final SignatureParameters parameters;

    private final FileManager file = new FileManager();
    private final MessageManager msg = new MessageManager();

    public Direct(FlagManager flag) throws Exception {
        this.parameters = flag.parameters;
        var fileMessage = flag.fileMessage;
        this.fileOut = flag.outputFileName;
        this.fileSig = flag.fileSig;
        this.Q = flag.Q;
        this.d = flag.d;

        if (!flag.fileD.equals("")) {
            var curveOperation = new EllipticCurve(parameters);
            file.writePublicKey(curveOperation.scalar(d, parameters.P), flag.outputFileName);
            msg.statusIO(0, flag.outputFileName);
        }
        else {
            var message = file.messageReader(fileMessage);
            if (message.length == 0) msg.errorsIO(2, fileMessage);

            var digit = 256;
            if (parameters.digit) digit = 512;

            var stribog = new Hash(digit);
            this.hash = stribog.getHash(message);
            if (Q.getX() == null) signing();
            else verification();
        }
    }

    private void signing() throws Exception {
        var sign = new Sign();

        var signature = sign.signing(hash, d, parameters);
        file.writeSignature(signature, fileOut);
    }

    private void verification() throws Exception {
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
