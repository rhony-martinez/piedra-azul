/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.MedicoEstado;
import com.mycompany.piedrazul.domain.repository.IMedicoRepository;
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
public class MedicoRepositoryImpl implements IMedicoRepository {

    @Override
    public boolean create(int personaId, String tipoProfesional, MedicoEstado estado) {

        String sql = "INSERT INTO Medico (per_id, med_tipo_profesional, med_estado) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personaId);
            stmt.setString(2, tipoProfesional);
            stmt.setString(3, String.valueOf(estado));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Medico findById(int id) {

        String sql = """
                    SELECT m.*, per.*
                    FROM Medico m
                    JOIN Persona per ON m.per_id = per.per_id
                    WHERE m.per_id = ?
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
    public List<Medico> findAll() {

        List<Medico> lista = new ArrayList<>();

        String sql = """
                    SELECT m.*, per.*
                    FROM Medico m
                    JOIN Persona per ON m.per_id = per.per_id
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

    @Override
    public List<Medico> findAllActivos() {

        List<Medico> lista = new ArrayList<>();

        String sql = """
                    SELECT m.*, per.*
                    FROM Medico m
                    JOIN Persona per ON m.per_id = per.per_id
                    WHERE med_estado = 'ACTIVO'
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

    private Medico map(ResultSet rs) throws SQLException {

        Medico m = new Medico();

        m.setId(rs.getInt("per_id"));
        m.setPrimerNombre(rs.getString("per_primer_nombre"));
        m.setPrimerApellido(rs.getString("per_primer_apellido"));
        m.setTipoProfesional(rs.getString("med_tipo_profesional"));
        m.setEstado(MedicoEstado.valueOf(rs.getString("med_estado")));

        return m;
    }
}
