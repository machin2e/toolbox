package camp.computer.construct;

import camp.computer.Application;

public class Error {

    // TODO: Make Error extend Type

    enum Type {
        ERROR,
        WARNING,
        INFO
    }

    public String message = null;

    private Error(String message) {
        this.message = Application.ANSI_RED + "Error: " + message + Application.ANSI_RESET;
    }

    public static Error get(String message) {
        return new Error(message);
    }

    @Override
    public String toString() {
        return this.message;
    }
}
