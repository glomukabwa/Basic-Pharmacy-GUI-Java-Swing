package PharmacyGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                JOptionPane.showMessageDialog(allPanels,//This part is known as the parent component.See more info below
                        "Log In successful!",
                        "Success Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }//A parent component is what the dialog box belongs to
            //This button is inside allPanels so that's why it's the parent component
            //lowPanel can also be the parent class if you wanna be more direct
            //Just know that if it is inside a panel, you have to specify the panel not the Frame
            //You'll notice that when you make allPanels the parent class, the dialog box appears
            //at the center but if its lowPanel, it appears towards the bottom since that's where lowPanel is located cz it's tied to it
            //If it had been directly inside the frame, I would've used the word "this" to mean JFrame
            //You can also indicate null there to mean it belongs to no component but normally in coding it's advisable to give a component a parent component
            //If it is null, it will appear at the center of the screen since it is tied to nothing
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LogIn();
        });
    }
}
