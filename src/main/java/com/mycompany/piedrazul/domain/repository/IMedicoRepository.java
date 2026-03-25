/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.repository;

import java.util.List;

import com.mycompany.piedrazul.domain.model.Medico;

/**
 *
 * @author asus
 */
public interface IMedicoRepository {
    boolean create(int personaId, String tipoProfesional);
    public Medico findById(int id);
    public List<Medico> findAll();
}
