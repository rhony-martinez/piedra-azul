package com.mycompany.piedrazul.domain.model;

public class Usuario {
    private int id;
    private String username;
    private String passwordHash;
    private String nombreCompleto;
    private Rol rol; // ADMINISTRADOR, MEDICO_TERAPISTA, AGENDADOR
    private boolean activo;
    private int intentosFallidos;
    
    // Constructor vacío
    public Usuario() {}
    
    // Constructor sin id
    public Usuario(String username, String passwordHash, String nombreCompleto, Rol rol) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.activo = true;
        this.intentosFallidos = 0;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol){
        this.rol = rol;
    }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }
}