package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    Client client;


    public Repl(String serverUrl) {
        this.client = new Client(serverUrl);
    }

    public void run() {
        System.out.println(BLACK_KING + SET_TEXT_COLOR_YELLOW + "Welcome to the Ultimate Chess Experience" + RESET_TEXT_COLOR + WHITE_KING+"\n");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("exit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(RESET_BG_COLOR+ "\n" + SET_TEXT_ITALIC + SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK +
                                 result + RESET_TEXT_COLOR + RESET_BG_COLOR + "\n");
            } catch (Exception e) {
                System.out.println(SET_TEXT_ITALIC + SET_BG_COLOR_RED + SET_TEXT_COLOR_BLACK + e.getMessage() + RESET_TEXT_COLOR + RESET_BG_COLOR);
            }
        }
        System.out.println(BLACK_KING + SET_TEXT_COLOR_YELLOW +"Thank you for using Ultimate Chess Experience"+RESET_TEXT_COLOR + WHITE_KING+"\n");
    }

    private void printPrompt(){
        System.out.println("\n" + RESET_TEXT_COLOR + RESET_BG_COLOR + ">>> ** Type " + SET_TEXT_ITALIC +"help" +
                           RESET_TEXT_ITALIC+ " to review options **" + SET_TEXT_COLOR_GREEN + "\n");
    }

}
