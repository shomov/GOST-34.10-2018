package Major;

import signature.Point;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {

    boolean fileCheck(String path) {
        var fInput = Paths.get(path);
        if (Files.notExists(fInput.toAbsolutePath()) || !Files.isRegularFile(fInput.toAbsolutePath())) {
            //исключение
            return false;
        }
        return true;
    }

    ArrayList<BigInteger> keyReader(String path) throws IOException {
        var fr = new FileReader(path);
        var reader = new BufferedReader(fr);
        var line = reader.readLine();
        var result = new ArrayList<BigInteger>();

        while (line != null) {
            result.add(new BigInteger(line.strip()));
            line = reader.readLine();
        }
        return result;
    }

    int[] messageReader(String path) throws IOException {

        var fis = new FileInputStream(path);
        var data = fis.readAllBytes();
        var dataOut = new int[data.length];

        for (var i = 0; i < data.length; i++) {
            dataOut[i] = (data[i] * 8) & 0xFF; //перевод числа в положительный диапазон
        }

        return dataOut;
    }

    String signReader(String path) throws IOException {
        var fr = new FileReader(path);
        var reader = new BufferedReader(fr);
        return reader.readLine();
    }

    void writeSignature(String signature, String fileOut) {
        try(var writer = new FileWriter(fileOut, false)) {
            writer.write(signature);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    void writePublicKey(Point Q, String file) {
        try (var writer = new FileWriter(file)) {
            var newLine = System.getProperty("line.separator");
            writer.write(Q.getX().toString() + newLine);
            writer.write(Q.getY().toString());
//            log.info("Save to " + file.getAbsolutePath());
        } catch (IOException e) {
//            error(false);
//            log.error(e);
        }


    }

}
