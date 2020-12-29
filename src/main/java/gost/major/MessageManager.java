// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package gost.major;

import gost.occasion.Statuses;
import gost.signature.SignatureParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageManager {
    private final Logger log = LogManager.getLogger(MessageManager.class.getName());

    public void status(String remark){
        log.info("The program started with arguments: " + remark);
    }

    public void status(Statuses code) {
        switch (code) {
            case HELP -> {
                System.out.println(help());
                log.info("help called");
            }
            case VERIVIED -> {
                System.out.println("Подпись успешно прошла проверку");
                log.info("The Main.signature was verified");
            }
            case UNVERIVIED -> {
                System.out.println("Подпись не верна!");
                log.info("The Main.signature is not correct");
            }
            default -> {
                System.out.println("Применение несанкционированного кода сообщения!");
                log.error("Unauthorized message code");
            }
        }
    }

    public void statusIO(Statuses code, String path) {
        switch (code) {
            case KEYWRITE -> {
                System.out.println("Публичный ключ успешно записан в файл " + path);
                log.info("The public key was successfully written to " + path);
            }
            case SIGNWRITE -> {
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
