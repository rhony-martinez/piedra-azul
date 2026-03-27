package com.mycompany.piedrazul.domain.model;

public class Usuario {

    private int id;
    private String username;
    private String passwordHash;
    private Rol rol;
    private boolean activo;
    private int personaId;
    private int intentosFallidos;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor recomendado (sin id)
    public Usuario(String username, String passwordHash, Rol rol, int personaId) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.personaId = personaId;
        this.activo = true;
        this.intentosFallidos = 0;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    @Override
    public String toString() {
        return username; // ahora sí tiene sentido
    }

    public String getNombreCompleto() {
        return username != null ? username : "N/A";
    }
    private Persona persona;

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
