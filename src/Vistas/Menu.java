package Vistas;

import ModeloDTO.ClienteDTO;
import java.awt.*;
import javax.swing.*;

public class Menu extends JFrame {
    private ClienteDTO clienteLogueado;

    public Menu() { this(null); }
    public Menu(ClienteDTO cliente) { this.clienteLogueado = cliente; initComponents(); }

    private void initComponents() {
        VistaTheme.prepararFrame(this, "Menú principal", 900, 560);
        JPanel root = VistaTheme.fondo();
        root.setLayout(new BorderLayout(22, 22));

        JPanel sidebar = VistaTheme.card();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BorderLayout(0, 20));
        sidebar.add(VistaTheme.logo(), BorderLayout.NORTH);
        JLabel info = new JLabel("<html><center>Panel del cliente<br><b>GROUP SOFTPLEX</b></center></html>", SwingConstants.CENTER);
        info.setFont(VistaTheme.texto(14));
        info.setForeground(VistaTheme.TEXTO_SUAVE);
        sidebar.add(info, BorderLayout.CENTER);
        root.add(sidebar, BorderLayout.WEST);

        JPanel centro = VistaTheme.card();
        centro.setLayout(new BorderLayout(18, 18));
        JPanel header = new JPanel(new GridLayout(2,1));
        header.setBackground(Color.WHITE);
        header.add(VistaTheme.titulo("Hola, " + (clienteLogueado != null ? clienteLogueado.getNombre() : "invitado")));
        header.add(VistaTheme.subtitulo("Elige una opción para continuar con tus compras"));
        centro.add(header, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(1, 3, 18, 18));
        opciones.setBackground(Color.WHITE);
        JButton btnCatalogo = cardButton("Catálogo", "Ver productos disponibles", VistaTheme.ROSA);
        JButton btnCarrito = cardButton("Carrito", "Revisar productos seleccionados", VistaTheme.MORADO);
        JButton btnHistorial = cardButton("Historial", "Ver compras realizadas", VistaTheme.CORAL);
        opciones.add(btnCatalogo);
        opciones.add(btnCarrito);
        opciones.add(btnHistorial);
        centro.add(opciones, BorderLayout.CENTER);

        JButton cerrar = VistaTheme.botonPeligro("Cerrar sesión");
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        footer.add(cerrar);
        centro.add(footer, BorderLayout.SOUTH);

        btnCatalogo.addActionListener(e -> { new CatalogoForm(clienteLogueado).setVisible(true); dispose(); });
        btnCarrito.addActionListener(e -> { new CarritoForm(clienteLogueado).setVisible(true); dispose(); });
        btnHistorial.addActionListener(e -> { new HistorialCompras(clienteLogueado).setVisible(true); dispose(); });
        cerrar.addActionListener(e -> { new login().setVisible(true); dispose(); });

        root.add(centro, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JButton cardButton(String titulo, String desc, Color color) {
        JButton b = VistaTheme.boton("<html><div style='text-align:left'><b style='font-size:14px'>" + titulo + "</b><br><span style='font-size:10px'>" + desc + "</span></div></html>", color);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setPreferredSize(new Dimension(220, 110));
        return b;
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new Menu().setVisible(true)); }
}
