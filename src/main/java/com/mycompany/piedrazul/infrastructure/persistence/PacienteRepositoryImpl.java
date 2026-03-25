/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Paciente;
import com.mycompany.piedrazul.domain.repository.IPacienteRepository;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asus
 */
public class PacienteRepositoryImpl implements IPacienteRepository {

    @Override
    public boolean create(int personaId) {
        String sql = "INSERT INTO Paciente (per_id) VALUES (?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personaId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Paciente findById(int id) {

        String sql = """
                    SELECT p.*, per.*
                    FROM Paciente p
                    JOIN Persona per ON p.per_id = per.per_id
                    WHERE p.per_id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Paciente findByDni(int dni) {

        String sql = """
                    SELECT p.*, per.*
                    FROM Paciente p
                    JOIN Persona per ON p.per_id = per.per_id
                    WHERE per.per_dni = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Paciente> findAll() {

        List<Paciente> lista = new ArrayList<>();

        String sql = """
                    SELECT p.*, per.*
                    FROM Paciente p
                    JOIN Persona per ON p.per_id = per.per_id
                    ORDER BY per.per_primer_nombre
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    private Paciente map(ResultSet rs) throws SQLException {

        Paciente p = new Paciente();

        p.setId(rs.getInt("per_id"));
        p.setPrimerNombre(rs.getString("per_primer_nombre"));
        p.setSegundoNombre(rs.getString("per_segundo_nombre"));
        p.setPrimerApellido(rs.getString("per_primer_apellido"));
        p.setSegundoApellido(rs.getString("per_segundo_apellido"));
        p.setGenero(rs.getString("per_genero"));
        p.setFechaNacimiento(rs.getDate("per_fecha_nac").toLocalDate());
        p.setTelefono(rs.getString("per_telefono"));
        p.setDni(rs.getInt("per_dni"));
        p.setCorreo(rs.getString("per_correo"));

        return p;
    }

}
