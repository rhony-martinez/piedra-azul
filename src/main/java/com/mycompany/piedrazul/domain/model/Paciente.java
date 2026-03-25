/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.model;

/**
 *
 * @author asus
 */
public class Paciente extends Persona {

    public Paciente() {
        super();
    }

    @Override
    public String toString() {
        return getPrimerNombre() + " " + getPrimerApellido() + " - " + getDni();
    }
}
