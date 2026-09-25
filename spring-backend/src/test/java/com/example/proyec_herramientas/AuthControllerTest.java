package com.example.proyec_herramientas;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.proyec_herramientas.dto.AuthRequest;
import com.example.proyec_herramientas.dto.RegistroRequest;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void loginValidoDevuelveUsuarioYRol() throws Exception {
        AuthRequest request = new AuthRequest("juanp", "123456");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario").value("juanp"))
                .andExpect(jsonPath("$.tipoUsuario").value("CLIENTE"))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @Order(2)
    void loginClaveIncorrectaDevuelve401() throws Exception {
        AuthRequest request = new AuthRequest("juanp", "clave-incorrecta");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    void loginUsuarioInexistenteDevuelve401() throws Exception {
        AuthRequest request = new AuthRequest("usuario-no-existe", "123456");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(4)
    void registroDuplicadoDevuelve400() throws Exception {
        RegistroRequest request = new RegistroRequest();
        request.setNombre("Otro");
        request.setApellido("Usuario");
        request.setCorreo("otro@correo.com");
        request.setTelefono("999111222");
        request.setUsuario("juanp");
        request.setContrasena("123456");

        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void registroNuevoDevuelve201() throws Exception {
        String usuario = "test_" + System.currentTimeMillis();
        RegistroRequest request = new RegistroRequest();
        request.setNombre("Test");
        request.setApellido("Registro");
        request.setCorreo(usuario + "@correo.com");
        request.setTelefono("999888777");
        request.setUsuario(usuario);
        request.setContrasena("123456");

        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario").value(usuario))
                .andExpect(jsonPath("$.tipoUsuario").value("CLIENTE"));
    }
}