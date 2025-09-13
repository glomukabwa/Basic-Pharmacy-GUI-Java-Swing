package PharmacyGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class SignUp extends JFrame {
    //I want the below components to be public. I'm not assigning anything here though cz the default is public so I don't need to.
    JLabel lblEnterD, lblFname, lblLname, lblEmail, lblPassword;
    JTextField txtFname, txtLname, txtEmail;
    JPasswordField txtPassword;
    JButton btnCancel,btnSignUp;
    public SignUp(){
        super("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);

        lblEnterD = new JLabel("Enter Details");
        lblFname = new JLabel("First name:");
        lblLname = new JLabel("Last name:");
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Password:");

        txtFname = new JTextField(20);
        txtLname = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);

        btnCancel = new JButton("Cancel");
        btnSignUp = new JButton("Sign Up");


        JPanel allPanels = new JPanel();//If you don't create a panel with all panels, it will only show one panel eg just topPanel. So in the frame, u'll just add this one cz it has all of them
        allPanels.setLayout(new BoxLayout(allPanels, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));

        JPanel insideM1 = new JPanel();
        insideM1.setLayout(new FlowLayout());

        JPanel insideM2 = new JPanel();
        insideM2.setLayout(new FlowLayout());

        JPanel insideM3 = new JPanel();
        insideM3.setLayout(new FlowLayout());

        JPanel insideM4 = new JPanel();
        insideM4.setLayout(new FlowLayout());

        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new BoxLayout(lowPanel, BoxLayout.X_AXIS));


        topPanel.add(lblEnterD);

        midPanel.add(insideM1);
        midPanel.add(insideM2);
        midPanel.add(insideM3);
        midPanel.add(insideM4);


        lblFname.setPreferredSize(new Dimension(80,16));
        insideM1.add(lblFname);
        insideM1.add(txtFname);

        lblLname.setPreferredSize(new Dimension(80,16));
        insideM2.add(lblLname);
        insideM2.add(txtLname);

        lblEmail.setPreferredSize(new Dimension(80,16));
        insideM3.add(lblEmail);
        insideM3.add(txtEmail);

        lblPassword.setPreferredSize(new Dimension(80,16));
        insideM4.add(lblPassword);
        insideM4.add(txtPassword);

        //midPanel.setMaximumSize(new Dimension(300,100));//I don't know why but with setting preferred size, it reaches a point and just starts ignoring me so like below around 100 were not working. I think this one is more strict.

        lowPanel.add(Box.createHorizontalGlue());
        lowPanel.add(btnCancel);
        lowPanel.add(Box.createHorizontalStrut(50));
        lowPanel.add(btnSignUp);
        lowPanel.add(Box.createHorizontalGlue());

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

        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActualSignUp(txtFname.getText(), txtLname.getText(), txtEmail.getText(), txtPassword.getPassword());
            }
        });

        setVisible(true);
    }

    public void ActualSignUp(String fname, String lname, String email, char[] password){
        //I'm passing a char for password as an argument cz it's safer to use getPassword() than getText() in the Sign Up button above.
        //getPassword() returns characters so a password like ['1','2','3']
        //Chat says its cz if u pass arguments in the button above, they stay in memory until Garbage Collection.
        //Strings are immutable meaning once they have been created, they don't change
        //If you use getText() to get the password, you make the password immutable meaning it exists in memory for a while and is susceptible to exposure
        //However with chars, you can clean them up using Arrays.fill(name_of_array, ' '); Here, you are making the array empty so it's not exposed. I'm guessing this method isn't available for strings

        String query = "INSERT INTO pharmacists (fname, lname, email, password) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1,fname);
            stmt.setString(2,lname);
            stmt.setString(3,email);
            String pass = new String(password);//You do this cz PreparedStatement only accepts Strings
            stmt.setString(4,pass);

            //Ensuring data has been entered by user:
            int rows_inserted = stmt.executeUpdate();
            //For insertion, deletion and update (anything other than selection), we use executeUpdate(). For selection we use executeQuery()
            //executeUpdate() returns an int which is the number of rows affected by the query
            //So here, we want to check to ensure that the insertion has taken place that is why we are checking if the rows are more than zero
            //For anything other than selection, we don't use the ResultSet which is used with executeQuery(). See in Log In
            if(rows_inserted > 0){
                System.out.println("Success Sign Up");
                JOptionPane.showMessageDialog(null,
                        "You have signed up successfully!",
                        "Successful Sign Up", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null,
                        "Sign up is unsuccessful. Please try again later",
                        "Unsuccessful Sign Up",
                        JOptionPane.ERROR_MESSAGE);
            }
        }catch (SQLException e){
            System.out.println("SQL Error");
            e.printStackTrace();
        }finally{
            Arrays.fill(password,' ');
            //Here's how try-catch-finally works:
            //try might throw an exception
            //catch handles any exception thrown
            //finally runs whatsoever so this is the perfect place to put the clean up of the password
        }

    }

    public static void main(String[] args) {

        //JLabel label = new JLabel("Password");
        //System.out.println(label.getPreferredSize());
        SwingUtilities.invokeLater(() -> {
            new SignUp();
        });
    }
}