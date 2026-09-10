package Vistas_administrativas;

import Config.Conexion;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GestionProductosAdmin extends JFrame {
    private JTable tabla;
    private JTextField txtId, txtNombre, txtPrecio, txtStock, txtCategoria;

    public GestionProductosAdmin() {
        initComponents();
        cargarProductos();
    }

    private void initComponents() {
        AdminTheme.prepararFrame(this, "Gestión de productos", 1100, 680);
        JPanel root = AdminTheme.fondo();
        root.setLayout(new BorderLayout(18, 18));

        JPanel header = AdminTheme.card();
        header.setLayout(new GridLayout(2,1));
        header.add(AdminTheme.titulo("Gestión de productos"));
        header.add(AdminTheme.subtitulo("Agrega productos, actualiza precios y modifica stock"));
        root.add(header, BorderLayout.NORTH);

        tabla = AdminTheme.tabla();
        root.add(AdminTheme.scroll(tabla), BorderLayout.CENTER);

        JPanel form = AdminTheme.card();
        form.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        txtId = AdminTheme.campo(); txtNombre = AdminTheme.campo(); txtPrecio = AdminTheme.campo(); txtStock = AdminTheme.campo(); txtCategoria = AdminTheme.campo();
        addCampo(form, g, 0, "ID", txtId);
        addCampo(form, g, 1, "Nombre", txtNombre);
        addCampo(form, g, 2, "Precio", txtPrecio);
        addCampo(form, g, 3, "Stock", txtStock);
        addCampo(form, g, 4, "Categoría", txtCategoria);

        JButton agregar = AdminTheme.botonVerde("Agregar producto");
        JButton actualizar = AdminTheme.botonAzul("Actualizar precio/stock");
        JButton limpiar = AdminTheme.boton("Limpiar");
        JButton cerrar = AdminTheme.botonRojo("Cerrar");
        g.gridy = 5; g.gridx = 0; g.gridwidth = 5; g.fill = GridBagConstraints.HORIZONTAL;
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        acciones.setBackground(Color.WHITE);
        acciones.add(agregar); acciones.add(actualizar); acciones.add(limpiar); acciones.add(cerrar);
        form.add(acciones, g);
        root.add(form, BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarProducto());
        agregar.addActionListener(e -> agregarProducto());
        actualizar.addActionListener(e -> actualizarProducto());
        limpiar.addActionListener(e -> limpiarCampos());
        cerrar.addActionListener(e -> dispose());
        setContentPane(root);
    }

    private void addCampo(JPanel p, GridBagConstraints g, int x, String label, JTextField campo) {
        g.gridx = x; g.gridy = 0; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        p.add(AdminTheme.etiqueta(label), g);
        g.gridy = 1;
        p.add(campo, g);
    }

    private void cargarProductos() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock", "Categoría"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        String sql = "SELECT id_producto, nombre, precio, stock, categoria FROM producto ORDER BY nombre";
        try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id_producto"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"), rs.getString("categoria")});
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage()); }
        tabla.setModel(model);
    }

    private void seleccionarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        int r = tabla.convertRowIndexToModel(fila);
        txtId.setText(tabla.getModel().getValueAt(r, 0).toString());
        txtNombre.setText(tabla.getModel().getValueAt(r, 1).toString());
        txtPrecio.setText(tabla.getModel().getValueAt(r, 2).toString());
        txtStock.setText(tabla.getModel().getValueAt(r, 3).toString());
        Object cat = tabla.getModel().getValueAt(r, 4);
        txtCategoria.setText(cat != null ? cat.toString() : "");
    }

    private void agregarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String categoria = txtCategoria.getText().trim();
            if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el nombre del producto."); return; }
            String sql = "INSERT INTO producto(id_producto, nombre, precio, stock, categoria) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id); ps.setString(2, nombre); ps.setDouble(3, precio); ps.setInt(4, stock); ps.setString(5, categoria);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
                limpiarCampos(); cargarProductos();
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "No se pudo agregar: " + e.getMessage()); }
    }

    private void actualizarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String categoria = txtCategoria.getText().trim();
            String sql = "UPDATE producto SET nombre = ?, precio = ?, stock = ?, categoria = ? WHERE id_producto = ?";
            try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre); ps.setDouble(2, precio); ps.setInt(3, stock); ps.setString(4, categoria); ps.setInt(5, id);
                int filas = ps.executeUpdate();
                if (filas > 0) JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                else JOptionPane.showMessageDialog(this, "No se encontró el producto.");
                limpiarCampos(); cargarProductos();
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "No se pudo actualizar: " + e.getMessage()); }
    }

    private void limpiarCampos() {
        txtId.setText(""); txtNombre.setText(""); txtPrecio.setText(""); txtStock.setText(""); txtCategoria.setText("");
        tabla.clearSelection();
    }
}
