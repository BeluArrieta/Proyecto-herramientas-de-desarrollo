package Vistas;

import Config.Conexion;
import ModeloDTO.ClienteDTO;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HistorialCompras extends JFrame {
    private ClienteDTO cliente;
    private JTable tabla;
    private JLabel lblTotal;

    public HistorialCompras(ClienteDTO cliente) {
        this.cliente = cliente;
        initComponents();
        cargarHistorial();
    }

    private void initComponents() {
        VistaTheme.prepararFrame(this, "Historial de compras", 980, 620);
        JPanel root = VistaTheme.fondo(); root.setLayout(new BorderLayout(18, 18));
        JPanel header = VistaTheme.card(); header.setLayout(new BorderLayout(14, 5));
        JPanel textos = new JPanel(new GridLayout(2,1)); textos.setBackground(Color.WHITE);
        textos.add(VistaTheme.titulo("Historial de compras"));
        textos.add(VistaTheme.subtitulo("Cliente: " + (cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : "No identificado")));
        header.add(textos, BorderLayout.CENTER);
        lblTotal = VistaTheme.etiqueta("Total registros: 0"); header.add(lblTotal, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        tabla = VistaTheme.tabla();
        root.add(VistaTheme.scroll(tabla), BorderLayout.CENTER);
        JButton volver = VistaTheme.botonSecundario("Volver al menú");
        JPanel footer = VistaTheme.card(); footer.setLayout(new FlowLayout(FlowLayout.RIGHT)); footer.add(volver);
        volver.addActionListener(e -> { new Menu(cliente).setVisible(true); dispose(); });
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void cargarHistorial() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Venta", "Fecha", "Documento", "Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        if (cliente == null) { tabla.setModel(model); return; }
        String sql = """
            SELECT v.id_venta, v.fecha_emision, COALESCE(td.nombre, v.id_tipo_documento) documento,
                   p.nombre producto, dv.cantidad, dv.precio_unitario, dv.subtotal
            FROM venta v
            INNER JOIN detalle_venta dv ON v.id_venta = dv.id_venta
            INNER JOIN producto p ON dv.id_producto = p.id_producto
            LEFT JOIN tipo_documento td ON v.id_tipo_documento = td.id_documento
            WHERE v.id_persona = ?
            ORDER BY v.fecha_emision DESC
        """;
        try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getIdCliente());
            ResultSet rs = ps.executeQuery();
            int c = 0;
            while (rs.next()) {
                c++;
                model.addRow(new Object[]{rs.getString("id_venta"), rs.getTimestamp("fecha_emision"), rs.getString("documento"), rs.getString("producto"), rs.getInt("cantidad"), String.format("S/ %.2f", rs.getDouble("precio_unitario")), String.format("S/ %.2f", rs.getDouble("subtotal"))});
            }
            lblTotal.setText("Total registros: " + c);
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage()); }
        tabla.setModel(model);
    }
}
