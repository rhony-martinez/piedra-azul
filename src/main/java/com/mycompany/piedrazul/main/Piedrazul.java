package com.mycompany.piedrazul.main;

import com.mycompany.piedrazul.domain.builder.*;
//import com.mycompany.piedrazul.domain.model.Appointment;
import com.mycompany.piedrazul.domain.model.Rol;
import com.mycompany.piedrazul.domain.model.Usuario;
import com.mycompany.piedrazul.domain.service.UsuarioService;
import com.mycompany.piedrazul.infrastructure.persistence.PersonaRepositoryImpl;
import com.mycompany.piedrazul.infrastructure.persistence.UsuarioRepositoryImpl;
import com.mycompany.piedrazul.ui.LoginFrame;
import java.time.LocalDateTime;

public class Piedrazul {
    
    private static UsuarioService usuarioService;
    private static LoginFrame loginFrame;
    
    public static void main(String[] args) {
        // Inicializar dependencias
        UsuarioRepositoryImpl ususarioRepository = new UsuarioRepositoryImpl();
        PersonaRepositoryImpl personaRepository = new PersonaRepositoryImpl();
        usuarioService = new UsuarioService(ususarioRepository, personaRepository);
        
        // Probar el patrón Builder antes de iniciar la UI
        //probarBuilderPattern();
        
        // Iniciar interfaz gráfica
        loginFrame = new LoginFrame(usuarioService);
        loginFrame.setVisible(true);
    }
    
    // private static void probarBuilderPattern() {
    //     System.out.println("=== PRUEBA DEL PATRÓN BUILDER PARA CITAS ===\n");
        
    //     try {
    //         // Crear algunos usuarios de prueba (simulados, no se guardan en BD)
    //         Usuario paciente = new Usuario("paciente1", "", "Juan Pérez", Rol.PACIENTE);
    //         Usuario medico = new Usuario("medico1", "", "Dra. María Gómez", Rol.MEDICO_TERAPISTA);
    //         Usuario agendador = new Usuario("agendador1", "", "Carlos Rodríguez", Rol.AGENDADOR);
            
    //         // 1. Prueba de Cita Manual (por agendador)
    //         System.out.println("1. Creando cita MANUAL (por agendador):");
    //         AppointmentDirector director = new AppointmentDirector();
    //         ManualAppointmentBuilder manualBuilder = new ManualAppointmentBuilder();
    //         director.setAppointmentBuilder(manualBuilder);
            
    //         Appointment citaManual = director.buildManualAppointment(
    //             paciente, 
    //             medico, 
    //             LocalDateTime.now().plusDays(3), 
    //             "CONSULTA", 
    //             "Dolor de cabeza persistente", 
    //             "Primera consulta", 
    //             agendador
    //         );
    //         System.out.println("   ✓ Cita manual creada: " + citaManual);
            
    //         // 2. Prueba de Cita por Autoservicio (paciente)
    //         System.out.println("\n2. Creando cita AUTOSERVICIO (por paciente):");
    //         SelfServiceAppointmentBuilder selfBuilder = new SelfServiceAppointmentBuilder();
    //         director.setAppointmentBuilder(selfBuilder);
            
    //         Appointment citaAutoservicio = director.buildSelfServiceAppointment(
    //             paciente, 
    //             medico, 
    //             LocalDateTime.now().plusDays(5).withHour(10), 
    //             "CONTROL", 
    //             "Control de rutina", 
    //             paciente
    //         );
    //         System.out.println("   ✓ Cita autoservicio creada: " + citaAutoservicio);
            
    //         // 3. Prueba de Reprogramación
    //         System.out.println("\n3. Reprogramando cita:");
    //         RescheduledAppointmentBuilder rescheduleBuilder = new RescheduledAppointmentBuilder(citaManual);
    //         director.setAppointmentBuilder(rescheduleBuilder);
            
    //         Appointment citaReprogramada = director.buildRescheduledAppointmentSameProfessional(
    //             citaManual, 
    //             LocalDateTime.now().plusDays(10), 
    //             agendador
    //         );
    //         System.out.println("   ✓ Cita reprogramada: " + citaReprogramada);
    //         System.out.println("   ✓ Cita original: " + citaReprogramada.getOriginalAppointment().getDateTime());
            
    //         System.out.println("\n=== PRUEBA COMPLETADA EXITOSAMENTE ===\n");
            
    //     } catch (Exception e) {
    //         System.out.println("❌ Error en prueba: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    // }
    
    public static UsuarioService getUsuarioService() {
        return usuarioService;
    }
}