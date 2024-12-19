/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elgamalencryption.controller;

/**
 *
 * @author adm
 */
import com.elgamalencryption.model.ElGamalModel;
import com.elgamalencryption.view.ElGamalView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class ElGamalController {

    private ElGamalModel model;
    private ElGamalView view;

    public ElGamalController(ElGamalModel model, ElGamalView view) {
        this.model = model;
        this.view = view;

        this.view.getBtnTaoKhoa().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taoKhoa();
            }
        });

        this.view.getBtnMaHoa().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maHoa();
            }
        });

        this.view.getBtnGiaiMa().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                giaiMa();
            }
        });

        this.view.getBtnMoFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moFile();
            }
        });

        this.view.getBtnLuuFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                luuFile();
            }
        });
    }

    private void taoKhoa() {
        if (view.getRdTuDong().isSelected()) {
            model.generateKeys(512);
            updateKeyFields();
        } else {
            try {
                BigInteger p = new BigInteger(view.getTxtP().getText());
                model.setP(p);
                model.setG(new BigInteger(view.getTxtG().getText()));
                model.setX(new BigInteger(view.getTxtX().getText()));
                model.setY(model.getG().modPow(model.getX(), model.getP()));
                model.setK(new BigInteger(view.getTxtK().getText()));
                updateKeyFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập các số hợp lệ cho khóa.");
            }
        }
    }

    private void updateKeyFields() {
        view.getTxtP().setText(model.getP().toString());
        view.getTxtG().setText(model.getG().toString());
        view.getTxtX().setText(model.getX().toString());
        view.getTxtY().setText(model.getY().toString());
        view.getTxtK().setText(model.getK().toString());
    }

    private void maHoa() {
        String banRo = view.getTxtBanRo().getText();
        if (!banRo.isEmpty()) {
            String banMa = model.encrypt(banRo);
            view.getTxtBanMa().setText(banMa);
        } else {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập bản rõ trước khi mã hóa.");
        }
    }

    private void giaiMa() {
        String banMa = view.getTxtBanMa().getText();
        if (!banMa.isEmpty()) {
            String ketQua = model.decrypt(banMa);
            view.getTxtKetQua().setText(ketQua);
        } else {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập bản mã trước khi giải mã.");
        }
    }

    private void moFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(view);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileReader reader = new FileReader(file)) {
                StringBuilder content = new StringBuilder();
                int ch;
                while ((ch = reader.read()) != -1) { // Đọc từng ký tự cho đến khi hết file
                    content.append((char) ch);
                }
                view.txtBanRo.setText(content.toString()); // Hiển thị nội dung
                JOptionPane.showMessageDialog(view, "Lấy file thành công.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi! Không thể lấy file!");
            }
        }
    }

//    private String readPdfContent(File file) throws IOException {
//        try (PDDocument document = PDDocument.load(file)) {
//            PDFTextStripper stripper = new PDFTextStripper();
//            return stripper.getText(document);
//        }
//    }
    private void luuFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(view);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(view.getTxtBanMa().getText().getBytes());
                JOptionPane.showMessageDialog(view, "Lưu file thành công.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi! Không thể lưu file!");
            }
        }
    }

}
