/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.repository;

import java.util.List;

import com.mycompany.piedrazul.domain.model.Medico;
import com.mycompany.piedrazul.domain.model.MedicoEstado;

/**
 *
 * @author asus
 */
public interface IMedicoRepository {
    boolean create(int personaId, String tipoProfesional, MedicoEstado estado);
    public Medico findById(int id);
    public List<Medico> findAll();
    public List<Medico> findAllActivos();
}
