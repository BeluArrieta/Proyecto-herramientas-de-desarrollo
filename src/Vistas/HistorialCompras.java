package Vistas;

import ModeloDAO.BoletaDAO;
import ModeloDTO.ClienteDTO;
import java.awt.*;
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

        BoletaDAO boletaDAO = new BoletaDAO();
        int c = 0;

        for (Object[] fila : boletaDAO.obtenerHistorialCompras(cliente.getIdCliente())) {
            c++;
            model.addRow(new Object[]{
                fila[0],
                fila[1],
                fila[2],
                fila[3],
                fila[4],
                String.format("S/ %.2f", (Double) fila[5]),
                String.format("S/ %.2f", (Double) fila[6])
            });
        }

        lblTotal.setText("Total registros: " + c);
        tabla.setModel(model);
    }
}
