package camp.computer.platform_infrastructure;

import camp.computer.Interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InterpretLineTask {

    public void execute(String inputLine) {
        Interpreter.getInstance().interpretLine(inputLine);
    }
}
