package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;

import controller.ElgamalDigitalSignatureController;
import model.ElgamalDigitalSignatureModel;

import javax.swing.border.EtchedBorder;
import java.awt.Color;

public class ElgamalDigitalSignatureView extends JFrame {

	public static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JTextPane jtp_X;
	public JTextPane jtp_P;
	public JTextPane jtp_G;
	public JTextPane jtp_Y;
	public JTextPane jtp_VanBanKy;
	public JTextPane textPane_ChuKy;
	public JTextPane textPane_VanBanKy;
	public JTextPane jtp_ThongBao;
	public JTextPane jtp_ChuKy;
	public ElgamalDigitalSignatureModel model = new ElgamalDigitalSignatureModel();
	public JButton btn_SinhKhoa;
	public JButton btn_File;
	public JTextPane textPane_Y;
	public JRadioButton rdbtn_TuDong;
	public JRadioButton rdbtn_TuChon;

	public ElgamalDigitalSignatureView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1425, 850);
		setLocationRelativeTo(null);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ActionListener ac = new ElgamalDigitalSignatureController(this);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Sinh Kh\u00F3a", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 10, 450, 1000);
		contentPane.add(panel);
		panel.setLayout(null);
		
		rdbtn_TuDong = new JRadioButton("Tự động sinh khóa");
		rdbtn_TuDong.setSelected(true);
		rdbtn_TuDong.setFont(new Font("Arial", Font.BOLD, 14));
		rdbtn_TuDong.setBounds(10, 30, 200, 30);
		panel.add(rdbtn_TuDong);
		
		rdbtn_TuChon = new JRadioButton("Tự chọn sinh khóa");
		rdbtn_TuChon.setFont(new Font("Arial", Font.BOLD, 14));
		rdbtn_TuChon.setBounds(210, 30, 200, 30);
		panel.add(rdbtn_TuChon);
		
		ButtonGroup bg_SinhKhoa = new ButtonGroup();
		bg_SinhKhoa.add(rdbtn_TuDong);
		bg_SinhKhoa.add(rdbtn_TuChon);
		
		JLabel lbl_P = new JLabel("Số nguyên tố p:");
		lbl_P.setFont(new Font("Arial", Font.BOLD, 14));
		lbl_P.setBounds(10, 100, 150, 100);
		panel.add(lbl_P);

		jtp_X = new JTextPane();
		jtp_X.setFont(new Font("Arial", Font.PLAIN, 14));
		jtp_X.setBounds(0, 0, 250, 100);
		JScrollPane jsp_X = new JScrollPane(jtp_X, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_X.setBounds(160, 400, 250, 100);
		panel.add(jsp_X);

		jtp_P = new JTextPane();
		jtp_P.setFont(new Font("Arial", Font.PLAIN, 14));
		jtp_P.setBounds(0, 0, 250, 100);
		JScrollPane jsp_P = new JScrollPane(jtp_P, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_P.setBounds(160, 100, 250, 100);
		panel.add(jsp_P);
		
		JLabel lbl_X = new JLabel("Khóa bí mật x:");
		lbl_X.setFont(new Font("Arial", Font.BOLD, 14));
		lbl_X.setBounds(10, 400, 150, 100);
		panel.add(lbl_X);
		
		JLabel lbl_G = new JLabel("Số căn nguyên g:");
		lbl_G.setFont(new Font("Arial", Font.BOLD, 14));
		lbl_G.setBounds(10, 250, 150, 100);
		panel.add(lbl_G);
		
		JLabel lbl_Y = new JLabel("Khóa công khai y:");
		lbl_Y.setFont(new Font("Arial", Font.BOLD, 14));
		lbl_Y.setBounds(10, 550, 150, 100);
		panel.add(lbl_Y);
		
		jtp_G = new JTextPane();
		jtp_G.setFont(new Font("Arial", Font.PLAIN, 14));
		jtp_G.setBounds(0, 0, 250, 100);
		JScrollPane jsp_G = new JScrollPane(jtp_G, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_G.setBounds(160, 250, 250, 100);
		panel.add(jsp_G);
		
		jtp_Y = new JTextPane();
		jtp_Y.setFont(new Font("Arial", Font.PLAIN, 14));
		jtp_Y.setBounds(0, 0, 250, 100);
		JScrollPane jsp_Y = new JScrollPane(jtp_Y, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_Y.setBounds(160, 550, 250, 100);
		panel.add(jsp_Y);
		
		btn_SinhKhoa = new JButton("Sinh khóa");
		btn_SinhKhoa.addActionListener(ac);
		btn_SinhKhoa.setFont(new Font("Arial", Font.BOLD, 14));
		btn_SinhKhoa.setBounds(20, 700, 120, 50);
		panel.add(btn_SinhKhoa);
		
		JButton btn_LuuKhoa = new JButton("Lưu khóa");
		btn_LuuKhoa.addActionListener(ac);
		btn_LuuKhoa.setFont(new Font("Arial", Font.BOLD, 14));
		btn_LuuKhoa.setBounds(150, 700, 120, 50);
		panel.add(btn_LuuKhoa);
		
		JButton btn_ChuyenKhoa = new JButton("Chuyển khóa");
		btn_ChuyenKhoa.setBounds(280, 700, 150, 50);
		panel.add(btn_ChuyenKhoa);
		btn_ChuyenKhoa.setFont(new Font("Arial", Font.BOLD, 14));
		btn_ChuyenKhoa.addActionListener(ac);
		
		JPanel panel_PhatSinhChuKy = new JPanel();
		panel_PhatSinhChuKy.setLayout(null);
		panel_PhatSinhChuKy.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Ph\u00E1t Sinh Ch\u1EEF K\u00FD", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_PhatSinhChuKy.setBounds(480, 10, 450, 1000);
		contentPane.add(panel_PhatSinhChuKy);
		
		JLabel lbl_ChuKy = new JLabel("Chữ ký");
		lbl_ChuKy.setFont(new Font("Arial", Font.BOLD, 18));
		lbl_ChuKy.setBounds(10, 420, 150, 40);
		panel_PhatSinhChuKy.add(lbl_ChuKy);
		
		jtp_VanBanKy = new JTextPane();
		jtp_VanBanKy.setFont(new Font("Arial", Font.PLAIN, 14));
		jtp_VanBanKy.setBounds(0, 0, 420, 200);
		JScrollPane jsp_VanBanKy = new JScrollPane(jtp_VanBanKy, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_VanBanKy.setBounds(10, 100, 420, 200);
		panel_PhatSinhChuKy.add(jsp_VanBanKy);
		
		JLabel lbl_VanBanKy = new JLabel("Văn bản ký");
		lbl_VanBanKy.setFont(new Font("Arial", Font.BOLD, 18));
		lbl_VanBanKy.setBounds(10, 50, 150, 50);
		panel_PhatSinhChuKy.add(lbl_VanBanKy);
		
		btn_File = new JButton("Chọn");
		btn_File.addActionListener(ac);
		btn_File.setFont(new Font("Arial", Font.BOLD, 14));
		btn_File.setBounds(80, 340, 120, 50);
		panel_PhatSinhChuKy.add(btn_File);
		
		JButton btn_Ky = new JButton("Ký");
		btn_Ky.setFont(new Font("Arial", Font.BOLD, 14));
		btn_Ky.setBounds(240, 340, 120, 50);
		btn_Ky.addActionListener(ac);
		panel_PhatSinhChuKy.add(btn_Ky);
		
		jtp_ChuKy = new JTextPane();
		jtp_ChuKy.setFont(new Font("Arial", Font.PLAIN, 14));
		jtp_ChuKy.setBounds(0, 0, 420, 200);
		JScrollPane jsp_ChuKy = new JScrollPane(jtp_ChuKy, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_ChuKy.setBounds(10, 460, 420, 200);
		panel_PhatSinhChuKy.add(jsp_ChuKy);
		
		JButton btn_Save = new JButton("Lưu chữ ký");
		btn_Save.setFont(new Font("Arial", Font.BOLD, 14));
		btn_Save.setBounds(80, 700, 120, 50);
		btn_Save.addActionListener(ac);
		panel_PhatSinhChuKy.add(btn_Save);
		
		JButton btn_Move = new JButton("Chuyển");
		btn_Move.setFont(new Font("Arial", Font.BOLD, 14));
		btn_Move.setBounds(240, 700, 120, 50);
		btn_Move.addActionListener(ac);
		panel_PhatSinhChuKy.add(btn_Move);
		
		JPanel panel_KiemTraChuKy = new JPanel();
		panel_KiemTraChuKy.setLayout(null);
		panel_KiemTraChuKy.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Ki\u1EC3m Tra Ch\u1EEF K\u00FD", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_KiemTraChuKy.setBounds(950, 10, 450, 1000);
		contentPane.add(panel_KiemTraChuKy);
		
		textPane_ChuKy = new JTextPane();
		textPane_ChuKy.setFont(new Font("Arial", Font.PLAIN, 14));
		textPane_ChuKy.setBounds(0, 0, 420, 100);
		JScrollPane jsp_ChuKyKiemTra = new JScrollPane(textPane_ChuKy, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_ChuKyKiemTra.setBounds(15, 50, 420, 100);
		panel_KiemTraChuKy.add(jsp_ChuKyKiemTra);
		
		textPane_VanBanKy = new JTextPane();
		textPane_VanBanKy.setFont(new Font("Arial", Font.PLAIN, 14));
		textPane_VanBanKy.setBounds(0, 0, 420, 100);
		JScrollPane jsp_VanBanKiemTra = new JScrollPane(textPane_VanBanKy, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_VanBanKiemTra.setBounds(15, 250, 420, 100);
		panel_KiemTraChuKy.add(jsp_VanBanKiemTra);
		
		jtp_ThongBao = new JTextPane();
		jtp_ThongBao.setFont(new Font("Arial", Font.PLAIN, 14));
		jtp_ThongBao.setBounds(0, 0, 420, 100);
		JScrollPane jsp_ThongBao = new JScrollPane(jtp_ThongBao, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_ThongBao.setBounds(15, 610, 420, 100);
		panel_KiemTraChuKy.add(jsp_ThongBao);
		
		JLabel lblVBK = new JLabel("Văn bản ký");
		lblVBK.setFont(new Font("Arial", Font.BOLD, 16));
		lblVBK.setBounds(15, 210, 150, 30);
		panel_KiemTraChuKy.add(lblVBK);
		
		JButton btn_File_VanBanKy = new JButton("Chọn văn bản ký");
		btn_File_VanBanKy.setFont(new Font("Arial", Font.BOLD, 14));
		btn_File_VanBanKy.setBounds(140, 360, 160, 50);
		btn_File_VanBanKy.addActionListener(ac);
		panel_KiemTraChuKy.add(btn_File_VanBanKy);
		
		JButton btn_KiemTra = new JButton("Kiểm tra");
		btn_KiemTra.setFont(new Font("Arial", Font.BOLD, 14));
		btn_KiemTra.setBounds(260, 720, 100, 50);
		btn_KiemTra.addActionListener(ac);
		panel_KiemTraChuKy.add(btn_KiemTra);
		
		JLabel lblCK = new JLabel("Chữ ký");
		lblCK.setFont(new Font("Arial", Font.BOLD, 16));
		lblCK.setBounds(15, 10, 150, 30);
		panel_KiemTraChuKy.add(lblCK);
		
		JButton btn_File_ChuKy = new JButton("Chọn chữ ký");
		btn_File_ChuKy.setFont(new Font("Arial", Font.BOLD, 14));
		btn_File_ChuKy.setBounds(155, 160, 130, 50);
		btn_File_ChuKy.addActionListener(ac);
		panel_KiemTraChuKy.add(btn_File_ChuKy);
		
		JLabel lbl_ThongBao = new JLabel("Thông báo");
		lbl_ThongBao.setFont(new Font("Arial", Font.BOLD, 18));
		lbl_ThongBao.setBounds(15, 570, 150, 30);
		panel_KiemTraChuKy.add(lbl_ThongBao);
		
		JButton btn_ChonKhoa = new JButton("Chọn khóa");
		btn_ChonKhoa.setFont(new Font("Arial", Font.BOLD, 14));
		btn_ChonKhoa.setBounds(80, 720, 120, 50);
		btn_ChonKhoa.addActionListener(ac);
		panel_KiemTraChuKy.add(btn_ChonKhoa);
		
		textPane_Y = new JTextPane();
		textPane_Y.setFont(new Font("Arial", Font.PLAIN, 14));
		textPane_Y.setBounds(0, 0, 420, 100);
		JScrollPane jsp_YKiemTra = new JScrollPane(textPane_Y, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp_YKiemTra.setBounds(15, 460, 420, 100);
		panel_KiemTraChuKy.add(jsp_YKiemTra);
		
		JLabel lbl_KhoaCongKhai = new JLabel("Khóa công khai y");
		lbl_KhoaCongKhai.setFont(new Font("Arial", Font.BOLD, 16));
		lbl_KhoaCongKhai.setBounds(15, 420, 200, 30);
		panel_KiemTraChuKy.add(lbl_KhoaCongKhai);
	}
}
