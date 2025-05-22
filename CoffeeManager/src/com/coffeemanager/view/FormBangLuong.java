package com.coffeemanager.view;

import com.coffeemanager.model.BangLuong;
import com.coffeemanager.model.Connect;
import com.coffeemanager.model.Employees;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;

/**
 * Lớp FormBangLuong: Quản lý giao diện và logic cho bảng lương của nhân viên.
 */
public class FormBangLuong extends javax.swing.JFrame {

    private String chucVu;
    private List<Employees> employeeList; // Danh sách nhân viên được lưu trữ

    /**
     * Constructor: Khởi tạo form bảng lương.
     */
    public FormBangLuong(String chucVu) {
        this.chucVu = chucVu;
        initComponents(); // Khởi tạo giao diện

        initializeTables(); // Tạo bảng cơ sở dữ liệu nếu chưa có
        setLocationRelativeTo(null);
        txt_chucVu.setEditable(false); // Không cho chỉnh sửa ô chức vụ
        txt_maNV.setEditable(false); // Không cho chỉnh sửa ô mã nhân viên

        // Thiết lập danh sách lựa chọn lương cơ bản
        DefaultComboBoxModel<String> luongModel = new DefaultComboBoxModel<>(new String[]{
            "20000 VND/giờ", "25000 VND/giờ", "30000 VND/giờ", "-- Nhập lương khác --"
        });
        cbboxLuong.setModel(luongModel);
        cbboxLuong.setEnabled(true);
        cbboxLuong.setVisible(true);

        // Thiết lập mô hình bảng lương
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"MaNV", "Họ Tên", "Chức vụ", "Giờ làm", "Lương cơ bản", "Thực nhận"}, 0
        ) {
            Class[] types = new Class[]{String.class, String.class, String.class, Double.class, String.class, String.class};
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        tbl_BangLuong.setModel(model);
        tbl_BangLuong.getSelectionModel().addListSelectionListener(this::tableRowSelected); // Thêm sự kiện chọn dòng

        loadData(); // Tải dữ liệu bảng lương
        loadEmployeeNames(); // Tải danh sách nhân viên vào combobox
    }

    /**
     * Tạo bảng Employees và BangLuong trong cơ sở dữ liệu nếu chưa tồn tại.
     */
    private void initializeTables() {
        // Câu lệnh SQL tạo bảng Employees
        String sqlEmployees = "CREATE TABLE IF NOT EXISTS Employees ("
                + "maNV TEXT PRIMARY KEY, "
                + "fullName TEXT, "
                + "sex TEXT, "
                + "chucVu TEXT, "
                + "taiKhoan TEXT, "
                + "matKhau TEXT, "
                + "soDienThoai TEXT, "
                + "email TEXT)";

        // Câu lệnh SQL tạo bảng BangLuong
        String sqlBangLuong = "CREATE TABLE IF NOT EXISTS BangLuong ("
                + "id TEXT PRIMARY KEY, "
                + "maNV TEXT, "
                + "fullName TEXT, "
                + "chucVu TEXT, "
                + "gioLam REAL, "
                + "baseLuong REAL, "
                + "thucNhan REAL, "
                + "FOREIGN KEY (maNV) REFERENCES Employees(maNV))";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;"); // Bật ràng buộc khóa ngoại
            stmt.execute(sqlEmployees); // Tạo bảng Employees
            stmt.execute(sqlBangLuong); // Tạo bảng BangLuong
            System.out.println("Tạo bảng Employees và BangLuong thành công.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo bảng: " + e.getMessage());
        }
    }

    /**
     * Định dạng số tiền sang dạng tiền tệ Việt Nam.
     *
     * @param amount Số tiền cần định dạng.
     * @return Chuỗi định dạng tiền tệ (ví dụ: ₫20,000).
     */
    private String formatCurrency(double amount) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));
        return formatter.format(amount);
    }

    /**
     * Kết nối tới cơ sở dữ liệu SQLite.
     *
     * @return Đối tượng Connection.
     * @throws SQLException Nếu kết nối thất bại.
     */
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:CoffeeManager.db";
        try {
            Class.forName("org.sqlite.JDBC"); // Tải driver SQLite
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy SQLite JDBC Driver: " + e.getMessage());
        }
        return DriverManager.getConnection(url);
    }

    /**
     * Tải danh sách tên nhân viên vào combobox cbx_tenNhanVien.
     */
    private void loadEmployeeNames() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Connect connect = new Connect();
        employeeList = connect.selectAllEmployees(); // Lấy tất cả nhân viên
        if (employeeList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có nhân viên nào trong cơ sở dữ liệu.");
        } else {
            for (Employees emp : employeeList) {
                model.addElement(emp.getFullName() + " (" + emp.getMaNV() + ")"); // Thêm tên và mã NV
            }
        }
        cbx_tenNhanVien.setModel(model);
        cbx_tenNhanVien.setEnabled(!employeeList.isEmpty()); // Vô hiệu hóa nếu không có nhân viên
    }

    /**
     * Tải dữ liệu bảng lương từ cơ sở dữ liệu vào bảng tbl_BangLuong.
     */
    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) tbl_BangLuong.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        String sql = "SELECT * FROM BangLuong";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("fullName");
                String chucVu = rs.getString("chucVu");
                double gioLam = rs.getDouble("gioLam");
                double baseLuong = rs.getDouble("baseLuong");
                double thucNhan = rs.getDouble("thucNhan");

                // Thêm dòng dữ liệu vào bảng
                model.addRow(new Object[]{
                    maNV,
                    hoTen,
                    chucVu,
                    gioLam,
                    formatCurrency(baseLuong),
                    formatCurrency(thucNhan)
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra mã nhân viên có tồn tại trong bảng Employees không.
     *
     * @param maNV Mã nhân viên cần kiểm tra.
     * @return true nếu tồn tại, false nếu không.
     */
    private boolean isValidEmployee(String maNV) {
        String sql = "SELECT maNV FROM Employees WHERE maNV = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Thêm bản ghi lương vào cơ sở dữ liệu.
     *
     * @param luong Đối tượng BangLuong chứa thông tin lương.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    private boolean addLuong(BangLuong luong) {
        Connect connect = new Connect();
        return connect.addLuong(luong);
    }

    /**
     * Cập nhật bản ghi lương trong cơ sở dữ liệu.
     *
     * @param luong Đối tượng BangLuong chứa thông tin cần cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    private boolean updateLuong(BangLuong luong) {
        String sql = "UPDATE BangLuong SET maNV = ?, fullName = ?, chucVu = ?, gioLam = ?, baseLuong = ?, thucNhan = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, luong.getMaNV());
            pstmt.setString(2, luong.getFullName());
            pstmt.setString(3, luong.getChucVu());
            pstmt.setDouble(4, luong.getGioLam());
            pstmt.setDouble(5, luong.getBaseLuong());
            pstmt.setDouble(6, luong.getThucNhan());
            pstmt.setString(7, luong.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật lương: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa bản ghi lương khỏi cơ sở dữ liệu.
     *
     * @param id ID của bản ghi lương cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    private boolean deleteLuong(String id) {
        String sql = "DELETE FROM BangLuong WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa lương: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy ID của bản ghi lương từ dòng được chọn trong bảng.
     *
     * @param row Chỉ số dòng được chọn.
     * @return ID của bản ghi hoặc null nếu lỗi.
     */
    private String getIdFromSelectedRow(int row) {
        String maNV = tbl_BangLuong.getModel().getValueAt(row, 0).toString();
        String sql = "SELECT id FROM BangLuong WHERE maNV = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Xử lý sự kiện khi chọn một dòng trong bảng tbl_BangLuong. Điền thông tin
     * vào cbx_tenNhanVien, txt_maNV, txt_chucVu, txt_gioLam, cbboxLuong để
     * chỉnh sửa.
     *
     * @param evt Sự kiện chọn dòng.
     */
    private void tableRowSelected(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) { // Chỉ xử lý khi việc chọn dòng hoàn tất
            int selectedRow = tbl_BangLuong.getSelectedRow();
            if (selectedRow >= 0) {
                DefaultTableModel model = (DefaultTableModel) tbl_BangLuong.getModel();
                String maNV = model.getValueAt(selectedRow, 0).toString();
                String fullName = model.getValueAt(selectedRow, 1).toString();
                String chucVu = model.getValueAt(selectedRow, 2).toString();
                double gioLam = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());
                String baseLuongStr = model.getValueAt(selectedRow, 4).toString().replaceAll("[^\\d]", "");

                // Cập nhật các ô văn bản
                txt_maNV.setText(maNV);
                txt_chucVu.setText(chucVu);
                txt_gioLam.setText(String.valueOf(gioLam));

                // Cập nhật combobox tên nhân viên
                String employeeItem = fullName + " (" + maNV + ")";
                cbx_tenNhanVien.setSelectedItem(employeeItem);

                // Cập nhật combobox lương cơ bản
                String luongItem = baseLuongStr + " VND/giờ";
                DefaultComboBoxModel<String> luongModel = (DefaultComboBoxModel<String>) cbboxLuong.getModel();
                if (luongModel.getIndexOf(luongItem) == -1) {
                    luongModel.insertElementAt(luongItem, luongModel.getSize() - 1); // Thêm lương mới nếu chưa có
                }
                cbboxLuong.setSelectedItem(luongItem);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_maNV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_gioLam = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbboxLuong = new javax.swing.JComboBox<>();
        btn_themLuong = new javax.swing.JButton();
        btn_SuaLuong = new javax.swing.JButton();
        btn_XoaLuong = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txt_chucVu = new javax.swing.JTextField();
        cbx_tenNhanVien = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_BangLuong = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel7.setText("jLabel7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Bảng lương");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 16, 234, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Mã Nhân Viên:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 22));

        txt_maNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_maNVActionPerformed(evt);
            }
        });
        getContentPane().add(txt_maNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 230, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Lương cơ bản:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 160, -1, 22));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Chức vụ:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, 22));

        txt_gioLam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_gioLamActionPerformed(evt);
            }
        });
        getContentPane().add(txt_gioLam, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 230, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Giờ làm:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, 22));

        cbboxLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbboxLuongActionPerformed(evt);
            }
        });
        getContentPane().add(cbboxLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, 230, -1));

        btn_themLuong.setText("Thêm");
        btn_themLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themLuongActionPerformed(evt);
            }
        });
        getContentPane().add(btn_themLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 100, -1));

        btn_SuaLuong.setText("Sửa");
        btn_SuaLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaLuongActionPerformed(evt);
            }
        });
        getContentPane().add(btn_SuaLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 100, -1));

        btn_XoaLuong.setText("Xóa");
        btn_XoaLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaLuongActionPerformed(evt);
            }
        });
        getContentPane().add(btn_XoaLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 100, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Tên Nhân Viên:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 22));

        txt_chucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_chucVuActionPerformed(evt);
            }
        });
        getContentPane().add(txt_chucVu, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 230, -1));

        cbx_tenNhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbx_tenNhanVien.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbx_tenNhanVienItemStateChanged(evt);
            }
        });
        cbx_tenNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_tenNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(cbx_tenNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 230, -1));

        tbl_BangLuong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã Nhân Viên", "Họ Và Tên", "Chức Vụ", "Số Giờ Làm", "Lương Cơ Bản", "Thực nhận"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbl_BangLuong);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 687, 160));

        jMenu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMenu1.setText("MENU");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenuItem1.setText("Quay lại home");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenuItem2.setText("Bảng ứng lương");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if ("Quản lý".equalsIgnoreCase(chucVu)) {
            new FormQuanLyHome(chucVu).setVisible(true);
        } else if ("Nhân viên".equalsIgnoreCase(chucVu)) {
            new FormNhanVienHome(chucVu).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Vai trò không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose(); // Đóng form hiện tại
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void txt_maNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_maNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_maNVActionPerformed

    private void txt_gioLamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_gioLamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_gioLamActionPerformed

    private void btn_themLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themLuongActionPerformed
        try {
            String maNV = txt_maNV.getText().trim();
            String selectedName = (String) cbx_tenNhanVien.getSelectedItem();
            if (selectedName == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên.");
                return;
            }
            String hoTen = selectedName.substring(0, selectedName.lastIndexOf("(")).trim();
            String chucVu = txt_chucVu.getText().trim();
            String gioText = txt_gioLam.getText().trim();
            String luongStr = cbboxLuong.getSelectedItem().toString().replace(",", "").replaceAll("[^\\d]", "");

            // Kiểm tra dữ liệu đầu vào
            if (maNV.isEmpty() || hoTen.isEmpty() || chucVu.isEmpty() || gioText.isEmpty() || luongStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            // Kiểm tra mã nhân viên
            if (!isValidEmployee(maNV)) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại trong bảng Employees.");
                return;
            }

            double gioLam = Double.parseDouble(gioText);
            double baseLuong = Double.parseDouble(luongStr);
            double thucNhan = gioLam * baseLuong;

            // Tạo đối tượng lương
            BangLuong luong = new BangLuong();
            luong.setId(java.util.UUID.randomUUID().toString());
            luong.setMaNV(maNV);
            luong.setFullName(hoTen);
            luong.setChucVu(chucVu);
            luong.setGioLam(gioLam);
            luong.setBaseLuong(baseLuong);
            luong.setThucNhan(thucNhan);

            // Thêm lương và làm mới bảng
            if (addLuong(luong)) {
                loadData();
                JOptionPane.showMessageDialog(this, "Thêm lương thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm lương thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho giờ làm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm lương: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_themLuongActionPerformed

    private void btn_SuaLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaLuongActionPerformed
        int row = tbl_BangLuong.getSelectedRow();
        if (row >= 0) {
            try {
                String maNV = txt_maNV.getText().trim();
                String selectedName = (String) cbx_tenNhanVien.getSelectedItem();
                if (selectedName == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên.");
                    return;
                }
                String hoTen = selectedName.substring(0, selectedName.lastIndexOf("(")).trim();
                String chucVu = txt_chucVu.getText().trim();
                String gioText = txt_gioLam.getText().trim();
                String luongStr = cbboxLuong.getSelectedItem().toString().replace(",", "").replaceAll("[^\\d]", "");

                // Kiểm tra dữ liệu đầu vào
                if (maNV.isEmpty() || hoTen.isEmpty() || chucVu.isEmpty() || gioText.isEmpty() || luongStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                    return;
                }

                // Kiểm tra mã nhân viên
                if (!isValidEmployee(maNV)) {
                    JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại trong bảng Employees.");
                    return;
                }

                double gioLam = Double.parseDouble(gioText);
                double baseLuong = Double.parseDouble(luongStr);
                double thucNhan = gioLam * baseLuong;

                // Lấy ID bản ghi
                String id = getIdFromSelectedRow(row);
                if (id == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy ID của bản ghi lương.");
                    return;
                }

                // Tạo đối tượng lương
                BangLuong luong = new BangLuong();
                luong.setId(id);
                luong.setMaNV(maNV);
                luong.setFullName(hoTen);
                luong.setChucVu(chucVu);
                luong.setGioLam(gioLam);
                luong.setBaseLuong(baseLuong);
                luong.setThucNhan(thucNhan);

                // Cập nhật lương và làm mới bảng
                if (updateLuong(luong)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Cập nhật lương thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật lương thất bại!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho giờ làm hoặc lương.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật lương: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để sửa.");
        }
    }//GEN-LAST:event_btn_SuaLuongActionPerformed

    private void btn_XoaLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaLuongActionPerformed
        int row = tbl_BangLuong.getSelectedRow();
        if (row >= 0) {
            String id = getIdFromSelectedRow(row);
            if (id != null && deleteLuong(id)) {
                ((DefaultTableModel) tbl_BangLuong.getModel()).removeRow(row);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để xóa.");
        }
    }//GEN-LAST:event_btn_XoaLuongActionPerformed

    private void cbboxLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbboxLuongActionPerformed
        Object selected = cbboxLuong.getSelectedItem();
        if (selected != null && selected.toString().equals("-- Nhập lương khác --")) {
            String input = JOptionPane.showInputDialog(this, "Nhập số lương cơ bản theo giờ (vd: 20000):");
            if (input != null && input.matches("\\d+")) {
                String newLuong = input + " VND/giờ";
                DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cbboxLuong.getModel();
                model.insertElementAt(newLuong, model.getSize() - 1);
                cbboxLuong.setSelectedItem(newLuong);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số.");
                cbboxLuong.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_cbboxLuongActionPerformed

    private void txt_chucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_chucVuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_chucVuActionPerformed

    private void cbx_tenNhanVienItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbx_tenNhanVienItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String selected = (String) cbx_tenNhanVien.getSelectedItem();
            if (selected != null && !selected.isEmpty() && !selected.startsWith("Item")) {
                try {
                    String maNV = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
                    for (Employees emp : employeeList) {
                        if (emp.getMaNV().equals(maNV)) {
                            txt_maNV.setText(emp.getMaNV());
                            txt_chucVu.setText(emp.getChucVu());
                            return;
                        }
                    }
                    txt_maNV.setText("");
                    txt_chucVu.setText("");
                } catch (StringIndexOutOfBoundsException e) {
                    JOptionPane.showMessageDialog(this, "Định dạng tên nhân viên không hợp lệ: " + selected);
                    txt_maNV.setText("");
                    txt_chucVu.setText("");
                }
            } else {
                txt_maNV.setText("");
                txt_chucVu.setText("");
            }
        }
    }//GEN-LAST:event_cbx_tenNhanVienItemStateChanged

    private void cbx_tenNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_tenNhanVienActionPerformed
        String selected = (String) cbx_tenNhanVien.getSelectedItem();
        if (selected != null && !selected.isEmpty() && !selected.startsWith("Item")) {
            try {
                String maNV = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
                for (Employees emp : employeeList) {
                    if (emp.getMaNV().equals(maNV)) {
                        txt_maNV.setText(emp.getMaNV());
                        txt_chucVu.setText(emp.getChucVu());
                        return;
                    }
                }
                txt_maNV.setText("");
                txt_chucVu.setText("");
            } catch (StringIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(this, "Định dạng tên nhân viên không hợp lệ: " + selected);
                txt_maNV.setText("");
                txt_chucVu.setText("");
            }
        } else {
            txt_maNV.setText("");
            txt_chucVu.setText("");
        }
    }//GEN-LAST:event_cbx_tenNhanVienActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if ("Quản lý".equalsIgnoreCase(chucVu)) {
            new FormUngLuong(chucVu).setVisible(true);
        } else if ("Nhân viên".equalsIgnoreCase(chucVu)) {
            new FormUngLuong(chucVu).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Vai trò không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose(); // Đóng form hiện tại
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_SuaLuong;
    private javax.swing.JButton btn_XoaLuong;
    private javax.swing.JButton btn_themLuong;
    private javax.swing.JComboBox<String> cbboxLuong;
    private javax.swing.JComboBox<String> cbx_tenNhanVien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tbl_BangLuong;
    private javax.swing.JTextField txt_chucVu;
    private javax.swing.JTextField txt_gioLam;
    private javax.swing.JTextField txt_maNV;
    // End of variables declaration//GEN-END:variables

}
