package Vistas;

import Modelo.Registro;
import java.awt.*;
import javax.swing.*;

public class Register extends JFrame {
    private JTextField Nombre, Apellido, Usuario, Telefono, correo;
    private JPasswordField Contraseña;
    private JButton btnAtras, btnRegistrar;

    public Register() { initComponents(); }

    private void initComponents() {
        VistaTheme.prepararFrame(this, "Registro de cliente", 900, 650);
        JPanel root = VistaTheme.fondo();
        root.setLayout(new GridBagLayout());

        JPanel card = VistaTheme.card();
        card.setPreferredSize(new Dimension(760, 520));
        card.setLayout(new BorderLayout(25, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(VistaTheme.logo(), BorderLayout.WEST);
        JPanel titulos = new JPanel(new GridLayout(2,1));
        titulos.setBackground(Color.WHITE);
        titulos.add(VistaTheme.titulo("Crear cuenta"));
        titulos.add(VistaTheme.subtitulo("Registra tus datos para comprar productos"));
        header.add(titulos, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12); g.anchor = GridBagConstraints.WEST;

        Nombre = VistaTheme.campo(); Apellido = VistaTheme.campo(); Usuario = VistaTheme.campo(); Contraseña = VistaTheme.password(); Telefono = VistaTheme.campo(); correo = VistaTheme.campo();
        addFila(form, g, 0, "Nombre", Nombre);
        addFila(form, g, 1, "Apellido", Apellido);
        addFila(form, g, 2, "Usuario", Usuario);
        addFila(form, g, 3, "Contraseña", Contraseña);
        addFila(form, g, 4, "Teléfono", Telefono);
        addFila(form, g, 5, "Correo", correo);
        card.add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        botones.setBackground(Color.WHITE);
        btnAtras = VistaTheme.botonSecundario("Atrás");
        btnRegistrar = VistaTheme.boton("Registrar");
        botones.add(btnAtras); botones.add(btnRegistrar);
        card.add(botones, BorderLayout.SOUTH);

        btnAtras.addActionListener(e -> { new login().setVisible(true); dispose(); });
        btnRegistrar.addActionListener(e -> registrar());
        root.add(card); setContentPane(root);
    }

    private void addFila(JPanel form, GridBagConstraints g, int y, String label, JComponent comp) {
        g.gridx = 0; g.gridy = y; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        form.add(VistaTheme.etiqueta(label), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        form.add(comp, g);
    }

    private void registrar() {
        String idusuario = Usuario.getText().trim();
        String nombre = Nombre.getText().trim();
        String apellido = Apellido.getText().trim();
        String contraseña = new String(Contraseña.getPassword()).trim();
        String telefono = Telefono.getText().trim();
        String Correo = correo.getText().trim();
        if (idusuario.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios");
            return;
        }
        Registro dao = new Registro();
        boolean registrado = dao.registrarUsuario(idusuario, nombre, apellido, Correo, telefono, idusuario, contraseña);
        if (registrado) {
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");
            Usuario.setText(""); Nombre.setText(""); Apellido.setText(""); Contraseña.setText(""); Telefono.setText(""); correo.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario");
        }
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new Register().setVisible(true)); }
}
