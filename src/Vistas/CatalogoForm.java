package Vistas;

import Modelo.Cart;
import ModeloDAO.ProductoDAO;
import ModeloDTO.ClienteDTO;
import ModeloDTO.ProductoDTO;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CatalogoForm extends JFrame {
    private ClienteDTO clienteLogueado;
    private ArrayList<ProductoDTO> productosCatalogo = new ArrayList<>();
    private JTable jTable1;
    private JButton btnAgregarCarrito, btnVerCarrito, btnVerHistorial, btnVolverMenu;

    public CatalogoForm(ClienteDTO cliente) {
        this.clienteLogueado = cliente;
        initComponents();
        cargarProductosDesdeBD();
    }

    private void initComponents() {
        VistaTheme.prepararFrame(this, "Catálogo de Productos - " + (clienteLogueado != null ? clienteLogueado.getNombre() : "Invitado"), 980, 640);
        JPanel root = VistaTheme.fondo();
        root.setLayout(new BorderLayout(18, 18));

        JPanel header = VistaTheme.card();
        header.setLayout(new BorderLayout(15, 5));
        JPanel textos = new JPanel(new GridLayout(2,1)); textos.setBackground(Color.WHITE);
        textos.add(VistaTheme.titulo("Catálogo de productos"));
        textos.add(VistaTheme.subtitulo("Selecciona un producto y agrégalo a tu carrito"));
        header.add(textos, BorderLayout.CENTER);
        header.add(VistaTheme.logo(), BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        jTable1 = VistaTheme.tabla();
        jTable1.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Código", "Nombre", "Precio", "Stock"}));
        root.add(VistaTheme.scroll(jTable1), BorderLayout.CENTER);

        JPanel acciones = VistaTheme.card();
        acciones.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnAgregarCarrito = VistaTheme.boton("Agregar al carrito");
        btnVerCarrito = VistaTheme.botonSecundario("Ir al carrito");
        btnVerHistorial = VistaTheme.botonSecundario("Ver historial");
        btnVolverMenu = VistaTheme.botonPeligro("Volver al menú");
        acciones.add(btnAgregarCarrito); acciones.add(btnVerCarrito); acciones.add(btnVerHistorial); acciones.add(btnVolverMenu);
        root.add(acciones, BorderLayout.SOUTH);

        btnAgregarCarrito.addActionListener(e -> agregarProductoAlCarrito());
        btnVerCarrito.addActionListener(e -> abrirCarrito());
        btnVerHistorial.addActionListener(e -> abrirHistorial());
        btnVolverMenu.addActionListener(e -> volverAlMenu());
        setContentPane(root);
    }

    private void cargarProductosDesdeBD() {
        ProductoDAO productoDAO = new ProductoDAO();
        productosCatalogo = productoDAO.listarTodo();
        if (productosCatalogo == null || productosCatalogo.isEmpty()) {
            productosCatalogo = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "No se encontraron productos en la base de datos.");
        }
        llenarTablaCatalogo();
    }

    private void llenarTablaCatalogo() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Código", "Nombre", "Precio", "Stock"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (ProductoDTO p : productosCatalogo) {
            model.addRow(new Object[]{p.getIdProducto(), p.getNombre(), String.format("S/ %.2f", p.getPrecio()), p.getStock()});
        }
        jTable1.setModel(model);
    }

    private void agregarProductoAlCarrito() {
        int fila = jTable1.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        ProductoDTO producto = productosCatalogo.get(jTable1.convertRowIndexToModel(fila));
        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad para " + producto.getNombre() + ":", "1");
        if (cantidadStr == null) return;
        try {
            int cantidad = Integer.parseInt(cantidadStr.trim());
            if (cantidad <= 0) { JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida."); return; }
            if (cantidad > producto.getStock()) { JOptionPane.showMessageDialog(this, "Stock insuficiente."); return; }
            Cart.addProducto(producto, cantidad);
            JOptionPane.showMessageDialog(this, "Producto agregado al carrito.");
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Cantidad inválida."); }
    }

    private void abrirCarrito() { new CarritoForm(clienteLogueado).setVisible(true); dispose(); }
    private void abrirHistorial() { new HistorialCompras(clienteLogueado).setVisible(true); dispose(); }
    private void volverAlMenu() { new Menu(clienteLogueado).setVisible(true); dispose(); }
}
