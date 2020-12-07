package Main;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {

    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    public void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void success256() throws Exception {
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"});
        Main.main(new String[]{"-p", "Parameters/Signature256", "-q", "Keys/private256.key", "-o", "TestDirectory/publicQ256.kq"});
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ256.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись успешно прошла проверку";
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }

    @Test
    public void success512() throws Exception {
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-s", "Keys/private512.key"});
        Main.main(new String[]{"-p", "Parameters/Signature512", "-q", "Keys/private512.key", "-o", "TestDirectory/publicQ512.kq"});
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ512.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись успешно прошла проверку";
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }

    @Test
    public void unsuccess256() throws Exception {
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"});
        Main.main(new String[]{"-p", "Parameters/Signature512", "-q", "Keys/private256.key", "-o", "TestDirectory/publicQ256.kq"});
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ256.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись не верна!";
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }

    @Test
    public void unsuccess512() throws Exception {
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-s", "Keys/private512.key"});
        Main.main(new String[]{"-p", "Parameters/Signature512", "-q", "Keys/private512.key", "-o", "TestDirectory/publicQ512.kq"});
        msgGen();
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ512.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись не верна!";
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }
    
    @Test
    public void incorrectFlag() {
        assertThrows(Exception.class, () -> Main.main(new String[]{"-t", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"}));
    }

    @Test
    public void openFailed() {
        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "Parameters/Signature25", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"}));
    }
    
    @Test
    public void incorrectParameters() throws IOException {
        parametersGen();
        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "TestDirectory/Parameter", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"}));
    }

    @Test
    public void incorrectVerificationKey() {
        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ256.kq", "-sig", "TestDirectory/message.txt.sig"}));
    }

    @Test
    public void incorrectSignature() throws Exception {
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"});
        Files.delete(Path.of("TestDirectory/message.txt.sig"));
        fakeSigGen();
        Main.main(new String[]{"-p", "Parameters/Signature256", "-q", "Keys/private256.key", "-o", "TestDirectory/publicQ256.kq"});
        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ256.kq", "-sig", "TestDirectory/message.txt.sig"}));
    }

    @Test
    public void incorrectSaving() {
        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key", "-o", "/root/message.sig"}));
        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "Parameters/Signature256", "-q", "Keys/private256.key", "-o", "/root/publicQ256.kq"}));
    }

    @Test
    public void help() throws Exception {
        Main.main(new String[]{"-h"});
        var help = "Справка\n " +
                "Приложение реализует алгоритм формирования электронной цифровой подписи в соответствии с ГОСТ 34.10-2018\n" +
                "Имеет три режима работы:\n" +
                "Генерация ЭЦП\n" +
                "Верификация ЭЦП\n" +
                "Генерация ключа проверки\n\n" +
                "Формат аргументов входа\n" +
                "-p %путь_до_файла_параметров_кривой -m %путь_до_сообщения (-s %путь_до_файла_секретного_ключа(режим генерации ЭЦП) [-o %путь_выходного_файла] || -v %путь_до_файла_ключа_верификации -sig %путь_до_файла_подписи(режим верификации)\n" +
                "-q %путь_до_файла_секретного_ключа(режим генерации ключа проверки)\n" +
                "-p вывод текущих параметров эллиптической кривой\n" +
                "-h справка\n\n--\n" +
                "Михаил Шомов\n2020\n";
        assertEquals(help.trim(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void parametersView() throws Exception {
        Main.main(new String[]{"-p", "Parameters/Signature512"});
        var parameters = "Параметры эллиптической кривой \n" +
        "p = 3623986102229003635907788753683874306021320925534678605086546150450856166624002482588482022271496854025090823603058735163734263822371964987228582907372403 (модуль эллиптической кривой)\n" +
        "a = 7 (коэффициент эллиптичекой кривой)\n" +
        "b = 1518655069210828534508950034714043154928747527740206436194018823352809982443793732829756914785974674866041605397883677596626326413990136959047435811826396 (коэффициент эллиптичекой кривой)\n" +
        "m = 3623986102229003635907788753683874306021320925534678605086546150450856166623969164898305032863068499961404079437936585455865192212970734808812618120619743 (порядок группы точек эллиптической кривой)\n" +
        "q = 3623986102229003635907788753683874306021320925534678605086546150450856166623969164898305032863068499961404079437936585455865192212970734808812618120619743 (порядок циклической подгруппы группы точек эллиптической кривой)\n" +
        "Xp = 1928356944067022849399309401243137598997786635459507974357075491307766592685835441065557681003184874819658004903212332884252335830250729527632383493573274 (коэффициент точки эллиптической кривой)\n" +
        "Yp = 2288728693371972859970012155529478416353562327329506180314497425931102860301572814141997072271708807066593850650334152381857347798885864807605098724013854 (коэффициент точки эллиптичекой кривой)";
        assertEquals(parameters.trim(), outputStreamCaptor.toString().trim());
    }

    public void parametersGen() throws IOException {
        var generator = new EasyRandom();
        var msg = generator.nextObject(String.class);
        Files.write(Paths.get("TestDirectory/Parameter"), msg.getBytes());
    }

    @BeforeEach
    public void folderCreate() throws IOException {
        Files.createDirectory(Paths.get("TestDirectory"));
        msgGen();
    }

    public void msgGen() throws IOException {
        var randomParameters = new EasyRandomParameters().seed(new Random().nextLong());
        var generator = new EasyRandom(randomParameters);
        var msg = generator.nextObject(String.class);
        Files.write(Paths.get("TestDirectory/message.txt"), msg.getBytes());
    }

    public void fakeSigGen() throws IOException {
        var generator = new EasyRandom();
        var msg = generator.nextObject(String.class);
        Files.write(Paths.get("TestDirectory/message.txt.sig"), msg.getBytes());
    }

    @AfterEach
    public void deleteFolderAndItsContent() throws IOException {
        var folder = Paths.get("TestDirectory");
        Files.walkFileTree(folder, new SimpleFileVisitor<>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}