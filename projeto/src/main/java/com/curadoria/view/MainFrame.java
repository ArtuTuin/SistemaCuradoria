package com.curadoria.view;

import com.curadoria.model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(User user){
        setTitle("Curadoria - Bem-vindo(a), " + user.getNome());
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(35,35,35));

        if(user.getTipo().equals("ADMIN")){
            setContentPane(new AdminPanel());
        } else {
            setContentPane(new UserPanel(user));
        }
    }
}

