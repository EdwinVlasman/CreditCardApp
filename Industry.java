package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Industry {

    private String databaseURL;
    private final List<Account> accounts;
    private final Set<String> accountNumbers;
    private final int IIN;

    public Industry() {
        this.accounts = new ArrayList<>();
        this.accountNumbers = new HashSet<>();
        this.IIN = 400000;
    }

    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public void createAccount() {
        String accountNumber;
        do {
            Random r = new Random();
            accountNumber = String.format("%09d",  r.nextInt( 1000000000));
        } while (accountNumbers.contains(accountNumber));
        accountNumbers.add(accountNumber);
        String cardNumberExceptLastDigit = this.IIN +
                accountNumber;
        String cardNumber = this.IIN +
                accountNumber +
                checkLuhn(cardNumberExceptLastDigit);
        Account newCard = new Account(cardNumber);
        accounts.add(newCard);
        newCard.setId(this.accounts.size());

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(databaseURL);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("INSERT INTO card VALUES " +
                        "(" + newCard.getId() + "," +
                        newCard.getCreditCardNumber() + "," +
                        newCard.getPin() + "," +
                        newCard.getSaldo() + ")"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(java.util.Scanner scanner) {
        System.out.println("Enter your card number:");
        String accountNumber = scanner.nextLine();
        System.out.println("enter your PIN:");
        String pin = scanner.nextLine();
        if (isAccount(accountNumber, pin)) {
            for (Account account : accounts) {
                if (accountNumber.equals(account.getCreditCardNumber())) {
                    System.out.println("You have successfully logged in!\n");
                    return accounts.get(accounts.indexOf(account));
                }
            }
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
        return null;
    }

    private boolean isAccount(String accountNumber, String pin) {
        boolean accountnr = false;
        boolean pinnr = false;
        for (Account account : accounts) {
            if (accountNumber.equals(account.getCreditCardNumber())) {
                accountnr = true;
                if (pin.equals(account.getPin())) {
                    pinnr = true;
                }
            }
        }
        return accountnr && pinnr;
    }

    private String checkLuhn(String accountNumber) {
        char[] numbers = accountNumber.toCharArray();
        int [] numbersToInt = new int[numbers.length];
        for (int i = 0; i < numbers.length; i ++) {
            numbersToInt[i] = Character.getNumericValue(numbers[i]);
        }
        int[] temp = new int[accountNumber.length()];
        for (int i = 0; i < numbersToInt.length; i++) {
            if ((i + 1) % 2 != 0) {
                if (numbersToInt[i] * 2 > 9) {
                    temp [i] = numbersToInt[i] * 2 - 9;
                } else {
                    temp [i] = numbersToInt[i] * 2;
                }
            } else {
                temp[i] = numbersToInt[i];
            }
        }
        int sum = 0;
        for (int i : temp) {
            sum += i;
        } if (sum % 10 == 0) {
            return "0";
        }
        return String.valueOf(10 - (sum % 10));
    }
}
