package PharmacyGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteRecord extends JFrame {
    DefaultTableModel model;
    JTable table;
    JButton btnDelete;
    JLabel SelectR;
    public DeleteRecord(){
        super("Delete Record");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //Model is the columns and rows. In this case I'll only specify the columns cz we need a loop to specify the rows
        model = new DefaultTableModel(new String[]{"ID","Name","Use","Amount","Date of purchase","Pharmacist ID"}, 0);
        table = new JTable(model);
        btnDelete = new JButton("Delete Selected Drug");

        loadDrugs();

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedDrug();
            }
        });

        SelectR = new JLabel("Select The row you want to delete");
        JPanel top = new JPanel(new FlowLayout());
        top.setMaximumSize(new Dimension(300,20));
        top.setAlignmentX(CENTER_ALIGNMENT);
        top.add(SelectR);


        panel.add(Box.createVerticalStrut(10));
        panel.add(top);
        panel.add(Box.createVerticalStrut(20));
        panel.add(table);
        panel.add(Box.createVerticalStrut(20));
        JPanel button = new JPanel();
        button.add(btnDelete);
        panel.add(button);


        add(panel);

        setVisible(true);
    }

    private void loadDrugs(){
        String query = "SELECT * FROM drugs";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet result = stmt.executeQuery();
            while(result.next()){//Next stands before the table then when it is called, it moves to the next row which is number one and then the specified instructions are performed then te continues looping through the rows in the DB till they are over
                model.addRow(new Object[]{result.getInt("id"), result.getString("dname"), result.getString("duse"), result.getString("damount"), result.getDate("dateofpurchase"), result.getInt("pharmacistID")});
                //When adding a row we use new Object unlike new String when creating the columns of the table above. Column names are usually in form of Strings that's why
                //We use new Object because each cell has a different dtat type eg Int, String, Date etc and Object can hold all kinds of data types

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void deleteSelectedDrug(){
        int row = table.getSelectedRow(); //This is an inbuilt method that detects the row selected. It counts from 0 so if the 1st row has been selected, its index is 0, the second is 1 etc. If no row has been selected, the index is -1
        if(row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            //Syntax for getValue is getValueAt(row,column)
            //We have assigned row with a value so we don't need to specify(cz the pharmacist can pick any row). 0 in the position of column means column 1 and id is stored in the first column in the database
            //The (int) before model is the illustrattionof casting
            //Casting is the conversion of a data type to another data type
            //We need to perform casting cz remember that the rows are stored as objects, they can be of any data type so we're telling the compiler that it is actually as integer so that we can use it in the SQL query that follows

            String query2 = "DELETE FROM drugs WHERE id = ?"; //We don't need to specify what we're deleting cz in SQL when you delete the Primary Key, it deletes the whole row

            try(Connection conn2 = DBConnection.getConnection();
            PreparedStatement stmt2 = conn2.prepareStatement(query2)){

                stmt2.setInt(1, id);
                stmt2.executeUpdate();

                model.removeRow(row);
                JOptionPane.showMessageDialog(null,
                        "Drug has been deleted successfully",
                        "Success deletion",
                        JOptionPane.INFORMATION_MESSAGE);
            }catch(SQLException e2){
                e2.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null,
                    "Please select the drug you want to delete",
                    "Unsuccessful deletion",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DeleteRecord();
        });
    }
}

//Constructors vs Methods
//A constructor is a special method that runs automatically when you create an object with the "new" keyword.
//It must have the same name as the class (PharmacyGUI in your case).
//It does not need (and cannot have) a return type (void, int, etc.).
//Calling a constructor:
//PharmacyGUI app = new PharmacyGUI();
//egular methods must specify a return type (void, int, String, etc.).
//They are called explicitly when you need them. You can call them inside constructors
//Example of calling them: app.loadDrugsFromDB(); or the way I am calling them directly in the above constructor
//Simple terms:
//Constructor = special setup code that runs when the object is born.
//Methods = actions you can tell the object to do later.
