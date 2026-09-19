package Vistas_administrativas;

import ModeloDAO.ClienteDAO;
import ModeloDAO.ProductoDAO;
import ModeloDAO.VentaDAO;
import Vistas.login;
import java.awt.*;
import javax.swing.*;

public class MenuAdmin extends JFrame {

    private String nombreAdmin;

    private JLabel lblTotalClientes;
    private JLabel lblProductoMasVendido;
    private JLabel lblProductosAgotados;

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private VentaDAO ventaDAO = new VentaDAO();

    private Timer timerPanelAdmin;

    public MenuAdmin() {
        this("Administrador");
    }

    public MenuAdmin(String nombreAdmin) {
        this.nombreAdmin = nombreAdmin;
        initComponents();
    }

    private void initComponents() {
        AdminTheme.prepararFrame(this, "Panel Administrativo", 960, 580);
        JPanel root = AdminTheme.fondo();
        root.setLayout(new BorderLayout(18, 18));

        JPanel header = AdminTheme.card();
        header.setLayout(new BorderLayout(18, 8));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setBackground(Color.WHITE);
        textos.add(AdminTheme.titulo("Panel administrativo"));
        textos.add(AdminTheme.subtitulo("Bienvenido, " + nombreAdmin + " | Control general del sistema"));
        header.add(textos, BorderLayout.CENTER);

        JLabel etiqueta = new JLabel("ADMIN", SwingConstants.CENTER);
        etiqueta.setFont(AdminTheme.bold(14));
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(AdminTheme.PRIMARIO);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        header.add(etiqueta, BorderLayout.EAST);

        JPanel parteSuperior = new JPanel(new BorderLayout(18, 18));
        parteSuperior.setOpaque(false);
        parteSuperior.add(header, BorderLayout.NORTH);

        JPanel contenedores = new JPanel(new GridLayout(1, 3, 18, 18));
        contenedores.setOpaque(false);

        lblTotalClientes = valor(String.valueOf(obtenerTotalClientes()), AdminTheme.bold(24), AdminTheme.PRIMARIO);
        lblProductoMasVendido = valor(obtenerProductoMasVendidoMes(), AdminTheme.bold(18), AdminTheme.ACENTO);
        lblProductosAgotados = valor(String.valueOf(obtenerProductosAgotados()), AdminTheme.bold(24), Color.RED);

        contenedores.add(contenedor("Total clientes", lblTotalClientes, "Clientes registrados en el sistema"));
        contenedores.add(contenedor("Producto más vendido", lblProductoMasVendido, "Producto con más ventas este mes"));
        contenedores.add(contenedor("Productos agotados", lblProductosAgotados, "Alerta de productos sin stock"));

        parteSuperior.add(contenedores, BorderLayout.CENTER);
        root.add(parteSuperior, BorderLayout.NORTH);

        JPanel opciones = AdminTheme.card();
        opciones.setLayout(new GridLayout(1, 2, 18, 18));

        JButton historial = opcion("Historial total", "Ver compras de todos los clientes", AdminTheme.PRIMARIO);
        JButton productos = opcion("Productos", "Agregar, actualizar precios y stock", AdminTheme.ACENTO);

        opciones.add(historial);
        opciones.add(productos);

        root.add(opciones, BorderLayout.CENTER);

        JPanel footer = AdminTheme.card();
        footer.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton cerrarSesion = AdminTheme.botonRojo("Cerrar sesión");
        footer.add(cerrarSesion);

        root.add(footer, BorderLayout.SOUTH);

        historial.addActionListener(e -> new HistorialGeneralAdmin().setVisible(true));
        productos.addActionListener(e -> new GestionProductosAdmin().setVisible(true));

        cerrarSesion.addActionListener(e -> {
            new login().setVisible(true);
            dispose();
        });

        iniciarActualizacionPanelAdmin();

        setContentPane(root);
    }

    private JLabel valor(String texto, Font fuente, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        lbl.setForeground(color);
        return lbl;
    }

    private JPanel contenedor(String titulo, JLabel lblValor, String descripcion) {
        JPanel panel = AdminTheme.card();
        panel.setLayout(new BorderLayout(8, 8));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(AdminTheme.bold(14));
        lblTitulo.setForeground(Color.DARK_GRAY);

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(AdminTheme.texto(12));
        lblDescripcion.setForeground(Color.GRAY);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        panel.add(lblDescripcion, BorderLayout.SOUTH);

        return panel;
    }

    private void iniciarActualizacionPanelAdmin() {
        timerPanelAdmin = new Timer(10000, e -> {
            lblTotalClientes.setText(String.valueOf(obtenerTotalClientes()));
            lblProductoMasVendido.setText(obtenerProductoMasVendidoMes());
            lblProductosAgotados.setText(String.valueOf(obtenerProductosAgotados()));
        });

        timerPanelAdmin.start();
    }

    private int obtenerTotalClientes() {
        return clienteDAO.obtenerTotalClientes();
    }

    private String obtenerProductoMasVendidoMes() {
        return ventaDAO.obtenerProductoMasVendidoMes();
    }

    private int obtenerProductosAgotados() {
        return productoDAO.obtenerProductosAgotados();
    }

    private JButton opcion(String titulo, String desc, Color color) {
        JButton b = AdminTheme.boton("<html><div style='text-align:left'><b style='font-size:18px'>" + titulo + "</b><br><span style='font-size:11px'>" + desc + "</span></div></html>", color);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAdmin().setVisible(true));
    }
}