package com.company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

public class Editor {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;
    private int editorID;
    private boolean continueLoop = true;
    public Editor(Connection c,Statement s, ResultSet r) throws SQLException {
        this.connection = c;
        this.statement = s;
        this.result = r;
        if (validateEditor()) {
            options();
        }
    }

    public boolean validateEditor() throws SQLException {
        System.out.println("Enter Editor ID: ");
        Scanner sc = new Scanner(System.in);
        editorID = sc.nextInt();
        result = this.statement.executeQuery("SELECT PersonID FROM Person WHERE Type = 'editor'");
        boolean personExists = false;
        while (result.next()) {
            if (result.getInt("PersonID") == editorID) {
                personExists = true;
                break;
            }
        }
        if (!personExists) {
            System.out.println("Incorrect Editor ID");
            return false;
        }
        return true;
    }

    public void options() throws SQLException {

        while (continueLoop) {
            System.out.println("Select one of the options to perform a task");
            System.out.println("1. View all books assigned");
            System.out.println("2. View all periodic publications assigned");
            System.out.println("3. Update publication title");
            System.out.println("4. Update publication topic");
            System.out.println("5. Exit");

            Scanner sc = new Scanner(System.in);
            int optionSelected = sc.nextInt();

            switch (optionSelected) {
                case 1:
                    showBooks();
                    break;
                case 2:
                    showPeriodicPublications();
                    break;
                case 3:
                    updatePublicationTitle();
                    break;
                case 4:
                    updatePublicationTopic();
                    break;
                case 5:
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Please enter a valid input");
                    break;
            }
        }
    }

    public void showBooks() {
        try {
            result = this.statement.executeQuery("SELECT * FROM EditedBy AS EB NATURAL JOIN Publication AS P" +
                    " NATURAL JOIN Book AS B where EB.PersonID = " + editorID + " and P.PubID = EB.PubID and P.ISBN = EB.ISBN" +
                    " and B.PubID = P.PubID and B.ISBN = P.ISBN");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Creation Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getString("Cost"));
                System.out.println("Edition: " + result.getString("Edition"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showPeriodicPublications() {
        try {
            result = this.statement.executeQuery("SELECT * FROM EditedBy AS EB NATURAL JOIN Publication AS P" +
                    " NATURAL JOIN PeriodicPublication AS PP where EB.PersonID = " + editorID +
                    " and P.PubID = EB.PubID and P.ISBN = EB.ISBN and PP.PubID = P.PubID and PP.ISBN = P.ISBN");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Creation Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getString("Cost"));
                System.out.println("Periodicity: " + result.getString("Periodicity"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePublicationTitle() {
        try{
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            result = this.statement.executeQuery("SELECT * FROM EditedBy AS EB NATURAL JOIN Publication AS P" +
                    " NATURAL JOIN Book AS B where EB.PersonID = " + editorID + " and P.PubID = EB.PubID and P.ISBN = EB.ISBN" +
                    " and B.PubID = P.PubID and B.ISBN = P.ISBN");
            while (result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
            }

            result = this.statement.executeQuery("SELECT * FROM EditedBy AS EB NATURAL JOIN Publication AS P" +
                    " NATURAL JOIN PeriodicPublication AS PP where EB.PersonID = " + editorID +
                    " and P.PubID = EB.PubID and P.ISBN = EB.ISBN and PP.PubID = P.PubID and PP.ISBN = P.ISBN");
            while (result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
            }

            System.out.println("Enter Publication ID");
            Scanner sc = new Scanner(System.in);
            int pubID = sc.nextInt();
            if (!pubIDs.contains(pubID)) {
                System.out.println("Publication ID entered is invalid");
                return;
            }
            System.out.println("Enter ISBN");
            int isbn = sc.nextInt();
            if (!isbns.contains(isbn)) {
                System.out.println("ISBN entered is invalid");
                return;
            }
            sc.nextLine();
            System.out.println("Enter new title");
            String title = sc.nextLine();
            this.statement.executeQuery("UPDATE Publication SET Title = '" + title + "' WHERE PubID = " + pubID +
                    " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePublicationTopic() {
        try {
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            result = this.statement.executeQuery("SELECT * FROM EditedBy AS EB NATURAL JOIN Publication AS P" +
                    " NATURAL JOIN Book AS B where EB.PersonID = " + editorID + " and P.PubID = EB.PubID and P.ISBN = EB.ISBN" +
                    " and B.PubID = P.PubID and B.ISBN = P.ISBN");
            while (result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
            }

            result = this.statement.executeQuery("SELECT * FROM EditedBy AS EB NATURAL JOIN Publication AS P" +
                    " NATURAL JOIN PeriodicPublication AS PP where EB.PersonID = " + editorID +
                    " and P.PubID = EB.PubID and P.ISBN = EB.ISBN and PP.PubID = P.PubID and PP.ISBN = P.ISBN");
            while (result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
            }

            System.out.println("Enter Publication ID");
            Scanner sc = new Scanner(System.in);
            int pubID = sc.nextInt();
            if (!pubIDs.contains(pubID)) {
                System.out.println("Publication ID entered is invalid");
                return;
            }
            System.out.println("Enter ISBN");
            int isbn = sc.nextInt();
            if (!isbns.contains(isbn)) {
                System.out.println("ISBN entered is invalid");
                return;
            }
            sc.nextLine();
            System.out.println("Enter new topic");
            String topic = sc.nextLine();
            this.statement.executeQuery("UPDATE Publication SET Topic = '" + topic + "' WHERE PubID = " + pubID +
                    " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
