package Miinterfaces;

import ModeloDTO.FacturaDTO;
import java.util.ArrayList;

public interface FacturaInterface {

    public ArrayList<FacturaDTO> obtenerHistorialFacturasPorCliente(String idCliente);
}