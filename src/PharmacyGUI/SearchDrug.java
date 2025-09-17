package PharmacyGUI;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SearchDrug extends JFrame {
    JLabel lblEnterD, lblSname,lblOr, lblSuse, lblResults, lblDname, lblDuse, lblamount, lblpurchase, lblPharmacist;
    JTextField txtSname, txtSuse, txtDname, txtDuse, txtamount, txtpurchase, txtPharmacist;
    JButton btnSearch, btnUpdate, btnClose;

    public SearchDrug(){
        super("Search Drugs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,550);

        lblEnterD = new JLabel("Enter Drug Name or Use");
        lblSname = new JLabel("Drug name:");
        lblSname.setPreferredSize(new Dimension(120,16));
        lblOr = new JLabel("OR");
        lblSuse = new JLabel("Drug use:");
        lblSuse.setPreferredSize(new Dimension(120,16));
        lblResults = new JLabel("RESULTS");
        lblDname = new JLabel("Drug name:");
        lblDname.setPreferredSize(new Dimension(120,16));
        lblDuse = new JLabel("Drug use:");
        lblDuse.setPreferredSize(new Dimension(120,16));
        lblamount = new JLabel("Drug amount:");
        lblamount.setPreferredSize(new Dimension(120,16));
        lblpurchase = new JLabel("Date of Purchase:");
        lblpurchase.setPreferredSize(new Dimension(120,16));
        lblPharmacist = new JLabel("Pharmacist Id:");
        lblPharmacist.setPreferredSize(new Dimension(120,16));

        txtSname = new JTextField(20);
        txtSuse = new JTextField(20);
        txtDname = new JTextField(20);
        txtDuse = new JTextField(20);
        txtamount = new JTextField(20);
        txtpurchase = new JTextField(20);
        txtPharmacist = new JTextField(20);

        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnClose = new JButton("Close");

        JPanel allPanels = new JPanel();
        allPanels.setLayout(new BoxLayout(allPanels, BoxLayout.Y_AXIS));
        JPanel NameSearch = new JPanel(new FlowLayout());
        JPanel UseSearch = new JPanel(new FlowLayout());
        JPanel Mid1 = new JPanel(new FlowLayout());
        JPanel Mid2 = new JPanel(new FlowLayout());
        JPanel Mid3 = new JPanel(new FlowLayout());
        JPanel Mid4 = new JPanel(new FlowLayout());
        JPanel Mid5 = new JPanel(new FlowLayout());

        allPanels.add(Box.createVerticalStrut(10));
        JPanel Title = new JPanel(new FlowLayout());
        Title.add(lblEnterD);
        allPanels.add(Title);

        NameSearch.add(lblSname);
        NameSearch.add(txtSname);
        allPanels.add(NameSearch);

        JPanel Or = new JPanel(new FlowLayout());
        Or.add(lblOr);
        allPanels.add(Or);

        UseSearch.add(lblSuse);
        UseSearch.add(txtSuse);
        allPanels.add(UseSearch);

        /*
        btnSearch.setAlignmentX(Component.LEFT_ALIGNMENT);
        allPanels.add(btnSearch);
        //I don't know why but the alignment doesn't work with the components directly.
        //You will only see the effect if u use a panel like I have below
        */
        JPanel Search = new JPanel(new FlowLayout());
        Search.add(btnSearch);
        Search.setAlignmentX(Component.LEFT_ALIGNMENT);//For some reason this makes it move to the right
        allPanels.add(Search);

        allPanels.add(Box.createVerticalStrut(20));
        JPanel Results = new JPanel(new FlowLayout());
        Results.add(lblResults);
        allPanels.add(Results);

        Mid1.add(lblDname);
        Mid1.add(txtDname);
        allPanels.add(Mid1);

        Mid2.add(lblDuse);
        Mid2.add(txtDuse);
        allPanels.add(Mid2);

        Mid3.add(lblamount);
        Mid3.add(txtamount);
        allPanels.add(Mid3);

        Mid4.add(lblpurchase);
        Mid4.add(txtpurchase);
        allPanels.add(Mid4);

        Mid5.add(lblPharmacist);
        Mid5.add(txtPharmacist);
        allPanels.add(Mid5);

        JPanel Close = new JPanel(new FlowLayout());
        Close.add(btnUpdate);
        Close.add(btnClose);
        Close.setAlignmentX(Component.LEFT_ALIGNMENT);
        allPanels.add(Close);
        allPanels.add(Box.createVerticalStrut(10));

        add(allPanels);

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Update();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActualSearch();
            }
        });

        setVisible(true);
    }

    //The Id I'll use in Update method
    int currentID = -1;
    //About me putting -1 as the default return and not the normally used 0, I've put it there to
    // demonstrate that just in case u have a table with an id zero, there won't be an issue.

    public void ActualSearch(){

        //Searching by drug use isn't completely reasonable cz there are so many drugs that have the
        //same use, but I wanted to see if I could come up with this method on my own and I did. Kudos to me!!

        String name = txtSname.getText();
        String use = txtSuse.getText();

        if(use.isBlank()){
            String query = "SELECT * FROM drugs WHERE dname = ?";

            try(Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)){

                stmt.setString(1,name);
                ResultSet result = stmt.executeQuery();
                if(result.next()){
                    System.out.println("Drug found");
                    //Getting the data from sql
                    String dname = result.getString("dname");
                    String duse = result.getString("duse");
                    String damount = result.getString("damount");
                    String ddate = String.valueOf(result.getDate("dateofpurchase"));
                    String pharmacist = String.valueOf(result.getInt("pharmacistID"));
                    //Putting the data in the respective textfields
                    txtDname.setText(dname);
                    txtDuse.setText(duse);
                    txtamount.setText(damount);
                    txtpurchase.setText(ddate);
                    txtPharmacist.setText(pharmacist);

                    //Getting the id for the Update method
                    currentID = result.getInt("id");

                }else{
                    System.out.println("Record not found");
                    JOptionPane.showMessageDialog(this,
                            "Record not found! Please enter existing name.",
                            "No such record",
                            JOptionPane.ERROR_MESSAGE);
                }

            }catch(SQLException e){
                System.out.println("SQL Error");
                e.printStackTrace();
            }

        }else{
            String query = "SELECT * FROM drugs WHERE duse = ?";

            try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

                stmt.setString(1,use);

                ResultSet result = stmt.executeQuery();
                if(result.next()){
                    System.out.println("Drug found");
                    String dname = result.getString("dname");
                    String duse = result.getString("duse");
                    String damount = result.getString("damount");
                    String ddate = String.valueOf(result.getDate("dateofpurchase"));
                    String pharmacist = String.valueOf(result.getInt("pharmacistID"));

                    txtDname.setText(dname);
                    txtDuse.setText(duse);
                    txtamount.setText(damount);
                    txtpurchase.setText(ddate);
                    txtPharmacist.setText(pharmacist);

                    //Getting the id for the Update method
                    currentID = result.getInt("id");

                }else{
                    System.out.println("Record not found");
                    JOptionPane.showMessageDialog(this,
                            "Record not found! Please enter existing name.",
                            "No such record",
                            JOptionPane.ERROR_MESSAGE);
                }
            }catch(SQLException e){
                System.out.println("SQL Error");
                e.printStackTrace();
            }
        }


    }

    public void Update(){
        //The reason I'm doing the update here is because I feel like it's reasonable for sb to have the option to
        // update sth when they search

        if(currentID == -1){
            System.out.println("Unsuccessful update");
            JOptionPane.showMessageDialog(null,
                    "Please update the field you want to change first",
                    "Unsuccessful Update",
                    JOptionPane.ERROR_MESSAGE);
        }

        String query2 = "UPDATE drugs SET dname = ?, duse = ?, damount = ?, dateofpurchase = ?, pharmacistID = ? WHERE id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query2) ){
            stmt.setString(1,txtDname.getText());
            stmt.setString(2,txtDuse.getText());
            stmt.setString(3,txtamount.getText());

            try{
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                //I know u're wondering why we need this when it can just convert them directly to date the way it is.
                //The problem with that is if the date is written in any other form aside from the one for year, month then day,
                // java won't accept it and it will throw an exception. If you don't specify the format of the date you want, Java usually
                //uses its programmed format which is only yyyy-MM-dd. If a user enters any other format eg dd-MM-yyyy, it will bring an error
                //cz Java doesn't recognize that format unless specified by a formatter
                LocalDate date = LocalDate.parse(txtpurchase.getText(),format);
                //So here(the code above), you can just pass a String or better, you can pass any format you want too
                java.sql.Date sqlDate = java.sql.Date.valueOf(date);
                //The reason I'm not using get time here is because the method LocalDate only contains the date not the time in milliseconds
                //that an action happened. Date method like the one I used in CreateRecord, contains the date plus the time
                //getTime converts the date of Date method to just milliseconds and the java.sql.Date picks just the date and displays
                //it to you. Computers normally calculate the date in milliseconds but then that's hard for us so SQL is set to display it
                //in a user-friendly format. Though u should know that u can print out gateTime() here in Java and u'll see how it looks

                stmt.setDate(4,sqlDate);
            }catch (DateTimeException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
                //Date formatter tells the compiler the format to expect and allows the format to be other forms other than the
                //default but if a format other than the one the date formatter has described is entered by the user, there will
                //still be an error so this ensures the user enters the date in the right format. However, date picker is the
                //recommended solution here but I'm too lazy to figure out how to include it here rn
            }


            int pID = Integer.parseInt(txtPharmacist.getText());
            stmt.setInt(5,pID);

            //Using the ID we got from search here
            stmt.setInt(6,currentID);

            int rows_affected = stmt.executeUpdate();//If you don't do statement execute, the query won't be executed
            //It's still rows affected cz even if an update happens in just one column, executeUpdate() marks the whole row as affected

            if(rows_affected > 0){
                System.out.println("Successful update");
                JOptionPane.showMessageDialog(null,
                        "Record Updated Successfully",
                        "Successful Update",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                System.out.println("Unsuccessful update");
                JOptionPane.showMessageDialog(null,
                        "No change made",
                        "Unsuccessful Update",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           new SearchDrug();
        });
    }
}
