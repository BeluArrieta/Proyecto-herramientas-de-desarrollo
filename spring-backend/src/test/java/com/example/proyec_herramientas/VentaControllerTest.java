package com.example.proyec_herramientas;

import com.example.proyec_herramientas.persistence.ClienteDocument;
import com.example.proyec_herramientas.persistence.ProductoDocument;
import com.example.proyec_herramientas.repository.ClienteRepository;
import com.example.proyec_herramientas.repository.ProductoRepository;
import com.example.proyec_herramientas.repository.VentaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limpiar() {
        ventaRepository.deleteAll();
        productoRepository.deleteAll();
        clienteRepository.deleteAll();
        productoRepository.save(new ProductoDocument(1, "Mouse Gamer", 8, 50.0, "Accesorios"));
        productoRepository.save(new ProductoDocument(2, "Teclado Mecánico", 5, 120.0, "Accesorios"));
        clienteRepository.save(new ClienteDocument("CLI-0001", "Juan", "Pérez", "987654321", "juan@correo.com"));
    }

    @Test
    void totalDeBoletaEsSumaDeItems() throws Exception {
        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carrito("CLI-0001", "Boleta", "Yape", List.of(
                                item(1, 2), item(2, 1)))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoDocumento", is("Boleta")))
                .andExpect(jsonPath("$.numeroDocumento", is("BOL-000001")))
                .andExpect(jsonPath("$.cliente", is("Juan Pérez")))
                .andExpect(jsonPath("$.total", is(220.0)))
                .andExpect(jsonPath("$.detalles", hasSize(2)))
                .andExpect(jsonPath("$.detalles[0].producto", is("Mouse Gamer")))
                .andExpect(jsonPath("$.detalles[0].subtotal", is(100.0)));
    }

    @Test
    void boletasSoloDevuelveTipoBoleta() throws Exception {
        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carrito("CLI-0001", "Boleta", "Yape", List.of(item(1, 1)))));

        mockMvc.perform(get("/boletas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipoDocumento", is("Boleta")))
                .andExpect(jsonPath("$[0].total", is(50.0)));
    }

    @Test
    void historialDevuelveVentasDelCliente() throws Exception {
        MvcResult resultado = mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carrito("CLI-0001", "Boleta", "Yape", List.of(item(1, 2)))))
                .andExpect(status().isCreated())
                .andReturn();

        String idVenta = objectMapper.readTree(resultado.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/historial").param("clienteId", "CLI-0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(idVenta)))
                .andExpect(jsonPath("$[0].cliente", is("Juan Pérez")));
    }

    @Test
    void historialGeneralDevuelveTodasLasVentas() throws Exception {
        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carrito("CLI-0001", "Boleta", "Efectivo", List.of(item(1, 1)))));

        mockMvc.perform(get("/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipoDocumento", is("Boleta")));
    }

    @Test
    void ventaDescartaElStock() throws Exception {
        mockMvc.perform(post("/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carrito("CLI-0001", "Boleta", "Yape", List.of(item(1, 2)))));

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock", is(6)));
    }

    @Test
    void ventaConStockInsuficienteDevuelve409() throws Exception {
        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carrito("CLI-0001", "Boleta", "Yape", List.of(item(1, 99)))))
                .andExpect(status().isConflict());
    }

    @Test
    void generaPdfDeBoleta() throws Exception {
        MvcResult resultado = mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carrito("CLI-0001", "Boleta", "Yape", List.of(item(1, 1)))))
                .andExpect(status().isCreated())
                .andReturn();

        String idVenta = objectMapper.readTree(resultado.getResponse().getContentAsString()).get("id").asText();

        MvcResult pdf = mockMvc.perform(get("/boleta/" + idVenta + "/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andReturn();

        byte[] bytes = pdf.getResponse().getContentAsByteArray();
        assertTrue(bytes.length > 1000, "El PDF debe tener contenido");
        assertTrue(new String(bytes, 0, 4).startsWith("%PDF"), "Debe ser un PDF válido");
    }

    private String carrito(String idCliente, String tipoDocumento, String medioPago, List<Map<String, Object>> items) throws Exception {
        Map<String, Object> body = Map.of(
                "idCliente", idCliente,
                "tipoDocumento", tipoDocumento,
                "medioPago", medioPago,
                "items", items);
        return objectMapper.writeValueAsString(body);
    }

    private Map<String, Object> item(int idProducto, int cantidad) {
        return Map.of("idProducto", idProducto, "cantidad", cantidad);
    }
}