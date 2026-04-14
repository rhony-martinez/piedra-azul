/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.piedrazul.domain.repository;

import com.mycompany.piedrazul.domain.model.Notification;
import java.util.List;

/**
 *
 * @author asus
 */
public interface INotificationRepository {

    Notification save(Notification notification);

    List<Notification> findByUsuario(int usuarioId);

    void markAsRead(int notificationId);
}
