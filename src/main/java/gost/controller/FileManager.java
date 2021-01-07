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
            var json = parametersReader(fileParameters);
            parameters = new ObjectMapper().readValue(json, SignatureParameters.class);
        } catch (Exception exception) {
            throw new AlienExceptions.FileCorruptedException(fileParameters);
        }
        return parameters;
    }

    public String parametersReader(String path) throws AlienExceptions.FileReadingException {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException exception) {
            throw new AlienExceptions.FileReadingException(path);
        }
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
            var ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            var writer = new FileWriter(file);
            writer.write(ow.writeValueAsString(Q));
        } catch (IOException e) {
            throw new AlienExceptions.FileWritingException(file);
        }

    }

}
