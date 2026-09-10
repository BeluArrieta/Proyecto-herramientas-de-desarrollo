package Vistas_administrativas;

import Config.Conexion;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HistorialGeneralAdmin extends JFrame {
    private JTable tabla;
    private JLabel lblResumen;
    private JTextField txtBuscar;

    public HistorialGeneralAdmin() {
        initComponents();
        cargarHistorial("");
    }

    private void initComponents() {
        AdminTheme.prepararFrame(this, "Historial total de clientes", 1100, 650);
        JPanel root = AdminTheme.fondo();
        root.setLayout(new BorderLayout(18, 18));

        JPanel header = AdminTheme.card();
        header.setLayout(new BorderLayout(18, 8));
        JPanel textos = new JPanel(new GridLayout(2,1));
        textos.setBackground(Color.WHITE);
        textos.add(AdminTheme.titulo("Historial total de compras"));
        textos.add(AdminTheme.subtitulo("Ventas registradas de todos los clientes"));
        header.add(textos, BorderLayout.CENTER);
        lblResumen = AdminTheme.etiqueta("Registros: 0 | Total: S/ 0.00");
        header.add(lblResumen, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        tabla = AdminTheme.tabla();
        root.add(AdminTheme.scroll(tabla), BorderLayout.CENTER);

        JPanel acciones = AdminTheme.card();
        acciones.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        txtBuscar = AdminTheme.campo();
        txtBuscar.setToolTipText("Buscar por cliente, documento o producto");
        JButton buscar = AdminTheme.botonAzul("Buscar");
        JButton limpiar = AdminTheme.boton("Limpiar");
        JButton cerrar = AdminTheme.botonRojo("Cerrar");
        acciones.add(AdminTheme.etiqueta("Filtro:"));
        acciones.add(txtBuscar);
        acciones.add(buscar);
        acciones.add(limpiar);
        acciones.add(cerrar);
        root.add(acciones, BorderLayout.SOUTH);

        buscar.addActionListener(e -> cargarHistorial(txtBuscar.getText().trim()));
        limpiar.addActionListener(e -> { txtBuscar.setText(""); cargarHistorial(""); });
        cerrar.addActionListener(e -> dispose());
        setContentPane(root);
    }

    private void cargarHistorial(String filtro) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Venta", "Fecha", "Cliente", "Documento", "Medio pago", "Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        String sql = """
            SELECT
                id_venta,
                fecha_emision,
                cliente || ' ' || apellido AS cliente,
                tipo_documento AS documento,
                medio_pago,
                producto,
                cantidad,
                precio_unitario,
                subtotal
            FROM historial_compras
            WHERE (? = ''
                OR LOWER(cliente || ' ' || apellido) LIKE LOWER(?)
                OR LOWER(producto) LIKE LOWER(?)
                OR LOWER(tipo_documento) LIKE LOWER(?)
                OR LOWER(medio_pago) LIKE LOWER(?)
                OR LOWER(id_venta) LIKE LOWER(?))
            ORDER BY fecha_emision DESC
        """;
        double total = 0;
        int registros = 0;
        try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + filtro + "%";
            ps.setString(1, filtro);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);
            ps.setString(6, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                registros++;
                total += rs.getDouble("subtotal");
                model.addRow(new Object[]{rs.getString("id_venta"), rs.getTimestamp("fecha_emision"), rs.getString("cliente"), rs.getString("documento"), rs.getString("medio_pago"), rs.getString("producto"), rs.getInt("cantidad"), String.format("S/ %.2f", rs.getDouble("precio_unitario")), String.format("S/ %.2f", rs.getDouble("subtotal"))});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial general: " + e.getMessage());
        }
        tabla.setModel(model);
        lblResumen.setText("Registros: " + registros + " | Total: " + String.format("S/ %.2f", total));
    }
}
