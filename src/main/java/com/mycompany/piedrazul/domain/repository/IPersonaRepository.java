/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.repository;

import com.mycompany.piedrazul.domain.model.Persona;
/**
 *
 * @author asus
 */
public interface IPersonaRepository {
    Persona create(Persona persona);

    Persona findById(int id);

    boolean dniExists(int dni);
}
