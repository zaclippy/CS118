import java.util.Scanner;

public class Age {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your age: ");
        int age = sc.nextInt();
        System.out.println("You are " +(age>=18 ? "over 18" : "going to be 18 in "+(18-age)+" years"));     
        sc.close();   
    }
}