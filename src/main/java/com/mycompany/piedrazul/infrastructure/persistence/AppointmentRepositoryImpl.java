package com.mycompany.piedrazul.infrastructure.persistence;

import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.AppointmentStatus;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
import com.mycompany.piedrazul.domain.repository.IMedicoRepository;
import com.mycompany.piedrazul.domain.repository.IPacienteRepository;
import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositoryImpl implements IAppointmentRepository {

    private final IUsuarioRepository usuarioRepository;
    private final IPacienteRepository pacienteRepository;
    private final IMedicoRepository medicoRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AppointmentRepositoryImpl(IUsuarioRepository usuarioRepository, IPacienteRepository pacienteRepository, IMedicoRepository medicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {

        String sql = """
                INSERT INTO Cita (usu_id, paciente_id, medico_id, fecha_hora_cita, cita_estado, observacion)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, appointment.getCreadoPor().getId());
            stmt.setInt(2, appointment.getPaciente().getId());
            stmt.setInt(3, appointment.getMedico().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(appointment.getFechaHora()));
            stmt.setString(5, appointment.getEstado().name());
            stmt.setString(6, appointment.getObservacion());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                appointment.setId(rs.getInt(1));
            }

            return appointment;

        } catch (SQLException e) {

            // CLAVE: manejo de simultaneidad
            if (e.getSQLState().equals("23505")) { // unique violation
                throw new RuntimeException("Horario no disponible");
            }

            throw new RuntimeException("Error al guardar cita", e);
        }
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM citas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return map(rs);
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

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(map(rs));
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

        try (Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(map(rs));
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

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String now = LocalDateTime.now().format(formatter);
            stmt.setInt(1, user.getId());
            stmt.setInt(2, user.getId());
            stmt.setInt(3, user.getId());
            stmt.setString(4, now);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(map(rs));
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

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String now = LocalDateTime.now().format(formatter);
            stmt.setInt(1, user.getId());
            stmt.setInt(2, user.getId());
            stmt.setInt(3, user.getId());
            stmt.setString(4, now);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public boolean update(Appointment appointment) {
        String sql = "UPDATE citas SET date_time = ?, status = ?, notes = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, appointment.getFechaHora().format(formatter));
            stmt.setString(2, appointment.getEstado().name());
            stmt.setString(3, appointment.getObservacion());
            stmt.setInt(4, appointment.getId());
            int affected = stmt.executeUpdate();
            System.out.println("Actualizando cita " + appointment.getId() + " a estado: " + appointment.getEstado());
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancel(int id) {
        String sql = "UPDATE citas SET status = 'CANCELLED' WHERE id = ?";

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
    public boolean delete(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Appointment map(ResultSet rs) throws SQLException {

        Appointment a = new Appointment();

        a.setId(rs.getInt("cita_id"));

        int pacienteId = rs.getInt("paciente_id");
        int medicoId = rs.getInt("medico_id");
        int usuarioId = rs.getInt("usu_id");

        a.setPaciente(pacienteRepository.findById(pacienteId));
        a.setMedico(medicoRepository.findById(medicoId));
        a.setCreadoPor(usuarioRepository.findById(usuarioId));

        a.setFechaHora(rs.getTimestamp("fecha_hora_cita").toLocalDateTime());
        a.setEstado(AppointmentStatus.valueOf(rs.getString("cita_estado")));
        a.setObservacion(rs.getString("observacion"));

        return a;
    }

    @Override
    public List<Appointment> findByCreatedBy(Usuario createdBy) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE created_by_id = ? ORDER BY date_time DESC";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, createdBy.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public boolean existsByPacienteAndFecha(int pacienteId, LocalDateTime fechaHora) {

        String sql = """
                SELECT 1 FROM Cita
                WHERE paciente_id = ? AND fecha_hora_cita = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaHora));

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error validando cita", e);
        }
    }
}