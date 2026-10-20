package dev.jfgonzalez.crudapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jfgonzalez.crudapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente,Long>{
    
}
