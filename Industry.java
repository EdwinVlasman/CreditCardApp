package banking;

import java.util.*;

public class Industry {

    private List<Account> accounts;
    private Set<Long> accountNumbers;
    private final int MII;
    private final int IIN;

    public Industry() {
        this.accounts = new ArrayList<>();
        this.accountNumbers = new HashSet<>();
        this.MII = 4;
        this.IIN = 400000;
    }

    public void createAccount() {
        long accountNumber;
        do {
            accountNumber = (long) ((Math.random() + 1) * 100000000);
        } while (accountNumbers.contains(accountNumber));
        accountNumbers.add(accountNumber);
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append(this.IIN);
        cardNumber.append(accountNumber);
        cardNumber.append(8);
        Account newCard = new Account(Long.parseLong(cardNumber.toString()));
        accounts.add(newCard);
    }

    public Account getAccount(java.util.Scanner scanner) {
        System.out.println("Enter your card number:");
        long accountNumber = scanner.nextLong();
        scanner.nextLine();
        System.out.println("enter your PIN:");
        int pin = scanner.nextInt();
        if (isAccount(accountNumber, pin)) {
            for (Account account : accounts) {
                if (account.getCreditCardNumber() == accountNumber) {
                    System.out.println("You have successfully logged in!\n");
                    return accounts.get(accounts.indexOf(account));
                }
            }
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
        return null;
    }

    private boolean isAccount(long accountNumber, int pin) {
        boolean accountnr = false;
        boolean pinnr = false;
        for (Account account : accounts) {
            if (account.getCreditCardNumber() == accountNumber) {
                accountnr = true;
                if (account.getPin() == pin) {
                    pinnr = true;
                }
            }
        }
        if (accountnr && pinnr) {
            return true;
        } else {
            return false;
        }
    }
}
