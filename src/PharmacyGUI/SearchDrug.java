package PharmacyGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchDrug extends JFrame {
    JLabel lblEnterD, lblSname,lblOr, lblSuse, lblResults, lblDname, lblDuse, lblamount, lblpurchase, lblPharmacist;
    JTextField txtSname, txtSuse, txtDname, txtDuse, txtamount, txtpurchase, txtPharmacist;
    JButton btnSearch, btnClose;

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

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActualSearch();
            }
        });

        setVisible(true);
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           new SearchDrug();
        });
    }
}
