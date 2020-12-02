import org.jeasy.random.EasyRandom;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Nested
@DisplayName("Success Cases")
public class MainTest {

    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    public void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void success256() throws Exception {
        msgGen();
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"});
        Main.main(new String[]{"-p", "Parameters/Signature256", "-q", "Keys/private256.key", "-o", "TestDirectory/publicQ256.kq"});
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ256.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись успешно прошла проверку";
        assertEquals(expected, outputStreamCaptor.toString().trim());
        deleteFolderAndItsContent();
    }

    @Test
    public void success512() throws Exception {
        msgGen();
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-s", "Keys/private512.key"});
        Main.main(new String[]{"-p", "Parameters/Signature512", "-q", "Keys/private512.key", "-o", "TestDirectory/publicQ512.kq"});
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ512.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись успешно прошла проверку";
        assertEquals(expected, outputStreamCaptor.toString().trim());
        deleteFolderAndItsContent();
    }

    @Test
    public void unsuccess256() throws Exception {
        msgGen();
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"});
        Main.main(new String[]{"-p", "Parameters/Signature256", "-q", "Keys/private256.key", "-o", "TestDirectory/publicQ256.kq"});
        msgGen();
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ256.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись не верна!";
        assertEquals(expected, outputStreamCaptor.toString().trim());
        deleteFolderAndItsContent();
    }

    @Test
    public void unsuccess512() throws Exception {
        msgGen();
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-s", "Keys/private512.key"});
        Main.main(new String[]{"-p", "Parameters/Signature512", "-q", "Keys/private512.key", "-o", "TestDirectory/publicQ512.kq"});
        msgGen();
        outputStreamCaptor.reset();
        Main.main(new String[]{"-p", "Parameters/Signature512", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ512.kq", "-sig", "TestDirectory/message.txt.sig"});
        final var expected = "Подпись не верна!";
        assertEquals(expected, outputStreamCaptor.toString().trim());
        deleteFolderAndItsContent();
    }

//    @Test
//    void incorrect() throws IOException {
//        //Неверный флаг
//        assertThrows(Exception.class, () -> Main.main(new String[]{"-t", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"}));
//        //Неверные параметры
//        parametersGen();
//        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "TestDirectory/Parameter", "-m", "TestDirectory/message.txt", "-s", "Keys/private256.key"}));
//        //Неверный ключ проверки
//        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-v", "TestDirectory/publicQ256.kq", "-sig", "TestDirectory/message.txt.sig"}));
//        //Неверный ключ подписи
//        assertThrows(Exception.class, () -> Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private.key"}));
//
//
//
//    }
//
//
//    @Test
//    @DisplayName("System.exit(1) is caught and detected")
//    @ExpectSystemExitWithStatus(1)
//    void privateKeyIsAbsent() throws Exception {
//        Main.main(new String[]{"-p", "Parameters/Signature256", "-m", "TestDirectory/message.txt", "-s", "Keys/private.key"});
//    }
//
//
//    @After
//    public void parametersGen() throws IOException {
//        var generator = new EasyRandom();
//        var msg = generator.nextObject(String.class);
//        var writer = new FileWriter("TestDirectory/Parameter", true);
//        writer.write(msg);
//        writer.flush();
//        writer.close();
//    }

    @After
    public void msgGen() throws IOException {
        var generator = new EasyRandom();
        var msg = generator.nextObject(String.class);
        new File("TestDirectory").mkdir();
        var writer = new FileWriter("TestDirectory/message.txt", true);
        writer.write(msg);
        writer.flush();
        writer.close();
    }

    @Before
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