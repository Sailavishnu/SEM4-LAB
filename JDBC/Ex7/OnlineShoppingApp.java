import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OnlineShoppingApp extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private int customerId = -1;
    Connection con;

    OnlineShoppingApp() {
        setTitle("Online Shopping Application");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connectDB();

        mainPanel.add(new LoginForm(this), "Login");
        mainPanel.add(new RegistrationForm(this), "Register");
        mainPanel.add(new CustomerDashboard(this), "Customer");
        mainPanel.add(new AdminDashboard(this), "Admin");

        add(mainPanel);
        showPanel("Login");
        setVisible(true);
    }

    void connectDB() {
        try {
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

    void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    void setCustomerId(int id) {
        this.customerId = id;
    }

    int getCustomerId() {
        return customerId;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineShoppingApp());
    }
}

class CustomerDashboard extends JPanel {
    private OnlineShoppingApp app;

    CustomerDashboard(OnlineShoppingApp app) {
        this.app = app;
        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton orderBtn = new JButton("A) Place an Order");
        JButton cartBtn = new JButton("B) Shopping Cart");
        JButton profileBtn = new JButton("C) Update Profile");
        JButton searchBtn = new JButton("D) Search Products");
        JButton historyBtn = new JButton("E) Order History");

        add(orderBtn);
        add(cartBtn);
        add(profileBtn);
        add(searchBtn);
        add(historyBtn);

        orderBtn.addActionListener(e -> new OrderPlacementForm(app).setVisible(true));
        cartBtn.addActionListener(e -> new ShoppingCartForm(app).setVisible(true));
        profileBtn.addActionListener(e -> new CustomerProfileUpdateForm(app).setVisible(true));
        searchBtn.addActionListener(e -> new ProductSearchForm(app).setVisible(true));
        historyBtn.addActionListener(e -> new OrderHistoryPage(app).setVisible(true));
    }
}

class AdminDashboard extends JPanel {
    private OnlineShoppingApp app;

    AdminDashboard(OnlineShoppingApp app) {
        this.app = app;
        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addBtn = new JButton("Add Product");
        JButton updateBtn = new JButton("Update Product");
        JButton manageBtn = new JButton("Manage Products");

        add(addBtn);
        add(updateBtn);
        add(manageBtn);

        addBtn.addActionListener(e -> new AddProductForm(app).setVisible(true));
        updateBtn.addActionListener(e -> new UpdateProductForm(app).setVisible(true));
        manageBtn.addActionListener(e -> new ManageProductsForm(app).setVisible(true));
    }
}

class LoginForm extends JPanel {
    private OnlineShoppingApp app;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm(OnlineShoppingApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(new JLabel("Login As:"), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("User Type:"), gbc);

        gbc.gridx = 1;
        String[] userTypes = { "Customer", "Admin" };
        JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes);
        add(userTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String userType = (String) userTypeComboBox.getSelectedItem();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if ("Admin".equals(userType)) {
                if ("admin".equals(username) && "admin123".equals(password)) {
                    app.showPanel("Admin");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Admin Credentials", "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (app.con == null) {
                    JOptionPane.showMessageDialog(this, "Database not connected");
                    return;
                }
                try {
                    String sql = "SELECT customer_id FROM Customerss WHERE name = ? AND password = ?";
                    PreparedStatement stmt = app.con.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        app.setCustomerId(rs.getInt("customer_id"));
                        app.showPanel("Customer");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Customer Credentials");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
                }
            }
        });

        registerButton.addActionListener(e -> app.showPanel("Register"));
    }
}

class RegistrationForm extends JPanel {
    private OnlineShoppingApp app;
    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JTextArea addressArea;

    public RegistrationForm(OnlineShoppingApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressArea = new JTextArea(3, 20);
        add(new JScrollPane(addressArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Register");
        add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            if (app.con == null) {
                JOptionPane.showMessageDialog(this, "Database not connected");
                return;
            }
            try {
                String sql = "INSERT INTO Customerss (customer_id, name, email, phone, password, address, created_date) VALUES (customerss_seq.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";
                PreparedStatement stmt = app.con.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, emailField.getText());
                stmt.setString(3, phoneField.getText());
                stmt.setString(4, new String(passwordField.getPassword()));
                stmt.setString(5, addressArea.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration Successful! Please login");
                app.showPanel("Login");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Registration Error: " + ex.getMessage());
            }
        });
    }
}

class OrderPlacementForm extends JFrame {
    private OnlineShoppingApp app;
    private JTable productsTable;
    private DefaultTableModel productsTableModel;
    private JTextField quantityField;

    public OrderPlacementForm(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Place an Order");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Product selection table
        productsTableModel = new DefaultTableModel(
                new Object[] { "Product ID", "Product Name", "Category", "Price", "Stock" }, 0);
        productsTable = new JTable(productsTableModel);
        add(new JScrollPane(productsTable), BorderLayout.CENTER);

        // Order placement panel
        JPanel orderPanel = new JPanel(new FlowLayout());
        orderPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        orderPanel.add(quantityField);

        JButton addToCartButton = new JButton("Add to Cart");
        JButton placeOrderButton = new JButton("Place Order");
        orderPanel.add(addToCartButton);
        orderPanel.add(placeOrderButton);

        add(orderPanel, BorderLayout.SOUTH);

        addToCartButton.addActionListener(e -> addToCart());
        placeOrderButton.addActionListener(e -> placeOrder());

        loadProducts();
        setVisible(true);
    }

    private void loadProducts() {
        try {
            String sql = "SELECT product_id, product_name, category, price, stock_quantity FROM Productss";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            productsTableModel.setRowCount(0);
            while (rs.next()) {
                productsTableModel.addRow(new Object[] {
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
        }
    }

    private void addToCart() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0");
                return;
            }

            int productId = (int) productsTableModel.getValueAt(selectedRow, 0);
            int stock = (int) productsTableModel.getValueAt(selectedRow, 4);

            if (quantity > stock) {
                JOptionPane.showMessageDialog(this, "Not enough stock available");
                return;
            }

            // Get or create cart
            String getCartSql = "SELECT cart_id FROM Cartss WHERE customer_id = ?";
            PreparedStatement getStmt = app.con.prepareStatement(getCartSql);
            getStmt.setInt(1, app.getCustomerId());
            ResultSet rs = getStmt.executeQuery();

            int cartId;
            if (rs.next()) {
                cartId = rs.getInt("cart_id");
            } else {
                // Create new cart
                String createCartSql = "INSERT INTO Cartss (cart_id, customer_id, created_date) VALUES (cartss_seq.NEXTVAL, ?, SYSDATE)";
                PreparedStatement createStmt = app.con.prepareStatement(createCartSql);
                createStmt.setInt(1, app.getCustomerId());
                createStmt.executeUpdate();

                getStmt = app.con.prepareStatement(getCartSql);
                getStmt.setInt(1, app.getCustomerId());
                rs = getStmt.executeQuery();
                rs.next();
                cartId = rs.getInt("cart_id");
            }

            // Add item to cart
            String insertItemSql = "INSERT INTO CartItemss (cart_item_id, cart_id, product_id, quantity) VALUES (cartitemss_seq.NEXTVAL, ?, ?, ?)";
            PreparedStatement insertStmt = app.con.prepareStatement(insertItemSql);
            insertStmt.setInt(1, cartId);
            insertStmt.setInt(2, productId);
            insertStmt.setInt(3, quantity);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Added to cart successfully");
            quantityField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void placeOrder() {
        try {
            // Get cart
            String getCartSql = "SELECT cart_id FROM Cartss WHERE customer_id = ?";
            PreparedStatement getStmt = app.con.prepareStatement(getCartSql);
            getStmt.setInt(1, app.getCustomerId());
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Cart is empty");
                return;
            }

            int cartId = rs.getInt("cart_id");

            // Calculate total amount
            String totalSql = "SELECT SUM(ci.quantity * p.price) as total FROM CartItemss ci JOIN Productss p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";
            PreparedStatement totalStmt = app.con.prepareStatement(totalSql);
            totalStmt.setInt(1, cartId);
            ResultSet totalRs = totalStmt.executeQuery();
            totalRs.next();
            double totalAmount = totalRs.getDouble("total");

            // Create order
            String orderSql = "INSERT INTO Orderss (order_id, customer_id, order_date, total_amount, order_status) VALUES (orderss_seq.NEXTVAL, ?, SYSDATE, ?, 'Pending')";
            PreparedStatement orderStmt = app.con.prepareStatement(orderSql);
            orderStmt.setInt(1, app.getCustomerId());
            orderStmt.setDouble(2, totalAmount);
            orderStmt.executeUpdate();

            // Get the order ID
            String getOrderSql = "SELECT order_id FROM Orderss WHERE customer_id = ? AND order_date = (SELECT MAX(order_date) FROM Orderss WHERE customer_id = ?)";
            PreparedStatement getOrderStmt = app.con.prepareStatement(getOrderSql);
            getOrderStmt.setInt(1, app.getCustomerId());
            getOrderStmt.setInt(2, app.getCustomerId());
            ResultSet orderRs = getOrderStmt.executeQuery();
            orderRs.next();
            int orderId = orderRs.getInt("order_id");

            // Move items from cart to order
            String itemsSql = "SELECT product_id, quantity FROM CartItemss WHERE cart_id = ?";
            PreparedStatement itemsStmt = app.con.prepareStatement(itemsSql);
            itemsStmt.setInt(1, cartId);
            ResultSet itemsRs = itemsStmt.executeQuery();

            while (itemsRs.next()) {
                int productId = itemsRs.getInt("product_id");
                int quantity = itemsRs.getInt("quantity");

                // Get product price
                String priceSql = "SELECT price FROM Productss WHERE product_id = ?";
                PreparedStatement priceStmt = app.con.prepareStatement(priceSql);
                priceStmt.setInt(1, productId);
                ResultSet priceRs = priceStmt.executeQuery();
                priceRs.next();
                double price = priceRs.getDouble("price");

                // Insert order item
                String insertOrderItemSql = "INSERT INTO OrderItemss (order_item_id, order_id, product_id, quantity, price) VALUES (orderitemss_seq.NEXTVAL, ?, ?, ?, ?)";
                PreparedStatement insertOrderItemStmt = app.con.prepareStatement(insertOrderItemSql);
                insertOrderItemStmt.setInt(1, orderId);
                insertOrderItemStmt.setInt(2, productId);
                insertOrderItemStmt.setInt(3, quantity);
                insertOrderItemStmt.setDouble(4, price);
                insertOrderItemStmt.executeUpdate();
            }

            // Clear cart
            String clearCartSql = "DELETE FROM CartItemss WHERE cart_id = ?";
            PreparedStatement clearStmt = app.con.prepareStatement(clearCartSql);
            clearStmt.setInt(1, cartId);
            clearStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order placed successfully! Order ID: " + orderId);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error placing order: " + ex.getMessage());
        }
    }
}

class ShoppingCartForm extends JFrame {
    private OnlineShoppingApp app;
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JLabel totalLabel;

    public ShoppingCartForm(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Shopping Cart");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cartTableModel = new DefaultTableModel(
                new Object[] { "Cart Item ID", "Product Name", "Quantity", "Price", "Subtotal" }, 0);
        cartTable = new JTable(cartTableModel);
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: 0.00");
        labelPanel.add(totalLabel);
        bottomPanel.add(labelPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateButton = new JButton("Update Quantity");
        JButton removeButton = new JButton("Remove Item");
        JButton placeOrderButton = new JButton("Place Order");

        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(placeOrderButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        updateButton.addActionListener(e -> updateQuantity());
        removeButton.addActionListener(e -> removeItem());
        placeOrderButton.addActionListener(e -> placeOrderFromCart());

        loadCartItems();
        setVisible(true);
    }

    private void loadCartItems() {
        try {
            // Get cart ID
            String getCartSql = "SELECT cart_id FROM Cartss WHERE customer_id = ?";
            PreparedStatement getStmt = app.con.prepareStatement(getCartSql);
            getStmt.setInt(1, app.getCustomerId());
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Cart is empty");
                return;
            }

            int cartId = rs.getInt("cart_id");

            String sql = "SELECT ci.cart_item_id, p.product_name, ci.quantity, p.price, (ci.quantity * p.price) as subtotal FROM CartItemss ci JOIN Productss p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            stmt.setInt(1, cartId);
            rs = stmt.executeQuery();

            cartTableModel.setRowCount(0);
            double total = 0;
            while (rs.next()) {
                double subtotal = rs.getDouble("subtotal");
                total += subtotal;
                cartTableModel.addRow(new Object[] {
                        rs.getInt("cart_item_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        subtotal
                });
            }

            totalLabel.setText(String.format("Total: %.2f", total));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading cart: " + ex.getMessage());
        }
    }

    private void updateQuantity() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item");
            return;
        }

        String newQty = JOptionPane.showInputDialog(this, "Enter new quantity:");
        if (newQty == null || newQty.trim().isEmpty())
            return;

        try {
            int quantity = Integer.parseInt(newQty);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0");
                return;
            }

            int cartItemId = (int) cartTableModel.getValueAt(selectedRow, 0);

            String updateSql = "UPDATE CartItemss SET quantity = ? WHERE cart_item_id = ?";
            PreparedStatement stmt = app.con.prepareStatement(updateSql);
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartItemId);
            stmt.executeUpdate();

            loadCartItems();
            JOptionPane.showMessageDialog(this, "Quantity updated");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void removeItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item");
            return;
        }

        try {
            int cartItemId = (int) cartTableModel.getValueAt(selectedRow, 0);

            String deleteSql = "DELETE FROM CartItemss WHERE cart_item_id = ?";
            PreparedStatement stmt = app.con.prepareStatement(deleteSql);
            stmt.setInt(1, cartItemId);
            stmt.executeUpdate();

            loadCartItems();
            JOptionPane.showMessageDialog(this, "Item removed from cart");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void placeOrderFromCart() {
        try {
            String getCartSql = "SELECT cart_id FROM Cartss WHERE customer_id = ?";
            PreparedStatement getStmt = app.con.prepareStatement(getCartSql);
            getStmt.setInt(1, app.getCustomerId());
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Cart is empty");
                return;
            }

            int cartId = rs.getInt("cart_id");

            // Check if cart has items
            String checkSql = "SELECT COUNT(*) as cnt FROM CartItemss WHERE cart_id = ?";
            PreparedStatement checkStmt = app.con.prepareStatement(checkSql);
            checkStmt.setInt(1, cartId);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt("cnt") == 0) {
                JOptionPane.showMessageDialog(this, "Cart is empty");
                return;
            }

            // Calculate total
            String totalSql = "SELECT SUM(ci.quantity * p.price) as total FROM CartItemss ci JOIN Productss p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";
            PreparedStatement totalStmt = app.con.prepareStatement(totalSql);
            totalStmt.setInt(1, cartId);
            ResultSet totalRs = totalStmt.executeQuery();
            totalRs.next();
            double totalAmount = totalRs.getDouble("total");

            // Create order
            String orderSql = "INSERT INTO Orderss (order_id, customer_id, order_date, total_amount, order_status) VALUES (orderss_seq.NEXTVAL, ?, SYSDATE, ?, 'Pending')";
            PreparedStatement orderStmt = app.con.prepareStatement(orderSql);
            orderStmt.setInt(1, app.getCustomerId());
            orderStmt.setDouble(2, totalAmount);
            orderStmt.executeUpdate();

            String getOrderSql = "SELECT order_id FROM Orderss WHERE customer_id = ? AND order_date = (SELECT MAX(order_date) FROM Orderss WHERE customer_id = ?)";
            PreparedStatement getOrderStmt = app.con.prepareStatement(getOrderSql);
            getOrderStmt.setInt(1, app.getCustomerId());
            getOrderStmt.setInt(2, app.getCustomerId());
            ResultSet orderRs = getOrderStmt.executeQuery();
            orderRs.next();
            int orderId = orderRs.getInt("order_id");

            // Transfer items
            String itemsSql = "SELECT product_id, quantity FROM CartItemss WHERE cart_id = ?";
            PreparedStatement itemsStmt = app.con.prepareStatement(itemsSql);
            itemsStmt.setInt(1, cartId);
            ResultSet itemsRs = itemsStmt.executeQuery();

            while (itemsRs.next()) {
                int productId = itemsRs.getInt("product_id");
                int quantity = itemsRs.getInt("quantity");

                String priceSql = "SELECT price FROM Productss WHERE product_id = ?";
                PreparedStatement priceStmt = app.con.prepareStatement(priceSql);
                priceStmt.setInt(1, productId);
                ResultSet priceRs = priceStmt.executeQuery();
                priceRs.next();
                double price = priceRs.getDouble("price");

                String insertOrderItemSql = "INSERT INTO OrderItemss (order_item_id, order_id, product_id, quantity, price) VALUES (orderitemss_seq.NEXTVAL, ?, ?, ?, ?)";
                PreparedStatement insertOrderItemStmt = app.con.prepareStatement(insertOrderItemSql);
                insertOrderItemStmt.setInt(1, orderId);
                insertOrderItemStmt.setInt(2, productId);
                insertOrderItemStmt.setInt(3, quantity);
                insertOrderItemStmt.setDouble(4, price);
                insertOrderItemStmt.executeUpdate();
            }

            // Clear cart
            String clearCartSql = "DELETE FROM CartItemss WHERE cart_id = ?";
            PreparedStatement clearStmt = app.con.prepareStatement(clearCartSql);
            clearStmt.setInt(1, cartId);
            clearStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order placed successfully! Order ID: " + orderId);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

class CustomerProfileUpdateForm extends JFrame {
    private OnlineShoppingApp app;
    private JTextField nameField, emailField, phoneField;
    private JTextArea addressArea;

    public CustomerProfileUpdateForm(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Update Customer Profile");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressArea = new JTextArea(3, 20);
        add(new JScrollPane(addressArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton updateButton = new JButton("Update");
        add(updateButton, gbc);

        updateButton.addActionListener(e -> updateProfile());

        loadCustomerData();
        setVisible(true);
    }

    private void loadCustomerData() {
        try {
            String sql = "SELECT name, email, phone, address FROM Customerss WHERE customer_id = ?";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            stmt.setInt(1, app.getCustomerId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                addressArea.setText(rs.getString("address"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + ex.getMessage());
        }
    }

    private void updateProfile() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressArea.getText();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            String updateSql = "UPDATE Customerss SET name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";
            PreparedStatement stmt = app.con.prepareStatement(updateSql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, app.getCustomerId());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage());
        }
    }
}

class ProductSearchForm extends JFrame {
    private OnlineShoppingApp app;
    private JTextField searchField;
    private JTable resultsTable;
    private DefaultTableModel resultsTableModel;
    private JComboBox<String> searchTypeCombo;

    public ProductSearchForm(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Product Search");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchTypeCombo = new JComboBox<>(new String[] { "By Name", "By Category" });
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        resultsTableModel = new DefaultTableModel(
                new Object[] { "Product ID", "Name", "Category", "Price", "Stock" }, 0);
        resultsTable = new JTable(resultsTableModel);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchProducts());

        setVisible(true);
    }

    private void searchProducts() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term");
            return;
        }

        try {
            String sql;
            if ("By Name".equals(searchTypeCombo.getSelectedItem())) {
                sql = "SELECT product_id, product_name, category, price, stock_quantity FROM Productss WHERE LOWER(product_name) LIKE ?";
            } else {
                sql = "SELECT product_id, product_name, category, price, stock_quantity FROM Productss WHERE LOWER(category) LIKE ?";
            }

            PreparedStatement stmt = app.con.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();

            resultsTableModel.setRowCount(0);
            while (rs.next()) {
                resultsTableModel.addRow(new Object[] {
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")
                });
            }

            if (resultsTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No products found");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }
}

class OrderHistoryPage extends JFrame {
    private OnlineShoppingApp app;
    private JTable orderHistoryTable;
    private DefaultTableModel orderHistoryTableModel;
    private JTable orderItemsTable;
    private DefaultTableModel orderItemsTableModel;

    public OrderHistoryPage(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Order History");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Orders table
        orderHistoryTableModel = new DefaultTableModel(
                new Object[] { "Order ID", "Date", "Total Amount", "Status" }, 0);
        orderHistoryTable = new JTable(orderHistoryTableModel);
        orderHistoryTable.getSelectionModel().addListSelectionListener(e -> showOrderDetails());

        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.add(new JLabel("Orders:"), BorderLayout.NORTH);
        ordersPanel.add(new JScrollPane(orderHistoryTable), BorderLayout.CENTER);

        // Order items table
        orderItemsTableModel = new DefaultTableModel(
                new Object[] { "Product Name", "Quantity", "Price", "Subtotal" }, 0);
        orderItemsTable = new JTable(orderItemsTableModel);

        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.add(new JLabel("Order Items:"), BorderLayout.NORTH);
        itemsPanel.add(new JScrollPane(orderItemsTable), BorderLayout.CENTER);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ordersPanel, itemsPanel);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        loadOrderHistory();
        setVisible(true);
    }

    private void loadOrderHistory() {
        try {
            String sql = "SELECT order_id, order_date, total_amount, order_status FROM Orderss WHERE customer_id = ? ORDER BY order_date DESC";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            stmt.setInt(1, app.getCustomerId());
            ResultSet rs = stmt.executeQuery();

            orderHistoryTableModel.setRowCount(0);
            while (rs.next()) {
                orderHistoryTableModel.addRow(new Object[] {
                        rs.getInt("order_id"),
                        rs.getDate("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("order_status")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + ex.getMessage());
        }
    }

    private void showOrderDetails() {
        int selectedRow = orderHistoryTable.getSelectedRow();
        if (selectedRow == -1) {
            orderItemsTableModel.setRowCount(0);
            return;
        }

        try {
            int orderId = (int) orderHistoryTableModel.getValueAt(selectedRow, 0);

            String sql = "SELECT p.product_name, oi.quantity, oi.price, (oi.quantity * oi.price) as subtotal FROM OrderItemss oi JOIN Productss p ON oi.product_id = p.product_id WHERE oi.order_id = ?";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            orderItemsTableModel.setRowCount(0);
            while (rs.next()) {
                orderItemsTableModel.addRow(new Object[] {
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("subtotal")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading order details: " + ex.getMessage());
        }
    }
}

class AddProductForm extends JFrame {
    private OnlineShoppingApp app;

    public AddProductForm(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Add Product");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(15);
        JTextField categoryField = new JTextField(15);
        JTextField priceField = new JTextField(15);
        JTextField stockField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        add(stockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton addBtn = new JButton("Add Product");
        add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            try {
                String sql = "INSERT INTO Productss (product_id, product_name, category, price, stock_quantity) VALUES (productss_seq.NEXTVAL, ?, ?, ?, ?)";
                PreparedStatement stmt = app.con.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, categoryField.getText());
                stmt.setDouble(3, Double.parseDouble(priceField.getText()));
                stmt.setInt(4, Integer.parseInt(stockField.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid price and stock");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}

class UpdateProductForm extends JFrame {
    private OnlineShoppingApp app;
    private JComboBox<String> productCombo;
    private JTextField nameField, categoryField, priceField, stockField;
    private boolean isLoading = false;

    public UpdateProductForm(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Update Product");
        setSize(400, 320);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        productCombo = new JComboBox<>();
        nameField = new JTextField(15);
        categoryField = new JTextField(15);
        priceField = new JTextField(15);
        stockField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Select Product:"), gbc);
        gbc.gridx = 1;
        add(productCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        add(stockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton updateBtn = new JButton("Update Product");
        add(updateBtn, gbc);

        isLoading = true;
        loadProducts();
        isLoading = false;

        productCombo.addActionListener(e -> {
            if (!isLoading) {
                loadProductDetails();
            }
        });

        if (productCombo.getItemCount() > 0) {
            loadProductDetails();
        }

        updateBtn.addActionListener(e -> {
            try {
                String selectedItem = productCombo.getSelectedItem().toString();
                int productId = Integer.parseInt(selectedItem.split(" - ")[0]);

                String sql = "UPDATE Productss SET product_name = ?, category = ?, price = ?, stock_quantity = ? WHERE product_id = ?";
                PreparedStatement stmt = app.con.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, categoryField.getText());
                stmt.setDouble(3, Double.parseDouble(priceField.getText()));
                stmt.setInt(4, Integer.parseInt(stockField.getText()));
                stmt.setInt(5, productId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    private void loadProducts() {
        try {
            String sql = "SELECT product_id, product_name FROM Productss ORDER BY product_id";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            productCombo.removeAllItems();
            while (rs.next()) {
                productCombo.addItem(rs.getInt("product_id") + " - " + rs.getString("product_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadProductDetails() {
        try {
            Object selected = productCombo.getSelectedItem();
            if (selected == null) {
                return;
            }

            int productId = Integer.parseInt(selected.toString().split(" - ")[0]);
            String sql = "SELECT product_name, category, price, stock_quantity FROM Productss WHERE product_id = ?";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("product_name"));
                categoryField.setText(rs.getString("category"));
                priceField.setText(String.valueOf(rs.getDouble("price")));
                stockField.setText(String.valueOf(rs.getInt("stock_quantity")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

class ManageProductsForm extends JFrame {
    private OnlineShoppingApp app;
    private DefaultTableModel model;
    private JTable table;

    public ManageProductsForm(OnlineShoppingApp app) {
        this.app = app;
        setTitle("Manage Products");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] { "ID", "Name", "Category", "Price", "Stock" }, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton deleteBtn = new JButton("Delete Product");
        JButton refreshBtn = new JButton("Refresh");
        panel.add(deleteBtn);
        panel.add(refreshBtn);
        add(panel, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> deleteProduct());
        refreshBtn.addActionListener(e -> loadProducts());

        loadProducts();
        setVisible(true);
    }

    private void loadProducts() {
        try {
            String sql = "SELECT product_id, product_name, category, price, stock_quantity FROM Productss ORDER BY product_id";
            PreparedStatement stmt = app.con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to delete");
            return;
        }

        try {
            int productId = (Integer) model.getValueAt(row, 0);
            String productName = (String) model.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(this, "Delete '" + productName + "'?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM Productss WHERE product_id = ?";
                PreparedStatement stmt = app.con.prepareStatement(sql);
                stmt.setInt(1, productId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product deleted!");
                loadProducts();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
