package Vistas_administrativas;

import ModeloDAO.BoletaDAO;
import java.awt.*;
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
        double total = 0;
        int registros = 0;

        try {
            BoletaDAO boletaDAO = new BoletaDAO();

            for (Object[] fila : boletaDAO.obtenerHistorialGeneral(filtro)) {
                registros++;
                total += (Double) fila[8];
                model.addRow(new Object[]{
                    fila[0],
                    fila[1],
                    fila[2],
                    fila[3],
                    fila[4],
                    fila[5],
                    fila[6],
                    String.format("S/ %.2f", (Double) fila[7]),
                    String.format("S/ %.2f", (Double) fila[8])
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial general: " + e.getMessage());
        }
        tabla.setModel(model);
        lblResumen.setText("Registros: " + registros + " | Total: " + String.format("S/ %.2f", total));
    }
}
