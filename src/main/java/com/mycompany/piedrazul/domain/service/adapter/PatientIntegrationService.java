/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.adapter;

import com.mycompany.piedrazul.domain.model.Paciente;

/**
 *
 * @author asus
 */
public class PatientIntegrationService {

    private final PatientDataProvider provider;

    public PatientIntegrationService(PatientDataProvider provider) {
        this.provider = provider;
    }

    public Paciente obtenerPacienteExterno() {
        return provider.getPaciente();
    }
}
