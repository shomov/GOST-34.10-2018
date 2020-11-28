package Major;

import signature.Point;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {

    MessageManager msg = new MessageManager();

    boolean fileCheck(String path) {
        var fInput = Paths.get(path);
        return !Files.notExists(fInput.toAbsolutePath()) && Files.isRegularFile(fInput.toAbsolutePath());
    }

    ArrayList<BigInteger> keyReader(String path) {
        var result = new ArrayList<BigInteger>();
        try {
            var fr = new FileReader(path);
            var reader = new BufferedReader(fr);
            var line = reader.readLine();

            while (line != null) {
                result.add(new BigInteger(line.strip()));
                line = reader.readLine();
            }

        } catch (IOException exception) {
            msg.IOerrors(3, path);
        }
        return result;
    }

    int[] messageReader(String path) {
        try {
            var fis = new FileInputStream(path);
            var data = fis.readAllBytes();
            var dataOut = new int[data.length];
            for (var i = 0; i < data.length; i++)
                dataOut[i] = (data[i] * 8) & 0xFF; //перевод числа в положительный диапазон

            return dataOut;
        } catch (IOException exception) {
            msg.IOerrors(3, path);
        }
        return new int[0];
    }

    String signReader(String path) {
        try {
            var fr = new FileReader(path);
            var reader = new BufferedReader(fr);
            return reader.readLine();
        } catch (IOException exception) {
            msg.IOerrors(3, path);
        }
        return "";
    }

    void writeSignature(String signature, String fileOut) {
        try(var writer = new FileWriter(fileOut, false)) {
            writer.write(signature);
            writer.flush();
            msg.IOstatus(1, fileOut);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    void writePublicKey(Point Q, String file) {
        try {
            var writer = new FileWriter(file);
            var newLine = System.getProperty("line.separator");
            writer.write(Q.getX().toString() + newLine);
            writer.write(Q.getY().toString());
            writer.flush();
            msg.IOstatus(0, file);
        } catch (IOException e) {
            msg.IOerrors(4, file);
        }

    }

}
