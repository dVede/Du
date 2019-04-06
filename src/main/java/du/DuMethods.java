package du;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author dVede
 * @version 1.0
 */
public class DuMethods {

    private final boolean useFormat;
    private final boolean displayTotalSize;
    private final boolean useUnit;

    /**
     * @param useFormat <b>true</b> если найден флаг <b>-h</b>
     * @param displayTotalSize <b>true</b> если найден флаг <b>-c</b>
     * @param useUnit <b>true</b> если найден флаг <b>--si</b>
     */
    public DuMethods(boolean useFormat, boolean displayTotalSize, boolean useUnit) {

        this.useFormat = useFormat;
        this.displayTotalSize = displayTotalSize;
        this.useUnit = useUnit;
    }

    /**
     * Нахождение размера
     * @param path Путь к файлу
     * @return Суммарный размер всех файлов
     * @throws IOException Выкидывется в тех случаях, когда не удалось найти
     * размер файла.
     */
    public long fileSize(Path path) throws IOException {
        ArrayList<Path> paths = new ArrayList<>(Collections.singletonList(path));
        ArrayList<Path> toAddPaths = new ArrayList<>();
        long totalSize = 0;
        while (!paths.isEmpty()) {
            Iterator<Path> iterator = paths.iterator();
            while (iterator.hasNext()) {
                Path next = iterator.next();
                if (Files.isDirectory(next)) {
                    DirectoryStream<Path> directoryFiles = Files.newDirectoryStream(next);
                    for (Path directoryFile : directoryFiles) {
                        toAddPaths.add(directoryFile);
                    }
                    directoryFiles.close();
                }
                else {
                    totalSize += Files.size(next);
                }
                iterator.remove();
            }
            paths.addAll(toAddPaths);
            toAddPaths.clear();
        }
        return totalSize;
    }

    private static final String[] FORMATS = {"B", "KB", "MB", "GB"};


    /**
     * Получение конечного результата
     * @param filePaths Файлы, размеры которыз нужно найти
     * @return Сообщение для вывода
     * @throws IOException Выкидывается в том случае, когда не удается определить размер
     * @throws MyException Выкидывается в том случае, когда путь файла не найден или при
     * недостаточном количестве аргументов
     */

    public List<String> answer(String[] filePaths) throws IOException, MyException{
        if (filePaths == null) {
            throw new MyException("Не введены аргументы");
        }
        List<String> result = new ArrayList<>();
        long summarySize = 0;
        int unit = useUnit ? 1000 : 1024;
        for (String filePath : filePaths) {
            Path path = Paths.get(filePath);
            if (!path.isAbsolute()) {
                path = Paths.get(System.getProperty("user.dir"), filePath);
            }
            if (Files.notExists(path)) {
                throw new MyException("Ошибка, файл " + path + " не существует");
            }
            long size = fileSize(path);
            if (displayTotalSize) {
                summarySize += size;
            }
            else {
                if (useFormat) {
                    result.add(appendFormat("Размер " + filePath + " равен ", size, unit));
                }
                else {
                    result.add("Размер " + filePath + " равен " + size / unit);
                }
            }
        }
        if (displayTotalSize) {
            if (useFormat) {
                result.add(appendFormat("Суммарный размер равен ", summarySize, unit));
            }
            else {
                result.add("Суммарный размер равен " + summarySize / unit);
            }
        }
        return result;
    }

    /**
     * Формат, в котором выведится ответ
     * @param text Текст, который будет содержаться в результате
     * @param size Размер файлов, который будет содержаться в результате
     * @param unit Единица измерения, которая будет содержаться в результате
     * @return Результат
     */
    private String appendFormat(String text, long size, int unit) {
        int form = 0;
        while (size / unit > 0) {
            size /= unit;
            form++;
        }
        return text + size + " " + FORMATS[form];
    }

    public static class MyException extends RuntimeException {
        public MyException(String message) {
            super(message);
        }
    }
}



