import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmpassword = String.valueOf(pfConfirmPassword.getPassword());

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please Enter all Field",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!password.equals(confirmpassword)) {
            JOptionPane.showMessageDialog(this,
                    "Password is incorrect",
                    "Please try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name, email, address, phone, password);
        if(user != null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again", JOptionPane.ERROR_MESSAGE);
        }
    }
    public User user;
    private User addUserToDatabase(String name, String email, String address, String phone, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://127.0.0.1:3306/users_details";
        final String USERNAME = "root";
        final String PASSWORD = "Careen254.";

        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            String sql = "INSERT INTO users (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            //insert rows into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }

            statement.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm( null);
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of: " + user.name);

        }
        else {
            System.out.println("Registration cancelled");
        }
    }
}