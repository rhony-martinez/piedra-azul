// package com.mycompany.piedrazul.infrastructure.persistence;

// import com.mycompany.piedrazul.domain.model.Appointment;
// import com.mycompany.piedrazul.domain.model.AppointmentStatus;
// import com.mycompany.piedrazul.domain.model.Usuario;
// import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
// import com.mycompany.piedrazul.domain.repository.IUsuarioRepository;
// import com.mycompany.piedrazul.infrastructure.persistence.connection.ConnectionFactory;
// import java.sql.*;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.List;


// public class AppointmentRepositoryImpl implements IAppointmentRepository {
    
//     //private final UsuarioRepositoryImpl usuarioRepository;
//     private final IUsuarioRepository usuarioRepository;
//     private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
//     public AppointmentRepositoryImpl(IUsuarioRepository usuarioRepository) {
//         this.usuarioRepository = usuarioRepository;
//     }
    
//     /*private void crearTablaSiNoExiste() {
//         String sql = "CREATE TABLE IF NOT EXISTS citas ("
//                 + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
//                 + "patient_id INTEGER NOT NULL,"
//                 + "professional_id INTEGER NOT NULL,"
//                 + "date_time TEXT NOT NULL,"
//                 + "appointment_type TEXT NOT NULL,"
//                 + "status TEXT NOT NULL,"
//                 + "reason TEXT,"
//                 + "notes TEXT,"
//                 + "created_by_id INTEGER NOT NULL,"
//                 + "created_at TEXT NOT NULL,"
//                 + "original_appointment_id INTEGER,"
//                 + "FOREIGN KEY (patient_id) REFERENCES usuarios(id),"
//                 + "FOREIGN KEY (professional_id) REFERENCES usuarios(id),"
//                 + "FOREIGN KEY (created_by_id) REFERENCES usuarios(id)"
//                 + ")";
        
//         try (Connection conn = getConnection();
//              Statement stmt = conn.createStatement()) {
//             stmt.execute(sql);
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }*/
    
//     @Override
//     public Appointment save(Appointment appointment) {
//         // Verificar que los IDs no sean null o 0
//         if (appointment.getPatient() == null || appointment.getPatient().getId() == 0) {
//             System.out.println("ERROR: Patient ID es 0 o null");
//             return null;
//         }
//         if (appointment.getProfessional() == null || appointment.getProfessional().getId() == 0) {
//             System.out.println("ERROR: Professional ID es 0 o null");
//             return null;
//         }

//         String sql = "INSERT INTO citas (patient_id, professional_id, date_time, appointment_type, "
//                 + "status, reason, notes, created_by_id, created_at, original_appointment_id) "
//                 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

//             System.out.println("Guardando cita - Patient ID: " + appointment.getPatient().getId());
//             System.out.println("Guardando cita - Professional ID: " + appointment.getProfessional().getId());

//             stmt.setInt(1, appointment.getPatient().getId());
//             stmt.setInt(2, appointment.getProfessional().getId());
//             stmt.setTimestamp(3, Timestamp.valueOf(appointment.getDateTime()));
//             stmt.setString(4, appointment.getAppointmentType());
//             stmt.setString(5, appointment.getStatus().name());
//             stmt.setString(6, appointment.getReason());
//             stmt.setString(7, appointment.getNotes());
//             stmt.setInt(8, appointment.getCreatedBy().getId());
//             stmt.setString(9, appointment.getCreatedAt().format(formatter));

//             if (appointment.getOriginalAppointment() != null) {
//                 stmt.setInt(10, appointment.getOriginalAppointment().getId());
//             } else {
//                 stmt.setNull(10, Types.INTEGER);
//             }

//             int affectedRows = stmt.executeUpdate();

//             if (affectedRows > 0) {
//                 try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                     if (generatedKeys.next()) {
//                         appointment.setId(generatedKeys.getInt(1));
//                         System.out.println("Cita guardada con ID: " + appointment.getId());
//                     }
//                 }
//             }

//             return appointment;

//         } catch (SQLException e) {
//             e.printStackTrace();
//             return null;
//         }
//     }
    
//     @Override
//     public Appointment findById(int id) {
//         String sql = "SELECT * FROM citas WHERE id = ?";
        
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
            
//             stmt.setInt(1, id);
//             ResultSet rs = stmt.executeQuery();
            
//             if (rs.next()) {
//                 return mapResultSetToAppointment(rs);
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return null;
//     }
    
//     @Override
//     public List<Appointment> findByPatient(Usuario patient) {
//         return findByUserField("patient_id", patient.getId());
//     }
    
//     @Override
//     public List<Appointment> findByProfessional(Usuario professional) {
//         return findByUserField("professional_id", professional.getId());
//     }
    
//     private List<Appointment> findByUserField(String fieldName, int userId) {
//         List<Appointment> appointments = new ArrayList<>();
//         String sql = "SELECT * FROM citas WHERE " + fieldName + " = ? ORDER BY date_time DESC";
        
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
            
//             stmt.setInt(1, userId);
//             ResultSet rs = stmt.executeQuery();
            
//             while (rs.next()) {
//                 appointments.add(mapResultSetToAppointment(rs));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return appointments;
//     }
    
//     @Override
//     public List<Appointment> findAll() {
//         List<Appointment> appointments = new ArrayList<>();
//         String sql = "SELECT * FROM citas ORDER BY date_time DESC";
        
//         try (Connection conn = ConnectionFactory.getConnection();
//              Statement stmt = conn.createStatement();
//              ResultSet rs = stmt.executeQuery(sql)) {
            
//             while (rs.next()) {
//                 appointments.add(mapResultSetToAppointment(rs));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return appointments;
//     }
    
//     @Override
//     public List<Appointment> findUpcoming(Usuario user) {
//         List<Appointment> appointments = new ArrayList<>();
//         String sql = "SELECT * FROM citas WHERE (patient_id = ? OR professional_id = ? OR created_by_id = ?) "
//                 + "AND date_time >= ? AND status != 'CANCELLED' AND status != 'COMPLETED' "
//                 + "ORDER BY date_time ASC";
        
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
            
//             String now = LocalDateTime.now().format(formatter);
//             stmt.setInt(1, user.getId());
//             stmt.setInt(2, user.getId());
//             stmt.setInt(3, user.getId());
//             stmt.setString(4, now);
            
//             ResultSet rs = stmt.executeQuery();
            
//             while (rs.next()) {
//                 appointments.add(mapResultSetToAppointment(rs));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return appointments;
//     }
    
//     @Override
//     public List<Appointment> findHistory(Usuario user) {
//         List<Appointment> appointments = new ArrayList<>();
//         String sql = "SELECT * FROM citas WHERE (patient_id = ? OR professional_id = ? OR created_by_id = ?) "
//                 + "AND (date_time < ? OR status = 'COMPLETED' OR status = 'CANCELLED') "
//                 + "ORDER BY date_time DESC";
        
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
            
//             String now = LocalDateTime.now().format(formatter);
//             stmt.setInt(1, user.getId());
//             stmt.setInt(2, user.getId());
//             stmt.setInt(3, user.getId());
//             stmt.setString(4, now);
            
//             ResultSet rs = stmt.executeQuery();
            
//             while (rs.next()) {
//                 appointments.add(mapResultSetToAppointment(rs));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return appointments;
//     }
    
//     @Override
//     public boolean update(Appointment appointment) {
//         String sql = "UPDATE citas SET date_time = ?, status = ?, notes = ? WHERE id = ?";

//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {

//             stmt.setString(1, appointment.getDateTime().format(formatter));
//             stmt.setString(2, appointment.getStatus().name());
//             stmt.setString(3, appointment.getNotes());
//             stmt.setInt(4, appointment.getId());

//             int affected = stmt.executeUpdate();
//             System.out.println("Actualizando cita " + appointment.getId() + " a estado: " + appointment.getStatus());
//             return affected > 0;

//         } catch (SQLException e) {
//             e.printStackTrace();
//             return false;
//         }
//     }
    
//     @Override
//     public boolean cancel(int id) {
//         String sql = "UPDATE citas SET status = 'CANCELLED' WHERE id = ?";
        
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
            
//             stmt.setInt(1, id);
//             return stmt.executeUpdate() > 0;
            
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return false;
//         }
//     }
    
//     @Override
//     public boolean delete(int id) {
//         String sql = "DELETE FROM citas WHERE id = ?";
        
//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
            
//             stmt.setInt(1, id);
//             return stmt.executeUpdate() > 0;
            
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return false;
//         }
//     }
    
//     private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
//         Appointment appointment = new Appointment();
//         appointment.setId(rs.getInt("id"));
        
//         // Cargar relaciones desde la BD
//         int patientId = rs.getInt("patient_id");
//         int professionalId = rs.getInt("professional_id");
//         int createdById = rs.getInt("created_by_id");
        
//         appointment.setPatient(usuarioRepository.findById(patientId));
//         appointment.setProfessional(usuarioRepository.findById(professionalId));
//         appointment.setCreatedBy(usuarioRepository.findById(createdById));
        
//         appointment.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
//         appointment.setAppointmentType(rs.getString("appointment_type"));
//         appointment.setStatus(AppointmentStatus.valueOf(rs.getString("status")));
//         appointment.setReason(rs.getString("reason"));
//         appointment.setNotes(rs.getString("notes"));
//         appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
//         return appointment;
//     }
    
//     @Override
//     public List<Appointment> findByCreatedBy(Usuario createdBy) {
//         List<Appointment> appointments = new ArrayList<>();
//         String sql = "SELECT * FROM citas WHERE created_by_id = ? ORDER BY date_time DESC";

//         try (Connection conn = ConnectionFactory.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {

//             stmt.setInt(1, createdBy.getId());
//             ResultSet rs = stmt.executeQuery();

//             while (rs.next()) {
//                 appointments.add(mapResultSetToAppointment(rs));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return appointments;
//     }
// }