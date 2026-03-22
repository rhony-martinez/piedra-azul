package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements IUsuarioRepository {
    
    /*private void createTableIfNotExists(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT UNIQUE NOT NULL,"
                + "password_hash TEXT NOT NULL,"
                + "nombre_completo TEXT NOT NULL,"
                + "rol TEXT NOT NULL,"
                + "activo INTEGER DEFAULT 1,"
                + "intentos_fallidos INTEGER DEFAULT 0"
                + ")";
        
        String sqlCitas = "CREATE TABLE IF NOT EXISTS citas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "patient_id INTEGER NOT NULL,"
                + "professional_id INTEGER NOT NULL,"
                + "date_time TEXT NOT NULL,"
                + "appointment_type TEXT NOT NULL,"
                + "status TEXT NOT NULL,"
                + "reason TEXT,"
                + "notes TEXT,"
                + "created_by_id INTEGER NOT NULL,"
                + "created_at TEXT NOT NULL,"
                + "original_appointment_id INTEGER,"
                + "FOREIGN KEY (patient_id) REFERENCES usuarios(id),"
                + "FOREIGN KEY (professional_id) REFERENCES usuarios(id),"
                + "FOREIGN KEY (created_by_id) REFERENCES usuarios(id),"
                + "FOREIGN KEY (original_appointment_id) REFERENCES citas(id)"
                + ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
    
    @Override
    public Usuario findByUsername(String username) {
        String sql = "SELECT u.*, r.rol_nombre " +
            "FROM Usuario u " +
            "JOIN UsuarioRol ur ON u.usu_id = ur.usu_id " +
            "JOIN Rol r ON ur.rol_id = r.rol_id " +
            "WHERE u.username = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
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
        String sql = "SELECT * FROM Usuario WHERE username = ? AND usu_password = ? AND usu_estado = 'ACTIVO'";
        
        try (Connection conn = ConnectionFactory.getConnection();
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

        String sqlUsuario = "INSERT INTO Usuario (per_id, username, usu_password, usu_estado) VALUES (?, ?, ?, ?)";
        String sqlUsuarioRol = "INSERT INTO UsuarioRol (usu_id, rol_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

            conn.setAutoCommit(false); // TRANSACTION

            // 1. Insert Usuario
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, usuario.getPersonaId());
                stmt.setString(2, usuario.getUsername());
                stmt.setString(3, usuario.getPasswordHash());
                stmt.setString(4, usuario.isActivo() ? "ACTIVO" : "INACTIVO");

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int usuarioId = rs.getInt(1);
                    usuario.setId(usuarioId);

                    // 2. Obtener rol_id
                    int rolId = obtenerRolIdPorNombre(usuario.getRol().name());

                    // 3. Insert UsuarioRol
                    try (PreparedStatement stmtRol = conn.prepareStatement(sqlUsuarioRol)) {
                        stmtRol.setInt(1, usuarioId);
                        stmtRol.setInt(2, rolId);
                        stmtRol.executeUpdate();
                    }
                }
            }

            conn.commit(); // OK
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";
        
        try (Connection conn = ConnectionFactory.getConnection();
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
    public boolean deactivate(int id) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
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
        String sql = "SELECT COUNT(*) FROM Usuario WHERE username = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
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
        
        try (Connection conn = ConnectionFactory.getConnection();
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
        
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(rs.getInt("usu_id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPasswordHash(rs.getString("usu_password"));
        usuario.setPersonaId(rs.getInt("per_id"));
        usuario.setActivo("ACTIVO".equals(rs.getString("usu_estado")));

        // obtener rol
        String rolNombre = rs.getString("rol_nombre");
        usuario.setRol(Rol.valueOf(rolNombre));

        return usuario;
    }
    
    @Override
    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
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
    public boolean update(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private int obtenerRolIdPorNombre(String rolNombre) {

        String sql = "SELECT rol_id FROM Rol WHERE rol_nombre = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rolNombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("rol_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Rol no encontrado: " + rolNombre);
    }
}