package com.example.GameVault.service;

import com.example.GameVault.models.Usuario;
import com.example.GameVault.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario obtenerUsuarioLogueado() {
        return usuarioRepository.findById(1L).orElse(null);
    }
}