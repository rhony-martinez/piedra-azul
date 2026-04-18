/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.adapter;

import com.mycompany.piedrazul.domain.model.Paciente;
import java.time.LocalDate;

import org.json.JSONObject;

/**
 *
 * @author asus
 */

public class ExternalPatientAdapter implements PatientDataProvider {

    private final ExternalService externalService;

    public ExternalPatientAdapter(ExternalService externalService) {
        this.externalService = externalService;
    }

    @Override
    public Paciente getPaciente() {

        String json = externalService.getPatientData();
        JSONObject obj = new JSONObject(json);

        String nombreCompleto = obj.getString("name");
        String[] partes = nombreCompleto.split(" ");

        Paciente paciente = new Paciente();

        // Mapear nombres 
        paciente.setPrimerNombre(partes.length > 0 ? partes[0] : "");
        paciente.setSegundoNombre(partes.length > 2 ? partes[1] : "");
        paciente.setPrimerApellido(partes.length > 1 ? partes[partes.length - 2] : "");
        paciente.setSegundoApellido(partes.length > 2 ? partes[partes.length - 1] : "");

        // Otros datos
        paciente.setDni(obj.getInt("dni"));
        paciente.setTelefono(obj.getString("telefono"));
        paciente.setCorreo(obj.getString("correo"));

        // Datos obligatorios que NO vienen del JSON
        paciente.setGenero("OTRO"); // default controlado
        paciente.setFechaNacimiento(LocalDate.of(2000, 1, 1)); // dummy controlado

        return paciente;
    }
}
