// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package gost.major;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gost.signature.SignatureParameters;

import java.io.IOException;

public class MessageManager {
    private final Logger log = LogManager.getLogger(MessageManager.class.getName());

    public void status(String remark){
        log.info("The program started with arguments: " + remark);
    }

    public void status(int code) {
        switch (code) {
            case (0) -> {
                System.out.println(help());
                log.info("help called");
            }
            case (2) -> {
                System.out.println("Подпись успешно прошла проверку");
                log.info("The Main.signature was verified");
            }
            case (3) -> {
                System.out.println("Подпись не верна!");
                log.info("The Main.signature is not correct");
            }
            default -> {
                System.out.println("Применение несанкционированного кода сообщения!");
                log.error("Unauthorized message code");
            }
        }
    }

    public void basicErrors(int code) throws Exception {
        var msg = "";
        switch (code) {
            case (0) -> {
                msg = "Неверно заданы аргументы\n -h  —  помощь";
                log.info("Invalid arguments");
            }
            case (1) -> {
                msg = "Отсутствует файл назначения\n -h  —  помощь";
                log.info("Missing destination file");
            }
            case (2) -> {
                msg = "Подпись нечитаема";
                log.info("The Main.signature is unreadable");
            }
             case (3) -> {
                msg = "Входные параметры неверны!";
                log.info("Input parameters are incorrect");
            }
            default -> {
                msg = "Применение несанкционированного кода ошибки!";
                log.error("Unauthorized error code");
            }
        }
    throw new Exception(msg);
    }

    public void statusIO(int code, String path) {
        switch (code) {
            case (0) -> {
                System.out.println("Публичный ключ успешно записан в файл " + path);
                log.info("The public key was successfully written to " + path);
            }
            case (1) -> {
                System.out.println("Электронная цифровая подпись успешно записана в " + path);
                log.info("The EDS was successfully recorded in " + path);
            }
            default -> {
                System.out.println("Применение несанкционированного кода сообщения!");
                log.error("Unauthorized message code");
                System.exit(1);
            }
        }
    }

    public void errorsIO(int code, String path) throws IOException {
        var msg = "";
        switch (code) {
            case (0) -> {
                msg = "Ошибка ввода/вывода. Проверьте пути " + path;
                log.info("IO error " + path);
            }
            case (1) -> {
                msg = "Файл повреждён " + path;
                log.info("The file is corrupted " + path);
            }
            case (2) -> {
                msg = "Ошибка чтения " + path;
                log.info("Read error " + path);
            }
            case (3) -> {
                msg = "Ошибка записи " + path;
                log.info("Write error " + path);
            }
            default -> {
                msg = "Применение несанкционированного кода сообщения!";
                log.error("Unauthorized message code");
            }
        }
        throw new IOException(msg);
    }

    public void curveSettings(SignatureParameters parameters) {
        System.out.println(parameters.toString());
    }

    private String help() {
        return """
                Справка
                Приложение реализует алгоритм формирования электронной цифровой подписи в соответствии с ГОСТ 34.10-2018
                Имеет три режима работы:
                Генерация ЭЦП
                Верификация ЭЦП
                Генерация ключа проверки

                Формат аргументов входа
                -p %путь_до_файла_параметров_кривой -m %путь_до_сообщения (-s %путь_до_файла_секретного_ключа(режим генерации ЭЦП) [-o %путь_выходного_файла] || -v %путь_до_файла_ключа_верификации -sig %путь_до_файла_подписи(режим верификации)
                -q %путь_до_файла_секретного_ключа(режим генерации ключа проверки)
                -p вывод текущих параметров эллиптической кривой
                -h справка

                --
                Михаил Шомов
                2020
                """;
    }

}
