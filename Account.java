package CreditCardCompany;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Account extends Company {

    private final String creditCardNumber;
    private double balance;

    public Account(int IIN, String companyName, String accountNumber, double balance) {
        super(IIN, companyName);
        this.creditCardNumber = accountNumber;
        this.balance = balance;
    }

    private void getBalance() {
        System.out.println("\nBalance: " + this.balance);
    }

    private void addIncome(java.util.Scanner scanner) {
        System.out.println("\nEnter income:");
        double income = scanner.nextDouble();
        this.balance += income;
        System.out.println("Income was added!\n");
    }

    private void saveAccount(SQLiteDataSource dataSource) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("UPDATE  card " +
                        "SET balance = " + this.balance +
                        " WHERE number='" + this.creditCardNumber + "'"

                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int accountMenu(java.util.Scanner scanner, SQLiteDataSource dataSource) {
        // Menu holding all options within account and controlling flow of program menu
        int choice;
        do {
            System.out.println("\n1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close Account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    getBalance();
                    break;
                case 2:
                    addIncome(scanner);
                    break;
                case 3:
                    transferMenu(scanner, dataSource);
                    break;
                case 4:
                    deleteAccount(dataSource);
                    break;
                case 5:
                    saveAccount(dataSource);
                    System.out.println("\nYou have successfully logged out!\n");
                    return 2;
                case 0:
                    saveAccount(dataSource);
                    return 0;
            }
        } while (true);
    }

    private void transferMenu(java.util.Scanner scanner, SQLiteDataSource dataSource) {
        boolean isAccount = false;
        System.out.println("\nTransfer\n" +
                "Enter card number:");
        String cardNr = scanner.nextLine();
        //checks if cardNr exists in database
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet account = statement.executeQuery("SELECT  number " +
                        "FROM card " +
                        "WHERE number='" + cardNr + "'"
                )) {
                    while (account.next()) {
                        String accountNumber = account.getString("number");
                        if (accountNumber != null) {
                            isAccount = true;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // checks if you didn't enter your own number, luhn is correct, account exists
        // else money is added to the account and subtracted from current account balance
        if (this.creditCardNumber.equals(cardNr)) {
            System.out.println("You can't transfer money to the same account!\n");
        } else if (!checkLuhn(cardNr.substring(0, cardNr.length() -1)).equals(cardNr.substring(cardNr.length() -1))) {
            System.out.println("Probably you made mistake in the card number. Please try again!\n");
        } else if (!isAccount) {
            System.out.println("Such a card does not exist.\n");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            double transferAmount = scanner.nextDouble();
            scanner.nextLine();
            if (transferAmount > this.balance) {
                System.out.println("Not enough money!\n");
            } else {
                this.balance -= transferAmount;
                System.out.println("Success!\n");
                // add transfer amount to account
                try (Connection con = dataSource.getConnection()) {
                    try (Statement statement = con.createStatement()) {
                        statement.executeUpdate("UPDATE  card " +
                                "SET balance = balance + " + transferAmount +
                                "WHERE number='" + cardNr + "'"

                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteAccount (SQLiteDataSource dataSource) {
    // deleting account from database
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("DELETE FROM  card " +
                        "WHERE number='" + this.creditCardNumber + "'"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
