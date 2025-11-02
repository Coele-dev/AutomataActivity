import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NfaChecker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter strings {a,b}. Type 'q' to quit.");
        while (true) {
            System.out.print("> ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("q")) break;
            if (input.isEmpty()) {
                System.out.println("\033[31mInvalid input. String cannot be empty.\033[0m");
                continue;
            }
            if (!input.matches("[ab]+")) {
                System.out.println("\033[31mInvalid input. Only a and b are allowed.\033[0m");
                continue;
            }

            char[] chars = input.toCharArray();
            boolean accepted = acceptsNfa(chars);
            if (accepted) {
                System.out.println("\033[32mAccepted: contains substring \"ab\".\033[0m");
            } else {
                System.out.println("\033[31mRejected: does not contain substring \"ab\".\033[0m");
            }
        }
        sc.close();
    }

    // Entry: start state = 0
    static boolean acceptsNfa(char[] input) {
        Set<String> seen = new HashSet<>(); // visited (state,pos) to avoid infinite recursion
        return dfs(input, 0, 0, seen);
    }

    // Recursive DFS: pos = current index in input, state = current NFA state
    static boolean dfs(char[] input, int pos, int state, Set<String> seen) {
        String key = state + ":" + pos;
        if (seen.contains(key)) return false;
        seen.add(key);

        //Triggers when end of input is reached
        if (pos == input.length) {
            // accept only if in accepting state (2)
            return state == 2;
        }

        char c = input[pos];
        List<Integer> nextStates = check(state, c);
        for (int ns : nextStates) {
            if (dfs(input, pos + 1, ns, seen)) return true;
        }
        return false;
    }
    /*
    NFA for language contains substring ab
    States:
    0 = start state or hasn't seen an a yet.
    1 = have seen an a that could be followed by b
    2 = accepting (we have seen ab in the string)
    */
    static List<Integer> check(int state, char c) {
        switch (state) {
            case 0:
                // if input a -> loop to 0 (continue scanning) and also go to 1. if input b -> stay on 0 
                return (c == 'a') ? Arrays.asList(0, 1) : Arrays.asList(0); 
            case 1:
                // if sees input b -> accept; if sees input a -> remain in 1 (tries to find input b to accept string)
                return (c == 'b') ? Arrays.asList(2) : Arrays.asList(1);
            case 2:
                // if string reaches this state it stays in this state (accepting state)
                return Arrays.asList(2);
            default:
                return Arrays.asList();
        }
    }
}