package Vistas;

import Modelo.Cart;
import Modelo.Moneda;
import ModeloDAO.FacturaDAO;
import ModeloDAO.ProductoDAO;
import ModeloDTO.BoletaDTO;
import ModeloDTO.ClienteDTO;
import ModeloDTO.FacturaDTO;
import ModeloDTO.ItemCarritoDTO;
import ModeloDTO.ProductoDTO;
import ModeloDTO.VentaDTO;
import reportes.BoletaPDFGenerator;
import reportes.FacturaPDFGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoletaForm extends JFrame {
    private final ClienteDTO cliente;
    private final SimpleDateFormat fFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat fHora = new SimpleDateFormat("HH:mm:ss");

    private JLabel lblClienteValor, lblNumValor, lblFechaValor, lblHoraValor, lblTotal;
    private JComboBox<String> cmbTipo, cmbMedioPago;
    private JTable tblDetalle;
    private JScrollPane scroll;
    private JButton btnRegistrar, btnVolver;

    public BoletaForm(ClienteDTO cliente) {
        this.cliente = cliente;
        initComponents();
        cargarCarrito();
    }

    private void cargarCarrito() {
        lblClienteValor.setText(cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : "Cliente no identificado");
        lblNumValor.setText("Pendiente");
        lblFechaValor.setText(fFecha.format(new Date()));
        lblHoraValor.setText(fHora.format(new Date()));
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Código", "Producto", "P. Unit.", "Cantidad", "Subtotal"}) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (ItemCarritoDTO item : Cart.getItems()) {
            ProductoDTO p = item.getProducto();
            model.addRow(new Object[]{p.getIdProducto(), p.getNombre(), Moneda.formatear(p.getPrecio()), item.getCantidad(), Moneda.formatear(item.getSubtotal())});
        }
        tblDetalle.setModel(model);
        lblTotal.setText("Total: " + Moneda.formatear(Cart.getTotal()));
    }

    private void registrarComprobante() {
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el cliente logueado.");
            return;
        }

        if (Cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos para registrar.");
            return;
        }

        String tipoDocumento = cmbTipo.getSelectedItem().toString();
        String medioPago = cmbMedioPago.getSelectedItem().toString();

        String serie = tipoDocumento.equals("Boleta") ? "B001-" : "F001-";
        String numeroDocumento = serie + System.currentTimeMillis();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tipo de comprobante: " + tipoDocumento
                        + "\nMedio de pago: " + medioPago
                        + "\nTotal: " + Moneda.formatear(Cart.getTotal())
                        + "\n\n¿Desea registrar la compra?",
                "Confirmar comprobante",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            FacturaDAO facturaDAO = new FacturaDAO();
            ProductoDAO productoDAO = new ProductoDAO();

            String idVenta = facturaDAO.registrarVenta(
                    cliente.getIdCliente(),
                    cliente.getNombre() + " " + cliente.getApellido(),
                    tipoDocumento,
                    numeroDocumento,
                    medioPago
            );

            for (ItemCarritoDTO item : Cart.getItems()) {
                facturaDAO.registrarDetalle(
                        idVenta,
                        item.getProducto().getIdProducto(),
                        item.getCantidad(),
                        item.getProducto().getPrecio(),
                        item.getSubtotal()
                );

                productoDAO.actualizarStock(
                        item.getProducto().getIdProducto(),
                        item.getCantidad()
                );
            }

            lblNumValor.setText(numeroDocumento);
            lblFechaValor.setText(fFecha.format(new Date()));
            lblHoraValor.setText(fHora.format(new Date()));

            JOptionPane.showMessageDialog(
                    this,
                    tipoDocumento + " registrada correctamente."
            );

            generarPDF(tipoDocumento, numeroDocumento);

            Cart.clear();

            dispose();
            new Menu(cliente).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar comprobante: " + e.getMessage()
            );
        }
    }

    private void generarPDF(String tipoDocumento, String numeroDocumento) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar " + tipoDocumento + " en PDF");
        fileChooser.setSelectedFile(new File(tipoDocumento + "_" + numeroDocumento + ".pdf"));

        int opcion = fileChooser.showSaveDialog(this);

        if (opcion != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this, "La compra fue registrada, pero no se guardó el PDF.");
            return;
        }

        String ruta = fileChooser.getSelectedFile().getAbsolutePath();

        if (!ruta.toLowerCase().endsWith(".pdf")) {
            ruta += ".pdf";
        }

        try {
            List<VentaDTO> ventas = convertirCarritoAVentas();
            Date fechaActual = new Date();

            if (tipoDocumento.equals("Boleta")) {
                BoletaDTO boletaDTO = new BoletaDTO(
                        numeroDocumento,
                        fechaActual,
                        cliente,
                        ventas
                );

                BoletaPDFGenerator.generarPDF(boletaDTO, ruta);
            } else {
                FacturaDTO facturaDTO = new FacturaDTO(
                        numeroDocumento,
                        fechaActual,
                        cliente,
                        ventas
                );

                FacturaPDFGenerator.generarPDF(facturaDTO, ruta);
            }

            JOptionPane.showMessageDialog(this, "PDF generado correctamente:\n" + ruta);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "La compra fue registrada, pero ocurrió un error al generar el PDF:\n" + e.getMessage());
        }
    }

    private List<VentaDTO> convertirCarritoAVentas() {
        List<VentaDTO> ventas = new ArrayList<>();

        for (ItemCarritoDTO item : Cart.getItems()) {
            VentaDTO venta = new VentaDTO();

            venta.setProductoId(item.getProducto().getIdProducto());
            venta.setProducto(item.getProducto());
            venta.setCantidad(item.getCantidad());
            venta.setPrecioUnitario(item.getProducto().getPrecio());

            ventas.add(venta);
        }

        return ventas;
    }

    private void volver() {
        dispose();
        new CarritoForm(cliente).setVisible(true);
    }


    private void initComponents() {
        VistaTheme.prepararFrame(this, "Boleta y factura", 980, 650);
        JPanel root = VistaTheme.fondo();
        root.setLayout(new BorderLayout(18, 18));

        JPanel header = VistaTheme.card();
        header.setLayout(new BorderLayout(15, 5));
        JPanel textos = new JPanel(new GridLayout(2,1));
        textos.setBackground(Color.WHITE);
        textos.add(VistaTheme.titulo("Generar comprobante"));
        textos.add(VistaTheme.subtitulo("Selecciona el tipo de documento y medio de pago"));
        header.add(textos, BorderLayout.CENTER);
        lblTotal = VistaTheme.titulo("Total: S/ 0.00");
        header.add(lblTotal, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JPanel datos = VistaTheme.card();
        datos.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 10, 7, 10);
        g.anchor = GridBagConstraints.WEST;
        cmbTipo = VistaTheme.combo(new String[]{"Boleta", "Factura"});
        cmbMedioPago = VistaTheme.combo(new String[]{"Efectivo", "Yape", "Plin", "Tarjeta", "Transferencia"});
        lblClienteValor = VistaTheme.etiqueta("—");
        lblNumValor = VistaTheme.etiqueta("Pendiente");
        lblFechaValor = VistaTheme.titulo("--/--/----");
        lblHoraValor = VistaTheme.titulo("--:--:--");
        addDato(datos, g, 0, 0, "Tipo de comprobante", cmbTipo);
        addDato(datos, g, 1, 0, "Medio de pago", cmbMedioPago);
        addDato(datos, g, 0, 1, "Cliente", lblClienteValor);
        addDato(datos, g, 1, 1, "Número", lblNumValor);
        addDato(datos, g, 0, 2, "Fecha", lblFechaValor);
        addDato(datos, g, 1, 2, "Hora", lblHoraValor);
        root.add(datos, BorderLayout.WEST);

        tblDetalle = VistaTheme.tabla();
        scroll = VistaTheme.scroll(tblDetalle);
        root.add(scroll, BorderLayout.CENTER);

        JPanel acciones = VistaTheme.card();
        acciones.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnVolver = VistaTheme.botonSecundario("Volver");
        btnRegistrar = VistaTheme.boton("Registrar y guardar PDF");
        acciones.add(btnVolver);
        acciones.add(btnRegistrar);
        root.add(acciones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarComprobante());
        btnVolver.addActionListener(e -> volver());
        setContentPane(root);
    }

    private void addDato(JPanel p, GridBagConstraints g, int x, int y, String label, JComponent comp) {
        g.gridx = x * 2;
        g.gridy = y;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        p.add(VistaTheme.etiqueta(label + ":"), g);
        g.gridx = x * 2 + 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        p.add(comp, g);
    }
}
