package com.mycompany.piedrazul.domain.repository;
import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentRepository {
    Appointment save(Appointment appointment);
    Appointment findById(int id);
    List<Appointment> findByPatient(Usuario patient);
    List<Appointment> findByProfessional(Usuario professional);
    List<Appointment> findByCreatedBy(Usuario createdBy);
    List<Appointment> findAll();
    List<Appointment> findUpcoming(Usuario user);
    List<Appointment> findHistory(Usuario user);
    boolean update(Appointment appointment);
    boolean cancel(int id);
    boolean delete(int id);
    boolean existsByPacienteAndFecha(int pacienteId, LocalDateTime fechaHora);
}