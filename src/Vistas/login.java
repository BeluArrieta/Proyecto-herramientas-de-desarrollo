package Vistas;

import Config.Conexion;
import ModeloDTO.ClienteDTO;
import Vistas_administrativas.MenuAdmin;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class login extends JFrame {
    private static final int MAX_INTENTOS = 3;
    private static final long BLOQUEO_MS = 15000;
    private final Map<String, Integer> intentosFallidos = new HashMap<>();
    private final Map<String, Long> finBloqueo = new HashMap<>();

    private JTextField Usuario;
    private JPasswordField Contraseña;
    private JButton btnLogin, btnRegistro;

    public login() {
        initComponents();
    }

    private void initComponents() {
        VistaTheme.prepararFrame(this, "Iniciar sesión", 880, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel root = VistaTheme.fondo();
        root.setLayout(new GridBagLayout());

        JPanel card = VistaTheme.card();
        card.setPreferredSize(new Dimension(760, 420));
        card.setLayout(new GridLayout(1, 2, 0, 0));

        JPanel lado = new JPanel(new GridBagLayout());
        lado.setBackground(new Color(255, 230, 242));
        lado.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints l = new GridBagConstraints();
        l.gridx = 0; l.gridy = 0; l.insets = new Insets(5,5,20,5);
        lado.add(VistaTheme.logo(), l);
        l.gridy++;
        JLabel bien = VistaTheme.titulo("Bienvenido");
        lado.add(bien, l);
        l.gridy++;
        JLabel frase = VistaTheme.subtitulo("Sistema de ventas y comprobantes");
        lado.add(frase, l);
        l.gridy++;
        JLabel deco = new JLabel("Compra fácil, rápido y ordenado");
        deco.setFont(VistaTheme.bold(15));
        deco.setForeground(VistaTheme.MORADO);
        lado.add(deco, l);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(35, 42, 35, 42));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.anchor = GridBagConstraints.WEST; g.insets = new Insets(0,0,8,0);
        form.add(VistaTheme.titulo("Iniciar sesión"), g);
        g.gridy++; g.insets = new Insets(0,0,25,0);
        form.add(VistaTheme.subtitulo("Ingresa tu usuario y contraseña"), g);

        Usuario = VistaTheme.campo();
        Contraseña = VistaTheme.password();
        btnLogin = VistaTheme.boton("Ingresar");
        btnRegistro = VistaTheme.botonSecundario("Crear cuenta");

        g.gridwidth = 1;
        g.fill = GridBagConstraints.NONE;
        g.weightx = 0;
        g.gridx = 0;
        g.gridy++;
        g.insets = new Insets(8, 0, 6, 18);
        form.add(VistaTheme.etiqueta("Usuario"), g);

        g.gridx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(8, 0, 6, 0);
        form.add(Usuario, g);

        g.gridx = 0;
        g.gridy++;
        g.fill = GridBagConstraints.NONE;
        g.weightx = 0;
        g.insets = new Insets(8, 0, 6, 18);
        form.add(VistaTheme.etiqueta("Contraseña"), g);

        g.gridx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(8, 0, 6, 0);
        form.add(Contraseña, g);

        g.gridx = 0;
        g.gridy++;
        g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(28, 0, 0, 0);
        form.add(btnLogin, g);

        g.gridy++;
        g.insets = new Insets(12, 0, 0, 0);
        form.add(btnRegistro, g);

        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistro.addActionListener(e -> { new Register().setVisible(true); dispose(); });
        Contraseña.addActionListener(e -> iniciarSesion());

        card.add(lado); card.add(form); root.add(card);
        setContentPane(root);
    }

    private void limpiarCampos() {
        Usuario.setText("");
        Contraseña.setText("");
        Usuario.requestFocus();
    }

    private void iniciarSesion() {
        String usuario = Usuario.getText().trim();
        String contraseña = new String(Contraseña.getPassword()).trim();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese su usuario y contraseña", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long bloqueoHasta = finBloqueo.get(usuario);
        long ahora = System.currentTimeMillis();

        if (bloqueoHasta != null && ahora < bloqueoHasta) {
            long segRestantes = (bloqueoHasta - ahora) / 1000;
            JOptionPane.showMessageDialog(this, "Usuario bloqueado. Intente de nuevo en " + segRestantes + " s.", "Bloqueo temporal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = """
            SELECT
                p.id_persona,
                p.nombre,
                p.apellido,
                p.correo,
                p.telefono,
                CASE
                    WHEN c.id_cliente IS NULL THEN 'ADMIN'
                    ELSE 'CLIENTE'
                END AS tipo_usuario
            FROM usuario u
            INNER JOIN persona p ON u.id_persona = p.id_persona
            LEFT JOIN cliente c ON LOWER(TRIM(c.correo)) = LOWER(TRIM(p.correo))
            WHERE TRIM(u.usuario) = ?
            AND u.contrasena = ?
        """;

        try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                intentosFallidos.remove(usuario);
                finBloqueo.remove(usuario);

                String tipoUsuario = rs.getString("tipo_usuario");
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");

                if ("ADMIN".equals(tipoUsuario)) {
                    JOptionPane.showMessageDialog(this, "Bienvenido administrador " + rs.getString("nombre"));
                    new MenuAdmin(nombreCompleto).setVisible(true);
                    dispose();
                    return;
                }

                ClienteDTO cliente = new ClienteDTO();
                cliente.setIdCliente(rs.getString("id_persona"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setTelefono(rs.getString("telefono"));

                JOptionPane.showMessageDialog(this, "Bienvenido " + cliente.getNombre());
                new Menu(cliente).setVisible(true);
                dispose();
                return;
            }

            intentosFallidos.merge(usuario, 1, Integer::sum);
            int intentos = intentosFallidos.get(usuario);

            if (intentos >= MAX_INTENTOS) {
                finBloqueo.put(usuario, ahora + BLOQUEO_MS);
                intentosFallidos.put(usuario, 0);
                JOptionPane.showMessageDialog(this, "Usuario bloqueado por 15 segundos");
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }

            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new login().setVisible(true));
    }
}
