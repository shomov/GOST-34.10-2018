// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import signature.Constants;
import signature.EllipticCurve;
import signature.Point;

import java.math.BigInteger;


public class FlagManager {
    BigInteger d;
    Point Q = Point.POINT_INFINITY;

    FileManager file = new FileManager();
    MessageManager msg = new MessageManager();

    @Option(name = "-h")
    boolean help;

    @Option(name = "-p")
    boolean parameters;

    @Option(name = "-q")
    String fileD = "";

    @Option(name = "-m")
    String fileMessage = "";

    @Option(name = "-sig")
    String fileSig = "";

    @Option(name = "-s")
    String filePrivateKey = "";

    @Option(name = "-v")
    String fileVerKey = "";

    @Option(name = "-o")
    String outputFileName;

    public void parsing(String[] args) {
        final var parser = new CmdLineParser(this);
        msg.status(String.join(" ", args));
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            msg.basicErrors(0);
        }

        if (help) msg.status(0);

        if (parameters) msg.status(1);

        if (!fileD.equals("")) {
            if (outputFileName.equals("")) msg.basicErrors(1);
            if (file.fileCheck(outputFileName)) msg.errorsIO(1, outputFileName);
            if (!file.fileCheck(fileD)) msg.errorsIO(0, fileD);
            filePrivateKey = fileD;
            setPrivateKey();
            createQ();
            msg.statusIO(0, outputFileName);
        }

        if (fileMessage.equals("")) msg.basicErrors(0);
        else if (!file.fileCheck(fileMessage)) msg.errorsIO(0, fileMessage);

        if (!filePrivateKey.equals("")) {
            if (outputFileName == null) setPathOut();
            if (file.fileCheck(filePrivateKey)) setPrivateKey();
        }
        else if (!fileVerKey.equals("") && !fileSig.equals("")) {
            if (file.fileCheck(fileVerKey) && file.fileCheck(fileSig)) setQ();
        }
        else msg.basicErrors(0);

    }

    private void setPrivateKey() {
        var list = file.keyReader(filePrivateKey);
        if (list.size() != 1){
            msg.errorsIO(2, filePrivateKey);
        }
        d = list.get(0);
    }

    private void setQ() {
        var list = file.keyReader(fileVerKey);
        if (list.size() != 2){
            msg.errorsIO(2, fileVerKey);
        }
        Q = new Point(list.get(0), list.get(1));

    }

    public void createQ() {
        var curveOperation = new EllipticCurve();
        file.writePublicKey(curveOperation.scalar(d, Constants.P), outputFileName);
    }


    void setPathOut() {
        outputFileName = fileMessage + ".sig";
        if (file.fileCheck(outputFileName)) msg.errorsIO(1, outputFileName);
    }
}
