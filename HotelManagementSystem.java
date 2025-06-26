package hotelmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HotelManagementSystem extends JFrame implements ActionListener {
JTextField tf1 = new JTextField();
JPasswordField tf2 = new JPasswordField();
Image backgroundImage;


public HotelManagementSystem() {
    setTitle("HOTEL MANAGEMENT SYSTEM");
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Load background image
    backgroundImage = new ImageIcon(getClass().getResource("/images/crave_logo.jpeg")).getImage();

    // Background panel
    JPanel backgroundPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    };

    // Force repaint on resize or restore
    this.addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            backgroundPanel.repaint();
        }

        public void componentShown(ComponentEvent e) {
            backgroundPanel.repaint();
        }
    });

    backgroundPanel.setLayout(new GridBagLayout()); // Center layout
    setContentPane(backgroundPanel);

    // Inner panel to hold login components
    JPanel loginPanel = new JPanel(null); // Absolute layout inside
    loginPanel.setOpaque(false);
    loginPanel.setPreferredSize(new Dimension(450, 300));

    // Components
    JLabel l1 = new JLabel("USER NAME:");
    l1.setBounds(50, 50, 100, 30);
    tf1.setBounds(160, 50, 220, 30);

    JLabel l2 = new JLabel("PASSWORD:");
    l2.setBounds(50, 100, 100, 30);
    tf2.setBounds(160, 100, 220, 30);

    JButton b1 = new JButton("OK");
    b1.setBounds(160, 180, 90, 30);

    JButton b2 = new JButton("CANCEL");
    b2.setBounds(260, 180, 90, 30);

    // Add to inner panel
    loginPanel.add(l1);
    loginPanel.add(tf1);
    loginPanel.add(l2);
    loginPanel.add(tf2);
    loginPanel.add(b1);
    loginPanel.add(b2);

    // Center the login panel on background panel
    backgroundPanel.add(loginPanel);

    // Button actions
    b1.addActionListener(this);
    b2.addActionListener(this);

    setVisible(true);
}

public void actionPerformed(ActionEvent e) {
    JButton source = (JButton) e.getSource();

    if (source.getText().equals("OK")) {
        String uname = tf1.getText();
        String pass = tf2.getText();

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/hotel1", "shivani", "s");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, uname);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.setVisible(false);
                new MainFrame(); // Ensure MainFrame class exists
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    } else if (source.getText().equals("CANCEL")) {
        tf1.setText("");
        tf2.setText("");
    }
}


   
public class MainFrame extends JFrame implements ActionListener {
    JButton billButton = new JButton("BILL");
    JButton reportButton = new JButton("REPORT");
    Image backgroundImage;

    public MainFrame() {
        setTitle("Main Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       

        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/images/crave_logo.jpeg")).getImage();

        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // To center child panel
        setContentPane(backgroundPanel);

        // Inner panel to hold buttons
        JPanel buttonPanel = new JPanel(null); // Absolute layout for positioning buttons
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(400, 200));
      

        // Buttons
        billButton.setBounds(100, 50, 90, 30);
        reportButton.setBounds(200, 50, 90, 30);
        buttonPanel.add(billButton);
        buttonPanel.add(reportButton);

        // Add inner panel to center
        backgroundPanel.add(buttonPanel);

        // Add action listeners
        billButton.addActionListener(this);
        reportButton.addActionListener(this);
        

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (source == billButton) {
            new BillFrame();
            this.setVisible(false);
        } else if (source == reportButton) {
            new ReportFrame();
            this.setVisible(false);
        }
    }
}


// Billing Window
class BillFrame extends JFrame implements ActionListener {
    JTextField totalField;
    JButton[] foodButtons = new JButton[10];
    String[] foodItems = {"Tea", "Coffee", "Juice", "Burger", "Pizza", "Fries", "Pasta", "Noodles", "Sandwich", "Soup"};
    double[] foodPrices = {20.0, 30.0, 25.0, 50.0, 100.0, 60.0, 80.0, 40.0, 30.0, 45.0};
    DefaultTableModel model;
    JTable table;
    JLabel billNoLabel;

    int billNo;


    public BillFrame() {
        setTitle("Billing");
        setSize(650, 500);
        setLayout(null);
        getContentPane().setBackground(Color.white);
        
         
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/crave_logo.jpeg"));
        Image bgImage = bgIcon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
        setContentPane(bgPanel);
        bgPanel.setLayout(null); // set layout on bgPanel, not on JFrame



        
      
        String[] columnNames = {"Food Item", "Quantity", "Price", "Total"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 20, 530, 150);
        bgPanel.add(scrollPane);

        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setBounds(350, 180, 50, 30);
        totalField = new JTextField();
        totalField.setBounds(400, 180, 180, 30);
        totalField.setEditable(false);
        bgPanel.add(totalLabel);
         bgPanel.add(totalField);

        model.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 1) {
                updateTotalForRow(row);
                calculateTotal();
            }
        });

        // Food Buttons 1–10 with quantity prompt
        for (int i = 0; i < 10; i++) {
            foodButtons[i] = new JButton(String.valueOf(i + 1));
            foodButtons[i].setBounds(50 + (i % 5) * 110, 230 + (i / 5) * 50, 100, 30);
            foodButtons[i].addActionListener(this);
           bgPanel.add(foodButtons[i]);
        }

        JButton okBtn = new JButton("OK");
        okBtn.setBounds(180, 370, 100, 30);
        okBtn.addActionListener(this);
       bgPanel.add(okBtn);

        JButton closeBtn = new JButton("CANCEL");
        closeBtn.setBounds(320, 370, 100, 30);
        closeBtn.addActionListener(this);
        bgPanel.add(closeBtn);
        
        billNo = generateBillNo();
        billNoLabel = new JLabel("Bill No: " + billNo);
        billNoLabel.setBounds(50, 180, 200, 30);  // Adjust position as needed
         bgPanel.add(billNoLabel);
        




        setLocationRelativeTo(null);
        setVisible(true);
        

    }
     

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

        // Food button clicked
        for (int i = 0; i < 10; i++) {
            if (src == foodButtons[i]) {
                String itemName = foodItems[i];
                double price = foodPrices[i];

                String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity for " + itemName + ":");
                if (qtyStr != null && qtyStr.matches("\\d+")) {
                    int quantity = Integer.parseInt(qtyStr);
                    double total = quantity * price;
                    model.addRow(new String[]{itemName, String.valueOf(quantity), String.valueOf(price), String.valueOf(total)});
                    calculateTotal();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.");
                }
                return;
            }
        }

        if (src.getText().equals("OK")) {
    try {
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/hotel1", "shivani", "s");
        PreparedStatement stmt = con.prepareStatement("INSERT INTO bill_data (item_name, quantity, price, total_price) VALUES (?, ?, ?, ?)");

        for (int i = 0; i < model.getRowCount(); i++) {
            String itemName = (String) model.getValueAt(i, 0);
            int quantity = Integer.parseInt((String) model.getValueAt(i, 1));
            double price = Double.parseDouble((String) model.getValueAt(i, 2));
            double totalPrice = quantity * price;

            stmt.setString(1, itemName);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setDouble(4, totalPrice);
            stmt.addBatch();
        }

        stmt.executeBatch();
        con.close();
        JOptionPane.showMessageDialog(this, "Stored in database!");
        
        // ✅ Fix: Go to MainFrame instead of login screen
        new MainFrame(); 
        dispose();
        
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

        

        // CANCEL button
        if (src.getText().equals("CANCEL")) {
            model.setRowCount(0);
            HotelManagementSystem.this.setVisible(true);
            dispose();
        }
    }

    private void updateTotalForRow(int rowIndex) {
        try {
            int quantity = Integer.parseInt((String) model.getValueAt(rowIndex, 1));
            double price = Double.parseDouble((String) model.getValueAt(rowIndex, 2));
            double total = quantity * price;
            model.setValueAt(String.valueOf(total), rowIndex, 3);
        } catch (Exception ignored) {}
    }

    private void calculateTotal() {
        double grandTotal = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            double total = Double.parseDouble((String) model.getValueAt(i, 3));
            grandTotal += total;
        }
        totalField.setText(String.format("%.2f", grandTotal));
    }
    private int generateBillNo() {
int newBillNo = 1;
try {
    Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/hotel1", "shivani", "s");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT MAX(bill_no) FROM bill_data");
    if (rs.next()) {
        newBillNo = rs.getInt(1) + 1;
    }
    con.close();
} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error generating bill number: " + e.getMessage());
}
return newBillNo;


}
    public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image image) {
        this.backgroundImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image scaled to fill the panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
    



}
public class ReportFrame extends JFrame {
    JTable table;
    JLabel totalLabel;
    JButton backButton;

    // Constructor to show all bills
    public ReportFrame() {
        initUI();
        loadAllBills();
    }

    // Constructor to show a specific bill
    public ReportFrame(int billNo) {
        initUI();
        loadBillData(billNo);
    }

    // Initialize the UI layout and background
    private void initUI() {
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load background image
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/images/crave_logo.jpeg"));
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundIcon.getImage());
        backgroundPanel.setLayout(new BorderLayout());

        // Table setup
        String[] columnNames = {"Item Name", "Quantity", "Price", "Total Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Total label
        totalLabel = new JLabel("Grand Total: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.WHITE); // White text for contrast

        // Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            new MainFrame(); // Go back to main screen
            dispose();
        });

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false); // Let background show through
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(backButton, BorderLayout.EAST);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(backgroundPanel);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    // Load all bills from database
    private void loadAllBills() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/hotel1", "shivani", "s");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bill_data");

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            double grandTotal = 0.0;

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double totalPrice = rs.getDouble("total_price");
                grandTotal += totalPrice;

                Object[] row = {itemName, quantity, price, totalPrice};
                model.addRow(row);
            }

            totalLabel.setText("Grand Total: ₹" + grandTotal);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load a specific bill from database
    private void loadBillData(int billNo) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/hotel1", "shivani", "s");
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM bill_data WHERE bill_no = ?");
            stmt.setInt(1, billNo);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            double grandTotal = 0.0;

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double totalPrice = rs.getDouble("total_price");
                grandTotal += totalPrice;

                Object[] row = {itemName, quantity, price, totalPrice};
                model.addRow(row);
            }
    

            totalLabel.setText("Grand Total: ₹" + grandTotal);
            totalLabel.setForeground(Color.BLACK); // Dark text
            totalLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Bold and larger

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Custom panel with background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
public static void main(String[] args) {
        new HotelManagementSystem();
    }
}