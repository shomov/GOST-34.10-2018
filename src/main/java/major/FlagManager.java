// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import signature.SignatureParameters;
import signature.EllipticCurve;
import signature.Point;

import java.math.BigInteger;


public class FlagManager {
    BigInteger d;
    Point Q = Point.POINT_INFINITY;

    FileManager file = new FileManager();
    MessageManager msg = new MessageManager();
    SignatureParameters parameters = new SignatureParameters();

    @Option(name = "-h")
    boolean help;

    @Option(name = "-p")
    String fileParameters = "";

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

    public SignatureParameters parsing(String[] args) {
        final var parser = new CmdLineParser(this);
        msg.status(String.join(" ", args));
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            msg.basicErrors(0);
        }

        if (help) msg.status(0);

        if (!fileParameters.equals(""))  {
            if (!file.fileCheck(fileParameters)) msg.errorsIO(0, fileParameters);
            setParameters();
        }

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

        return parameters;
    }

    private void setParameters() {
        var list = file.stringReader(fileParameters);
        try {
            var a = false;
            if (list.get(0).equals(new BigInteger("512"))) a = true;
            else if (!list.get(0).equals(new BigInteger("256"))) msg.errorsIO(2, fileParameters);
            parameters.setConstants(a,
                    list.get(1), list.get(2), list.get(3), list.get(4),
                    list.get(5), list.get(6), list.get(7));
        } catch (Exception exception) {
            msg.errorsIO(2, fileParameters);
        }
    }

    private void setPrivateKey() {
        var list = file.stringReader(filePrivateKey);
        if (list.size() != 1) msg.errorsIO(2, filePrivateKey);
        d = list.get(0);
    }

    private void setQ() {
        var list = file.stringReader(fileVerKey);
        if (list.size() != 2){
            msg.errorsIO(2, fileVerKey);
        }
        Q = new Point(list.get(0), list.get(1));

    }

    public void createQ() {
        var curveOperation = new EllipticCurve(parameters);
        file.writePublicKey(curveOperation.scalar(d, parameters.P), outputFileName);
    }


    void setPathOut() {
        outputFileName = fileMessage + ".sig";
        if (file.fileCheck(outputFileName)) msg.errorsIO(1, outputFileName);
    }
}
