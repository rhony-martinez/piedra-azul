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
import com.mycompany.piedrazul.domain.state.ConfirmadaState;
import com.mycompany.piedrazul.domain.state.ProgramadaState;
import com.mycompany.piedrazul.domain.state.AppointmentState;
import com.mycompany.piedrazul.domain.state.AtendidaState;
import com.mycompany.piedrazul.domain.state.CanceladaState;
import com.mycompany.piedrazul.domain.state.NoAsistidaState;
import com.mycompany.piedrazul.domain.state.ReagendadaState;

public class AppointmentRepositoryImpl implements IAppointmentRepository {

    private final IUsuarioRepository usuarioRepository;
    private final IPacienteRepository pacienteRepository;
    private final IMedicoRepository medicoRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AppointmentRepositoryImpl(IUsuarioRepository usuarioRepository, IPacienteRepository pacienteRepository,
            IMedicoRepository medicoRepository) {
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
            throw new RuntimeException("Error al guardar cita", e);
        }
    }

    @Override
    public Appointment findById(int id) {
        System.out.println("🔍 findById() llamado con ID: " + id);

        String sql = "SELECT * FROM Cita WHERE cita_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Cita encontrada en BD");
                return map(rs);
            } else {
                System.out.println("❌ No se encontró cita con ID: " + id);
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
        String sql = "SELECT * FROM Cita WHERE " + fieldName + " = ? ORDER BY date_time DESC";

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
        // Cambia "citas" por "Cita" o el nombre correcto de tu tabla
        String sql = "SELECT * FROM Cita ORDER BY fecha_hora_cita DESC";

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
        String sql = "SELECT * FROM Cita WHERE (patient_id = ? OR professional_id = ? OR created_by_id = ?) "
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
        String sql = "SELECT * FROM Cita WHERE (patient_id = ? OR professional_id = ? OR created_by_id = ?) "
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
        System.out.println("=== ACTUALIZANDO CITA EN BD ===");
        System.out.println("ID: " + appointment.getId());
        System.out.println("Fecha/Hora: " + appointment.getFechaHora());
        System.out.println("Estado EN JAVA: '" + appointment.getEstado().name() + "'");
        System.out.println("Observación: " + appointment.getObservacion());

        // Verificar caracteres ocultos en el estado
        String estadoStr = appointment.getEstado().name();
        System.out.println("Longitud del estado: " + estadoStr.length());
        for (int i = 0; i < estadoStr.length(); i++) {
            char c = estadoStr.charAt(i);
            System.out.println("  Char[" + i + "]: '" + c + "' - Código Unicode: " + (int) c);
        }

        String sql = """
                    UPDATE Cita
                    SET fecha_hora_cita = ?, cita_estado = ?, observacion = ?
                    WHERE cita_id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getFechaHora()));
            stmt.setString(2, appointment.getEstado().name());
            stmt.setString(3, appointment.getObservacion());
            stmt.setInt(4, appointment.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cita", e);
        }
    }

    @Override
    public boolean cancel(int id) {

        String sql = "UPDATE Cita SET cita_estado = 'CANCELADA' WHERE cita_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al cancelar cita", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Cita WHERE id = ?";

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
        a.setObservacion(rs.getString("observacion"));

        // reconstruir el State
        AppointmentStatus status = AppointmentStatus.valueOf(rs.getString("cita_estado"));
        a.setAppointmentState(convertToState(status));

        return a;
    }

    @Override
    public List<Appointment> findByCreatedBy(Usuario createdBy) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Cita WHERE created_by_id = ? ORDER BY date_time DESC";
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
                WHERE paciente_id = ?
                AND fecha_hora_cita >= ?
                AND fecha_hora_cita < ?
                """;

        LocalDateTime inicio = fechaHora.withSecond(0).withNano(0);
        LocalDateTime fin = inicio.plusMinutes(1);

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            stmt.setTimestamp(2, Timestamp.valueOf(inicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fin));

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error validando cita", e);
        }
    }

    @Override
    public boolean existsByMedicoAndFechaHora(int medicoId, LocalDateTime fechaHora) {
        // Redondear la fecha/hora a minutos (ignorar segundos)
        LocalDateTime fechaHoraSinSegundos = fechaHora.withSecond(0).withNano(0);

        String sql = "SELECT COUNT(*) FROM Cita WHERE medico_id = ? AND DATE_TRUNC('minute', fecha_hora_cita) = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicoId);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaHoraSinSegundos));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(
                        "Citas encontradas para médico " + medicoId + " a las " + fechaHoraSinSegundos + ": " + count);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private AppointmentState convertToState(AppointmentStatus status) {
        return switch (status) {
            case PROGRAMADA -> new ProgramadaState();
            case CONFIRMADA -> new ConfirmadaState();
            case CANCELADA -> new CanceladaState();
            case ATENDIDA -> new AtendidaState();
            case NO_ASISTIDA -> new NoAsistidaState();
            case REAGENDADA -> new ReagendadaState();
        };
    }
}
