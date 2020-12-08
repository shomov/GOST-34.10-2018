// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package main.major;

import main.signature.Point;
import main.signature.SignatureParameters;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {

    private final MessageManager msg = new MessageManager();

    public void fileCheck(String path, boolean needTo) throws IOException {
        var fInput = Paths.get(path);
        var result = Files.exists(fInput) && Files.isRegularFile(fInput);
        if (!result && needTo)
            msg.errorsIO(0, path);
    }

    public SignatureParameters setConstants (String fileParameters) throws IOException {
        var list = stringReader(fileParameters);
        var parameters = SignatureParameters.PARAMETERS_INFINITY;
        try {
            var a = false;
            if (list.get(0).equals(new BigInteger("512"))) a = true;
            else if (!list.get(0).equals(new BigInteger("256"))) msg.errorsIO(1, fileParameters);
            parameters = new SignatureParameters(a,
                    list.get(1), list.get(2), list.get(3), list.get(4),
                    list.get(5), list.get(6), list.get(7));
        } catch (Exception exception) {
            msg.errorsIO(1, fileParameters);
        }
        return parameters;
    }

    public ArrayList<BigInteger> stringReader(String path) throws IOException {
        var result = new ArrayList<BigInteger>();
        try {
            var reader = Files.newBufferedReader(Path.of(path));
            var line = reader.readLine();

            while (line != null) {
                try {
                    result.add(new BigInteger(line.strip()));
                } catch (Exception ignored){}
                line = reader.readLine();
            }

        } catch (IOException exception) {
            msg.errorsIO(2, path);
        }
        return result;
    }

    public int[] messageReader(String path) throws IOException {
        try {
            var fis = Files.newInputStream(Path.of(path));
            var data = fis.readAllBytes();
            var dataOut = new int[data.length];
            for (var i = 0; i < data.length; i++)
                dataOut[i] = (data[i] * 8) & 0xFF; //перевод числа в положительный диапазон
            return dataOut;
        } catch (IOException exception) {
            msg.errorsIO(2, path);
        }
        return new int[0];
    }

    public String signReader(String path) throws IOException {
        try {
            var reader = Files.newBufferedReader(Path.of(path));
            return reader.readLine();
        } catch (IOException exception) {
            msg.errorsIO(2, path);
        }
        return "";
    }

    public void writeSignature(String signature, String fileOut) throws IOException {
        try {
            var fos = new FileOutputStream(fileOut, true);
            fos.write(signature.getBytes(), 0, signature.getBytes().length);
            fos.close();
            msg.statusIO(1, fileOut);
        }
        catch(IOException ex){
            msg.errorsIO(3, fileOut);
        }
    }

    public void writePublicKey(Point Q, String file) throws IOException {
        try {
            var writer = new FileWriter(file);
            var newLine = System.getProperty("line.separator");
            writer.write(Q.x().toString() + newLine);
            writer.write(Q.y().toString());
            writer.flush();
        } catch (IOException e) {
            msg.errorsIO(3, file);
        }

    }

}
