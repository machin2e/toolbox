package camp.computer.platform_infrastructure;

import camp.computer.Interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadBuildFileTask {

    public void execute(String filePath) {

        FileReader in = null;
        try {
            in = new FileReader(filePath);
            BufferedReader br = new BufferedReader(in);
            String line = br.readLine();
            while (line!=null) {
                System.out.println(line);
                Interpreter.getInstance().interpretLine(line);
                line = br.readLine();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
