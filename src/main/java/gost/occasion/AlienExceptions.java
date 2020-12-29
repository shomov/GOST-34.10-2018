package gost.occasion;

import gost.controller.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlienExceptions {
    private final static Logger log = LogManager.getLogger(MessageManager.class.getName());

    public static class IllegalArgumentException extends Exception {
        public IllegalArgumentException() {
            super("Неверно заданы аргументы\n -h  —  помощь");
            log.info("Invalid arguments");
        }
    }

    public static class DestinationFileNotFoundException extends Exception {
        public DestinationFileNotFoundException() {
            super("Отсутствует файл назначения\n -h  —  помощь");
            log.info("Missing destination file");
        }
    }

    public static class SignatureUnreadableException extends Exception {
        public SignatureUnreadableException() {
            super("Подпись нечитаема");
            log.info("The signature is unreadable");
        }
    }

    public static class IncorrectParametersException extends Exception {
        public IncorrectParametersException() {
            super("Входные параметры неверны!");
            log.info("Input parameters are incorrect");
        }
    }

    public static class IOException extends Exception {
        public IOException(String path) {
            super("Ошибка ввода/вывода. Проверьте пути " + path);
            log.info("IO error " + path);
        }
    }

    public static class FileCorruptedException extends Exception {
        public FileCorruptedException(String path) {
            super("Файл повреждён " + path);
            log.info("The file is corrupted " + path);
        }
    }

    public static class FileReadingException extends Exception {
        public FileReadingException(String path) {
            super("Ошибка чтения " + path);
            log.info("Read error " + path);
        }
    }

    public static class FileWritingException extends Exception {
        public FileWritingException(String path) {
            super("Ошибка записи " + path);
            log.info("Write error " + path);
        }
    }

}
