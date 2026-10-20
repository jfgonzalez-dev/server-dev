package dev.jfgonzalez.crudapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jfgonzalez.crudapi.model.Cliente;
import dev.jfgonzalez.crudapi.model.Direccion;
import dev.jfgonzalez.crudapi.repository.ClienteRepository;
import dev.jfgonzalez.crudapi.repository.DireccionRepository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping ("/crudapi")
public class MyRestController {

    @Autowired 
    ClienteRepository clienteRepository;

    @Autowired 
    DireccionRepository direccionRepository;

    @GetMapping
    public String index() {
        try {
            
            Cliente cliente = new Cliente();
            cliente.setNombre("a");
            cliente.setApellidos("a");
            cliente.setEmail("a");
            cliente.setTelefono("a");

            Direccion direccion = new Direccion();
            direccion.setCalle("a");
            direccion.setCiudad("a");
            direccion.setCodigoPostal("a");
            direccion.setNumero("a");

            cliente.addDireccion(direccion);

            Cliente dbCliente = clienteRepository.save(cliente);            
            Direccion dbDireccion = direccionRepository.save(direccion);            
            return dbCliente.toString() + dbDireccion.toString();
        } catch (Exception e) {
            return Arrays.toString(e.getStackTrace());
        }

    }
    
}
