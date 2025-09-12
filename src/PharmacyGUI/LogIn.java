package PharmacyGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class LogIn extends JFrame{
    JLabel lblEnterD, lblEmail, lblPassword;
    JTextField txtEmail;
    JPasswordField txtPassword;
    JButton btnCancel, btnLogIn;
    public LogIn(){
        super("Log In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(350,300);

        lblEnterD = new JLabel("Enter Details");
        lblEmail = new JLabel("Email:");
        lblEmail.setPreferredSize(new Dimension(80,16));
        lblPassword = new JLabel("Password:");
        lblPassword.setPreferredSize(new Dimension(80,16));
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnCancel = new JButton("Cancel");
        btnLogIn = new JButton("Log In");

        JPanel allPanels = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel mid1 = new JPanel();
        JPanel mid2 = new JPanel();
        JPanel lowPanel = new JPanel();

        allPanels.setLayout(new BoxLayout(allPanels, BoxLayout.Y_AXIS));
        topPanel.setLayout(new FlowLayout());
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
        mid1.setLayout(new FlowLayout());
        mid2.setLayout(new FlowLayout());
        lowPanel.setLayout(new BoxLayout(lowPanel, BoxLayout.X_AXIS));


        topPanel.add(lblEnterD);
        mid1.add(lblEmail);
        mid1.add(txtEmail);
        mid2.add(lblPassword);
        mid2.add(txtPassword);
        midPanel.add(mid1);
        midPanel.add(mid2);
        lowPanel.add(btnCancel);
        lowPanel.add(Box.createHorizontalStrut(50));
        lowPanel.add(btnLogIn);

        allPanels.add(Box.createVerticalGlue());
        allPanels.add(topPanel);
        allPanels.add(midPanel);
        allPanels.add(lowPanel);
        allPanels.add(Box.createVerticalGlue());


        add(allPanels);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActualLogIn(txtEmail.getText(), txtPassword.getPassword());
            }
        });

        setVisible(true);
    }

    public void ActualLogIn(String email, char[] password){
        String query = "SELECT 1 FROM pharmacists WHERE email = ? and password = ?";
        //Here you can use the following queries to check:
        //"SELECT email, password FROM pharmacists WHERE email = ? and password = ?" This will select only email and password
        //"SELECT * FROM pharmacists WHERE email = ? and password = ?" This one is easy to type but I'm avoiding it cz it selects all columns which is a waste and you might expose sensitive data cz u're retrieving everything
        //"SELECT 1 FROM pharmacists WHERE email = ? and password = ?" This one is basically saying that if there is an email and password like the one specified, display 1 in a dummy column. The result.next() will still work. Check in notes that follow
        //It's like the technique we used in php where we were saying select everything WHERE 1=1. The query above is safe and u don't retrieve unncessary data so u don't waste resources

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1,email);
            String pass = new String(password);
            stmt.setString(2,pass);

            ResultSet result = stmt.executeQuery();//Notice that we are using executeQuery() here bcz its selection. Any other uses executeUpdate()
            //What result.next() means:
            //When you run a query in JDBC (like SELECT ...), you get back a ResultSet.
            //Think of it like a cursor over a table of rows returned by your query.
            //Initially, the cursor is before the first row.
            //result.next() moves the cursor forward one row, and returns:
            //true → if there was a row to move to.
            //false → if there are no more rows.
            //So in login:
            //If the query found a user → result.next() will be true.
            //If no user matches → result.next() will be false.
            //That’s how you know if login should succeed or fail.
            //The checking happens in the query cz if it returns nothing, that means the password or email is incorrect
            if(result.next()){
                System.out.println("Log In successful");
                JOptionPane.showMessageDialog(null,//This part is known as the parent component.See more info below
                        "Log In successful!",
                        "Success Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }//A parent component is what the dialog box belongs to. Since we are inside another method and can't make the respective panels parents, we make the parent "null"
            //If the option pane was outside this method and we would've made allPanels the parent class. The dialog box would've appeared
            //at the center but if it were lowPanel, it would've appeared towards the bottom since that's where lowPanel is located cz it's tied to it
            //If it had been directly inside the frame, I would've used the word "this" to mean JFrame
            //null means it belongs to no component but normally in coding it's advisable to give a component a parent component
            //If it is null, it will appear at the center of the screen since it is tied to nothing
            else{
                System.out.println("Unsuccessful log in!");
                JOptionPane.showMessageDialog(null,
                        "Email or password may be incorrect. Please try again",
                        "Unsuccessful Log In",
                        JOptionPane.ERROR_MESSAGE);
            }


        }catch(SQLException e){
            e.printStackTrace();
        }finally{//Cleaning up the password:
            Arrays.fill(password, ' ');
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LogIn();
        });
    }

}
