package com.mycompany.piedrazul.domain.repository;

import com.mycompany.piedrazul.domain.model.Usuario;
import java.util.List;

public interface IUsuarioRepository {
    Usuario findByUsername(String username);
    Usuario authenticate(String username, String passwordHash);
    boolean create(Usuario usuario);
    List<Usuario> findAll();
    boolean update(Usuario usuario);
    boolean deactivate(int id);
    boolean usernameExists(String username);
    void registrarIntentoFallido(String username);
    void resetearIntentosFallidos(String username);
}

