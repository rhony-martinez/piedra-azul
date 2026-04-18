/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.model;

/**
 *
 * @author asus
 */
public class Medico extends Persona {
    private String tipoProfesional;
    private MedicoEstado estado;

    public Medico() {
    }

    public String getTipoProfesional() {
        return tipoProfesional;
    }

    public void setTipoProfesional(String tipoProfesional) {
        this.tipoProfesional = tipoProfesional;
    }

    public MedicoEstado getEstado() {
        return estado;
    }

    public void setEstado(MedicoEstado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return getPrimerNombre() + " " + getPrimerApellido() + " - " + getTipoProfesional();
    }
}
