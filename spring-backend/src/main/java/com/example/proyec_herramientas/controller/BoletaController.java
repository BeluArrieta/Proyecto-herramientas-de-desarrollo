package com.example.proyec_herramientas.controller;

import com.example.proyec_herramientas.persistence.Venta;
import com.example.proyec_herramientas.service.ReportePdfService;
import com.example.proyec_herramientas.service.VentaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BoletaController {

    private static final MediaType PDF = MediaType.parseMediaType("application/pdf");

    private final VentaService ventaService;
    private final ReportePdfService reportePdfService;

    public BoletaController(VentaService ventaService, ReportePdfService reportePdfService) {
        this.ventaService = ventaService;
        this.reportePdfService = reportePdfService;
    }

    @GetMapping("/boletas")
    public List<Venta> boletas() {
        return ventaService.obtenerBoletas();
    }

    @GetMapping("/historial")
    public List<Venta> historial(@RequestParam(required = false) String clienteId) {
        return ventaService.obtenerHistorial(clienteId);
    }

    @GetMapping("/boleta/{id}/pdf")
    public ResponseEntity<byte[]> boletaPdf(@PathVariable String id) {
        return pdf(id, "boleta", reportePdfService.generarBoletaPdf(id));
    }

    @GetMapping("/factura/{id}/pdf")
    public ResponseEntity<byte[]> facturaPdf(@PathVariable String id) {
        return pdf(id, "factura", reportePdfService.generarFacturaPdf(id));
    }

    private ResponseEntity<byte[]> pdf(String id, String tipo, byte[] bytes) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + tipo + "-" + id + ".pdf\"")
                .contentType(PDF)
                .body(bytes);
    }
}