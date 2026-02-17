package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorySQLite implements IUsuarioRepository {
    
    private Connection getConnection() {
        Connection conn = null;
        try {
            // SQLite creará el archivo en el directorio del proyecto
            String url = "jdbc:sqlite:piedrazul.db";
            conn = DriverManager.getConnection(url);
            
            // Crear tabla si no existe
            createTableIfNotExists(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    private void createTableIfNotExists(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT UNIQUE NOT NULL,"
                + "password_hash TEXT NOT NULL,"
                + "nombre_completo TEXT NOT NULL,"
                + "rol TEXT NOT NULL,"
                + "activo INTEGER DEFAULT 1,"
                + "intentos_fallidos INTEGER DEFAULT 0"
                + ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Usuario findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Usuario authenticate(String username, String passwordHash) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password_hash = ? AND activo = 1";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean create(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password_hash, nombre_completo, rol, activo, intentos_fallidos) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setString(3, usuario.getNombreCompleto());
            stmt.setString(4, usuario.getRol());
            stmt.setInt(5, usuario.isActivo() ? 1 : 0);
            stmt.setInt(6, usuario.getIntentosFallidos());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
    
    @Override
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre_completo = ?, rol = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombreCompleto());
            stmt.setString(2, usuario.getRol());
            stmt.setInt(3, usuario.isActivo() ? 1 : 0);
            stmt.setInt(4, usuario.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deactivate(int id) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void registrarIntentoFallido(String username) {
        String sql = "UPDATE usuarios SET intentos_fallidos = intentos_fallidos + 1 WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void resetearIntentosFallidos(String username) {
        String sql = "UPDATE usuarios SET intentos_fallidos = 0 WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPasswordHash(rs.getString("password_hash"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
        usuario.setRol(rs.getString("rol"));
        usuario.setActivo(rs.getInt("activo") == 1);
        usuario.setIntentosFallidos(rs.getInt("intentos_fallidos"));
        return usuario;
    }
}