// package com.mycompany.piedrazul.domain.service;

// import com.mycompany.piedrazul.domain.model.AppointmentStatus;
// import com.mycompany.piedrazul.domain.model.Appointment;
// import com.mycompany.piedrazul.domain.model.Usuario;
// import com.mycompany.piedrazul.domain.repository.IAppointmentRepository;
// import java.util.List;

// public class AppointmentService {
    
//     private final IAppointmentRepository appointmentRepository;
    
//     public AppointmentService(IAppointmentRepository appointmentRepository) {
//         this.appointmentRepository = appointmentRepository;
//     }
    
//     public Appointment crearCita(Appointment appointment) {
//         if (appointment == null) {
//             throw new IllegalArgumentException("La cita no puede ser nula");
//         }
//         if (appointment.getPatient() == null || appointment.getPatient().getId() <= 0) {
//             throw new IllegalArgumentException("Paciente inválido");
//         }
//         if (appointment.getProfessional() == null || appointment.getProfessional().getId() <= 0) {
//             throw new IllegalArgumentException("Profesional inválido");
//         }
//         if (appointment.getDateTime() == null) {
//             throw new IllegalArgumentException("Fecha y hora requeridas");
//         }
        
//         return appointmentRepository.save(appointment);
//     }
    
//     public List<Appointment> obtenerCitasPaciente(Usuario paciente) {
//         return appointmentRepository.findByPatient(paciente);
//     }
    
//     public List<Appointment> obtenerCitasProfesional(Usuario profesional) {
//         return appointmentRepository.findByProfessional(profesional);
//     }
    
//     public List<Appointment> obtenerProximasCitas(Usuario usuario) {
//         return appointmentRepository.findUpcoming(usuario);
//     }
    
//     public List<Appointment> obtenerHistorial(Usuario usuario) {
//         return appointmentRepository.findHistory(usuario);
//     }
    
//     public boolean confirmarCita(int id) {
//         Appointment cita = appointmentRepository.findById(id);
//         if (cita != null) {
//             cita.setStatus(AppointmentStatus.CONFIRMED);
//            return appointmentRepository.update(cita);
//         }
//         return false;
//     }
    
//     public boolean cancelarCita(int id) {
//         return appointmentRepository.cancel(id);
//     }
    
//     public boolean reprogramarCita(Appointment nuevaCita) {
//         if (nuevaCita.getOriginalAppointment() == null) {
//             throw new IllegalArgumentException("Debe especificar la cita original");
//         }
//         return appointmentRepository.save(nuevaCita) != null;
//     }
// }