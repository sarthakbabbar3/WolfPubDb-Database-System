package com.company;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;


public class Main {
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet result = null;

    public static void main(String[] args) throws SQLException {
	// write your code here
        System.out.println("Welcome to Wolfpub");
        System.out.println(" 1. Admin");
        System.out.println(" 2. Distributor");
        System.out.println(" 3. Author");
        System.out.println(" 4. Editor");
        System.out.println(" 5. Reports");
        System.out.println("Enter an option:");

        Scanner sc = new Scanner(System.in);
        int roleId = sc.nextInt();

        DBConnection dbConn = new DBConnection();
        connection = dbConn.getConnection();
        statement = dbConn.getStatement();
        result = dbConn.getResult();

        switch (roleId){
            case 1:
                Admin admin = new Admin(connection,statement,result);
                break;
            case 2:
                Distributor distributor = new Distributor(connection, statement, result);
                break;
            case 3:
                Author author = new Author(connection, statement, result);
                break;
            case 4:
                Editor editor = new Editor(connection, statement, result);
                break;
            case 5:
                Reports reports = new Reports(connection,statement,result);
                break;
        }

        dbConn.close();

    }
}
