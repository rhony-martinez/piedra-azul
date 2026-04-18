/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.service.adapter;

/**
 *
 * @author asus
 */
public class ExternalService {

    public String getPatientData() {
        return """
        {
            "name": "Jose Antonio Lopez Perez",
            "dni": 12345678,
            "telefono": "3001234567",
            "correo": "jose@example.com"
        }
        """;
    }
}
