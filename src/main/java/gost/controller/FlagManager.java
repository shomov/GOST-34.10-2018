// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package gost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gost.occasion.AlienExceptions;
import gost.occasion.Statuses;
import gost.signature.Point;
import gost.signature.SignatureParameters;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.math.BigInteger;


public class FlagManager {
    public BigInteger d;
    public Point Q = new Point(null, null);

    private final FileManager file = new FileManager();
    private final MessageManager msg = new MessageManager();
    SignatureParameters parameters = new SignatureParameters(null, null, null, null, null, null, new Point(null, null));

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
            throw new AlienExceptions.IllegalArgumentException();
        }
        if (help)
            msg.status(Statuses.HELP);
        else if (!fileParameters.equals("") && fileSig.equals("") && filePrivateKey.equals("")
                && fileD.equals("") && fileMessage.equals("")) {
            setParameters();
            msg.curveSettings(parameters);
        }
        else {
            setParameters();
            if (!fileD.equals("")) {
                if (outputFileName == null)
                    throw new AlienExceptions.DestinationFileNotFoundException();
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
            } else throw new AlienExceptions.IllegalArgumentException();
        }
    }

    private void setParameters() throws Exception {
        if (fileParameters.equals(""))
            throw new AlienExceptions.IllegalArgumentException();
        file.fileCheck(fileParameters, true);
        parameters = file.setConstants(fileParameters);
    }

    private void setPrivateKey() throws AlienExceptions.FileCorruptedException, AlienExceptions.FileReadingException {
        var json = file.parametersReader(filePrivateKey);
        try {
            d = new BigInteger(json);
        } catch (Exception e){
            throw new AlienExceptions.FileCorruptedException(filePrivateKey);
        }
    }

    private void setQ() throws AlienExceptions.FileReadingException, AlienExceptions.FileCorruptedException {
        var json = file.parametersReader(fileVerKey);
        try {
            Q  = new ObjectMapper().readValue(json, Point.class);
        } catch (Exception e){
            throw new AlienExceptions.FileCorruptedException(filePrivateKey);
        }
    }

    private void setPathOut() throws AlienExceptions.IOException {
        outputFileName = fileMessage + ".sig";
        file.fileCheck(outputFileName, false);
    }
}
