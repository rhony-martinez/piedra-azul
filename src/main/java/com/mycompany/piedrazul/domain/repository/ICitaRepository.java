/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.repository;

import java.sql.*;
import java.util.*;
import com.mycompany.piedrazul.domain.model.Cita;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;

/**
 *
 * @author si812
 */
public class ICitaRepository {

    public List<Cita> listar(String medico, String fecha) {
        List<Cita> lista = new ArrayList<>();

        String sql
                = "SELECT "
                + "c.fecha_hora_cita::DATE AS fecha, "
                + "c.fecha_hora_cita::TIME AS hora, "
                + "(p.per_primer_nombre || ' ' || p.per_primer_apellido) AS paciente, "
                + "(m.per_primer_nombre || ' ' || m.per_primer_apellido) AS medico, "
                + "c.observacion AS descripcion "
                + "FROM Cita c "
                + "INNER JOIN Persona p ON p.per_id = c.paciente_id "
                + "INNER JOIN Persona m ON m.per_id = c.medico_id "
                + "WHERE m.per_primer_nombre ILIKE ? "
                + "AND c.fecha_hora_cita::DATE = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + medico + "%");
            ps.setDate(2, java.sql.Date.valueOf(fecha));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cita c = new Cita();
                c.setFecha(rs.getString("fecha"));
                c.setHora(rs.getString("hora"));
                c.setPaciente(rs.getString("paciente"));
                c.setMedico(rs.getString("medico"));
                c.setDescripcion(rs.getString("descripcion"));

                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
