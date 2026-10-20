package com.example.GameVault.service;

import com.example.GameVault.models.Juego;
import com.example.GameVault.repositorio.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class JuegoService {

    @Autowired
    JuegoRepository juegoRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public List<Juego> obtenerTodos() {
        return juegoRepository.findAll();
    }

    public List<Juego> obtenerJuegosPorUsuario(Long usuarioId) {
        return juegoRepository.findByUsuarioId(usuarioId);
    }

    public Juego obtenerJuegoPorId(Long id) {
        return juegoRepository.findById(id).orElse(null);
    }

    public void eliminarJuego(Long id) {
        juegoRepository.deleteById(id);
    }

    public List<Juego> buscarPorTitulo(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            return obtenerTodos();
        }

        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }

    public void guardarJuego(Juego juego, MultipartFile portada) {

        if (portada != null && !portada.isEmpty()) {
            String nombreArchivo = guardarImagenLocal(portada);
            juego.setPortadaUrl(nombreArchivo);
        } else if (juego.getPortadaUrl() == null || juego.getPortadaUrl().isEmpty()) {
            juego.setPortadaUrl("default.png");
        }

        juegoRepository.save(juego);
    }

    public String guardarImagenLocal(MultipartFile portada) {
        try {
            // Tip para clase: Crear la carpeta si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generamos un nombre único para evitar sobreescribir archivos con el mismo nombre
            String nombreArchivo = UUID.randomUUID().toString() + "-" + portada.getOriginalFilename();
            Path filePath = uploadPath.resolve(nombreArchivo);

            // Guardamos el archivo físicamente en disco
            Files.copy(portada.getInputStream(), filePath);

            return nombreArchivo;

        } catch (IOException e) {
            e.printStackTrace();
            return "default.png";
        }
    }
}