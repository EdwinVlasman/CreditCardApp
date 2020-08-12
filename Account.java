package banking;

public class Account {

    private final int pin;
    private final long creditCardNumber;
    private double balance;

    public Account(long accountNumber) {
        this.pin = (int) ((Math.random() +1) * 1000);
        this.creditCardNumber = accountNumber;
        this.balance = 0.0;
        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                this.creditCardNumber +
                "\nYour card PIN:\n" +
                + this.pin + "\n");
    }

    private void getBalance() {
        System.out.println("\nBalance: " + this.balance);
    }

    public long getCreditCardNumber() {
        return creditCardNumber;
    }

    public int getPin() {
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
