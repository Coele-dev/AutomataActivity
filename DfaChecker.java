import java.util.Scanner;

public class DfaChecker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter binary strings (only 0/1). Type 'q' to quit.");
        while (true) {
            System.out.print("> ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("q")) break;

            if (input.isEmpty()) {
                System.out.println("\033[31mInvalid input. String cannot be empty.\033[0m");
                continue;
            }
            if (!input.matches("[01]+")) {
                System.out.println("\033[31mInvalid input. Only '0' and '1' are allowed.\033[0m");
                continue;
            }
            char[] binary = input.toCharArray();
            int state = 0;
            for (char cell : binary) {
                switch (state) {
                    case 0:
                        state = (cell == '0') ? 1 : 0;
                        break;
                    case 1:
                        state = (cell == '1') ? 2 : 1; // if input 0, stay in state 1. if input 1, proceed to state 2
                        break;
                    case 2:
                        state = (cell == '0') ? 1 : 0; // after 01, update based on new char
                        break;
                }
            }
            if (state == 2) {
                System.out.println("\033[32mAccepted: string ends with 01.\033[0m");
            } else {
                System.out.println("\033[31mRejected: string does not end with 01.\033[0m");
            }
        }

        sc.close();
    }
}