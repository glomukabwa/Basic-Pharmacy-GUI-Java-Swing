package PharmacyGUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String url = "jdbc:mysql://localhost:3306/pharmacydb";
    //final → makes the variable a constant. Once it’s assigned, it cannot be changed. This ensures your connection URL won’t be accidentally modified elsewhere in your code.
    //static → means the variable belongs to the class instead of an instance. You don’t need to create an object of the class to use it.[Connection conn = DatabaseConnection.getConnection();] It’s shared by all instances.
    //In the above example, conn is a reference not an object. It is a reference to the Connection object created inside the driver manager
    //If you don't write static, you'd need to create an object everytime you want to access the url like this:
    //DatabaseConnection db = new DatabaseConnection();
    //Connection conn = db.getConnection();
    //Think of static as a TV everyone in the house can watch and non-static as a phone that can only be used by a person at a time to watch and only after requesting
    //private → ensures that no outside class can directly access or modify it. Only methods inside this class can use it.
    //You're probably wondering why we'd make them private considering that other classes will use it. That's the thing. The method below is public
    //The classes can still use the method but they do not know the data inside it. This is the use of encapsulation. We don't want other classes to have the right to tamper with the url
    private static final String user = "root";
    private static final String password = "L@ur3nceangelina";

    public static Connection getConnection(){//This is also static so that the methods don't belong to objects created through this method but to DBConnection class
        //So instead of
        //DBConnection db = new DBConnection();
        //Connection conn = db.getConnection();
        //You'll do:
        //Connection conn = DatabaseConnection.getConnection();
        //The reason we specify Connection before getConnection() is to tell the compiler the return type of this method is a connection object that can be used for the rest of the code(to make PrepareStatement etc)
        //Remember the way eg for a method that is returning String, u'd do public String getString(String name = "Gloria"...return name; ) That is the same concept here. It returns a connection object and then I'll use it in the other pages to access methods that come with Connection like PrepareStatement
        Connection conn = null;//Apparently this helps with safety.So that if the connection has an issue, we can always fall on this. This also allows us to access it later if we want to use a finally block
        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println("Database Connected Successfully");
        }catch(SQLException e){
            System.out.println("Database connection failed!");
            e.printStackTrace();//This will print out the error message and the chain of methods that led to the exception
        }
        return conn;

    }

    /* //Testing it in main
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
    }*/

}
