package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Industry bank = new Industry();
        Account account;
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    bank.createAccount();
                    break;
                case 2:
                    account = bank.getAccount(scanner);
                    if (account != null) {
                        choice = account.accountMenu(scanner);
                    }
                    break;
            }
            if (choice == 0) {
                System.out.println("\nBye!");
                break;
            }

        }
    }

    public static void printMenu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }
}
