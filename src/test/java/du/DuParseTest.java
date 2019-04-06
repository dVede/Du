package du;

import du.DuParse;
import du.DuParse.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class DuParseTest {
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut = System.out;
    private ByteArrayInputStream inContent;

    @Before
    public void duStream() {
        String input = "du";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void consoleTest() throws IOException {
    String[] args1 = {"-c", "-h", "C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\1.txt",
            "C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory"};
    DuParse.main(args1);
    String excepted = "Суммарный размер равен 499 KB\r\n" ;
    String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @Test
    public void absoluteTest() throws IOException {
        String[] args1 = {"-c", "-h", "1.txt",
                "C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory"};
        DuParse.main(args1);
        String excepted = "Суммарный размер равен 413 KB\r\n" ;
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @Test
    public void noArgumentTest() throws IOException {
        String[] args1 = {"-c", "-h"};
        DuParse.main(args1);
        String excepted = "" ;
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @Test
    public void notCorrectTest() throws IOException {
        String[] args1 = {"-correct", "-h"};
        DuParse.main(args1);
        String excepted = "" ;
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @Test
    public void fileNotFound() throws IOException {
        String[] args1 = {"-c", "-h", "2.txt",
                "C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory"};
        DuParse.main(args1);
        String excepted = "" ;
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @Test
    public void withNoSummarySize() throws IOException {
        String[] args1 = {"-h", "--si","1.txt",
                "C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory"};
        DuParse.main(args1);
        String excepted = "Размер 1.txt равен 12 KB\r\n" +
                "Размер C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory равен 410 KB\r\n" ;
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @Test
    public void onlyUseUnit() throws IOException {
        String[] args1 = {"--si","1.txt",
                "C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory"};
        DuParse.main(args1);
        String excepted = "Размер 1.txt равен 12\r\n" +
                "Размер C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory равен 410\r\n" ;
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @Test
    public void withNoUseUnit() throws IOException {
        String[] args1 = {"-c", "--si", "1.txt",
                "C:\\Users\\пользователь\\IdeaProjects\\du\\out\\artifacts\\du_jar\\testDirectory"};
        DuParse.main(args1);
        String excepted = "Суммарный размер равен 423\r\n";
        String actual = outContent.toString();
        Assertions.assertEquals(excepted, actual);
    }
    @After
    public void restoreStreams(){
        System.setOut(originalOut);
    }
}