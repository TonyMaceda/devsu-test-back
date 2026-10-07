package org.bank.banktestback.service.impl;

import lombok.RequiredArgsConstructor;
import org.bank.banktestback.dto.ClienteDTO;
import org.bank.banktestback.exception.ClienteCuentasActivasException;
import org.bank.banktestback.exception.RecursoExistenteException;
import org.bank.banktestback.exception.RecursoNoEncontradoException;
import org.bank.banktestback.model.Cliente;
import org.bank.banktestback.repository.ClienteRepository;
import org.bank.banktestback.service.IClienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<ClienteDTO> obtenerPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream().map(this::toDTO).toList();
    }

    @Override
    public ClienteDTO obtenerPorId(Long id) {
        return toDTO(obtenerClientePorId(id));
    }

    @Override
    public ClienteDTO crear(ClienteDTO dto) {
        clienteRepository.findByClienteId(dto.clienteId()).ifPresent(c -> {
            throw new RecursoExistenteException("Ya existe un cliente con id : " + dto.clienteId());
        });
        Cliente clienteGuardado = clienteRepository.save(toEntity(dto));
        return toDTO(clienteGuardado);
    }

    @Override
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Cliente cliente = obtenerClientePorId(id);
        cliente.setNombre(dto.nombre());
        cliente.setGenero(dto.genero());
        cliente.setEdad(dto.edad());
        cliente.setIdentificacion(dto.identificacion());
        cliente.setDireccion(dto.direccion());
        cliente.setTelefono(dto.telefono());
        cliente.setEstado(dto.estado());
        return toDTO(clienteRepository.save(cliente));
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = obtenerClientePorId(id);
        if (!cliente.getCuentas().isEmpty()) {
            throw new ClienteCuentasActivasException("No se puede eliminar un cliente con cuentas activas asociadas.");
        }
        clienteRepository.delete(cliente);
    }

    private Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + id));
    }

    private ClienteDTO toDTO(Cliente c) {
        return ClienteDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .genero(c.getGenero())
                .edad(c.getEdad())
                .identificacion(c.getIdentificacion())
                .direccion(c.getDireccion())
                .telefono(c.getTelefono())
                .clienteId(c.getClienteId())
                .estado(c.getEstado())
                .build();
    }

    private Cliente toEntity(ClienteDTO dto) {
        Cliente c = new Cliente();
        c.setNombre(dto.nombre());
        c.setGenero(dto.genero());
        c.setEdad(dto.edad());
        c.setIdentificacion(dto.identificacion());
        c.setDireccion(dto.direccion());
        c.setTelefono(dto.telefono());
        c.setClienteId(dto.clienteId());
        c.setContrasena(dto.contrasena());
        c.setEstado(dto.estado() != null ? dto.estado() : true);
        return c;
    }
}
