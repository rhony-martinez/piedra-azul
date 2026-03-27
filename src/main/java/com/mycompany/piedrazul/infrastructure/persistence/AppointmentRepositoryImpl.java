package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Persona;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositoryImpl implements IAppointmentRepository {

    private final IUsuarioRepository usuarioRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AppointmentRepositoryImpl(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getPatient() == null || appointment.getPatient().getId() == 0) {
            System.out.println("ERROR: Patient ID es 0 o null");
            return null;
        }
        if (appointment.getProfessional() == null || appointment.getProfessional().getId() == 0) {
            System.out.println("ERROR: Professional ID es 0 o null");
            return null;
        }

        String sql = "INSERT INTO citas (patient_id, professional_id, date_time, appointment_type, "
                + "status, reason, notes, created_by_id, created_at, original_appointment_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, appointment.getPatient().getId());
            stmt.setInt(2, appointment.getProfessional().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getDateTime()));
            stmt.setString(4, appointment.getAppointmentType());
            stmt.setString(5, appointment.getStatus().name());
            stmt.setString(6, appointment.getReason());
            stmt.setString(7, appointment.getNotes());
            stmt.setInt(8, appointment.getCreatedBy().getId());
            stmt.setString(9, appointment.getCreatedAt().format(formatter));

            if (appointment.getOriginalAppointment() != null) {
                stmt.setInt(10, appointment.getOriginalAppointment().getId());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return appointment;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM citas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Appointment> findByPatient(Usuario patient) {
        return findByUserField("patient_id", patient.getId());
    }

    @Override
    public List<Appointment> findByProfessional(Usuario professional) {
        return findByUserField("professional_id", professional.getId());
    }

    private List<Appointment> findByUserField(String fieldName, int userId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE " + fieldName + " = ? ORDER BY date_time DESC";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY date_time DESC";

        try (Connection conn = ConnectionFactory.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findUpcoming(Usuario user) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE (patient_id = ? OR professional_id = ? OR created_by_id = ?) "
                + "AND date_time >= ? AND status != 'CANCELLED' AND status != 'COMPLETED' "
                + "ORDER BY date_time ASC";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String now = LocalDateTime.now().format(formatter);
            stmt.setInt(1, user.getId());
            stmt.setInt(2, user.getId());
            stmt.setInt(3, user.getId());
            stmt.setString(4, now);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> findHistory(Usuario user) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE (patient_id = ? OR professional_id = ? OR created_by_id = ?) "
                + "AND (date_time < ? OR status = 'COMPLETED' OR status = 'CANCELLED') "
                + "ORDER BY date_time DESC";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String now = LocalDateTime.now().format(formatter);
            stmt.setInt(1, user.getId());
            stmt.setInt(2, user.getId());
            stmt.setInt(3, user.getId());
            stmt.setString(4, now);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public boolean update(Appointment appointment) {
        String sql = "UPDATE citas SET date_time = ?, status = ?, notes = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getDateTime().format(formatter));
            stmt.setString(2, appointment.getStatus().name());
            stmt.setString(3, appointment.getNotes());
            stmt.setInt(4, appointment.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancel(int id) {
        String sql = "UPDATE citas SET status = 'CANCELLED' WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Appointment> findByCreatedBy(Usuario createdBy) {
        return findByUserField("created_by_id", createdBy.getId());
    }

    @Override
    public List<Appointment> listarPorMedicoYFecha(String medico, String fecha) {
        List<Appointment> lista = new ArrayList<>();
        String sql = "SELECT c.cita_id, c.fecha_hora_cita, c.observacion, c.cita_estado, "
                + "p.per_primer_nombre, p.per_primer_apellido, "
                + "m.per_primer_nombre AS med_nombre, m.per_primer_apellido AS med_apellido "
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
                Appointment cita = new Appointment();
                cita.setId(rs.getInt("cita_id"));
                cita.setDateTime(rs.getTimestamp("fecha_hora_cita").toLocalDateTime());
                cita.setReason(rs.getString("observacion"));

                String estadoBD = rs.getString("cita_estado");
                cita.setStatus(convertirEstado(estadoBD));

                Usuario paciente = new Usuario();
                Persona personaPaciente = new Persona();

                personaPaciente.setPrimerNombre(rs.getString("per_primer_nombre"));
                personaPaciente.setPrimerApellido(rs.getString("per_primer_apellido"));

                paciente.setPersona(personaPaciente);
                cita.setPatient(paciente);

// MEDICO
                Usuario medicoObj = new Usuario();
                Persona personaMedico = new Persona();

                personaMedico.setPrimerNombre(rs.getString("med_nombre"));
                personaMedico.setPrimerApellido(rs.getString("med_apellido"));

                medicoObj.setPersona(personaMedico);
                cita.setProfessional(medicoObj);
                lista.add(cita);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private AppointmentStatus convertirEstado(String estado) {
        if (estado == null) {
            return null;
        }
        switch (estado) {
            case "PROGRAMADA":
                return AppointmentStatus.SCHEDULED;
            case "CANCELADA":
                return AppointmentStatus.CANCELLED;
            case "ATENDIDA":
                return AppointmentStatus.COMPLETED;
            case "NO_ASISTIDA":
                return AppointmentStatus.NO_SHOW;
            default:
                return AppointmentStatus.SCHEDULED;
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));

        appointment.setPatient(usuarioRepository.findById(rs.getInt("patient_id")));
        appointment.setProfessional(usuarioRepository.findById(rs.getInt("professional_id")));
        appointment.setCreatedBy(usuarioRepository.findById(rs.getInt("created_by_id")));

        appointment.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        appointment.setAppointmentType(rs.getString("appointment_type"));
        appointment.setStatus(AppointmentStatus.valueOf(rs.getString("status")));
        appointment.setReason(rs.getString("reason"));
        appointment.setNotes(rs.getString("notes"));
        appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return appointment;
    }
}
