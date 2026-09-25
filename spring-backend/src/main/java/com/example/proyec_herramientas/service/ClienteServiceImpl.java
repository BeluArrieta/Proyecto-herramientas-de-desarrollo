package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.model.ClienteDTO;
import com.example.proyec_herramientas.persistence.ClienteDocument;
import com.example.proyec_herramientas.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    public ClienteServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ClienteDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public ClienteDTO obtenerPorId(String idCliente) {
        ClienteDocument doc = repository.findById(idCliente).orElse(null);
        return doc != null ? toDTO(doc) : null;
    }

    private ClienteDTO toDTO(ClienteDocument doc) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(doc.getId());
        dto.setNombre(doc.getNombre());
        dto.setApellido(doc.getApellido());
        dto.setTelefono(doc.getTelefono());
        dto.setCorreo(doc.getCorreo());
        return dto;
    }
}