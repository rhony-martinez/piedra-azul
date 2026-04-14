package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements IUsuarioRepository {

    @Override
    public Usuario findByUsername(String username) {

        String sql = """
                    SELECT u.usu_id, u.username, u.usu_password, u.usu_estado, u.per_id,
                            r.rol_nombre
                    FROM Usuario u
                    JOIN UsuarioRol ur ON u.usu_id = ur.usu_id
                    JOIN Rol r ON ur.rol_id = r.rol_id
                    WHERE u.username = ?
                """;

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

        String sql = """
                    SELECT u.usu_id, u.username, u.usu_password, u.usu_estado, u.per_id,
                            r.rol_nombre
                    FROM Usuario u
                    JOIN UsuarioRol ur ON u.usu_id = ur.usu_id
                    JOIN Rol r ON ur.rol_id = r.rol_id
                    WHERE u.username = ?
                        AND u.usu_password = ?
                        AND u.usu_estado = 'ACTIVO'
                """;

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
        String sql = "SELECT * FROM Usuario ORDER BY usu_id";

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
        String sql = "UPDATE Usuario SET activo = 0 WHERE id = ?";

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
        String sql = "UPDATE Usuario SET intentos_fallidos = intentos_fallidos + 1 WHERE username = ?";

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
        usuario.setActivo("ACTIVO".equals(rs.getString("usu_estado")));
        usuario.setPersonaId(rs.getInt("per_id"));

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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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

    @Override
    public Usuario findByPersonaId(int personaId) {

        String sql = "SELECT * FROM Usuario WHERE per_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personaId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("usu_id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("usu_password"));
                u.setActivo("ACTIVO".equals(rs.getString("usu_estado")));

                return u;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por personaId", e);
        }

        return null;
    }
}