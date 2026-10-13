package com.example.GameVault.repositorio;

import com.example.GameVault.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {

    // Metodos gratis como insert(save), update, delete, find

    List<Juego> findByTituloContainingIgnoreCase(String titulo);

    // List<Juego> findByPrecioLessThan(Double precio);

    @Query("SELECT j FROM Juego j ORDER BY j.id DESC")
    List<Juego> obtenerJuegosRecientes();

    @Query("SELECT j FROM Juego j WHERE LOWER(j.descripcion) LIKE LOWER(CONCAT('%', :palabra, '%'))")
    List<Juego> buscarPorDescripcion(String palabra);

}
