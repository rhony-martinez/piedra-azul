/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Persona;
import com.mycompany.piedrazul.domain.repository.IPersonaRepository;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
/**
 *
 * @author asus
 */
public class PersonaRepositoryImpl implements IPersonaRepository {

    @Override
    public Persona create(Persona persona) {

        String sql = "INSERT INTO persona (per_primer_nombre, per_segundo_nombre, per_primer_apellido, per_segundo_apellido, per_genero, per_fecha_nac, per_telefono, per_dni, per_correo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, persona.getPrimerNombre());
            stmt.setString(2, persona.getSegundoNombre());
            stmt.setString(3, persona.getPrimerApellido());
            stmt.setString(4, persona.getSegundoApellido());
            stmt.setString(5, persona.getGenero());
            stmt.setDate(6, Date.valueOf(persona.getFechaNacimiento()));
            stmt.setString(7, persona.getTelefono());
            stmt.setInt(8, persona.getDni());
            stmt.setString(9, persona.getCorreo());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                persona.setId(rs.getInt(1));
            }

            return persona;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear persona", e);
        }
    }

    @Override
    public Persona findById(int id) {
        String sql = "SELECT * FROM persona WHERE per_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Persona p = new Persona();
                p.setId(rs.getInt("per_id"));
                p.setPrimerNombre(rs.getString("per_primer_nombre"));
                p.setPrimerApellido(rs.getString("per_primer_apellido"));
                p.setGenero(rs.getString("per_genero"));
                p.setTelefono(rs.getString("per_telefono"));
                p.setDni(rs.getInt("per_dni"));
                // completar los demás
                return p;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public boolean dniExists(int dni) {
        String sql = "SELECT COUNT(*) FROM persona WHERE per_dni = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
