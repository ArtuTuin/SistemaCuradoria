package com.curadoria.controller;

import com.curadoria.dao.UserDAO;
import com.curadoria.model.User;
import com.curadoria.utils.HashUtil;

public class AuthController {

    private UserDAO userDAO = new UserDAO();

    public User authenticate(String email, String senha){
        User user = userDAO.findByEmail(email);

        if(user == null){
            return null;
        }

        String hash = HashUtil.sha256(senha);

        if(hash.equals(user.getSenhaHash())){
            return user;
        }

        return null;
    }
}
