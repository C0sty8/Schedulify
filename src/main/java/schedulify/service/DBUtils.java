package schedulify.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Clasa utilitara pentru gestionarea conexiunii la baza de date
 */
public class DBUtils {
    private Connection dbLink;
    private static String password = "123qaz###";
    private static String dbUser = "root";
    private static String dbName = "SYS";
    private static String url = "jdbc:mysql://localhost/" + dbName;

    /**
     * Metoda pentru stabilirea si obtinerea unei conexiuni intre aplicatia si baza de date
     * @return O conexiune intre aplicatia si baza de date
     */
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(url, dbUser, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbLink;
    }

    /**
     * Metoda pentru crearea bazei de date daca nu exista
     */
    public static void ensureDBExists() {
        try {
            Connection conn = DriverManager.getConnection(url, dbUser, password);
            Statement s = conn.createStatement();

            s.executeUpdate("CREATE DATABASE IF NOT EXISTS SCHEDULIFY");

            dbName = "SCHEDULIFY";
            url = "jdbc:mysql://localhost/" + dbName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru crearea tabelelor daca nu exista
     */
    public static void ensureTablesExists() {
        try {
            FileReader reader = new FileReader("tables.sql");
            BufferedReader bufferedReader = new BufferedReader(reader);
            Connection connection = DriverManager.getConnection(url, dbUser, password);
            Statement statement = connection.createStatement();
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null ) {
                builder.append(line);
                if (line.endsWith(";")) {
                    statement.executeUpdate(builder.toString());
                    builder.setLength(0);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
