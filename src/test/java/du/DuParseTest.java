package du;

import du.DuParse;
import du.DuParse.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.After;
import org.junit.jupiter.api.*;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path;

public class DuParseTest {
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut = System.out;
    private ByteArrayInputStream inContent;

    private String resourcesDirectory = new File("src/test/resources").getAbsolutePath();

    private Path dir2 = Paths.get("src","test","resources","testDirectory");

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void duStream() {
        String input = "du";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void consoleTest() throws IOException {
        Path dir1 = Paths.get("src","test","resources","1.txt");
        Path dir2 = Paths.get("src","test","resources","testDirectory");
        String[] args1 = {"-c", "-h", dir1.toString(), dir2.toString()};
        DuParse.main(args1);
        String excepted = "Суммарный размер равен 499 KB" + System.lineSeparator() ;
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }

    @Test
    public void absoluteTest() throws IOException { ;
        String[] args1 = {"-c", "-h", "1.txt", dir2.toString()};
        DuParse.main(args1);
        String excepted = "Суммарный размер равен 413 KB" + System.lineSeparator();
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }

    @Test
    public void noArgumentTest() throws IOException {
        String[] args1 = {"-c", "-h"};
        exit.expectSystemExitWithStatus(1);
        DuParse.main(args1);
    }

    @Test(expected = NullPointerException.class)
    public void notCorrectTest() throws IOException {
        String[] args1 = {"-correct", "-h"};
        DuParse.main(args1);
    }

    @Test
    public void fileNotFound() throws IOException {
        String[] args1 = {"-c", "-h", "2.txt", "testDirectory"};
        exit.expectSystemExitWithStatus(1);
        DuParse.main(args1);
    }

    @Test
    public void withNoSummarySize() throws IOException {
        String[] args1 = {"-h", "--si", "1.txt", dir2.toString()};
        DuParse.main(args1);
        String excepted = "Размер 1.txt равен 12 KB" + System.lineSeparator() +
                "Размер " + dir2.toString() + " равен 410 KB" + System.lineSeparator();
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }

    @Test
    public void onlyUseUnit() throws IOException {
        String[] args1 = {"--si", "1.txt", dir2.toString()};
        DuParse.main(args1);
        String excepted = "Размер 1.txt равен 12" + System.lineSeparator() +
                "Размер " + dir2.toString() + " равен 410" + System.lineSeparator();
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }

    @Test
    public void withNoUseFormat() throws IOException {
        String[] args1 = {"-c", "--si", "1.txt",
                dir2.toString()};
        DuParse.main(args1);
        String excepted = "Суммарный размер равен 423" + System.lineSeparator();
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }

    @After
    public void restoreStreams(){
        System.setOut(originalOut);
    }
}