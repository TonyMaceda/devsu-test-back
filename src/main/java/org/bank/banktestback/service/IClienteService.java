package org.bank.banktestback.service;

import org.bank.banktestback.dto.ClienteDTO;

import java.util.List;

public interface IClienteService {

    List<ClienteDTO> listarClientes();
    List<ClienteDTO> obtenerPorNombre(String nombre);
    ClienteDTO obtenerPorId(Long id);
    ClienteDTO crear(ClienteDTO dto);
    ClienteDTO actualizar(Long id, ClienteDTO dto);
    void eliminar(Long id);
}
