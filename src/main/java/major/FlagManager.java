// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import signature.Point;
import signature.SignatureParameters;

import java.io.IOException;
import java.math.BigInteger;


public class FlagManager {
    BigInteger d;
    Point Q = Point.POINT_INFINITY;

    private final FileManager file = new FileManager();
    private final MessageManager msg = new MessageManager();
    public SignatureParameters parameters = SignatureParameters.PARAMETERS_INFINITY;

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

    public void parsing(String[] args) throws Exception {
        final var parser = new CmdLineParser(this);
        msg.status(String.join(" ", args));
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            msg.basicErrors(0);
        }
        if (help)
            msg.status(0);
        else if (!fileParameters.equals("") && fileSig.equals("") && filePrivateKey.equals("")
                && fileD.equals("") && fileMessage.equals("")) {
            setParameters();
            msg.curveSettings(parameters);
        }
        else {
            setParameters();
            if (!fileD.equals("")) {
                if (outputFileName.equals("")) msg.basicErrors(1);
                if (file.fileCheck(outputFileName))
                    msg.errorsIO(1, outputFileName);
                if (!file.fileCheck(fileD))
                    msg.errorsIO(0, fileD);
                filePrivateKey = fileD;
                setPrivateKey();
            }

            if (!fileMessage.equals("") && filePrivateKey.equals("") && fileD.equals("") && fileSig.equals(""))
                msg.basicErrors(0);
            else if (!fileMessage.equals("") && !file.fileCheck(fileMessage))
                msg.errorsIO(0, fileMessage);

            if (!filePrivateKey.equals("")) {
                if (outputFileName == null) setPathOut();
                if (file.fileCheck(filePrivateKey)) setPrivateKey();
                else msg.basicErrors(0);
            } else if (!fileVerKey.equals("") && !fileSig.equals("")) {
                if (file.fileCheck(fileVerKey) && file.fileCheck(fileSig)) setQ();
                else if (!file.fileCheck(fileVerKey))
                    msg.errorsIO(0, fileVerKey);
                else if (!file.fileCheck(fileSig))
                    msg.errorsIO(0, fileSig);
            } else msg.basicErrors(0);
        }
    }

    private void setParameters() throws Exception {
        if (fileParameters.equals(""))
            msg.basicErrors(0);
        if (!file.fileCheck(fileParameters))
            msg.errorsIO(0, fileParameters);
        parameters = file.setConstants(fileParameters);
    }

    private void setPrivateKey() throws IOException {
        var list = file.stringReader(filePrivateKey);
        if (list.size() != 1)
            msg.errorsIO(2, filePrivateKey);
        d = list.get(0);
    }

    private void setQ() throws IOException {
        var list = file.stringReader(fileVerKey);
        if (list.size() != 2) msg.errorsIO(2, fileVerKey);
        Q = new Point(list.get(0), list.get(1));

    }

    void setPathOut() throws IOException {
        outputFileName = fileMessage + ".sig";
        if (file.fileCheck(outputFileName))
            msg.errorsIO(1, outputFileName);
    }
}
