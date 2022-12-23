package com.company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;

public class Author {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;
    private int authorID;
    private boolean continueLoop = true;
    public Author(Connection c,Statement s, ResultSet r) throws SQLException {
        this.connection = c;
        this.statement = s;
        this.result = r;
        if (validateAuthor()) {
            options();
        }
    }

    public boolean validateAuthor() {
        try {
            System.out.println("Enter Author ID: ");
            Scanner sc = new Scanner(System.in);
            authorID = sc.nextInt();
            result = this.statement.executeQuery("SELECT PersonID FROM Person WHERE Type = 'author'");
            boolean personExists = false;
            while (result.next()) {
                if (result.getInt("PersonID") == authorID) {
                    personExists = true;
                    break;
                }
            }
            if (!personExists) {
                System.out.println("Incorrect Author ID");
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void options() throws SQLException {

        while (continueLoop) {
            System.out.println("Select one of the options to perform a task");
            System.out.println("1. View all articles assigned");
            System.out.println("2. View all chapters assigned");
            System.out.println("3. Enter a new article");
            System.out.println("4. Enter a new chapter");
            System.out.println("5. Update article's title");
            System.out.println("6. Update article's author");
            System.out.println("7. Update article's topic");
            System.out.println("8. Update article's date");
            System.out.println("9. Update chapter's title");
            System.out.println("10. Update chapter's author");
            System.out.println("11. Update chapter's topic");
            System.out.println("12. Update chapter's date");
            System.out.println("13. Update article's text");
            System.out.println("14. Delete article");
            System.out.println("15. Delete chapter");
            System.out.println("16. Exit");

            Scanner sc = new Scanner(System.in);
            int optionSelected = sc.nextInt();

            switch (optionSelected) {
                case 1:
                    showArticles();
                    break;
                case 2:
                    showChapters();
                    break;
                case 3:
                    addArticle();
                    break;
                case 4:
                    addChapter();
                    break;
                case 5:
                    updateArticleTitle();
                    break;
                case 6:
                    updateArticleAuthor();
                    break;
                case 7:
                    updateArticleTopic();
                    break;
                case 8:
                    updateArticleDate();
                    break;
                case 9:
                    updateChapterTitle();
                    break;
                case 10:
                    updateChapterAuthor();
                    break;
                case 11:
                    updateChapterTopic();
                    break;
                case 12:
                    updateChapterDate();
                    break;
                case 13:
                    updateArticleText();
                    break;
                case 14:
                    deleteArticle();
                    break;
                case 15:
                    deleteChapter();
                    break;
                case 16:
                    continueLoop = false;
                    break;
                default:
                    System.out.println("Please enter a valid input");
                    break;
            }
        }
    }

    public void showArticles() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesArticles AS WA NATURAL JOIN " +
                    "Articles AS A where WA.PersonID = " + authorID + " and WA.PubID = A.PubID and" +
                    " WA.ArticleNumber = A.ArticleNumber and WA.ISBN = A.ISBN");
            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Article Number: " + result.getInt("ArticleNumber"));
                System.out.println("Topic: " + result.getString("Topic"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Creation Date: " + result.getString("ArticleCreationDate"));
                System.out.println("Text: " + result.getString("Text"));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showChapters() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesChapters AS WC NATURAL JOIN Chapters AS C" +
                    " where WC.PersonID = " + authorID + " and WC.PubID = C.PubID and" +
                    " WC.ChapterNumber = C.ChapterNumber and WC.ISBN = C.ISBN");

            while (result.next()) {
                System.out.println("Publication ID: " + result.getInt("PubID"));
                System.out.println("ISBN: " + result.getInt("ISBN"));
                System.out.println("Chapter Number: " + result.getInt("ChapterNumber"));
                System.out.println("Chapter Topic: " + result.getString("ChapterTopic"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Creation Date: " + result.getString("DateOfCreation"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addArticle() throws SQLException {

        System.out.println("Enter Publication ID: ");
        Scanner sc = new Scanner(System.in);
        int pubID = sc.nextInt();
        System.out.println("Enter Article Number: ");
        int articleNumber = sc.nextInt();
        System.out.println("Enter ISBN: ");
        int isbn = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Topic: ");
        String topic = sc.nextLine();
        System.out.println("Enter Title: ");
        String title = sc.nextLine();
        System.out.println("Enter Article Creation Date: ");
        String date = sc.nextLine();
        System.out.println("Enter Article Text: ");
        String text = sc.nextLine();
//        String query = "INSERT INTO  Articles(PubID,ArticleNumber,ISBN,Topic,Title," +
//        " ArticleCreationDate,Text) VALUES ("+ pubID + " , " + articleNumber + " , " + isbn + " , '"
//                + topic + "' , '" + title + "' , '" + date + "' , '" + text + "')";
//        System.out.println(query);

        try {
            result = this.statement.executeQuery("INSERT INTO  Articles(PubID,ArticleNumber,ISBN,Topic,Title," +
                    " ArticleCreationDate,Text) VALUES ("+ pubID + " , " + articleNumber + " , " + isbn + " , '"
                    + topic + "' , '" + title + "' , '" + date + "' , '" + text + "')");

            result = this.statement.executeQuery("INSERT INTO WritesArticles" +
                    " VALUES(" + authorID + ", " + pubID + ", " + articleNumber + ", " + isbn + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void addChapter() throws SQLException {
        System.out.println("Enter Publication ID: ");
        Scanner sc = new Scanner(System.in);
        int pubID = sc.nextInt();
        System.out.println("Enter ISBN: ");
        int isbn = sc.nextInt();
        System.out.println("Enter Chapter Number: ");
        int chapterNumber = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Chapter Topic: ");
        String chapterTopic = sc.nextLine();
        System.out.println("Enter Chapter Title: ");
        String chapterTitle = sc.nextLine();
        System.out.println("Enter Date of Creation: ");
        String date = sc.nextLine();
        try {
            result = this.statement.executeQuery("INSERT INTO Chapters(PubID,ISBN,ChapterNumber,ChapterTopic,Title," +
                    " DateOfCreation) VALUES ("+ pubID + " , " + isbn + " , " + chapterNumber + " , '"
                    + chapterTopic + "' , '" + chapterTitle + "' , '" + date + "')");
            result = this.statement.executeQuery("INSERT INTO WritesChapters" +
                    " VALUES(" + authorID + ", " + pubID + ", " + chapterNumber + ", " + isbn + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void updateArticleTitle() throws SQLException {

        try {
            result = this.statement.executeQuery("SELECT * FROM WritesArticles AS WA NATURAL JOIN " +
                    "Articles AS A where WA.PersonID = " + authorID + " and WA.PubID = A.PubID and" +
                    " WA.ArticleNumber = A.ArticleNumber and WA.ISBN = A.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> articleNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                articleNumbers.add(result.getInt("ArticleNumber"));
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
            System.out.println("Enter Article Number");
            int articleNumber = sc.nextInt();
            if (!articleNumbers.contains(articleNumber)) {
                System.out.println("Article Number entered is invalid");
                return;
            }
            sc.nextLine();

            System.out.println("Enter new Article Title");
            String articleTitle = sc.nextLine();
            this.statement.executeQuery("UPDATE Articles SET Title = '"+
                    articleTitle +"' WHERE PubID = " + pubID + " and ISBN = " + isbn + " and ArticleNumber = " + articleNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateArticleAuthor() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesArticles AS WA NATURAL JOIN " +
                    "Articles AS A where WA.PersonID = " + authorID + " and WA.PubID = A.PubID and" +
                    " WA.ArticleNumber = A.ArticleNumber and WA.ISBN = A.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> articleNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                articleNumbers.add(result.getInt("ArticleNumber"));
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
            System.out.println("Enter Article Number");
            int articleNumber = sc.nextInt();
            if (!articleNumbers.contains(articleNumber)) {
                System.out.println("Article Number entered is invalid");
                return;
            }
            System.out.println("Enter Author ID");
            int authorID = sc.nextInt();
            result =  this.statement.executeQuery("UPDATE WritesArticle SET PersonID = " + authorID +
                    "WHERE PubID = " + pubID + " and ISBN = " + isbn + " and ArticleNumber = " + articleNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateArticleTopic() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesArticles AS WA NATURAL JOIN " +
                    "Articles AS A where WA.PersonID = " + authorID + " and WA.PubID = A.PubID and" +
                    " WA.ArticleNumber = A.ArticleNumber and WA.ISBN = A.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> articleNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                articleNumbers.add(result.getInt("ArticleNumber"));
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
            System.out.println("Enter Article Number");
            int articleNumber = sc.nextInt();
            if (!articleNumbers.contains(articleNumber)) {
                System.out.println("Article Number entered is invalid");
                return;
            }
            sc.nextLine();

            System.out.println("Enter Article Topic");
            String articleTopic = sc.nextLine();
            result = this.statement.executeQuery("UPDATE Articles SET Topic = '" + articleTopic + "' WHERE" +
                    " PubID = " + pubID + "and ISBN = " + isbn + " and ArticleNumber = " + articleNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateArticleDate() throws  SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesArticles AS WA NATURAL JOIN " +
                    "Articles AS A where WA.PersonID = " + authorID + " and WA.PubID = A.PubID and" +
                    " WA.ArticleNumber = A.ArticleNumber and WA.ISBN = A.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> articleNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                articleNumbers.add(result.getInt("ArticleNumber"));
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
            System.out.println("Enter Article Number");
            int articleNumber = sc.nextInt();
            if (!articleNumbers.contains(articleNumber)) {
                System.out.println("Article Number entered is invalid");
                return;
            }

            sc.nextLine();
            System.out.println("Enter Article Date");
            String articleDate = sc.nextLine();
            result = this.statement.executeQuery("UPDATE Articles SET ArticleCreationDate = '" + articleDate + "' WHERE" +
                    " PubID = " + pubID + "and ISBN = " + isbn + " and ArticleNumber = " + articleNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateChapterTitle() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesChapters AS WC NATURAL JOIN Chapters AS C" +
                    " where WC.PersonID = " + authorID + " and WC.PubID = C.PubID and" +
                    " WC.ChapterNumber = C.ChapterNumber and WC.ISBN = C.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> chapterNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                chapterNumbers.add(result.getInt("ChapterNumber"));
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
            System.out.println("Enter Chapter Number");
            int chapterNumber = sc.nextInt();
            if (!chapterNumbers.contains(chapterNumber)) {
                System.out.println("Chapter Number entered is invalid");
                return;
            }
            sc.nextLine();

            System.out.println("Enter Chapter Title");
            String chapterTitle = sc.nextLine();
            result = this.statement.executeQuery("UPDATE Chapters SET Title = '" + chapterTitle + "' WHERE" +
                    " PubID = " + pubID + "and ISBN = " + isbn + " and ChapterNumber = " + chapterNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateChapterAuthor() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesChapters AS WC NATURAL JOIN Chapters AS C" +
                    " where WC.PersonID = " + authorID + " and WC.PubID = C.PubID and" +
                    " WC.ChapterNumber = C.ChapterNumber and WC.ISBN = C.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> chapterNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                chapterNumbers.add(result.getInt("ChapterNumber"));
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
            System.out.println("Enter Chapter Number");
            int chapterNumber = sc.nextInt();
            if (!chapterNumbers.contains(chapterNumber)) {
                System.out.println("Chapter Number entered is invalid");
                return;
            }
            System.out.println("Enter Author ID");
            int authorID = sc.nextInt();
            result = this.statement.executeQuery("UPDATE WritesChapter SET PersonID = '" + authorID + "' WHERE" +
                    " PubID = " + pubID + "and ISBN = " + isbn + " and ChapterNumber = " + chapterNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateChapterTopic() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesChapters AS WC NATURAL JOIN Chapters AS C" +
                    " where WC.PersonID = " + authorID + " and WC.PubID = C.PubID and" +
                    " WC.ChapterNumber = C.ChapterNumber and WC.ISBN = C.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> chapterNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                chapterNumbers.add(result.getInt("ChapterNumber"));
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
            System.out.println("Enter Chapter Number");
            int chapterNumber = sc.nextInt();
            if (!chapterNumbers.contains(chapterNumber)) {
                System.out.println("Chapter Number entered is invalid");
                return;
            }
            sc.nextLine();
            System.out.println("Enter Chapter Topic");
            String chapterTopic = sc.nextLine();
            result = this.statement.executeQuery("UPDATE Chapters SET Topic = '" + chapterTopic + "' WHERE" +
                    " PubID = " + pubID + "and ISBN = " + isbn + " and ChapterNumber = " + chapterNumber);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateChapterDate() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesChapters AS WC NATURAL JOIN Chapters AS C" +
                    " where WC.PersonID = " + authorID + " and WC.PubID = C.PubID and" +
                    " WC.ChapterNumber = C.ChapterNumber and WC.ISBN = C.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> chapterNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                chapterNumbers.add(result.getInt("ChapterNumber"));
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
            System.out.println("Enter Chapter Number");
            int chapterNumber = sc.nextInt();
            if (!chapterNumbers.contains(chapterNumber)) {
                System.out.println("Chapter Number entered is invalid");
                return;
            }
            sc.nextLine();
            System.out.println("Enter Chapter Date");
            String chapterDate = sc.nextLine();
            result = this.statement.executeQuery("UPDATE Chapters SET DateOfCreation = '" + chapterDate + "' WHERE" +
                    " PubID = " + pubID + "and ISBN = " + isbn + " and ChapterNumber = " + chapterNumber);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateArticleText() throws  SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesArticles AS WA NATURAL JOIN " +
                    "Articles AS A where WA.PersonID = " + authorID + " and WA.PubID = A.PubID and" +
                    " WA.ArticleNumber = A.ArticleNumber and WA.ISBN = A.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> articleNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                articleNumbers.add(result.getInt("ArticleNumber"));
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
            System.out.println("Enter Article Number");
            int articleNumber = sc.nextInt();
            if (!articleNumbers.contains(articleNumber)) {
                System.out.println("Article Number entered is invalid");
                return;
            }
            sc.nextLine();
            System.out.println("Enter Article Text");
            String articleText = sc.nextLine();
            result = this.statement.executeQuery("UPDATE Articles SET Text = '" + articleText + "' WHERE" +
                    " PubID = " + pubID + "and ISBN = " + isbn + " and ArticleNumber = " + articleNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteArticle() throws  SQLException {

        try {
            result = this.statement.executeQuery("SELECT * FROM WritesArticles AS WA NATURAL JOIN " +
                    "Articles AS A where WA.PersonID = " + authorID + " and WA.PubID = A.PubID and" +
                    " WA.ArticleNumber = A.ArticleNumber and WA.ISBN = A.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> articleNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                articleNumbers.add(result.getInt("ArticleNumber"));
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
            System.out.println("Enter Article Number");
            int articleNumber = sc.nextInt();
            if (!articleNumbers.contains(articleNumber)) {
                System.out.println("Article Number entered is invalid");
                return;
            }
            result = this.statement.executeQuery("DELETE FROM Articles WHERE PubID = " + pubID + " and " +
                    "ISBN = " + isbn + " and ArticleNumber = " + articleNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteChapter() throws SQLException {
        try {
            result = this.statement.executeQuery("SELECT * FROM WritesChapters AS WC NATURAL JOIN Chapters AS C" +
                    " where WC.PersonID = " + authorID + " and WC.PubID = C.PubID and" +
                    " WC.ChapterNumber = C.ChapterNumber and WC.ISBN = C.ISBN");
            HashSet<Integer> pubIDs = new HashSet<>();
            HashSet<Integer> isbns = new HashSet<>();
            HashSet<Integer> chapterNumbers = new HashSet<>();
            while(result.next()) {
                pubIDs.add(result.getInt("PubID"));
                isbns.add(result.getInt("ISBN"));
                chapterNumbers.add(result.getInt("ChapterNumber"));
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
            System.out.println("Enter Chapter Number");
            int chapterNumber = sc.nextInt();
            if (!chapterNumbers.contains(chapterNumber)) {
                System.out.println("Chapter Number entered is invalid");
                return;
            }
            result = this.statement.executeQuery("DELETE FROM Chapters WHERE PubID = " + pubID + " and " +
                    "ISBN = " + isbn + " and ChapterNumber = " + chapterNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
