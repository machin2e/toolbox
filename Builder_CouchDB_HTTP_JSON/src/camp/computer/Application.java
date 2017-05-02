package camp.computer;

import java.util.Scanner;

import camp.computer.model.Device;
import camp.computer.model.Port;
import camp.computer.model.Project;
import camp.computer.util.CouchDB;

public class Application {

    CouchDB couchDB;

    public Application() {
        couchDB = new CouchDB();
    }

    public void start() {

        // Authenticate
        System.out.println("1. new user");
        System.out.println("2. existing user");
        System.out.println("3. exit");
        System.out.print("~ ");

        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();

        if (name.equals("1")) {
            System.out.print("username: ");
            String username = scanner.next();
            System.out.print("password: ");
            String password = scanner.next();
            couchDB.createUser(username, password);
        } else if (name.equals("2")) {
            System.out.print("username: ");
            String username = scanner.next();
            System.out.print("password: ");
            String password = scanner.next();
            boolean isAuthenticated = couchDB.authenticateUser(username, password);
            if (isAuthenticated) {
                // Cache the user's username and password for later requests
                couchDB.username = username;
                couchDB.password = password;
                run();
            }
        } else if (name.equals("3")) {
            System.exit(0);
        }

    }

    private void run() {

        Scanner scanner = new Scanner(System.in);
        String inputLine = null;

        while (true) {
            System.out.print("~ ");
            inputLine = scanner.nextLine();
            interpretLine(inputLine);
        }

    }

    public void interpretLine(String inputLine) {

        // TODO: create user (if not exists)
        // TODO: create user database (if not exists)
        // TODO: add user as member of their database

        if (inputLine.matches("^(list)($|[ ]+.*)")) {
            // list [port|device|project|...]
            String[] inputTokens = inputLine.split("[ ]+");
            if (inputTokens.length == 1) {
                couchDB.listDocuments(null);
            } else if (inputTokens.length == 2) {
                couchDB.listDocuments(inputTokens[1]);
            }
        } else if (inputLine.matches("^(save)($|[ ]+.*)")) {
            // save [port|device|project|...]
            System.out.println("saving...");
            String[] inputTokens = inputLine.split("[ ]+");
            if (inputTokens.length == 1) {
                // TODO: Ask user what to save. Then start interactive construction of object.
            } else if (inputTokens.length == 2) {
                saveDocument(inputTokens[1]);
            }
        } else if (inputLine.matches("^(find)($|[ ]+.*)")) {
            String[] inputTokens = inputLine.split("[ ]+");
            if (inputTokens.length == 5 && inputTokens[1].equals("port")) {
                couchDB.findPortDocument(inputTokens[2], inputTokens[3], inputTokens[4]);
            } else if (inputTokens[1].equals("device")) {
//                findDeviceDocument(inputTokens[2], inputTokens[3], inputTokens[4]);
                couchDB.findDeviceDocument("", "", "");
            } else {
                System.out.println("Error: Invalid number of arguments.");
            }
        } else if (inputLine.matches("^(load)($|[ ]+.*)")) {
            System.out.println("loading...");
        } else if (inputLine.matches("^(exit)($|[ ]+.*)")) {
            System.out.println("exiting...");
            System.exit(0);
        }

    }

    public void saveDocument(String type) {
        if (type.equals("port")) {
            Port port = Port.generateRandom();
            couchDB.savePortDocument(port);
        } else if (type.equals("device")) {
            Device device = Device.generateRandom();
//            saveDeviceDocument("component name here", "component description here");
            couchDB.saveDeviceDocument(device);
        } else if (type.equals("project")) {
            Project project = Project.generateRandom();
//            saveProjectDocument("My Project Name", "My project description here!");
            couchDB.saveProjectDocument(project);
        }
    }

}
