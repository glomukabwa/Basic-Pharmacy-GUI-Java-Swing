package PharmacyGUI;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {

    //I'm declaring these components here so that I can access them wherever in this class
    //If I declare them inside a method, they won't be accessible outside the method
    //If I declare them inside the panel like this: panel.add(new JButton("Log In")), I won't be able to
    //manipulate them(like changing the text they display or adding an action listener or making a button unclickable)
    //Yes you can change the name of a button or the text of a label
    private JLabel lblWelcome;
    private JButton btnLogIn, btnSignUp;

    //The WelcomePage is a method not a function cz it is created inside a class
    //Java doesn't allow creation of methods outside a class so there are no functions in Java
    //The method being public doesn't mean that it is a function. It just means that it is accessible outside this class
    public WelcomePage(){
        super("Pharma");//Or: setTitle("Pharma")
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        setLocationRelativeTo(null);//This is centering the Frame on the screen

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));//If we wanted them to be from left to right, we would have used X_AXIS

        lblWelcome = new JLabel("Welcome!");
        btnLogIn = new JButton("Log In");
        btnSignUp = new JButton("Sign Up");

        //Centering the components inside the panel:
        //Note the X because we're centering them horizontally
        //However, in vertical box layout you can't center them vertically using this. It won't work. You have to use glue. See below.
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);


        panel.add(Box.createVerticalGlue());//See what glue means in the lines below
        panel.add(lblWelcome);
        panel.add(Box.createVerticalStrut(30));//This is for creating a fixed space.30 is the number of pixels
        //If I wanted Horizontal space if it was X_AXIS, I would've written reateHorizontalStrut
        //If you want the space to be flexible, use createVerticalGlue()
        //For glue, you don't specify the number of pixels. The space will grow if there is available space and shrink if there is no space
        panel.add(btnLogIn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnSignUp);
        panel.add(Box.createVerticalGlue());//This glue and the glue on top make the components to be centered vertically cz they push up and down if there is space.

        add(panel);

        setVisible(true);
    }
}

