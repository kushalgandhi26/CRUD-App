import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DataForm {
    private JPanel Main;
    private JTextField txtName;
    private JButton saveButton;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtId;
    private JScrollPane table_1;


    public static void main(String[] args) {
        JFrame frame = new JFrame("DataForm");
        frame.setContentPane(new DataForm().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;
    public void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crudapp","root",Personal.password);
            System.out.println("Connected");
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private void table_load() {
        try {
            pst = con.prepareStatement("select * from users");
            ResultSet res = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(res));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public DataForm() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username,email,phono;
                username = txtName.getText();
                email = txtEmail.getText();
                phono = txtPhone.getText();

                try{
                    pst = con.prepareStatement("insert into users(username,phono,email)values(?,?,?)");
                    pst.setString(1,username);
                    pst.setString(2,phono);
                    pst.setString(3,email);
                    if(username.isEmpty() || email.isEmpty() || phono.isEmpty()){
                        JOptionPane.showMessageDialog(null,"Enter All Details.");
                        return;
                    }
                    if(phono.length() != 10){
                        JOptionPane.showMessageDialog(null,"Enter Valid Number");
                        return;
                    }
                    if(!email.contains("@") || !email.contains(".com")){
                        JOptionPane.showMessageDialog(null,"Enter Valid Email");
                        return;
                    }
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Added!");
                    table_load();
                    txtName.setText("");
                    txtEmail.setText("");
                    txtPhone.setText("");
                    txtName.requestFocus();
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String id = txtId.getText();
                    pst = con.prepareStatement("select username,phono,email from users where idusers = ?");
                    pst.setString(1,id);
                    if(id.isEmpty()){
                        JOptionPane.showMessageDialog(null,"Enter Search ID");
                        table_load();
                        return;
                    }
                    ResultSet res = pst.executeQuery();
                    if(res.next()){
                        String username = res.getString(1);
                        String phono = res.getString(2);
                        String email = res.getString(3);

                        txtName.setText(username);
                        txtPhone.setText(phono);
                        txtEmail.setText(email);
                    }else {
                        txtName.setText("");
                        txtPhone.setText("");
                        txtEmail.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid ID");
                    }

                }catch (SQLException err){
                    err.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username,email,phono,id;
                username = txtName.getText();
                email = txtEmail.getText();
                phono = txtPhone.getText();
                id = txtId.getText();

                try{
                    pst = con.prepareStatement("update users set username = ?,phono = ?,email = ? where idusers = ?");
                    pst.setString(1,username);
                    pst.setString(2,phono);
                    pst.setString(3,email);
                    pst.setString(4,id);

                    if(id.isEmpty()){
                        JOptionPane.showMessageDialog(null,"Enter Search ID");
                        table_load();
                        return;
                    }
                    if(phono.length() != 10){
                        JOptionPane.showMessageDialog(null,"Enter Valid Number");
                        return;
                    }
                    if(!email.contains("@") || !email.contains(".com")){
                        JOptionPane.showMessageDialog(null,"Enter Valid Email");
                        return;
                    }
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Updated!");
                    table_load();
                    txtName.setText("");
                    txtPhone.setText("");
                    txtEmail.setText("");
                    txtId.setText("");
                }catch(SQLException err){
                    err.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id;
                id = txtId.getText();
                try {
                    pst = con.prepareStatement("delete from users where idusers = ?");
                    pst.setString(1,id);
                    if(id.isEmpty()){
                        JOptionPane.showMessageDialog(null,"Enter Search ID");
                        table_load();
                        return;
                    }
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Deleted!");
                    table_load();
                    txtName.setText("");
                    txtPhone.setText("");
                    txtEmail.setText("");
                    txtId.setText("");
                }catch(SQLException err){
                    err.printStackTrace();
                }
            }
        });
    }
}
