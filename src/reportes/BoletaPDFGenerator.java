/**
 * @author Chamorro Baldera Jose Luis
 */
package reportes;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ModeloDTO.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class BoletaPDFGenerator {

    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 11);
    private static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

    public static void generarPDF(BoletaDTO boleta, String ruta) {
        Document doc = new Document(PageSize.A5, 36, 36, 54, 36); // Tamaño tipo boleta
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            // ----------------------------------------------------------
            // Aquí puedes agregar el LOGO de la empresa (en formato PNG)
            // ----------------------------------------------------------

            // ENCABEZADO EMPRESA
            Paragraph empresa = new Paragraph("EMPRESA DE TRANSPORTES SOFTFKES S.A.", FONT_BOLD);
            empresa.setAlignment(Element.ALIGN_CENTER);
            doc.add(empresa);

            Paragraph ruc = new Paragraph("R.U.C. 20604567891", FONT_NORMAL);
            ruc.setAlignment(Element.ALIGN_CENTER);
            doc.add(ruc);

            Paragraph dir = new Paragraph("AV. LOS INGENIEROS 456 - LIMA, PERÚ", FONT_NORMAL);
            dir.setAlignment(Element.ALIGN_CENTER);
            doc.add(dir);

            Paragraph titulo = new Paragraph("BOLETA ELECTRÓNICA", FONT_TITLE);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            doc.add(new Paragraph(boleta.getNumeroDocumento(), FONT_NORMAL));
            doc.add(Chunk.NEWLINE);

            // FECHA Y CLIENTE
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            PdfPTable datos = new PdfPTable(2);
            datos.setWidthPercentage(100);
            datos.setWidths(new float[]{30, 70});
            datos.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            datos.addCell(celdaSinBorde("Fecha emisión:", FONT_BOLD));
            datos.addCell(celdaSinBorde(sdf.format(boleta.getFechaEmision()), FONT_NORMAL));

            if (boleta.getCliente() != null) {
                datos.addCell(celdaSinBorde("Cliente:", FONT_BOLD));
                datos.addCell(celdaSinBorde(boleta.getCliente().getNombre() + " " + boleta.getCliente().getApellido(), FONT_NORMAL));
            } else {
                datos.addCell(celdaSinBorde("Cliente:", FONT_BOLD));
                datos.addCell(celdaSinBorde("[NO REGISTRADO]", FONT_NORMAL));
            }

            datos.addCell(celdaSinBorde("Moneda:", FONT_BOLD));
            datos.addCell(celdaSinBorde("PEN", FONT_NORMAL));

            doc.add(datos);
            doc.add(Chunk.NEWLINE);

            // TABLA DE PRODUCTOS
            PdfPTable tabla = new PdfPTable(new float[]{1.5f, 6, 2, 2});
            tabla.setWidthPercentage(100);

            addHeader(tabla, "CANT.");
            addHeader(tabla, "DESCRIPCIÓN");
            addHeader(tabla, "P. UNIT");
            addHeader(tabla, "TOTAL");

            for (VentaDTO v : boleta.getVentas()) {
                ProductoDTO p = v.getProducto();
                double subtotal = v.getCantidad() * v.getPrecioUnitario();

                tabla.addCell(celdaCentro(String.valueOf(v.getCantidad()), FONT_NORMAL));
                tabla.addCell(celdaIzq(p.getNombre(), FONT_NORMAL));
                tabla.addCell(celdaDer(String.format("S/ %.2f", v.getPrecioUnitario()), FONT_NORMAL));
                tabla.addCell(celdaDer(String.format("S/ %.2f", subtotal), FONT_NORMAL));
            }
            doc.add(tabla);
            doc.add(Chunk.NEWLINE);

            // TOTALES
            PdfPTable totales = new PdfPTable(2);
            totales.setWidthPercentage(60);
            totales.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totales.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            double subtotal = boleta.getTotal() / 1.18;
            double igv = boleta.getTotal() - subtotal;

            totales.addCell(celdaSinBorde("Subtotal:", FONT_BOLD));
            totales.addCell(celdaDer(String.format("S/ %.2f", subtotal), FONT_NORMAL));
            totales.addCell(celdaSinBorde("IGV (18%):", FONT_BOLD));
            totales.addCell(celdaDer(String.format("S/ %.2f", igv), FONT_NORMAL));
            totales.addCell(celdaSinBorde("TOTAL:", FONT_BOLD));
            totales.addCell(celdaDer(String.format("S/ %.2f", boleta.getTotal()), FONT_BOLD));
            doc.add(totales);

            doc.add(Chunk.NEWLINE);

            Paragraph gracias = new Paragraph("¡Gracias por su compra!", FONT_NORMAL);
            gracias.setAlignment(Element.ALIGN_CENTER);
            doc.add(gracias);

            // (Opcional) Bloque QR o código de validación
            // BarcodeQRCode qr = new BarcodeQRCode("https://softfkes.com/validar?boleta=" + boleta.getIdBoleta(), 100, 100, null);
            // Image qrImg = qr.getImage();
            // qrImg.setAlignment(Element.ALIGN_CENTER);
            // doc.add(qrImg);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }

    // -------- Métodos auxiliares --------
    private static void addHeader(PdfPTable t, String txt) {
        PdfPCell c = new PdfPCell(new Phrase(txt, FONT_BOLD));
        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c);
    }

    private static PdfPCell celdaSinBorde(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private static PdfPCell celdaCentro(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private static PdfPCell celdaIzq(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        return c;
    }

    private static PdfPCell celdaDer(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return c;
    }
}

