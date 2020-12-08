// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package main.major;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import main.signature.Point;
import main.signature.SignatureParameters;

import java.io.IOException;
import java.math.BigInteger;


public class FlagManager {
    public BigInteger d;
    public Point Q = new Point(null, null);

    private final FileManager file = new FileManager();
    private final MessageManager msg = new MessageManager();
    SignatureParameters parameters = new SignatureParameters(false, null, null, null, null, null, new Point(null, null));

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
                if (outputFileName == null)
                    msg.basicErrors(1);
                file.fileCheck(outputFileName, false);
                file.fileCheck(fileD, true);
                filePrivateKey = fileD;
                setPrivateKey();
            }

            if (!filePrivateKey.equals("")) {
                if (outputFileName == null)
                    setPathOut();
                file.fileCheck(filePrivateKey, true);
                setPrivateKey();
            } else if (!fileVerKey.equals("") && !fileSig.equals("")) {
                file.fileCheck(fileVerKey, true);
                file.fileCheck(fileSig, true);
                setQ();
            } else msg.basicErrors(0);
        }
    }

    private void setParameters() throws Exception {
        if (fileParameters.equals(""))
            msg.basicErrors(0);
        file.fileCheck(fileParameters, true);
        parameters = file.setConstants(fileParameters);
    }

    private void setPrivateKey() throws IOException {
        var list = file.stringReader(filePrivateKey);
        if (list.size() != 1)
            msg.errorsIO(1, filePrivateKey);
        d = list.get(0);
    }

    private void setQ() throws IOException {
        var list = file.stringReader(fileVerKey);
        if (list.size() != 2) msg.errorsIO(1, fileVerKey);
        Q = new Point(list.get(0), list.get(1));
    }

    private void setPathOut() throws IOException {
        outputFileName = fileMessage + ".sig";
        file.fileCheck(outputFileName, false);
    }
}
