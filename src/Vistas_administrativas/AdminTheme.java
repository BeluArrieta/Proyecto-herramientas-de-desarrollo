package Vistas_administrativas;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class AdminTheme {
    public static final Color FONDO = new Color(241, 245, 249);
    public static final Color TARJETA = Color.WHITE;
    public static final Color PRIMARIO = new Color(30, 41, 59);
    public static final Color SECUNDARIO = new Color(51, 65, 85);
    public static final Color ACENTO = new Color(14, 165, 233);
    public static final Color VERDE = new Color(22, 163, 74);
    public static final Color ROJO = new Color(220, 38, 38);
    public static final Color TEXTO = new Color(15, 23, 42);
    public static final Color TEXTO_SUAVE = new Color(100, 116, 139);
    public static final Color BORDE = new Color(203, 213, 225);

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
        lbl.setForeground(PRIMARIO);
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
        lbl.setFont(bold(13));
        lbl.setForeground(TEXTO);
        return lbl;
    }

    public static JTextField campo() {
        JTextField c = new JTextField();
        c.setFont(texto(14));
        c.setForeground(TEXTO);
        c.setBackground(Color.WHITE);
        c.setBorder(new CompoundBorder(new LineBorder(BORDE, 1, true), new EmptyBorder(9, 12, 9, 12)));
        c.setPreferredSize(new Dimension(230, 40));
        return c;
    }

    public static JButton boton(String texto) { return boton(texto, PRIMARIO); }
    public static JButton botonAzul(String texto) { return boton(texto, ACENTO); }
    public static JButton botonVerde(String texto) { return boton(texto, VERDE); }
    public static JButton botonRojo(String texto) { return boton(texto, ROJO); }

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
        t.setGridColor(new Color(226, 232, 240));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(TEXTO);
        JTableHeader h = t.getTableHeader();
        h.setFont(bold(13));
        h.setForeground(PRIMARIO);
        h.setBackground(new Color(248, 250, 252));
        h.setPreferredSize(new Dimension(0, 38));
        return t;
    }

    public static JScrollPane scroll(JTable tabla) {
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(new LineBorder(BORDE, 1, true));
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }
}
