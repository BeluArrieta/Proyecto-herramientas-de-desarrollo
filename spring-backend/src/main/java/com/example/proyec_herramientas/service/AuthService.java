package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.dto.AuthRequest;
import com.example.proyec_herramientas.dto.LoginResponse;
import com.example.proyec_herramientas.dto.RegistroRequest;
import com.example.proyec_herramientas.model.Cliente;
import com.example.proyec_herramientas.model.Persona;
import com.example.proyec_herramientas.model.Usuario;
import com.example.proyec_herramientas.repository.ClienteRepository;
import com.example.proyec_herramientas.repository.PersonaRepository;
import com.example.proyec_herramientas.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;
    private final SeguridadContrasenaService seguridad;

    public AuthService(UsuarioRepository usuarioRepository,
            PersonaRepository personaRepository,
            ClienteRepository clienteRepository,
            SeguridadContrasenaService seguridad) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.clienteRepository = clienteRepository;
        this.seguridad = seguridad;
    }

    public LoginResponse login(AuthRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuario()).orElse(null);
        if (usuario == null || !seguridad.verificar(request.getContrasena(), usuario.getContrasena())) {
            return null;
        }

        Persona persona = personaRepository.findById(usuario.getIdPersona()).orElse(null);
        if (persona == null) {
            return null;
        }

        String correo = persona.getCorreo();
        boolean esCliente = clienteRepository.existsById(usuario.getIdPersona())
                || (correo != null && clienteRepository.existsByCorreo(correo));

        String nombre = persona.getNombre() != null ? persona.getNombre() : "";
        String apellido = persona.getApellido() != null ? persona.getApellido() : "";

        return new LoginResponse(
                usuario.getId(),
                esCliente ? "CLIENTE" : "ADMIN",
                nombre,
                apellido,
                correo != null ? correo : "",
                persona.getTelefono() != null ? persona.getTelefono() : "");
    }

    public LoginResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsById(request.getUsuario())) {
            throw new IllegalStateException("El usuario ya existe");
        }
        if (personaRepository.countByCorreo(request.getCorreo()) > 0) {
            throw new IllegalStateException("El correo ya esta registrado");
        }

        String contrasenaCifrada = seguridad.hash(request.getContrasena());
        String idPersona = generarIdPersona();

        Persona persona = new Persona(
                idPersona, request.getNombre(), request.getApellido(),
                request.getCorreo(), request.getTelefono(), request.getUsuario());

        Usuario usuario = new Usuario(
                request.getUsuario(), contrasenaCifrada, idPersona);

        Cliente cliente = new Cliente();
        cliente.setId(idPersona);
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setTelefono(request.getTelefono());
        cliente.setCorreo(request.getCorreo());
        cliente.setContrasena(contrasenaCifrada);

        personaRepository.save(persona);
        usuarioRepository.save(usuario);
        clienteRepository.save(cliente);

        return new LoginResponse(
                usuario.getId(), "CLIENTE",
                request.getNombre(), request.getApellido(),
                request.getCorreo(), request.getTelefono());
    }

    private String generarIdPersona() {
        return "PER-" + String.format("%04d", personaRepository.count() + 1);
    }

    public static Map<String, String> error(String mensaje) {
        Map<String, String> mapa = new HashMap<>();
        mapa.put("error", mensaje);
        return mapa;
    }
}