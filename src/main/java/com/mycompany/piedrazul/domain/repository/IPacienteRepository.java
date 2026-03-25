/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.repository;

import java.util.List;

import com.mycompany.piedrazul.domain.model.Paciente;

/**
 *
 * @author asus
 */
public interface IPacienteRepository {
    boolean create(int personaId);
    public Paciente findById(int id);
    public Paciente findByDni(int dni);
    public List<Paciente> findAll();
}
