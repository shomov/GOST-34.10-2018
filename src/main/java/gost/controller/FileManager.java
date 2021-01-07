// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package gost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gost.occasion.AlienExceptions;
import gost.occasion.Statuses;
import gost.signature.Point;
import gost.signature.SignatureParameters;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {

    private final MessageManager msg = new MessageManager();

    public void fileCheck(String path, boolean needTo) throws AlienExceptions.IOException {
        var fInput = Paths.get(path);
        var result = Files.exists(fInput) && Files.isRegularFile(fInput);
        if (!result && needTo)
            throw new AlienExceptions.IOException(path);
    }

    public SignatureParameters setConstants (String fileParameters) throws AlienExceptions.FileCorruptedException {
        var parameters = new SignatureParameters(null, null, null, null, null, null, new Point(null, null));
        try {
            var list = parametersReader(fileParameters);
            parameters = new ObjectMapper().readValue(list.get(0), SignatureParameters.class);
        } catch (Exception exception) {
            throw new AlienExceptions.FileCorruptedException(fileParameters);
        }
        return parameters;
    }

    public ArrayList<String> parametersReader(String path) throws AlienExceptions.FileReadingException {
        var result = new ArrayList<String>();
        try {
            var reader = Files.newBufferedReader(Path.of(path));
            var line = reader.readLine();
            while (line != null) {
                result.add(line.strip());
                line = reader.readLine();
            }
        } catch (IOException exception) {
            throw new AlienExceptions.FileReadingException(path);
        }
        return result;
    }

    public int[] messageReader(String path) throws AlienExceptions.FileReadingException {
        try {
            var fis = Files.newInputStream(Path.of(path));
            var data = fis.readAllBytes();
            var dataOut = new int[data.length];
            for (var i = 0; i < data.length; i++)
                dataOut[i] = (data[i] * 8) & 0xFF; //перевод числа в положительный диапазон
            return dataOut;
        } catch (IOException exception) {
            throw new AlienExceptions.FileReadingException(path);
        }
    }

    public String signReader(String path) throws AlienExceptions.FileReadingException {
        try {
            var reader = Files.newBufferedReader(Path.of(path));
            return reader.readLine();
        } catch (IOException exception) {
            throw new AlienExceptions.FileReadingException(path);
        }
    }

    public void writeSignature(String signature, String fileOut) throws AlienExceptions.FileWritingException {
        try {
            var fos = new FileOutputStream(fileOut, true);
            fos.write(signature.getBytes(), 0, signature.getBytes().length);
            fos.close();
            msg.statusIO(Statuses.SIGNWRITE, fileOut);
        }
        catch(IOException ex){
            throw new AlienExceptions.FileWritingException(fileOut);
        }
    }

    public void writePublicKey(Point Q, String file) throws AlienExceptions.FileWritingException {
        try {
            var writer = new FileWriter(file);
            var newLine = System.getProperty("line.separator");
            writer.write(Q.x().toString() + newLine);
            writer.write(Q.y().toString());
            writer.flush();
        } catch (IOException e) {
            throw new AlienExceptions.FileWritingException(file);
        }

    }

}
