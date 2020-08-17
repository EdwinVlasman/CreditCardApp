package banking;

import java.util.Random;

public class Account {

    private int id = 0;
    private final String pin;
    private final String creditCardNumber;
    private double balance;

    public Account(String accountNumber) {
        Random r = new Random();
        this.pin = String.format("%04d",  r.nextInt( 10000));
        this.creditCardNumber = accountNumber;
        this.balance = 0.0;
        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                this.creditCardNumber +
                "\nYour card PIN:\n" +
                this.pin + "\n");
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getSaldo() {
        return balance;
    }

    private void getBalance() {
        System.out.println("\nBalance: " + this.balance);
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int accountMenu(java.util.Scanner scanner) {
        int choice;
        do {
            System.out.println("\n1. Balance\n" +
                    "2. Log out\n" +
                    "0. Exit");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    getBalance();
                    break;
                case 2:
                    System.out.println("\nYou have successfully logged out!\n");
                    return 2;
                case 0:
                    return 0;
            }
        } while (choice == 1);
        return 1;
    }
}
