package Major;

import java.io.IOException;
import java.math.BigInteger;


import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import signature.Constants;
import signature.EllipticCurve;
import signature.Point;


public class FlagManager {
    BigInteger d;
    Point Q = Point.POINT_INFINITY;

    FileManager file = new FileManager();
    MessageManager msg = new MessageManager();

    @Option(name = "-h")
    boolean help;

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
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {

        }


        if (help) {

        }

        if (!fileD.equals("")) {
            if (outputFileName.equals("")) {
                //err должен быть исходящий путь
            }

            file.fileCheck(outputFileName);
            file.fileCheck(fileD);
            filePrivateKey = fileD;
            setPrivateKey();
            createQ();
            System.exit(0);

        }

        if (!fileMessage.equals("")){
            file.fileCheck(fileMessage);
        }
        else {
            //err
        }

        if (!filePrivateKey.equals("")) {
            if (outputFileName == null) {
                setPathOut();
            }
            if (file.fileCheck(filePrivateKey)) {
                setPrivateKey();
            }

        }
        else if (!fileVerKey.equals("") && !fileSig.equals("")) {
            if (file.fileCheck(fileVerKey) && file.fileCheck(fileSig)) {
                setQ();
            }
        }
        else {
            //err
        }

//        if (outputFileName != null && fileVerKey == null)
//            pathOut = outputFileName;
//        else if (outputFileName == null)
//            pathOut = null;
//        else{
//            //err
//        }
//        setPathOut(outputFileName != null);
    }

    private void setPrivateKey() throws IOException {
        var list = file.keyReader(filePrivateKey);
        if (list.size() != 1){
            //ошибка файла
        }
        d = list.get(0);
    }

    private void setQ() throws IOException {
        var list = file.keyReader(fileVerKey);
        if (list.size() != 2){
            //ошибка файла
        }
        Q = new Point(list.get(0), list.get(1));

    }

    public void createQ() {
        var curveOperation = new EllipticCurve();
        file.writePublicKey(curveOperation.scalar(d, Constants.P), outputFileName);
    }



    void setPathOut() {
        outputFileName = fileMessage + ".sig";
        if (file.fileCheck(outputFileName)) {
            //err надо задать путь
            System.err.println("path!");
            System.exit(1);
        }

    }
}
