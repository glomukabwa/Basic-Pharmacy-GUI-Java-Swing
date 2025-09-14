package PharmacyGUI;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

public class CreateRecord extends JFrame {
    JLabel lblEnterD,lblDname, lblDuse, lblDamount, lblDate, lblPharcmacist;
    JTextField txtDname, txtDuse, txtDamount, txtPharmacist;
    JButton btnClear,btnCancel,btnAdd;

    public CreateRecord(){
        super("Create Drug Records");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400,400);

        lblEnterD = new JLabel("Enter Drug Details");
        lblDname = new JLabel("Drug Name:");
        lblDname.setPreferredSize(new Dimension(120,16));
        lblDuse = new JLabel("Drug Use:");
        lblDuse.setPreferredSize(new Dimension(120,16));
        lblDamount = new JLabel("Drug Amount:");
        lblDamount.setPreferredSize(new Dimension(120,16));
        lblDate = new JLabel("Date of Purchase:");
        lblDate.setPreferredSize(new Dimension(120,16));
        lblPharcmacist = new JLabel("Pharmacist Id:");
        lblPharcmacist.setPreferredSize(new Dimension(120,16));
        txtDname = new JTextField(20);
        txtDuse = new JTextField(20);
        txtDamount = new JTextField(20);
        //txtDate = new JTextField(20);
        txtPharmacist = new JTextField(20);
        btnClear = new JButton("Clear");
        btnCancel = new JButton("Cancel");
        btnAdd = new JButton("Add");

        /*
        //Using JFormattedTextField to specify date input
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);//This creates the format accepted which if the short date
        DateFormatter dateformatter = new DateFormatter(format);//JFormattedTextField doesn't know how to use DateFormat directly. It knows how to use DateFormatter which converts it to a form JFormattedField can use
        JFormattedTextField txtDate2 = new JFormattedTextField(dateformatter);*/

        //I'm donna use date picker cz its more user-friendly plus I'm having a hard time adding the formatted textfield
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setDate(new Date());//This will set today as the default
        datePicker.setFormats("dd/MM/yyyy");//The format of the date
        //Other formats of the date:
        //datePicker.setFormats("dd/MM/yyyy");      // 13/09/2025
        //datePicker.setFormats("MM-dd-yyyy");      // 09-13-2025
        //datePicker.setFormats("yyyy-MM-dd");      // 2025-09-13
        //datePicker.setFormats("EEEE, MMM dd yyyy"); // Saturday, Sep 13 2025
        datePicker.setPreferredSize(new Dimension(185,20));


        JPanel allPanels = new JPanel();
        allPanels.setLayout(new BoxLayout(allPanels, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));

        JPanel InM1 = new JPanel(new FlowLayout());
        JPanel InM2 = new JPanel(new FlowLayout());
        JPanel InM3 = new JPanel(new FlowLayout());
        JPanel InM4 = new JPanel(new FlowLayout());
        JPanel InM5 = new JPanel(new FlowLayout());

        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new BoxLayout(lowPanel, BoxLayout.X_AXIS));

        topPanel.add(lblEnterD);

        InM1.add(lblDname);
        InM1.add(txtDname);
        InM2.add(lblDuse);
        InM2.add(txtDuse);
        InM3.add(lblDamount);
        InM3.add(txtDamount);
        InM4.add(lblDate);
        InM4.add(datePicker);
        InM5.add(lblPharcmacist);
        InM5.add(txtPharmacist);
        midPanel.add(InM1);
        midPanel.add(InM2);
        midPanel.add(InM3);
        midPanel.add(InM4);
        midPanel.add(InM5);

        lowPanel.add(Box.createHorizontalGlue());
        lowPanel.add(btnClear);
        lowPanel.add(Box.createHorizontalStrut(20));
        lowPanel.add(btnCancel);
        lowPanel.add(Box.createHorizontalStrut(100));
        lowPanel.add(btnAdd);
        lowPanel.add(Box.createHorizontalGlue());

        allPanels.add(Box.createVerticalGlue());
        allPanels.add(topPanel);
        allPanels.add(midPanel);
        allPanels.add(lowPanel);
        allPanels.add(Box.createVerticalGlue());

        add(allPanels);

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtDname.setText("");
                txtDuse.setText("");
                txtDamount.setText("");
                txtPharmacist.setText("");
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Retrieving the date you get from date picker:
                    Date utilDate = datePicker.getDate();//You use this method to retrieve it. getDate() returns a util.Date
                    //You have to convert a util.Date to an sql.Date which is now accepteptable by sql
                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());//getTime() is the right method to use. It returns the date and exact time in milliseconds that the record was inserted and then sql.Date takes the date from all the info brought by getTime()

                    //Converting the Pharmacist Id to an Int cz it's an int in the Database:
                    int pharmId = Integer.parseInt(txtPharmacist.getText());

                    SendRecord(txtDname.getText(), txtDuse.getText(), txtDamount.getText(), sqlDate, pharmId);
                }catch (NumberFormatException ex){//This exception ensures tha input entered is a valid number cz in the above, we are supposed to convert the input of the pharmacist id to a number. If the system converts the string and the output is still not a valid number, an exception will be thrown and this will catch it
                    JOptionPane.showMessageDialog(null,
                            "Please enter valid numeric values for Pharmacist ID",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public void SendRecord(String dname, String duse, String damount, java.sql.Date ddate, int pharmd ){
        String query = "INSERT INTO drugs (dname, duse, damount, dateofpurchase, pharmacistID) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1,dname);
            stmt.setString(2,duse);
            stmt.setString(3,damount);
            stmt.setDate(4,ddate);//You use setDate cz the data is of type date in the database
            stmt.setInt(5,pharmd);//You use setInt instead cz The id is an Integer in the database

            int rows_affected = stmt.executeUpdate();
            if(rows_affected > 0){
                System.out.println("Successful Insertion");
                JOptionPane.showMessageDialog(null,
                        "Drug added successfully",
                        "Successful Insertion",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                System.out.println("Unsuccessful Insertion");
                JOptionPane.showMessageDialog(null,
                        "Drug has not been added. Please try again",
                        "Unsuccessful Insertion",
                        JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e){
            System.out.println("SQL Error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setDate(new Date());//This will set today as the default
        datePicker.setFormats("dd/MM/yyyy");
        System.out.println(datePicker.getPreferredSize());*/
        /*JTextField text = new JTextField(20);
        System.out.println(text.getPreferredSize());*/
        SwingUtilities.invokeLater(() -> {
            new CreateRecord();
        });
    }
}
