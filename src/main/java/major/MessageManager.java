// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package major;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MessageManager {
    Logger log = LogManager.getLogger(MessageManager.class.getName());

    public void status(String remark){
        log.info("The program started with arguments: " + remark);
    }

    public void status(int code) {
        switch (code) {
            case (0):
                System.out.println(help());
                log.info("help called");
                break;
//            case (1):
//                System.out.println(curveSettings());
//                log.info("The requested parameters");
//                break;
            case (2):
                System.out.println("Подпись успешно прошла проверку");
                log.info("The signature was verified");
                break;
            case (3):
                System.out.println("Подпись не верна!");
                log.info("The signature is not correct");
                break;
            default:
                System.out.println("Применение несанкционированного кода сообщения!");
                log.error("Unauthorized message code");
                break;
        }
//        System.exit(0);
    }

    public void basicErrors(int code) throws Exception {
        var msg = "";
        switch (code) {
            case (0):
                msg = "Неверно заданы аргументы\n -h  —  помощь";
                log.info("Invalid arguments");
                break;
            case (1):
                msg = "Отсутствует файл назначения\n -h  —  помощь";
                log.info("Missing destination file");
                break;
            case (2):
                msg = "Подпись нечитаема";
                log.info("The signature is unreadable");
                break;
            case (3):
                msg = "Входные параметры неверны!";
                log.info("Input parameters are incorrect");
                break;
            default:
                msg = "Применение несанкционированного кода ошибки!";
                log.error("Unauthorized error code");
                break;
        }
    throw new Exception(msg);
    }

    public void statusIO(int code, String path) {
        switch (code) {
            case (0):
                System.out.println("Публичный ключ успешно записан в файл " + path);
                log.info("The public key was successfully written to " + path);
                break;
            case (1):
                System.out.println("Электронная цифровая подпись успешно записана в " + path);
                log.info("The EDS was successfully recorded in " + path);
                break;
            default:
                System.out.println("Применение несанкционированного кода сообщения!");
                log.error("Unauthorized message code");
                System.exit(1);
                break;
        }
    }

    public void errorsIO(int code, String path) throws IOException {
        var msg = "";
        switch (code) {
            case (0):
                msg = "Файл отсутствует " + path;
                log.info("The file is missing " + path);
                break;
            case (1):
                msg = "Файл уже присутствует " + path + " воспользуйтесь ручным выбором -o ";
                log.info("The file is already present" + path);
                break;
            case (2):
                msg = "Файл повреждён " + path;
                log.info("The file is corrupted " + path);
                break;
            case (3):
                msg = "Ошибка чтения " + path;
                log.info("Read error " + path);
                break;
            case (4):
                msg = "Ошибка записи " + path;
                log.info("Write error " + path);
                break;
            default:
                msg = "Применение несанкционированного кода сообщения!";
                log.error("Unauthorized message code");
                break;
        }
        throw new IOException(msg);
    }

//    private String curveSettings() {
//        return "Параметры эллиптической кривой \n" +
//                "p = " + SignatureConstants.p + " (модуль эллиптической кривой)\n" +
//                "a = " + SignatureConstants.a + " (коэффициент эллиптичекой кривой)\n" +
//                "b = " + SignatureConstants.b + " (коэффициент эллиптичекой кривой)\n" +
//                "m = " + SignatureConstants.m + " (порядок группы точек эллиптической кривой)\n" +
//                "q = " + SignatureConstants.q + " (порядок циклической подгруппы группы точек эллиптической кривой)\n" +
//                "Xp = " + SignatureConstants.xp + " (коэффициент точки эллиптической кривой)\n" +
//                "Yp = " + SignatureConstants.yp + " (коэффициент точки эллиптичекой кривой)\n";
//    }

    private String help() {
        return "Справка\n " +
                "Приложение реализует алгоритм формирования электронной цифровой подписи в соответствии с ГОСТ 34.10-2018\n" +
                "Имеет три режима работы:\n" +
                "Генерация ЭЦП\n" +
                "Верификация ЭЦП\n" +
                "Генерация ключа проверки\n\n" +
                "Формат аргументов входа\n" +
                "-m %путь_до_сообщения (-s %путь_до_файла_секретного_ключа(режим генерации ЭЦП) [-o %путь_выходного_файла] || -v %путь_до_файла_ключа_верификации -sig %путь_до_файла_подписи(режим верификации)\n" +
                "-q %путь_до_файла_секретного_ключа(режим генерации ключа проверки)\n" +
                "-p вывод текущих параметров эллиптической кривой\n" +
                "-h справка\n\n--\n" +
                "Михаил Шомов\n2020\n";
    }

}
