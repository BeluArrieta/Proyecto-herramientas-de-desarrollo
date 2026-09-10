package Vistas;

import Modelo.Cart;
import ModeloDTO.ClienteDTO;
import ModeloDTO.ItemCarritoDTO;
import ModeloDTO.ProductoDTO;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarritoForm extends JFrame {
    private ClienteDTO clienteLogueado;
    private JButton btnEliminar, btnPagar, btnVolver;
    private JLabel jLabelTotal;
    private JTable jTable1;

    public CarritoForm(ClienteDTO cliente) {
        this.clienteLogueado = cliente;
        initComponents();
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { volverAlCatalogo(); } });
        actualizarTabla();
    }

    private void initComponents() {
        VistaTheme.prepararFrame(this, "Carrito de Compras - " + (clienteLogueado != null ? clienteLogueado.getNombre() : "Invitado"), 980, 640);
        JPanel root = VistaTheme.fondo();
        root.setLayout(new BorderLayout(18, 18));

        JPanel header = VistaTheme.card(); header.setLayout(new BorderLayout());
        JPanel txt = new JPanel(new GridLayout(2,1)); txt.setBackground(Color.WHITE);
        txt.add(VistaTheme.titulo("Tu carrito de compras"));
        txt.add(VistaTheme.subtitulo("Revisa tus productos antes de generar el comprobante"));
        header.add(txt, BorderLayout.CENTER);
        jLabelTotal = VistaTheme.titulo("Total: S/ 0.00");
        header.add(jLabelTotal, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        jTable1 = VistaTheme.tabla();
        jTable1.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID Producto", "Nombre", "P. Unitario", "Cantidad", "Subtotal"}));
        root.add(VistaTheme.scroll(jTable1), BorderLayout.CENTER);

        JPanel acciones = VistaTheme.card(); acciones.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnEliminar = VistaTheme.botonPeligro("Eliminar seleccionado");
        btnPagar = VistaTheme.boton("Realizar compra");
        btnVolver = VistaTheme.botonSecundario("Volver al catálogo");
        acciones.add(btnEliminar); acciones.add(btnPagar); acciones.add(btnVolver);
        root.add(acciones, BorderLayout.SOUTH);

        btnEliminar.addActionListener(e -> eliminarProducto());
        btnPagar.addActionListener(e -> realizarCompra());
        btnVolver.addActionListener(e -> volverAlCatalogo());
        setContentPane(root);
    }

    private void actualizarTabla() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Producto", "Nombre", "P. Unitario", "Cantidad", "Subtotal"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        List<ItemCarritoDTO> itemsCarrito = Cart.getItems();
        for (ItemCarritoDTO item : itemsCarrito) {
            ProductoDTO p = item.getProducto();
            model.addRow(new Object[]{p.getIdProducto(), p.getNombre(), String.format("S/ %.2f", p.getPrecio()), item.getCantidad(), String.format("S/ %.2f", item.getSubtotal())});
        }
        jTable1.setModel(model);
        jLabelTotal.setText("Total: " + String.format("S/ %.2f", Cart.getTotal()));
    }

    private void eliminarProducto() {
        int fila = jTable1.getSelectedRow();
        if (fila != -1) {
            Cart.removeProducto(jTable1.convertRowIndexToModel(fila));
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
        }
    }

    private void realizarCompra() {
        if (Cart.isEmpty()) { JOptionPane.showMessageDialog(this, "El carrito está vacío."); return; }
        irBoletaFactura();
    }

    private void volverAlCatalogo() { new CatalogoForm(clienteLogueado).setVisible(true); dispose(); }
    private void irBoletaFactura() { new BoletaForm(clienteLogueado).setVisible(true); dispose(); }
}
