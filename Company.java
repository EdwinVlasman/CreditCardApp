package CreditCardCompany;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Company {

    private final String companyName;
    private final int IIN;
    private final SQLiteDataSource dataSource;

    public Company(int IIN, String companyName) {
        this.companyName = companyName;
        this.IIN = IIN;
        this.dataSource = new SQLiteDataSource();
    }

    public void setDatabaseURL(String databaseURL) {
        this.dataSource.setUrl(databaseURL);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void createAccount() {
        String accountNumber;
        Random r = new Random();
        //create a random pin number
        String pin = String.format("%04d",  r.nextInt( 10000));
        //create a random unique account number
        do {
            accountNumber = String.format("%09d",  r.nextInt( 1000000000));
        } while (isAccount(accountNumber, pin));
        // create credit card number with luhn algorithm
        String cardNumberExceptLastDigit = this.IIN +
                accountNumber;
        String cardNumber = this.IIN +
                accountNumber +
                checkLuhn(cardNumberExceptLastDigit);
        // add newly created account to database
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("INSERT INTO card (number, pin, balance) VALUES " +
                        "('" + cardNumber + "', '" +
                        pin + "', " +
                                "0)"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // show message with credit card and pin number
        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                cardNumber +
                "\nYour card PIN:\n" +
                pin + "\n");
    }

    public Account getAccount(java.util.Scanner scanner) {
        System.out.println("Enter your card number:");
        String accountNumber = scanner.nextLine();
        System.out.println("enter your PIN:");
        String pin = scanner.nextLine();
        // checks if account is valid/exists
        if (!isAccount(accountNumber, pin)) {
            System.out.println("Wrong card number or PIN!\n");
        } else {
            // load account with database values and return loaded account
            try (Connection con = dataSource.getConnection()) {
                try (Statement statement = con.createStatement()) {
                    try (ResultSet account = statement.executeQuery("SELECT number, " +
                            "pin, " +
                            "balance " +
                            "FROM card " +
                            "WHERE number='" + accountNumber + "'")) {
                        String number = null;
                        double balance = 0.0;

                            while (account.next()) {
                                number = account.getString("number");
                                balance = account.getDouble("balance");
                            }
                        System.out.println("You have successfully logged in!");
                                return new Account(this.IIN, this.companyName, number, balance);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        // if account doesn't exist null is returned
        return null;
    }

    private boolean isAccount(String accountNumber, String pin) {
        boolean isAccount = false;
        // checks if account exists and account number + pin is a valid combination
        try (Connection con = dataSource.getConnection(); Statement statement = con.createStatement()) {
                try (ResultSet account = statement.executeQuery("SELECT number, " +
                        "pin " +
                        "FROM card " +
                        "WHERE number='" + accountNumber + "'")) {

                    while (account.next()) {
                        String pinCode = account.getString("pin");
                        if (pinCode.equals(pin)) {
                            isAccount = true;
                        }
                    }
                }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return isAccount;
    }

    public String checkLuhn(String accountNumber) {
        // converting account number to int array
        char[] numbers = accountNumber.toCharArray();
        int [] numbersToInt = new int[numbers.length];
        for (int i = 0; i < numbers.length; i ++) {
            numbersToInt[i] = Character.getNumericValue(numbers[i]);
        }
        //create empty array for holding Luhn values
        int[] temp = new int[accountNumber.length()];
        // converting account number array to temp array with luhn values
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
        //Sum all values of luhn array and check what the final digit should be
        int sum = 0;
        for (int i : temp) {
            sum += i;
        } if (sum % 10 == 0) {
            return "0";
        }
        //return final number of credit card number
        return String.valueOf(10 - (sum % 10));
    }
}
