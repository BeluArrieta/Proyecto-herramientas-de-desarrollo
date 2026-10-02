package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.model.ClienteDTO;

import java.util.List;

public interface ClienteService {

    List<ClienteDTO> listar();

    ClienteDTO obtenerPorId(String idCliente);
}
