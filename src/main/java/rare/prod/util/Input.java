package rare.prod.util;

import java.util.Scanner;

public class Input {

    private static Scanner sc = new Scanner(System.in);

    public static String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return sc.nextLine();
    }

}
