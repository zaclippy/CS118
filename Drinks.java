import java.util.Arrays;
import java.util.Scanner;

public class Drinks {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your age: ");
        int age = sc.nextInt();
        System.out.println("You are " + (age >= 18 ? "over 18. You may continue."
                : "going to be 18 in " + (18 - age) + " years.\nKindly piss off."));
        if (age >= 18) {
            String drinks[] = new String[3];
            System.out.println("Enter your two favourite drinks...");
            drinks[0] = sc.next();
            drinks[1] = sc.next();
            drinks[2] = sc.next();
            System.out.println("Favourite drinks: " + Arrays.toString(drinks));
        }
        sc.close();
    }
}