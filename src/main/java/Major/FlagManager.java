package Major;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    //MessageManager msg = new MessageManager();

    @Option(name = "-h")
    boolean help;

    @Option(name = "-q")
    String fileD = "";

    @Option(name = "-m")
    String fileMessage = "";

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

        if (fileD != "") {
            if (outputFileName == "") {
                //err должен быть исходящий путь
            }

            fileCheck(outputFileName);
            fileCheck(fileD);
            filePrivateKey = fileD;
            setPrivateKey();
            createQ();
            System.exit(0);

        }

        if (fileMessage != ""){
            fileCheck(fileMessage);
        }
        else {
            //err
        }

        if (!filePrivateKey.equals("")) {
            if (fileCheck(filePrivateKey)) {
                setPrivateKey();
            }

        }
        else if (!fileVerKey.equals("")) {
            if (fileCheck(fileVerKey)) {
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
//        Q.setX(list.get(0));
//        Q.setY(list.get(1));
    }

    public void createQ() {
        var curveOperation = new EllipticCurve();
        file.writePublicKey(curveOperation.scalar(d, Constants.P), outputFileName);
    }

    private boolean fileCheck(String path) {
        var fInput = Paths.get(path);
        if (Files.notExists(fInput.toAbsolutePath()) || !Files.isRegularFile(fInput.toAbsolutePath())) {
            //исключение
            return false;
        }
        return true;
    }

    void setPathOut (boolean custom) throws Exception {
 //       FileManager file = new FileManager();
 //      pathOut = file.creator(approach, pathOut, custom);
    }
}
