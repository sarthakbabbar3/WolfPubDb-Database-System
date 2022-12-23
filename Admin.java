package com.company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

public class Admin {
    private  Connection connection = null;
    private  Statement statement = null;
    private  ResultSet result = null;
    private boolean continueLoop = true;
    public Admin(Connection c,Statement s, ResultSet r) throws SQLException {
        this.connection = c;
        this.statement = s;
        this.result = r;
        options();
    }

    public void options() throws SQLException {

        while (continueLoop) {
            System.out.println("Select one of the options to perform a task");
            System.out.println("1. Show all publications");
            System.out.println("2. Add publication");
            System.out.println("3. Update publication title");
            System.out.println("4. Update publication topic");
            System.out.println("5. Update publication date");
            System.out.println("6. Update publication cost");
            System.out.println("6. Update article's author");
            System.out.println("7. Add book edition");
            System.out.println("8. Add publication issue");
            System.out.println("9. Assign editor to publication");
            System.out.println("10. Update Book's edition");
            System.out.println("11. Update Periodic Publication Periodicity");
            System.out.println("12. Delete Book or Periodic Publication");
            System.out.println("13. Find Book by Topic");
            System.out.println("14. Find Book by Date");
            System.out.println("15. Find Book by Title");
            System.out.println("16. Exit");

            Scanner sc = new Scanner(System.in);
            int optionSelected = sc.nextInt();

            switch (optionSelected) {
                case 1:
                    showPublications();
                    break;
                case 2:
                    addPublication();
                    break;
                case 3:
                    updatePublicationTitle();
                    break;
                case 4:
                    updatePublicationTopic();
                    break;
                case 5:
                    updatePublicationDate();
                    break;
                case 6:
                    updatePublicationCost();
                    break;
                case 7:
                    addBookEdition();
                    break;
                case 8:
                    addPublicationIssue();
                    break;
                case 9:
                    assignEditorToPublication();
                    break;
                case 10:
                    updateBookEdition();
                    break;
                case 11:
                    updatePeriodicPublicationPublicity();
                    break;
                case 12:
                    deleteABookOrPeriodicPublication();
                    break;
                case 13:
                    findBookByTopic();
                    break;
                case 14:
                    findBookByDate();
                    break;
                case 15:
                    findBookByTitle();
                    break;
//                case 15:
//                    deleteChapter();
//                    break;
                case 16:
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Please enter a valid input");
                    break;
            }
        }
    }

    public void showPublications() {
        try {
            result = this.statement.executeQuery("SELECT * FROM Publication");
            while(result.next()) {
                System.out.println();
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
            }
        } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void addPublication() {

        try {
            result = this.statement.executeQuery("SELECT * FROM Publication");
            HashSet<String> pubIDAndISBN = new HashSet<>();
            while (result.next()) {
                String pubid = Integer.toString(result.getInt("PubID"));
                String isbn = Integer.toString(result.getInt("ISBN"));
                String s = pubid + isbn;
                pubIDAndISBN.add(s);
            }
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();

            String s = Integer.toString(pubID) + Integer.toString(isbn);
            if (pubIDAndISBN.contains(s)) {
                System.out.println("Publication ID and ISBN combination is already present");
                return;
            }
            System.out.println("Enter Title: ");
            sc.nextLine();
            String title = sc.nextLine();
            System.out.println("Enter Type: ");
            String type = sc.nextLine();
            System.out.println("Enter Topic: ");
            String topic = sc.nextLine();
            System.out.println("Enter Publication Date: ");
            String publicationDate = sc.nextLine();
            System.out.println("Enter cost: ");
            int cost = sc.nextInt();

            this.statement.executeQuery("INSERT INTO Publication(PubID,ISBN,Title,Type,Topic, PublicationDate,Cost) " +
                    "VALUES (" + pubID + "," +  isbn + ",'" + title + "','" + type + "','"+ topic + "','" + publicationDate + "'," + cost+ ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updatePublicationTitle() {

        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();
            System.out.println("Enter Title: ");
            sc.nextLine();
            String title = sc.nextLine();

            this.statement.executeQuery("UPDATE Publication SET Title = '"+ title +"' WHERE" +
                    " PubID = "+ pubID + " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePublicationTopic() {

        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();
            System.out.println("Enter Date: ");
            sc.nextLine();
            String topic = sc.nextLine();

            this.statement.executeQuery("UPDATE Publication SET Topic = '"+ topic +"' WHERE" +
                    " PubID = "+ pubID + " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updatePublicationDate() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();
            System.out.println("Enter Title: ");
            sc.nextLine();
            String date = sc.nextLine();

            this.statement.executeQuery("UPDATE Publication SET PublicationDate = '"+ date +"' WHERE" +
                    " PubID = "+ pubID + " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePublicationCost() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();
            System.out.println("Enter Cost: ");
            int cost = sc.nextInt();

            this.statement.executeQuery("UPDATE Publication SET Cost = '"+ cost +"' WHERE" +
                    " PubID = "+ pubID + " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBookEdition() {
        try {
            result = this.statement.executeQuery("SELECT * FROM Publication");
            HashSet<String> pubIDAndISBN = new HashSet<>();
            while (result.next()) {
                String pubid = Integer.toString(result.getInt("PubID"));
                String isbn = Integer.toString(result.getInt("ISBN"));
                String s = pubid + isbn;
                pubIDAndISBN.add(s);
            }
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();

            String s = Integer.toString(pubID) + Integer.toString(isbn);
            if (!pubIDAndISBN.contains(s)) {
                System.out.println("Publication ID and ISBN combination is not present");
                return;
            }
            System.out.println("Enter book edition");
            int edition = sc.nextInt();
            this.statement.executeQuery("INSERT INTO BOOK VALUES(" + pubID + "," + isbn + "," + edition + ")" );
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addPublicationIssue() {
        try {
            result = this.statement.executeQuery("SELECT * FROM Publication");
            HashSet<String> pubIDAndISBN = new HashSet<>();
            while (result.next()) {
                String pubid = Integer.toString(result.getInt("PubID"));
                String isbn = Integer.toString(result.getInt("ISBN"));
                String s = pubid + isbn;
                pubIDAndISBN.add(s);
            }
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();

            String s = Integer.toString(pubID) + Integer.toString(isbn);
            if (!pubIDAndISBN.contains(s)) {
                System.out.println("Publication ID and ISBN combination is not present");
                return;
            }
            System.out.println("Enter publication issue");
            int issue = sc.nextInt();
            this.statement.executeQuery("INSERT INTO PeriodicPublication VALUES(" + pubID + "," + isbn + "," + issue + ")" );
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignEditorToPublication() {
       try {
           Scanner sc = new Scanner(System.in);
           System.out.println("Enter Publication ID: ");
           int pubID = sc.nextInt();
           System.out.println("Enter ISBN: ");
           int isbn = sc.nextInt();
           System.out.println("Enter editor id");
           int personId = sc.nextInt();
           this.statement.executeQuery("INSERT INTO  EditedBy(PubID,PersonID,ISBN)" +
                   " VALUES (" + pubID + "," + personId + "," + isbn + ")");
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }

    public void updateBookEdition() {

        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();
            System.out.println("Enter Edition: ");
            int edition = sc.nextInt();
            this.statement.executeQuery("UPDATE Book SET Edition = "+ edition + " WHERE" +
                    " PubID = " + pubID + " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePeriodicPublicationPublicity() {

        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();
            System.out.println("Enter Periodicity: ");
            int periodicity = sc.nextInt();
            this.statement.executeQuery("UPDATE PeriodicPublication SET Periodicity = "+ periodicity + " WHERE" +
                    " PubID = " + pubID + " and ISBN = " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteABookOrPeriodicPublication() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Publication ID: ");
            int pubID = sc.nextInt();
            System.out.println("Enter ISBN: ");
            int isbn = sc.nextInt();
            this.statement.executeQuery("DELETE from Publication where PubID = " + pubID + " and ISBN = " +isbn);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void findBookByTopic() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter topic");
            String topic = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN Book WHERE" +
                    " Topic = '"+ topic + "'");
            while (result.next()) {
                System.out.println();
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Edition: " + result.getInt("Edition"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void findBookByDate() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Date");
            String date = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN Book WHERE" +
                    " PublicationDate = '"+ date + "'");
            while (result.next()) {
                System.out.println();
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Edition: " + result.getInt("Edition"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void findBookByTitle() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Title");
            String title = sc.nextLine();
            result = this.statement.executeQuery("SELECT * FROM Publication NATURAL JOIN Book WHERE" +
                    " Title = '"+ title + "'");
            while (result.next()) {
                System.out.println();
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Type: " + result.getString("Type"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Publication Date: " + result.getString("PublicationDate"));
                System.out.println("Cost: " + result.getInt("Cost"));
                System.out.println("Edition: " + result.getInt("Edition"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
