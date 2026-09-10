package Miinterfaces;

import ModeloDTO.ProductoDTO;
import java.util.ArrayList;

public interface ProductoInterface {
    public ArrayList<ProductoDTO> listarTodo();
    public ProductoDTO listarUno(String id);
    void obtenerProductosAgotados();
}