/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elgamalencryption.view;

/**
 *
 * @author adm
 */


import javax.swing.*;
import java.awt.*;

public class ElGamalView extends JFrame {
    public JTextArea txtBanRo, txtBanMa, txtKetQua;
    public JTextField txtP, txtG, txtX, txtY, txtK;
    private JButton btnTaoKhoa, btnMaHoa, btnGiaiMa, btnMoFile, btnLuuFile;
    private JRadioButton rdTuDong, rdTuChon;

    public ElGamalView() {
        setTitle("ElGamal Encryption");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel(new GridLayout(2, 5));
        pnlTop.add(new JLabel("P:"));
        pnlTop.add(txtP = new JTextField());
        pnlTop.add(new JLabel("G:"));
        pnlTop.add(txtG = new JTextField());
        pnlTop.add(new JLabel("X:"));
        pnlTop.add(txtX = new JTextField());
        pnlTop.add(new JLabel("Y:"));
        pnlTop.add(txtY = new JTextField());
        pnlTop.add(new JLabel("K:"));
        pnlTop.add(txtK = new JTextField());

        JPanel pnlCenter = new JPanel(new GridLayout(1, 3));
        pnlCenter.add(new JScrollPane(txtBanRo = new JTextArea()));
        pnlCenter.add(new JScrollPane(txtBanMa = new JTextArea()));
        pnlCenter.add(new JScrollPane(txtKetQua = new JTextArea()));

        JPanel pnlBottom = new JPanel();
        pnlBottom.add(btnTaoKhoa = new JButton("Tạo khóa"));
        pnlBottom.add(btnMaHoa = new JButton("Mã hóa"));
        pnlBottom.add(btnGiaiMa = new JButton("Giải mã"));
        pnlBottom.add(btnMoFile = new JButton("Mở file"));
        pnlBottom.add(btnLuuFile = new JButton("Lưu file"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(rdTuDong = new JRadioButton("Tự động", true));
        bg.add(rdTuChon = new JRadioButton("Tự chọn"));
        pnlBottom.add(rdTuDong);
        pnlBottom.add(rdTuChon);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    // Getters for components
    public JTextArea getTxtBanRo() { return txtBanRo; }
    public JTextArea getTxtBanMa() { return txtBanMa; }
    public JTextArea getTxtKetQua() { return txtKetQua; }
    public JTextField getTxtP() { return txtP; }
    public JTextField getTxtG() { return txtG; }
    public JTextField getTxtX() { return txtX; }
    public JTextField getTxtY() { return txtY; }
    public JTextField getTxtK() { return txtK; }
    public JButton getBtnTaoKhoa() { return btnTaoKhoa; }
    public JButton getBtnMaHoa() { return btnMaHoa; }
    public JButton getBtnGiaiMa() { return btnGiaiMa; }
    public JButton getBtnMoFile() { return btnMoFile; }
    public JButton getBtnLuuFile() { return btnLuuFile; }
    public JRadioButton getRdTuDong() { return rdTuDong; }
    public JRadioButton getRdTuChon() { return rdTuChon; }
}
