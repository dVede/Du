package du;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.*;

public class DuParse {
    @Option(name = "-h", usage = "Normal format")
    private boolean useFormat = false;

    @Option(name = "-c", usage = "Summary size")
    private boolean displayTotalSize = false;

    @Option(name = "--si", usage = "size / 1000")
    private boolean useUnit = false;

    @Argument(metaVar = "fileN", usage = "Input file name")
    private String[] fileN;


    public static void main(String[] args) throws IOException {
        new DuParse().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }

        DuMethods du = new DuMethods(useFormat, displayTotalSize, useUnit);
        try {
            List<String> out = du.answer(fileN);
            out.forEach(System.out::println);
        }
        catch (DuMethods.MyException e){
            System.err.println("No file chosen");
        }
    }
}

