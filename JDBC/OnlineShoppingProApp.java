import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OnlineShoppingProApp extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private int loggedInCustomerId = -1;
    Connection con;

    public OnlineShoppingProApp() {
        setTitle("Online Shopping Pro Application");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connectDB();
        setupDatabase(); // Create tables/sequences if they don't exist

        mainPanel.add(new OspLoginForm(this), "Login");
        mainPanel.add(new OspRegistrationForm(this), "Register");
        mainPanel.add(new OspCustomerDashboard(this), "Customer");
        mainPanel.add(new OspAdminDashboard(this), "Admin");

        add(mainPanel);
        showPanel("Login");
        setVisible(true);
    }

    void connectDB() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "oracle");
            System.out.println("Connected to Oracle DB");
        } catch (Exception e) {
            // fallback password "1229" based on earlier samples (OnlineShoppingApp2.java
            // used 1229)
            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "1229");
                System.out.println("Connected to Oracle DB (Alt password)");
            } catch (Exception ex) {
                con = null;
                JOptionPane.showMessageDialog(this,
                        "DB Connection Failed:\n" + ex.getMessage(), "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void setupDatabase() {
        if (con == null)
            return;
        String[] tables = {
                "CREATE TABLE Customerss (customer_id NUMBER PRIMARY KEY, name VARCHAR2(100), email VARCHAR2(100) UNIQUE, phone VARCHAR2(20), password VARCHAR2(100), address VARCHAR2(255), created_date DATE)",
                "CREATE TABLE Productss (product_id NUMBER PRIMARY KEY, product_name VARCHAR2(100), category VARCHAR2(50), price NUMBER, stock_quantity NUMBER, description VARCHAR2(255))",
                "CREATE TABLE Cartss (cart_id NUMBER PRIMARY KEY, customer_id NUMBER, created_date DATE)",
                "CREATE TABLE CartItemss (cart_item_id NUMBER PRIMARY KEY, cart_id NUMBER, product_id NUMBER, quantity NUMBER)",
                "CREATE TABLE Orderss (order_id NUMBER PRIMARY KEY, customer_id NUMBER, order_date DATE, total_amount NUMBER, order_status VARCHAR2(50))",
                "CREATE TABLE OrderItemss (order_item_id NUMBER PRIMARY KEY, order_id NUMBER, product_id NUMBER, quantity NUMBER, price NUMBER)"
        };

        String[] sequences = {
                "CREATE SEQUENCE customerss_seq START WITH 1 INCREMENT BY 1",
                "CREATE SEQUENCE productss_seq START WITH 1 INCREMENT BY 1",
                "CREATE SEQUENCE cartss_seq START WITH 1 INCREMENT BY 1",
                "CREATE SEQUENCE cartitemss_seq START WITH 1 INCREMENT BY 1",
                "CREATE SEQUENCE orderss_seq START WITH 1 INCREMENT BY 1",
                "CREATE SEQUENCE orderitemss_seq START WITH 1 INCREMENT BY 1"
        };

        try (Statement st = con.createStatement()) {
            for (String sql : tables) {
                try {
                    st.execute(sql);
                } catch (SQLException ignore) {
                } // ignore existing table
            }
            for (String sql : sequences) {
                try {
                    st.execute(sql);
                } catch (SQLException ignore) {
                } // ignore existing seq
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    void setCustomerId(int id) {
        this.loggedInCustomerId = id;
    }

    int getCustomerId() {
        return loggedInCustomerId;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineShoppingProApp());
    }
}

// ==============================================================================
// LOGIN FORM
// ==============================================================================
class OspLoginForm extends JPanel {
    public OspLoginForm(OnlineShoppingProApp app) {
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Online Shopping System");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        add(title, g);

        JComboBox<String> userTypeCombo = new JComboBox<>(new String[] { "Customer", "Admin" });
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Create Customer Account");

        g.gridy = 1;
        g.gridwidth = 1;
        add(new JLabel("User Type:"), g);
        g.gridx = 1;
        add(userTypeCombo, g);
        g.gridx = 0;
        g.gridy = 2;
        add(new JLabel("Email / Username:"), g);
        g.gridx = 1;
        add(usernameField, g);
        g.gridx = 0;
        g.gridy = 3;
        add(new JLabel("Password:"), g);
        g.gridx = 1;
        add(passwordField, g);

        JPanel btnPanel = new JPanel();
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        add(btnPanel, g);

        loginBtn.addActionListener(e -> {
            String type = (String) userTypeCombo.getSelectedItem();
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if ("Admin".equals(type)) {
                if ("deku".equals(user) && "allmight".equals(pass)) {
                    app.showPanel("Admin");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Admin Credentials");
                }
                return;
            }

            if (app.con == null) {
                JOptionPane.showMessageDialog(this, "DB not connected");
                return;
            }

            try {
                PreparedStatement st = app.con.prepareStatement(
                        "SELECT customer_id FROM Customerss WHERE (LOWER(email)=LOWER(?) OR LOWER(name)=LOWER(?)) AND password=?");
                st.setString(1, user);
                st.setString(2, user);
                st.setString(3, pass);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    app.setCustomerId(rs.getInt("customer_id"));
                    // Force refresh of customer dashboard tabs when logging in
                    app.showPanel("Customer");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Customer Email or Password");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        registerBtn.addActionListener(e -> app.showPanel("Register"));
    }
}

// ==============================================================================
// REGISTRATION FORM
// ==============================================================================
class OspRegistrationForm extends JPanel {
    public OspRegistrationForm(OnlineShoppingProApp app) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;

        JLabel titleLbl = new JLabel("Customer Registration");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 20));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        add(titleLbl, g);

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JTextArea addressArea = new JTextArea(3, 20);

        phoneField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || phoneField.getText().length() >= 10)
                    e.consume();
            }
        });

        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = 1;
        add(new JLabel("Full Name:"), g);
        g.gridx = 1;
        add(nameField, g);
        g.gridx = 0;
        g.gridy = 2;
        add(new JLabel("Email Address:"), g);
        g.gridx = 1;
        add(emailField, g);
        g.gridx = 0;
        g.gridy = 3;
        add(new JLabel("Phone Number:"), g);
        g.gridx = 1;
        add(phoneField, g);
        g.gridx = 0;
        g.gridy = 4;
        add(new JLabel("Password:"), g);
        g.gridx = 1;
        add(passField, g);
        g.gridx = 0;
        g.gridy = 5;
        add(new JLabel("Delivery Address:"), g);
        g.gridx = 1;
        add(new JScrollPane(addressArea), g);

        JButton registerBtn = new JButton("Register Account");
        JButton backBtn = new JButton("Back to Login");
        JPanel bp = new JPanel();
        bp.add(registerBtn);
        bp.add(backBtn);
        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 2;
        add(bp, g);

        backBtn.addActionListener(e -> app.showPanel("Login"));

        registerBtn.addActionListener(e -> {
            if (app.con == null) {
                JOptionPane.showMessageDialog(this, "DB not connected");
                return;
            }
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String password = new String(passField.getPassword()).trim();
                String address = addressArea.getText().trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                    return;
                }

                if (!name.matches("^[a-zA-Z\\s]+$")) {
                    JOptionPane.showMessageDialog(this, "Name must not contain any special characters or numbers.");
                    return;
                }

                int atCount = email.length() - email.replace("@", "").length();
                int dotCount = email.length() - email.replace(".", "").length();
                int atIndex = email.indexOf('@');
                int dotIndex = email.lastIndexOf('.');
                if (atCount != 1 || dotCount != 1 || atIndex <= 0 || dotIndex <= atIndex + 1
                        || dotIndex == email.length() - 1) {
                    JOptionPane.showMessageDialog(this, "Email must contain exactly one '@' and one '.'.");
                    return;
                }

                if (!phone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(this, "Phone must be exactly 10 digits.");
                    return;
                }

                int commaCount = address.length() - address.replace(",", "").length();
                if (commaCount < 2) {
                    JOptionPane.showMessageDialog(this,
                            "Please provide the address properly formatted with at least two commas (e.g., door number, street name, city).");
                    return;
                }

                // DATABASE VALIDATION RULE: Prevent duplicate Customer accounts by email
                PreparedStatement emailCheckSt = app.con
                        .prepareStatement("SELECT COUNT(*) FROM Customerss WHERE LOWER(email)=LOWER(?)");
                emailCheckSt.setString(1, email);
                ResultSet emailRs = emailCheckSt.executeQuery();
                if (emailRs.next() && emailRs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this,
                            "A customer with this email address already exists. Please use a different one or login.");
                    return;
                }

                PreparedStatement st = app.con.prepareStatement(
                        "INSERT INTO Customerss (customer_id,name,email,phone,password,address,created_date) " +
                                "VALUES (customerss_seq.NEXTVAL,?,?,?,?,?,SYSDATE)");
                st.setString(1, name);
                st.setString(2, email);
                st.setString(3, phone);
                st.setString(4, password);
                st.setString(5, address);
                st.executeUpdate();

                JOptionPane.showMessageDialog(this, "Registration Successful! You can now log in.");
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                passField.setText("");
                addressArea.setText("");
                app.showPanel("Login");
            } catch (SQLException ex) {
                // Also catches Unique Constraint violations directly from DB if email is
                // restricted natively
                if (ex.getMessage().contains("UNIQUE")) {
                    JOptionPane.showMessageDialog(this, "A customer with this email already exists!");
                } else {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
    }
}

// ==============================================================================
// CUSTOMER DASHBOARD (TABS)
// ==============================================================================
class OspCustomerDashboard extends JPanel {
    public OspCustomerDashboard(OnlineShoppingProApp app) {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Store & Search", new ProductSearchAndDisplayTab(app));
        tabs.addTab("Shopping Cart", new ShoppingCartTab(app));
        tabs.addTab("Order History", new OrderHistoryTab(app));

        // Add a logout button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            app.setCustomerId(-1);
            app.showPanel("Login");
        });
        topPanel.add(logoutBtn);

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        // Refresh tabs upon selection
        tabs.addChangeListener(e -> {
            Component selected = tabs.getSelectedComponent();
            if (selected instanceof Refreshable) {
                ((Refreshable) selected).refreshData();
            }
        });
    }
}

interface Refreshable {
    void refreshData();
}

// --- TAB 1: Product Search & Display ---
class ProductSearchAndDisplayTab extends JPanel implements Refreshable {
    OnlineShoppingProApp app;
    DefaultTableModel tableModel;
    JTable table;
    JTextField searchField, qtyField;
    JComboBox<String> categoryCombo;

    public ProductSearchAndDisplayTab(OnlineShoppingProApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        // TOP: Custom Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        categoryCombo = new JComboBox<>(new String[] { "All", "Electronics", "Clothing", "Books", "Home", "Other" });
        JButton searchBtn = new JButton("Search");

        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Category:"));
        searchPanel.add(categoryCombo);
        searchPanel.add(searchBtn);
        add(searchPanel, BorderLayout.NORTH);

        // MID: Data Table
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Product Name", "Category", "Price", "Stock", "Description" }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BOT: Cart Adder
        JPanel actionPanel = new JPanel(new FlowLayout());
        qtyField = new JTextField(5);
        JButton addCartBtn = new JButton("Add Selected to Cart");
        actionPanel.add(new JLabel("Qty:"));
        actionPanel.add(qtyField);
        actionPanel.add(addCartBtn);
        add(actionPanel, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> refreshData());

        addCartBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a product first.");
                return;
            }
            if (app.getCustomerId() == -1)
                return;

            try {
                int qty = Integer.parseInt(qtyField.getText().trim());
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be > 0");
                    return;
                }
                int stock = (int) tableModel.getValueAt(row, 4);
                if (qty > stock) {
                    JOptionPane.showMessageDialog(this, "Not enough stock available.");
                    return;
                }

                int productId = (int) tableModel.getValueAt(row, 0);

                // Find or create cart
                PreparedStatement getCart = app.con.prepareStatement("SELECT cart_id FROM Cartss WHERE customer_id=?");
                getCart.setInt(1, app.getCustomerId());
                ResultSet rs = getCart.executeQuery();
                int cartId;
                if (rs.next()) {
                    cartId = rs.getInt("cart_id");
                } else {
                    PreparedStatement create = app.con.prepareStatement(
                            "INSERT INTO Cartss (cart_id,customer_id,created_date) VALUES (cartss_seq.NEXTVAL,?,SYSDATE)");
                    create.setInt(1, app.getCustomerId());
                    create.executeUpdate();
                    rs = getCart.executeQuery();
                    rs.next();
                    cartId = rs.getInt("cart_id");
                }

                PreparedStatement ins = app.con.prepareStatement(
                        "INSERT INTO CartItemss (cart_item_id,cart_id,product_id,quantity) VALUES (cartitemss_seq.NEXTVAL,?,?,?)");
                ins.setInt(1, cartId);
                ins.setInt(2, productId);
                ins.setInt(3, qty);
                ins.executeUpdate();

                JOptionPane.showMessageDialog(this, "Product added to cart!");
                qtyField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be an integer.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }

    public void refreshData() {
        if (app.con == null)
            return;
        try {
            String nameFilter = searchField.getText().trim();
            String catFilter = (String) categoryCombo.getSelectedItem();

            String sql = "SELECT * FROM Productss WHERE LOWER(product_name) LIKE ?";
            if (!"All".equals(catFilter)) {
                sql += " AND LOWER(category)=LOWER(?)";
            }

            PreparedStatement st = app.con.prepareStatement(sql);
            st.setString(1, "%" + nameFilter.toLowerCase() + "%");
            if (!"All".equals(catFilter)) {
                st.setString(2, catFilter);
            }

            ResultSet rs = st.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("product_id"), rs.getString("product_name"),
                        rs.getString("category"), rs.getDouble("price"),
                        rs.getInt("stock_quantity"), rs.getString("description")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// --- TAB 2: Shopping Cart Workflow ---
class ShoppingCartTab extends JPanel implements Refreshable {
    OnlineShoppingProApp app;
    DefaultTableModel cartModel;
    JTable cartTable;
    JLabel totalLabel;

    public ShoppingCartTab(OnlineShoppingProApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        cartModel = new DefaultTableModel(new Object[] { "Item ID", "Product", "Qty", "Price", "Subtotal" }, 0);
        cartTable = new JTable(cartModel);
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total to pay: Rs. 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton removeBtn = new JButton("Remove Item");
        JButton checkoutBtn = new JButton("Checkout & Place Order");
        btnPanel.add(removeBtn);
        btnPanel.add(checkoutBtn);

        bottom.add(totalLabel, BorderLayout.WEST);
        bottom.add(btnPanel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        removeBtn.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row == -1)
                return;
            try {
                int itemId = (int) cartModel.getValueAt(row, 0);
                PreparedStatement st = app.con.prepareStatement("DELETE FROM CartItemss WHERE cart_item_id=?");
                st.setInt(1, itemId);
                st.executeUpdate();
                refreshData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        checkoutBtn.addActionListener(e -> placeOrder());
    }

    public void refreshData() {
        if (app.con == null || app.getCustomerId() == -1)
            return;
        try {
            PreparedStatement getCart = app.con.prepareStatement("SELECT cart_id FROM Cartss WHERE customer_id=?");
            getCart.setInt(1, app.getCustomerId());
            ResultSet rs = getCart.executeQuery();
            if (!rs.next()) {
                cartModel.setRowCount(0);
                totalLabel.setText("Total to pay: Rs. 0.00");
                return;
            }
            int cartId = rs.getInt("cart_id");

            PreparedStatement st = app.con.prepareStatement(
                    "SELECT ci.cart_item_id, p.product_name, ci.quantity, p.price, (ci.quantity*p.price) AS subtotal " +
                            "FROM CartItemss ci JOIN Productss p ON ci.product_id=p.product_id WHERE ci.cart_id=?");
            st.setInt(1, cartId);
            ResultSet r = st.executeQuery();

            cartModel.setRowCount(0);
            double grandTotal = 0;
            while (r.next()) {
                double sub = r.getDouble("subtotal");
                grandTotal += sub;
                cartModel.addRow(new Object[] { r.getInt("cart_item_id"), r.getString("product_name"),
                        r.getInt("quantity"), r.getDouble("price"), sub });
            }
            totalLabel.setText(String.format("Total to pay: Rs. %.2f", grandTotal));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void placeOrder() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        try {
            app.con.setAutoCommit(false);

            // 1. Get cart
            PreparedStatement getCart = app.con.prepareStatement("SELECT cart_id FROM Cartss WHERE customer_id=?");
            getCart.setInt(1, app.getCustomerId());
            ResultSet rs = getCart.executeQuery();
            if (!rs.next())
                return;
            int cartId = rs.getInt("cart_id");

            // 2. Fetch total
            PreparedStatement totSt = app.con.prepareStatement(
                    "SELECT SUM(ci.quantity*p.price) FROM CartItemss ci JOIN Productss p ON ci.product_id=p.product_id WHERE ci.cart_id=?");
            totSt.setInt(1, cartId);
            ResultSet totRs = totSt.executeQuery();
            totRs.next();
            double totalAmt = totRs.getDouble(1);

            // 3. Create Order
            PreparedStatement ordSt = app.con.prepareStatement(
                    "INSERT INTO Orderss (order_id, customer_id, order_date, total_amount, order_status) VALUES (orderss_seq.NEXTVAL, ?, SYSDATE, ?, 'Pending')");
            ordSt.setInt(1, app.getCustomerId());
            ordSt.setDouble(2, totalAmt);
            ordSt.executeUpdate();

            // 4. Get order_id
            ResultSet currRs = app.con.prepareStatement("SELECT orderss_seq.CURRVAL FROM dual").executeQuery();
            currRs.next();
            int orderId = currRs.getInt(1);

            // 5. Transfer items and deduce stock
            PreparedStatement getItems = app.con
                    .prepareStatement("SELECT product_id, quantity FROM CartItemss WHERE cart_id=?");
            getItems.setInt(1, cartId);
            ResultSet itemRs = getItems.executeQuery();
            while (itemRs.next()) {
                int pId = itemRs.getInt(1);
                int qty = itemRs.getInt(2);

                // Get price
                PreparedStatement pPrice = app.con.prepareStatement("SELECT price FROM Productss WHERE product_id=?");
                pPrice.setInt(1, pId);
                ResultSet pRs = pPrice.executeQuery();
                pRs.next();
                double price = pRs.getDouble(1);

                // Insert order item
                PreparedStatement oItem = app.con.prepareStatement(
                        "INSERT INTO OrderItemss (order_item_id, order_id, product_id, quantity, price) VALUES (orderitemss_seq.NEXTVAL, ?, ?, ?, ?)");
                oItem.setInt(1, orderId);
                oItem.setInt(2, pId);
                oItem.setInt(3, qty);
                oItem.setDouble(4, price);
                oItem.executeUpdate();

                // Deduce stock
                PreparedStatement stockSt = app.con.prepareStatement(
                        "UPDATE Productss SET stock_quantity = stock_quantity - ? WHERE product_id=? AND stock_quantity >= ?");
                stockSt.setInt(1, qty);
                stockSt.setInt(2, pId);
                stockSt.setInt(3, qty);
                int up = stockSt.executeUpdate();
                if (up == 0) {
                    app.con.rollback();
                    app.con.setAutoCommit(true);
                    JOptionPane.showMessageDialog(this, "Order failed! Insufficient stock for some items.");
                    return;
                }
            }

            // 6. Clear cart
            PreparedStatement delCart = app.con.prepareStatement("DELETE FROM CartItemss WHERE cart_id=?");
            delCart.setInt(1, cartId);
            delCart.executeUpdate();

            app.con.commit();
            app.con.setAutoCommit(true);
            JOptionPane.showMessageDialog(this, "Order Placed Successfully! Returning to clean cart.");
            refreshData();
        } catch (SQLException e) {
            try {
                app.con.rollback();
                app.con.setAutoCommit(true);
            } catch (Exception ex) {
            }
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}

// --- TAB 3: Order History Page ---
class OrderHistoryTab extends JPanel implements Refreshable {
    OnlineShoppingProApp app;
    DefaultTableModel historyModel;

    public OrderHistoryTab(OnlineShoppingProApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        historyModel = new DefaultTableModel(
                new Object[] { "Order ID", "Date", "Total Amount Base", "Status", "Detailed Item Info" }, 0);
        JTable table = new JTable(historyModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh Order Status");
        refreshBtn.addActionListener(e -> refreshData());
        add(refreshBtn, BorderLayout.SOUTH);
    }

    public void refreshData() {
        if (app.con == null || app.getCustomerId() == -1)
            return;
        try {
            // Retrieve and display previous orders
            PreparedStatement st = app.con.prepareStatement(
                    "SELECT order_id, order_date, total_amount, order_status FROM Orderss WHERE customer_id=? ORDER BY order_date DESC");
            st.setInt(1, app.getCustomerId());
            ResultSet rs = st.executeQuery();

            historyModel.setRowCount(0);
            while (rs.next()) {
                int oid = rs.getInt("order_id");

                // Get summary of items
                PreparedStatement itemSt = app.con.prepareStatement(
                        "SELECT p.product_name, oi.quantity FROM OrderItemss oi JOIN Productss p ON oi.product_id=p.product_id WHERE oi.order_id=?");
                itemSt.setInt(1, oid);
                ResultSet irs = itemSt.executeQuery();
                StringBuilder itemsDesc = new StringBuilder();
                while (irs.next()) {
                    itemsDesc.append(irs.getString("product_name")).append("(x").append(irs.getInt("quantity"))
                            .append(") ");
                }

                historyModel.addRow(new Object[] {
                        oid, rs.getDate("order_date"), "Rs. " + rs.getDouble("total_amount"),
                        rs.getString("order_status"), itemsDesc.toString()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// ==============================================================================
// ADMIN DASHBOARD
// ==============================================================================
class OspAdminDashboard extends JPanel {
    public OspAdminDashboard(OnlineShoppingProApp app) {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Manage Inventory", new AdminInventoryTab(app));
        tabs.addTab("Order Status Management", new AdminOrdersTab(app));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            app.showPanel("Login");
        });
        topPanel.add(logoutBtn);

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        tabs.addChangeListener(e -> {
            Component selected = tabs.getSelectedComponent();
            if (selected instanceof Refreshable) {
                ((Refreshable) selected).refreshData();
            }
        });
    }
}

// --- ADMIN TAB 1: Inventory ---
class AdminInventoryTab extends JPanel implements Refreshable {
    OnlineShoppingProApp app;
    DefaultTableModel pModel;

    public AdminInventoryTab(OnlineShoppingProApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        pModel = new DefaultTableModel(new Object[] { "ID", "Name", "Category", "Price", "Stock" }, 0);
        JTable table = new JTable(pModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JTextField nameField = new JTextField(12);
        JTextField catField = new JTextField(10);
        JTextField priceField = new JTextField(7);
        JTextField stockField = new JTextField(7);
        JButton addBtn = new JButton("Add Product");

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        formPanel.add(new JLabel("Name"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category"));
        formPanel.add(catField);
        formPanel.add(new JLabel("Price"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Stock"));
        formPanel.add(stockField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(addBtn);

        JPanel addPanel = new JPanel(new BorderLayout());
        addPanel.add(formPanel, BorderLayout.NORTH);
        addPanel.add(btnPanel, BorderLayout.SOUTH);

        add(addPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            try {
                PreparedStatement st = app.con.prepareStatement(
                        "INSERT INTO Productss (product_id, product_name, category, price, stock_quantity) VALUES (productss_seq.NEXTVAL,?,?,?,?)");
                st.setString(1, nameField.getText().trim());
                st.setString(2, catField.getText().trim());
                st.setDouble(3, Double.parseDouble(priceField.getText().trim()));
                st.setInt(4, Integer.parseInt(stockField.getText().trim()));
                st.executeUpdate();
                refreshData();
                nameField.setText("");
                catField.setText("");
                priceField.setText("");
                stockField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Valid Form Required: " + ex.getMessage());
            }
        });
    }

    public void refreshData() {
        if (app.con == null)
            return;
        try {
            ResultSet rs = app.con.createStatement().executeQuery("SELECT * FROM Productss ORDER BY product_id");
            pModel.setRowCount(0);
            while (rs.next()) {
                pModel.addRow(new Object[] { rs.getInt("product_id"), rs.getString("product_name"),
                        rs.getString("category"), rs.getDouble("price"), rs.getInt("stock_quantity") });
            }
        } catch (SQLException ex) {
        }
    }
}
class AdminOrdersTab extends JPanel implements Refreshable {
    OnlineShoppingProApp app;
    DefaultTableModel ordModel;
    JTable ordTable;

    public AdminOrdersTab(OnlineShoppingProApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        ordModel = new DefaultTableModel(new Object[] { "Order ID", "Customer ID", "Date", "Total", "Status" }, 0);
        ordTable = new JTable(ordModel);
        add(new JScrollPane(ordTable), BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout());
        JComboBox<String> statusBox = new JComboBox<>(new String[] { "Pending", "Shipped", "Delivered" });
        JButton updateBtn = new JButton("Update Status");

        botPanel.add(new JLabel("Update Selected Order to: "));
        botPanel.add(statusBox);
        botPanel.add(updateBtn);
        add(botPanel, BorderLayout.SOUTH);

        updateBtn.addActionListener(e -> {
            int row = ordTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an order to update!");
                return;
            }
            int oid = (int) ordModel.getValueAt(row, 0);
            String nStatus = (String) statusBox.getSelectedItem();

            try {
                PreparedStatement st = app.con.prepareStatement("UPDATE Orderss SET order_status=? WHERE order_id=?");
                st.setString(1, nStatus);
                st.setInt(2, oid);
                st.executeUpdate();
                JOptionPane.showMessageDialog(this, "Order #" + oid + " updated to " + nStatus);
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }

    public void refreshData() {
        if (app.con == null)
            return;
        try {
            ResultSet rs = app.con.createStatement().executeQuery("SELECT * FROM Orderss ORDER BY order_id DESC");
            ordModel.setRowCount(0);
            while (rs.next()) {
                ordModel.addRow(new Object[] {
                        rs.getInt("order_id"), rs.getInt("customer_id"), rs.getDate("order_date"),
                        rs.getDouble("total_amount"), rs.getString("order_status")
                });
            }
        } catch (SQLException ex) {
        }
    }
}




 & 'C:\Users\vishn\AppData\Local\Programs\Eclipse Adoptium\jre-25.0.1.8-hotspot\bin\java.exe' '@C:\Users\vishn\AppData\Local\Temp\cp_9petulqnpnx1q6x3ty7t2ge0a.argfile' 'OnlineShoppingProApp'



javac OnlineShoppingProApp.java



java -cp ".;ojdbc8.jar" OnlineShoppingProApp
