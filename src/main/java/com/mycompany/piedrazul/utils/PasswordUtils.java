package com.mycompany.piedrazul.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Pattern;

public class PasswordUtils {
    
    // Cifrar contraseña con SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al cifrar contraseña", e);
        }
    }
    
    // Validar contraseña según requisitos del taller SOLID
    public static boolean validarContrasena(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean tieneDigito = Pattern.compile("[0-9]").matcher(password).find();
        boolean tieneMayuscula = Pattern.compile("[A-Z]").matcher(password).find();
        boolean tieneEspecial = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find();
        
        return tieneDigito && tieneMayuscula && tieneEspecial;
    }
    
    // Verificar contraseña
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = hashPassword(inputPassword);
        return inputHash.equals(storedHash);
    }
}