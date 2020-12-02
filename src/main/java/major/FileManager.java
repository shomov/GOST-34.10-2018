// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import signature.Point;
import signature.SignatureParameters;

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

    public ArrayList<BigInteger> stringReader(String path) throws IOException {
        var result = new ArrayList<BigInteger>();
        try {
            var fr = new FileReader(path);
            var reader = new BufferedReader(fr);
            var line = reader.readLine();

            while (line != null) {
                try {
                    result.add(new BigInteger(line.strip()));
                } catch (Exception ignored){}
                line = reader.readLine();
            }

        } catch (IOException exception) {
            msg.errorsIO(3, path);
        }
        return result;
    }

    public SignatureParameters setConstants (String fileParameters) throws IOException {
        var list = stringReader(fileParameters);
        var parameters = SignatureParameters.PARAMETERS_INFINITY;
        try {
            var a = false;
            if (list.get(0).equals(new BigInteger("512"))) a = true;
            else if (!list.get(0).equals(new BigInteger("256"))) msg.errorsIO(2, fileParameters);
            parameters = new SignatureParameters(a,
                    list.get(1), list.get(2), list.get(3), list.get(4),
                    list.get(5), list.get(6), list.get(7));
        } catch (Exception exception) {
            msg.errorsIO(2, fileParameters);
        }
        return parameters;
    }

    int[] messageReader(String path) throws IOException {
        try {
            var fis = new FileInputStream(path);
            var data = fis.readAllBytes();
            var dataOut = new int[data.length];
            for (var i = 0; i < data.length; i++)
                dataOut[i] = (data[i] * 8) & 0xFF; //перевод числа в положительный диапазон

            return dataOut;
        } catch (IOException exception) {
            msg.errorsIO(3, path);
        }
        return new int[0];
    }

    String signReader(String path) throws IOException {
        try {
            var fr = new FileReader(path);
            var reader = new BufferedReader(fr);
            return reader.readLine();
        } catch (IOException exception) {
            msg.errorsIO(3, path);
        }
        return "";
    }

    void writeSignature(String signature, String fileOut) throws IOException {
        try(var writer = new FileWriter(fileOut, false)) {
            writer.write(signature);
            writer.flush();
            msg.statusIO(1, fileOut);
        }
        catch(IOException ex){
            msg.errorsIO(4, fileOut);
        }
    }

    void writePublicKey(Point Q, String file) throws IOException {
        try {
            var writer = new FileWriter(file);
            var newLine = System.getProperty("line.separator");
            writer.write(Q.getX().toString() + newLine);
            writer.write(Q.getY().toString());
            writer.flush();
        } catch (IOException e) {
            msg.errorsIO(4, file);
        }

    }

}
