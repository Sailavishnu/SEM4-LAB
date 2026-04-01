import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentOracleApp extends JFrame {

    JTextField idField, avgField;
    JButton avgBtn, menuBtn;
    JTable table;
    DefaultTableModel model;

    Connection con;

    void printStudentDetails(int id, String name, int m1, int m2, int m3, String actionLine) {
        System.out.println("id : " + id);
        System.out.println("name : " + name);
        System.out.println("mark1 : " + m1);
        System.out.println("mark2 : " + m2);
        System.out.println("mark3 : " + m3);
        if (actionLine != null && !actionLine.isEmpty()) {
            System.out.println(actionLine);
        }
    }

    void printStudentById(int studentId, String actionLineIfFound) {
        if (con == null) {
            return;
        }
        String sql = "SELECT stdid, name, mark1, mark2, mark3 FROM student WHERE stdid=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, studentId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    printStudentDetails(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getInt(4),
                            rs.getInt(5),
                            actionLineIfFound);
                } else {
                    System.out.println("Student with id " + studentId + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printAllStudents() {
        if (con == null) {
            return;
        }
        String sql = "SELECT stdid, name, mark1, mark2, mark3 FROM student ORDER BY stdid";
        try (Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- ALL STUDENTS ---");
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int m1 = rs.getInt(3);
                int m2 = rs.getInt(4);
                int m3 = rs.getInt(5);
                System.out.println(
                        "id : " + id + ", name : " + name + ", mark1 : " + m1 + ", mark2 : " + m2 + ", mark3 : " + m3);
            }
            System.out.println("--------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    StudentOracleApp() {
        setTitle("Student Management - Oracle");
        setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Row 1 - Enter ID
        JLabel idLabel = new JLabel("Enter Student ID:");
        idLabel.setBounds(50, 30, 200, 30);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(220, 30, 200, 30);
        add(idField);

        // Row 2 - Avg
        avgBtn = new JButton("Calculate Avg");
        avgBtn.setBounds(50, 80, 180, 35);
        add(avgBtn);

        avgField = new JTextField();
        avgField.setBounds(250, 80, 200, 35);
        avgField.setEditable(false);
        add(avgField);

        // Row 3 - Menu
        menuBtn = new JButton("Menu");
        menuBtn.setBounds(50, 130, 120, 35);
        add(menuBtn);

        // Row 4 - Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] { "ID", "Name", "M1", "M2", "M3" });

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 200, 1200, 500);
        add(sp);

        connectDB();
        if (con != null) {
            loadTable();
        } else {
            // Avoid NPEs later; user must fix DB config/classpath first.
            avgBtn.setEnabled(false);
            menuBtn.setEnabled(false);
        }

        avgBtn.addActionListener(e -> calculateAvg());
        menuBtn.addActionListener(e -> new MenuWindow());

        setVisible(true);
    }

    // DB CONNECTION
    void connectDB() {
        try {
            // Works with modern ojdbc drivers; older ones also accept
            // oracle.jdbc.driver.OracleDriver
            Class.forName("oracle.jdbc.OracleDriver");

            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "system";
            String pass = "1229";

            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to Oracle DB");

        } catch (Exception e) {
            con = null;
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "DB Connection Failed:\n" + e,
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // LOAD TABLE
    void loadTable() {
        if (con == null) {
            return;
        }
        try {
            model.setRowCount(0);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM student");

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // AVG
    void calculateAvg() {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "Not connected to DB.");
            return;
        }
        try {
            int id = Integer.parseInt(idField.getText());

            PreparedStatement pst = con.prepareStatement(
                    "SELECT mark1, mark2, mark3 FROM student WHERE stdid=?");
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int avg = (rs.getInt(1) + rs.getInt(2) + rs.getInt(3)) / 3;
                avgField.setText(String.valueOf(avg));
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!");
                avgField.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
            avgField.setText("");
        }
    }

    // MENU (SMALL)
    class MenuWindow extends JFrame {
        MenuWindow() {
            setTitle("Menu");
            setSize(250, 200);
            setLayout(new GridLayout(3, 1, 10, 10));
            setLocationRelativeTo(null);

            JButton insert = new JButton("Insert");
            JButton delete = new JButton("Delete");
            JButton update = new JButton("Update");

            add(insert);
            add(delete);
            add(update);

            insert.addActionListener(e -> new InsertForm());
            delete.addActionListener(e -> deleteStudent());
            update.addActionListener(e -> new UpdateForm());

            setVisible(true);
        }
    }

    // INSERT (SMALL)
    class InsertForm extends JFrame {
        JTextField id, name, m1, m2, m3;

        InsertForm() {
            setTitle("Insert Student");
            setSize(350, 300);
            setLayout(new GridLayout(6, 2, 10, 10));
            setLocationRelativeTo(null);

            id = new JTextField();
            name = new JTextField();
            m1 = new JTextField();
            m2 = new JTextField();
            m3 = new JTextField();

            add(new JLabel("ID"));
            add(id);
            add(new JLabel("Name"));
            add(name);
            add(new JLabel("Mark1"));
            add(m1);
            add(new JLabel("Mark2"));
            add(m2);
            add(new JLabel("Mark3"));
            add(m3);

            JButton save = new JButton("Save");
            add(save);

            save.addActionListener(e -> {
                try {
                    if (con == null) {
                        JOptionPane.showMessageDialog(this, "Not connected to DB.");
                        return;
                    }

                    int studentId = Integer.parseInt(id.getText());
                    String studentName = name.getText();
                    int mark1 = Integer.parseInt(m1.getText());
                    int mark2 = Integer.parseInt(m2.getText());
                    int mark3 = Integer.parseInt(m3.getText());

                    PreparedStatement pst = con.prepareStatement(
                            "INSERT INTO student VALUES(?,?,?,?,?)");

                    pst.setInt(1, studentId);
                    pst.setString(2, studentName);
                    pst.setInt(3, mark1);
                    pst.setInt(4, mark2);
                    pst.setInt(5, mark3);

                    pst.executeUpdate();

                    printStudentDetails(studentId, studentName, mark1, mark2, mark3, "inserted into DB.");

                    loadTable();
                    printAllStudents();
                    dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Insert Error!");
                }
            });

            setVisible(true);
        }
    }

    // DELETE
    void deleteStudent() {
        if (con == null) {
            JOptionPane.showMessageDialog(this, "Not connected to DB.");
            return;
        }
        String id = JOptionPane.showInputDialog("Enter ID to delete");
        if (id == null || id.trim().isEmpty()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int studentId = Integer.parseInt(id.trim());

                // Print the row that will be deleted (before deleting)
                printStudentById(studentId, "will be deleted...");

                PreparedStatement pst = con.prepareStatement(
                        "DELETE FROM student WHERE stdid=?");

                pst.setInt(1, studentId);
                int rows = pst.executeUpdate();

                if (rows > 0) {
                    System.out.println("deleted from DB.");
                    printAllStudents();
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!");
                }

                loadTable();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Delete Error!");
            }
        }
    }

    // UPDATE (SMALL)
    class UpdateForm extends JFrame {
        JTextField id, name, m1, m2, m3;

        UpdateForm() {
            setTitle("Update Student");
            setSize(350, 300);
            setLayout(new GridLayout(6, 2, 10, 10));
            setLocationRelativeTo(null);

            id = new JTextField();

            add(new JLabel("Enter ID"));
            add(id);

            name = new JTextField();
            m1 = new JTextField();
            m2 = new JTextField();
            m3 = new JTextField();

            add(new JLabel("New Name"));
            add(name);
            add(new JLabel("Mark1"));
            add(m1);
            add(new JLabel("Mark2"));
            add(m2);
            add(new JLabel("Mark3"));
            add(m3);

            JButton update = new JButton("Update");
            add(update);

            update.addActionListener(e -> {
                try {
                    if (con == null) {
                        JOptionPane.showMessageDialog(this, "Not connected to DB.");
                        return;
                    }

                    int studentId = Integer.parseInt(id.getText());

                    PreparedStatement pst = con.prepareStatement(
                            "UPDATE student SET name=?, mark1=?, mark2=?, mark3=? WHERE stdid=?");

                    pst.setString(1, name.getText());
                    pst.setInt(2, Integer.parseInt(m1.getText()));
                    pst.setInt(3, Integer.parseInt(m2.getText()));
                    pst.setInt(4, Integer.parseInt(m3.getText()));
                    pst.setInt(5, studentId);

                    int rows = pst.executeUpdate();

                    if (rows > 0) {
                        printStudentById(studentId, "updated in DB.");
                        printAllStudents();
                    } else {
                        JOptionPane.showMessageDialog(this, "Student not found!");
                    }

                    loadTable();
                    dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Update Error!");
                }
            });

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        new StudentOracleApp();
    }
}