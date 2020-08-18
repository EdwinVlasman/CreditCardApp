package CreditCardCompany;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter company name:");
        String companyName = scanner.nextLine().toLowerCase();
        System.out.println("Please enter company IIN:");
        int IIN = scanner.nextInt();
        scanner.nextLine();
        Company company = new Company(IIN, companyName);
        //creating or opening database
        String url = "jdbc:sqlite:P:\\" + company.getCompanyName() + ".s3db";
        company.setDatabaseURL(url);
        Account account;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        // create database to store credit card data or open database if it already exists
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF not EXISTS card(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance DECIMAL(8,2) DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // while loop for company options
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    company.createAccount();
                    break;
                case 2:
                    account = company.getAccount(scanner);
                    if (account != null) {
                        choice = account.accountMenu(scanner, dataSource);
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
