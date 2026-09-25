package com.example.proyec_herramientas.reportes;

import com.example.proyec_herramientas.model.BoletaDTO;
import com.example.proyec_herramientas.model.ProductoDTO;
import com.example.proyec_herramientas.model.VentaDTO;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

public class BoletaPDFGenerator {

    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 11);
    private static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

    public static void generarPDF(BoletaDTO boleta, String ruta) throws Exception {
        try (FileOutputStream out = new FileOutputStream(ruta)) {
            escribir(boleta, out);
        }
    }

    public static byte[] generarPdfBytes(BoletaDTO boleta) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            escribir(boleta, out);
            return out.toByteArray();
        }
    }

    private static void escribir(BoletaDTO boleta, OutputStream out) throws Exception {
        Document doc = new Document(PageSize.A5, 36, 36, 54, 36);
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

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

            datos.addCell(PdfUtil.celdaSinBorde("Fecha emisión:", FONT_BOLD));
            datos.addCell(PdfUtil.celdaSinBorde(sdf.format(boleta.getFechaEmision()), FONT_NORMAL));

            if (boleta.getCliente() != null) {
                datos.addCell(PdfUtil.celdaSinBorde("Cliente:", FONT_BOLD));
                datos.addCell(PdfUtil.celdaSinBorde(boleta.getCliente().getNombre() + " " + boleta.getCliente().getApellido(), FONT_NORMAL));
            } else {
                datos.addCell(PdfUtil.celdaSinBorde("Cliente:", FONT_BOLD));
                datos.addCell(PdfUtil.celdaSinBorde("[NO REGISTRADO]", FONT_NORMAL));
            }

            datos.addCell(PdfUtil.celdaSinBorde("Moneda:", FONT_BOLD));
            datos.addCell(PdfUtil.celdaSinBorde("PEN", FONT_NORMAL));

            doc.add(datos);
            doc.add(Chunk.NEWLINE);

            // TABLA DE PRODUCTOS
            PdfPTable tabla = new PdfPTable(new float[]{1.5f, 6, 2, 2});
            tabla.setWidthPercentage(100);

            PdfUtil.addHeader(tabla, "CANT.", FONT_BOLD);
            PdfUtil.addHeader(tabla, "DESCRIPCIÓN", FONT_BOLD);
            PdfUtil.addHeader(tabla, "P. UNIT", FONT_BOLD);
            PdfUtil.addHeader(tabla, "TOTAL", FONT_BOLD);

            for (VentaDTO v : boleta.getVentas()) {
                ProductoDTO p = v.getProducto();
                double subtotal = v.getCantidad() * v.getPrecioUnitario();

                tabla.addCell(PdfUtil.celda(String.valueOf(v.getCantidad()), FONT_NORMAL, Element.ALIGN_CENTER));
                tabla.addCell(PdfUtil.celda(p.getNombre(), FONT_NORMAL, Element.ALIGN_LEFT));
                tabla.addCell(PdfUtil.celda(Moneda.formatear(v.getPrecioUnitario()), FONT_NORMAL, Element.ALIGN_RIGHT));
                tabla.addCell(PdfUtil.celda(Moneda.formatear(subtotal), FONT_NORMAL, Element.ALIGN_RIGHT));
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

            totales.addCell(PdfUtil.celdaSinBorde("Subtotal:", FONT_BOLD));
            totales.addCell(PdfUtil.celda(Moneda.formatear(subtotal), FONT_NORMAL, Element.ALIGN_RIGHT));
            totales.addCell(PdfUtil.celdaSinBorde("IGV (18%):", FONT_BOLD));
            totales.addCell(PdfUtil.celda(Moneda.formatear(igv), FONT_NORMAL, Element.ALIGN_RIGHT));
            totales.addCell(PdfUtil.celdaSinBorde("TOTAL:", FONT_BOLD));
            totales.addCell(PdfUtil.celda(Moneda.formatear(boleta.getTotal()), FONT_BOLD, Element.ALIGN_RIGHT));
            doc.add(totales);

            doc.add(Chunk.NEWLINE);

            Paragraph gracias = new Paragraph("¡Gracias por su compra!", FONT_NORMAL);
            gracias.setAlignment(Element.ALIGN_CENTER);
            doc.add(gracias);
        } finally {
            if (doc.isOpen()) {
                doc.close();
            }
        }
    }
}