package core;

import consoleUI.ConsoleUI;
import consoleUI.UserInterface;

public class Kamene {

    private UserInterface userInterface;

    private static Kamene instance;

    private Settings setting; //nastavenie obtiaznosti

    public Kamene() {
        setting = Settings.BEGINNER;
        instance = this;
        userInterface = new ConsoleUI();
        Field field = new Field(setting.getRozmerStvorca(), setting.getRozmerStvorca());
        userInterface.newGameStarted(field);
    }

    public static void main(String[] args) {

        System.out.println("Hello " + System.getProperty("user.name") + " !");
        new Kamene();
    }

    public static Kamene getInstance() {
        if(instance == null) {
            instance = new Kamene();
        }
        return instance;
    }

    public Settings getSetting() {
        return setting;
    }
}
