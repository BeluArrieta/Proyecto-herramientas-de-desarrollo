package Vistas;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class VistaTheme {
    public static final Color FONDO = new Color(255, 242, 248);
    public static final Color FONDO_2 = new Color(250, 231, 242);
    public static final Color TARJETA = new Color(255, 255, 255);
    public static final Color ROSA = new Color(218, 39, 119);
    public static final Color ROSA_OSCURO = new Color(190, 24, 93);
    public static final Color MORADO = new Color(124, 58, 237);
    public static final Color CORAL = new Color(244, 63, 94);
    public static final Color TEXTO = new Color(34, 23, 45);
    public static final Color TEXTO_SUAVE = new Color(99, 82, 113);
    public static final Color BORDE = new Color(248, 180, 214);

    public static Font titulo(int size) { return new Font("Segoe UI", Font.BOLD, size); }
    public static Font texto(int size) { return new Font("Segoe UI", Font.PLAIN, size); }
    public static Font bold(int size) { return new Font("Segoe UI", Font.BOLD, size); }

    public static void prepararFrame(JFrame frame, String titulo, int ancho, int alto) {
        frame.setTitle(titulo);
        frame.setSize(ancho, alto);
        frame.setMinimumSize(new Dimension(ancho, alto));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(FONDO);
    }

    public static JPanel fondo() {
        JPanel p = new JPanel(new BorderLayout(18, 18));
        p.setBackground(FONDO);
        p.setBorder(new EmptyBorder(22, 28, 22, 28));
        return p;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(TARJETA);
        p.setBorder(new CompoundBorder(new LineBorder(BORDE, 1, true), new EmptyBorder(18, 18, 18, 18)));
        return p;
    }

    public static JLabel titulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(titulo(25));
        lbl.setForeground(ROSA_OSCURO);
        return lbl;
    }

    public static JLabel subtitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(texto(14));
        lbl.setForeground(TEXTO_SUAVE);
        return lbl;
    }

    public static JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(bold(14));
        lbl.setForeground(TEXTO);
        return lbl;
    }

    public static JTextField campo() {
        JTextField c = new JTextField();
        c.setFont(texto(14));
        c.setForeground(TEXTO);
        c.setBackground(Color.WHITE);
        c.setBorder(new CompoundBorder(new LineBorder(BORDE, 1, true), new EmptyBorder(9, 12, 9, 12)));
        c.setPreferredSize(new Dimension(210, 42));
        return c;
    }

    public static JPasswordField password() {
        JPasswordField c = new JPasswordField();
        c.setFont(texto(14));
        c.setForeground(TEXTO);
        c.setBackground(Color.WHITE);
        c.setBorder(new CompoundBorder(new LineBorder(BORDE, 1, true), new EmptyBorder(9, 12, 9, 12)));
        c.setPreferredSize(new Dimension(210, 42));
        return c;
    }

    public static JComboBox<String> combo(String[] valores) {
        JComboBox<String> c = new JComboBox<>(valores);
        c.setFont(texto(14));
        c.setForeground(TEXTO);
        c.setBackground(Color.WHITE);
        c.setBorder(new LineBorder(BORDE, 1, true));
        c.setPreferredSize(new Dimension(230, 42));
        return c;
    }

    public static JButton boton(String texto) { return boton(texto, ROSA); }
    public static JButton botonSecundario(String texto) { return boton(texto, MORADO); }
    public static JButton botonPeligro(String texto) { return boton(texto, CORAL); }

    public static JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(bold(13));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 18, 12, 18));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static JTable tabla() {
        JTable t = new JTable();
        t.setFont(texto(13));
        t.setRowHeight(34);
        t.setForeground(TEXTO);
        t.setBackground(Color.WHITE);
        t.setGridColor(new Color(255, 210, 229));
        t.setSelectionBackground(new Color(255, 220, 235));
        t.setSelectionForeground(TEXTO);
        JTableHeader h = t.getTableHeader();
        h.setFont(bold(13));
        h.setForeground(ROSA_OSCURO);
        h.setBackground(new Color(255, 248, 252));
        h.setPreferredSize(new Dimension(0, 38));
        return t;
    }

    public static JScrollPane scroll(JTable tabla) {
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(new LineBorder(BORDE, 1, true));
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    public static JLabel logo() {
        JLabel logo = new JLabel("GROUP\nSOFTPLEX", SwingConstants.CENTER);
        logo.setText("<html><center><b>GROUP</b><br>SOFTPLEX</center></html>");
        logo.setFont(bold(14));
        logo.setForeground(new Color(31, 41, 55));
        return logo;
    }
}
