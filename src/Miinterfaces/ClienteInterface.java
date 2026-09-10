package Miinterfaces;

import ModeloDTO.ClienteDTO; 
import java.util.ArrayList;

public interface ClienteInterface {

    public boolean agregar(ClienteDTO cliente);


    public ArrayList<ClienteDTO> listarTodo();


    public ClienteDTO listarUno(String id);


    public ClienteDTO buscarClienteParaLogin(String idCliente, String password);
}