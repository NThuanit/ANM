/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author adm
 */
package com.elgamalencryption;

import com.elgamalencryption.controller.ElGamalController;
import com.elgamalencryption.model.ElGamalModel;
import com.elgamalencryption.view.ElGamalView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ElGamalModel model = new ElGamalModel();
                ElGamalView view = new ElGamalView();
                ElGamalController controller = new ElGamalController(model, view);
                view.setVisible(true);
            }
        });
    }
}