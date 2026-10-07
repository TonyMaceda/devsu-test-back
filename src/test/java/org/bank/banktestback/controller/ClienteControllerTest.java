package org.bank.banktestback.controller;

import org.bank.banktestback.dto.ClienteDTO;
import org.bank.banktestback.service.IClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ClienteControllerTest {

    @Mock
    private IClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarClientes() {
        List<ClienteDTO> mockClientes = List.of(
                ClienteDTO.builder().id(1L).nombre("John Doe").build(),
                ClienteDTO.builder().id(2L).nombre("Jane Doe").build()
        );
        when(clienteService.listarClientes()).thenReturn(mockClientes);

        ResponseEntity<List<ClienteDTO>> response = clienteController.listar(null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockClientes, response.getBody());
        verify(clienteService, times(1)).listarClientes();
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void obtenerClientePorId() {
        ClienteDTO mockCliente = ClienteDTO.builder()
                .id(1L)
                .nombre("John Doe")
                .genero("M")
                .edad(20)
                .build();
        when(clienteService.obtenerPorId(1L)).thenReturn(mockCliente);

        ResponseEntity<ClienteDTO> response = clienteController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCliente, response.getBody());
        verify(clienteService, times(1)).obtenerPorId(1L);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void crearCliente() {
        ClienteDTO mockCliente = ClienteDTO.builder()
                .id(1L)
                .nombre("John Doe")
                .build();
        when(clienteService.crear(any(ClienteDTO.class))).thenReturn(mockCliente);

        ResponseEntity<ClienteDTO> response = clienteController.crear(mockCliente);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCliente, response.getBody());
        verify(clienteService, times(1)).crear(any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void actualizarCliente() {
        ClienteDTO mockCliente = ClienteDTO.builder()
                .id(1L)
                .nombre("John Doe")
                .build();
        when(clienteService.actualizar(eq(1L), any(ClienteDTO.class))).thenReturn(mockCliente);

        ResponseEntity<ClienteDTO> response = clienteController.actualizar(1L, mockCliente);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockCliente, response.getBody());
        verify(clienteService, times(1)).actualizar(eq(1L), any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void eliminarCliente() {
        doNothing().when(clienteService).eliminar(1L);

        clienteController.eliminar(1L);

        verify(clienteService, times(1)).eliminar(1L);
        verifyNoMoreInteractions(clienteService);
    }
}
