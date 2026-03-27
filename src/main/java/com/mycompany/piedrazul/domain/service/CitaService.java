/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service;
import java.util.List;
import com.mycompany.piedrazul.domain.model.Cita;
import com.mycompany.piedrazul.domain.repository.ICitaRepository;
/**
 *
 * @author si812
 */


public class CitaService {

    private final ICitaRepository repo = new ICitaRepository();

    public List<Cita> listar(String medico, String fecha) {
        return repo.listar(medico, fecha);
    }
}