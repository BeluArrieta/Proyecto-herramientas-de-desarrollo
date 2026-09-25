package com.example.proyec_herramientas.service;

public interface ReportePdfService {

    byte[] generarBoletaPdf(String idVenta);

    byte[] generarFacturaPdf(String idVenta);
}