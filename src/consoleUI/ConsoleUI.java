package consoleUI;

import core.Field;
import core.GameState;
import core.Kamene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ConsoleUI implements UserInterface {

    Field field;

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private final ArrayList<String> listOfCommands = new ArrayList<>(
            Arrays.asList("new", "exit", "w", "s", "a", "d", "up", "down", "right", "left"));

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void newGameStarted(Field field) {
        this.field = field;

        do {
            update();
            processInput();

            if(field.getGameState() == GameState.SOLVED) {
                System.out.println("Vyhral si hru");
                break;
            }

            if(field.getGameState() == GameState.EXIT) {
                System.out.println("Koniec hry");
                break;
            }

            if(field.getGameState() == GameState.NEW_GAME) {
                System.out.println("Vytvaram ti novu hru");
                break;
            }

        } while(true);

        if(field.getGameState() == GameState.EXIT) {
            System.out.println("Koniec hry");
            System.exit(0);
        }

        if(field.getGameState() == GameState.NEW_GAME) {
            newGameStarted(new Field(Kamene.getInstance().getSetting().getRozmerStvorca(),
                    Kamene.getInstance().getSetting().getRozmerStvorca()));
        }

        if(field.getGameState() == GameState.SOLVED) {
            if(wantNewRound()) {
                System.out.println("Vytvaram nove kolo");
                newGameStarted(new Field(Kamene.getInstance().getSetting().getRozmerStvorca(),
                        Kamene.getInstance().getSetting().getRozmerStvorca()));
            } else {
                System.out.println("Ukoncujem hru");
                System.exit(0);
            }
        }

    }

    public boolean wantNewRound() {
        System.out.println("Chces si zahrat este raz? Y/N");
        String choice = readLine().toLowerCase(Locale.ROOT);
        if(choice.equals("y")) {
            return true;
        }
        if(choice.equals("n")) {
            return false;
        }
        System.out.println("Nespravne zadanie, skus este raz");
        return wantNewRound();
    }

    public void handleInput(String playerInput) throws WrongFormatException {
        //overi vstup, porovna s listom prikazov
        if(!listOfCommands.contains(playerInput)) {
            throw new WrongFormatException("Zadal si nespravny vstup, skus este raz.");
        }
    }

    public void processInput() {
        String playerInput = readLine().toLowerCase(Locale.ROOT);

        try {
            handleInput(playerInput);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
            processInput();
            return;
        }

        //vykona operaciu podla vstupu
        doOperation(playerInput);
        //skontroluje ci hra je vyriesena
        if(field.isSolved()) {
            field.setGameState(GameState.SOLVED);
        }
        //vypise dobu trvania daneho kola v sekundach
        System.out.printf("Toto kolo hrajes %s sekund.%n", field.getPlayingSeconds());
    }

    public void doOperation(String playerInput) {
        switch (playerInput) {
            case "up":
            case "w":
                field.moveUp();
                break;
            case "down":
            case "s":
                field.moveDown();
                break;
            case "right":
            case "d":
                field.moveRight();
                break;
            case "left":
            case "a":
                field.moveLeft();
                break;
            case "new":
                field.setGameState(GameState.NEW_GAME);
                break;
            case "exit":
                field.setGameState(GameState.EXIT);
        }
    }

    @Override
    public void update() {
        System.out.println("Zadaj: (New) -> pre novu hru, (Exit) -> pre ukoncenie hry, (Up,w) -> pre posun hore," +
                " (Down,s) -> pre posun dole, (Right,d) -> pre posun doprava, (Left,a) -> pre posun dolava");
        printField();
    }

    public void printField() {
        for (int i = 0; i < this.field.getRowCount(); i++) {
            for (int j = 0; j < this.field.getColumnCount(); j++) {
                System.out.printf("%3s",this.field.getTile(i,j).toString());
            }
            System.out.println();
        }
    }
}
