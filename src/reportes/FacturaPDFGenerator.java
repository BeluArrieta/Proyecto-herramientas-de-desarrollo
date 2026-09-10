/**
 * @author Chamorro Baldera Jose Luis
 */
package reportes;

import ModeloDTO.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class FacturaPDFGenerator {

    private static final Font TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 11);

    public static void generarPDF(FacturaDTO factura, String rutaDestino) throws Exception {

        if (factura == null) {
            throw new IllegalArgumentException("Factura nula.");
        }

        Document doc = new Document(PageSize.A4, 40, 40, 50, 30);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaDestino));
        doc.open();

        // -----------------------------------------------------------
        // Aquí puedes agregar el LOGO de la empresa Softfkes (en PNG)
        // -----------------------------------------------------------

        PdfPTable encabezado = new PdfPTable(2);
        encabezado.setWidthPercentage(100);
        encabezado.setWidths(new float[]{70, 30});

        // Columna izquierda: datos de la empresa
        PdfPCell empresa = new PdfPCell();
        empresa.setBorder(Rectangle.NO_BORDER);
        empresa.addElement(new Paragraph("Group Softflex S.R.L.", TITLE));
        empresa.addElement(new Paragraph("Soluciones Tecnológicas Integrales", NORMAL));
        empresa.addElement(new Paragraph("Av. Los Ingenieros 123 - Lima, Perú", NORMAL));
        empresa.addElement(new Paragraph("Tel: +51 901-917-875", NORMAL));
        empresa.addElement(new Paragraph("Email: contacto@softflex.com", NORMAL));
        encabezado.addCell(empresa);

        // Columna derecha: datos de la factura
        PdfPCell datosFactura = new PdfPCell();
        datosFactura.setBorder(Rectangle.BOX);
        datosFactura.setPadding(8);
        datosFactura.addElement(new Paragraph("R.U.C. 20604567891", HEADER));
        datosFactura.addElement(new Paragraph("FACTURA", TITLE));
        datosFactura.addElement(new Paragraph("N° 0001 - " + factura.getIdFactura(), NORMAL));
        encabezado.addCell(datosFactura);

        doc.add(encabezado);
        doc.add(Chunk.NEWLINE);

        // Fecha y cliente
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        doc.add(new Paragraph("Fecha de emisión: " + sdf.format(factura.getFechaEmision()), NORMAL));

        ClienteDTO c = factura.getCliente();
        if (c != null) {
            doc.add(new Paragraph("Cliente: " + c.getNombre() + " " + c.getApellido(), NORMAL));
            doc.add(new Paragraph("Teléfono: " + c.getTelefono(), NORMAL));
        } else {
            doc.add(new Paragraph("Cliente: [NO ASIGNADO]", NORMAL));
        }
        doc.add(Chunk.NEWLINE);

        // Tabla de productos
        PdfPTable tbl = new PdfPTable(4);
        tbl.setWidthPercentage(100);
        tbl.setWidths(new float[]{40, 20, 20, 20});
        addHeader(tbl, "Descripción");
        addHeader(tbl, "Cantidad");
        addHeader(tbl, "P. Unitario");
        addHeader(tbl, "Subtotal");

        for (VentaDTO v : factura.getVentas()) {
            ProductoDTO p = v.getProducto();
            tbl.addCell(new Phrase(p.getNombre(), NORMAL));
            tbl.addCell(new Phrase(String.valueOf(v.getCantidad()), NORMAL));
            tbl.addCell(new Phrase(String.format("S/ %.2f", v.getPrecioUnitario()), NORMAL));
            tbl.addCell(new Phrase(String.format("S/ %.2f", v.getCantidad() * v.getPrecioUnitario()), NORMAL));
        }

        doc.add(tbl);
        doc.add(Chunk.NEWLINE);

        // Totales
        PdfPTable totales = new PdfPTable(2);
        totales.setWidthPercentage(40);
        totales.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totales.addCell(celdaSinBorde("SUBTOTAL", HEADER));
        totales.addCell(celdaSinBorde(String.format("S/ %.2f", factura.getTotal() * 0.82), NORMAL));
        totales.addCell(celdaSinBorde("IGV (18%)", HEADER));
        totales.addCell(celdaSinBorde(String.format("S/ %.2f", factura.getTotal() * 0.18), NORMAL));
        totales.addCell(celdaSinBorde("TOTAL", HEADER));
        totales.addCell(celdaSinBorde(String.format("S/ %.2f", factura.getTotal()), HEADER));

        doc.add(totales);
        doc.add(Chunk.NEWLINE);

        doc.add(new Paragraph("Recibí conforme: _______________________", NORMAL));
        doc.add(new Paragraph("Cancelado: ____________________________", NORMAL));

        doc.close();
    }

    private static void addHeader(PdfPTable t, String txt) {
        PdfPCell c = new PdfPCell(new Phrase(txt, HEADER));
        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c);
    }

    private static PdfPCell celdaSinBorde(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }
}
