package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import view.ElgamalDigitalSignatureView;

public class ElgamalDigitalSignatureController implements ActionListener {
	
	private ElgamalDigitalSignatureView  view;
	
	public ElgamalDigitalSignatureController(ElgamalDigitalSignatureView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String command = e.getActionCommand();
        JFileChooser fileChooser = new JFileChooser();
        if(command.equals("Sinh khóa")) {
        	sinhKhoa();
        }
        else if(command.equals("Lưu khóa")) {
        	if(this.view.jtp_X.getText().isEmpty() || this.view.jtp_Y.getText().isEmpty()) {
        		JOptionPane.showMessageDialog(this.view, "Khóa bí mật hoặc khóa công khai chưa có dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	Object[] possibleValues = { "Khóa bí mật", "Khóa công khai"};
			Object selectedValue = JOptionPane.showInputDialog(this.view, "<html>Chọn lưu khóa<br>1. Khóa bí mật - 2. Khóa công khai</html>", 
					"Thông báo", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
			if(selectedValue.equals("Khóa bí mật")) {
				int option = fileChooser.showSaveDialog(this.view);
	            if (option == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                try {
	                    String fileName = file.getName();
	                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

	                    if (fileExtension.equalsIgnoreCase("txt")) {
	                    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	                    		writer.write(this.view.jtp_X.getText());
	                    	} catch (IOException ex) {
	                    		ex.printStackTrace();
	                    		JOptionPane.showMessageDialog(this.view, "Lỗi! Không thể lưu file!");
	                    	}
	                    } else if (fileExtension.equalsIgnoreCase("docx")) {
	                        String content = this.view.jtp_X.getText();
	                        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(file))) {
	                            zipOut.putNextEntry(new ZipEntry("[Content_Types].xml"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n"
	                                    + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n"
	                                    + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>\n"
	                                    + "<Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>\n"
	                                    + "</Types>").getBytes());
	                            zipOut.closeEntry();

	                            zipOut.putNextEntry(new ZipEntry("_rels/.rels"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
	                                    + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>\n"
	                                    + "</Relationships>").getBytes());
	                            zipOut.closeEntry();

	                            zipOut.putNextEntry(new ZipEntry("word/_rels/document.xml.rels"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
	                                    + "</Relationships>").getBytes());
	                            zipOut.closeEntry();

	                            zipOut.putNextEntry(new ZipEntry("word/document.xml"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n"
	                                    + "<w:body>\n"
	                                    + "<w:p>\n"
	                                    + "<w:r>\n"
	                                    + "<w:t>" + escapeXml(content) + "</w:t>\n"
	                                    + "</w:r>\n"
	                                    + "</w:p>\n"
	                                    + "</w:body>\n"
	                                    + "</w:document>").getBytes());
	                            zipOut.closeEntry();
	                        }
	                    } else {
	                        throw new IOException("Định dạng tệp không được hỗ trợ");
	                    }

	                    JOptionPane.showMessageDialog(this.view, "Lưu khóa bí mật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(this.view, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	                }
	            }
			} else if(selectedValue.equals("Khóa công khai")) {
				int option = fileChooser.showSaveDialog(this.view);
	            if (option == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                try {
	                    String fileName = file.getName();
	                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

	                    if (fileExtension.equalsIgnoreCase("txt")) {
	                    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	                    		writer.write(this.view.jtp_Y.getText());
	                    	} catch (IOException ex) {
	                    		ex.printStackTrace();
	                    		JOptionPane.showMessageDialog(this.view, "Lỗi! Không thể lưu file!");
	                    	}
	                    } else if (fileExtension.equalsIgnoreCase("docx")) {
	                        String content = this.view.jtp_Y.getText();
	                        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(file))) {
	                            zipOut.putNextEntry(new ZipEntry("[Content_Types].xml"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n"
	                                    + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n"
	                                    + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>\n"
	                                    + "<Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>\n"
	                                    + "</Types>").getBytes());
	                            zipOut.closeEntry();

	                            zipOut.putNextEntry(new ZipEntry("_rels/.rels"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
	                                    + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>\n"
	                                    + "</Relationships>").getBytes());
	                            zipOut.closeEntry();

	                            zipOut.putNextEntry(new ZipEntry("word/_rels/document.xml.rels"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
	                                    + "</Relationships>").getBytes());
	                            zipOut.closeEntry();

	                            zipOut.putNextEntry(new ZipEntry("word/document.xml"));
	                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
	                                    + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n"
	                                    + "<w:body>\n"
	                                    + "<w:p>\n"
	                                    + "<w:r>\n"
	                                    + "<w:t>" + escapeXml(content) + "</w:t>\n"
	                                    + "</w:r>\n"
	                                    + "</w:p>\n"
	                                    + "</w:body>\n"
	                                    + "</w:document>").getBytes());
	                            zipOut.closeEntry();
	                        }
	                    } else {
	                        throw new IOException("Định dạng tệp không được hỗ trợ");
	                    }

	                    JOptionPane.showMessageDialog(this.view, "Lưu khóa công khai thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(this.view, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	                }
	            }
			}
        }
        else if (command.equals("Chọn")) {
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.getName().endsWith(".txt")) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        this.view.model.setContent(sb.toString());
                        this.view.jtp_VanBanKy.setText(this.view.model.getContent());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (selectedFile.getName().endsWith(".docx")) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        ZipInputStream zip = new ZipInputStream(new FileInputStream(selectedFile));
                        ZipEntry entry;
                        String documentXml = null;
                        while ((entry = zip.getNextEntry()) != null) {
                            if (entry.getName().equals("word/document.xml")) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(zip));
                                String line;
                                while ((line = br.readLine()) != null) {
                                    sb.append(line);
                                }
                                br.close();
                                documentXml = sb.toString();
                                break;
                            }
                        }
                        zip.close();

                        if (documentXml != null) {
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(documentXml)));
                            doc.getDocumentElement().normalize();

                            NodeList paragraphNodes = doc.getElementsByTagName("w:p");
                            StyledDocument styledDoc = this.view.jtp_VanBanKy.getStyledDocument();
                            this.view.jtp_VanBanKy.setText("");

                            for (int i = 0; i < paragraphNodes.getLength(); i++) {
                                org.w3c.dom.Node paragraphNode = paragraphNodes.item(i);
                                if (paragraphNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                    org.w3c.dom.Element paragraphElement = (org.w3c.dom.Element) paragraphNode;
                                    NodeList runNodes = paragraphElement.getElementsByTagName("w:r");

                                    for (int j = 0; j < runNodes.getLength(); j++) {
                                        org.w3c.dom.Node runNode = runNodes.item(j);
                                        if (runNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                            org.w3c.dom.Element runElement = (org.w3c.dom.Element) runNode;
                                            NodeList textNodes = runElement.getElementsByTagName("w:t");

                                            if (textNodes.getLength() > 0) {
                                                org.w3c.dom.Element textElement = (org.w3c.dom.Element) textNodes
                                                        .item(0);
                                                String text = textElement.getTextContent();

                                                SimpleAttributeSet attributes = new SimpleAttributeSet();
                                                NodeList colorNodes = runElement.getElementsByTagName("w:color");
                                                if (colorNodes.getLength() > 0) {
                                                    org.w3c.dom.Element colorElement = (org.w3c.dom.Element) colorNodes
                                                            .item(0);
                                                    String colorValue = colorElement.getAttribute("w:val");
                                                    Color color = Color.decode("#" + colorValue);
                                                    StyleConstants.setForeground(attributes, color);
                                                }

                                                styledDoc.insertString(styledDoc.getLength(), text, attributes);
                                            }
                                        }
                                    }
                                    styledDoc.insertString(styledDoc.getLength(), "\n", null);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            JOptionPane.showMessageDialog(this.view, "Lấy dữ liệu văn bản ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(command.equals("Ký")) {
        	kyVanBan();
        }
        else if(command.equals("Lưu chữ ký")) {
        	int option = fileChooser.showSaveDialog(this.view);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    String fileName = file.getName();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

                    if (fileExtension.equalsIgnoreCase("txt")) {
                    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    		writer.write(this.view.jtp_ChuKy.getText());
                    	} catch (IOException ex) {
                    		ex.printStackTrace();
                    		JOptionPane.showMessageDialog(this.view, "Lỗi! Không thể lưu file!");
                    	}
                    } else if (fileExtension.equalsIgnoreCase("docx")) {
                        String content = this.view.jtp_ChuKy.getText();
                        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(file))) {
                            zipOut.putNextEntry(new ZipEntry("[Content_Types].xml"));
                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                                    + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n"
                                    + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n"
                                    + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>\n"
                                    + "<Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>\n"
                                    + "</Types>").getBytes());
                            zipOut.closeEntry();

                            zipOut.putNextEntry(new ZipEntry("_rels/.rels"));
                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                                    + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                                    + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>\n"
                                    + "</Relationships>").getBytes());
                            zipOut.closeEntry();

                            zipOut.putNextEntry(new ZipEntry("word/_rels/document.xml.rels"));
                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                                    + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                                    + "</Relationships>").getBytes());
                            zipOut.closeEntry();

                            zipOut.putNextEntry(new ZipEntry("word/document.xml"));
                            zipOut.write(("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                                    + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n"
                                    + "<w:body>\n"
                                    + "<w:p>\n"
                                    + "<w:r>\n"
                                    + "<w:t>" + escapeXml(content) + "</w:t>\n"
                                    + "</w:r>\n"
                                    + "</w:p>\n"
                                    + "</w:body>\n"
                                    + "</w:document>").getBytes());
                            zipOut.closeEntry();
                        }
                    } else {
                        throw new IOException("Định dạng tệp không được hỗ trợ");
                    }
                    JOptionPane.showMessageDialog(this.view, "Lưu chữ ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this.view, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if(command.equals("Chuyển")) {
        	if(this.view.jtp_ChuKy.getText().isEmpty() || this.view.jtp_VanBanKy.getText().isEmpty()) {
        		JOptionPane.showMessageDialog(this.view, "Văn bản ký hoặc chữ ký không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
        	} else {
        		this.view.textPane_ChuKy.setText(this.view.jtp_ChuKy.getText());
            	this.view.textPane_VanBanKy.setText(this.view.jtp_VanBanKy.getText());
        		JOptionPane.showMessageDialog(this.view, "Chuyển văn bản ký và chữ ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        	}
        }
        else if(command.equals("Chọn chữ ký")) {
        	int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.getName().endsWith(".txt")) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        this.view.model.setContent(sb.toString());
                        this.view.textPane_ChuKy.setText(this.view.model.getContent());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (selectedFile.getName().endsWith(".docx")) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        ZipInputStream zip = new ZipInputStream(new FileInputStream(selectedFile));
                        ZipEntry entry;
                        String documentXml = null;
                        while ((entry = zip.getNextEntry()) != null) {
                            if (entry.getName().equals("word/document.xml")) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(zip));
                                String line;
                                while ((line = br.readLine()) != null) {
                                    sb.append(line);
                                }
                                br.close();
                                documentXml = sb.toString();
                                break;
                            }
                        }
                        zip.close();

                        if (documentXml != null) {
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(documentXml)));
                            doc.getDocumentElement().normalize();

                            NodeList paragraphNodes = doc.getElementsByTagName("w:p");
                            StyledDocument styledDoc = this.view.textPane_ChuKy.getStyledDocument();
                            this.view.textPane_ChuKy.setText("");

                            for (int i = 0; i < paragraphNodes.getLength(); i++) {
                                org.w3c.dom.Node paragraphNode = paragraphNodes.item(i);
                                if (paragraphNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                    org.w3c.dom.Element paragraphElement = (org.w3c.dom.Element) paragraphNode;
                                    NodeList runNodes = paragraphElement.getElementsByTagName("w:r");

                                    for (int j = 0; j < runNodes.getLength(); j++) {
                                        org.w3c.dom.Node runNode = runNodes.item(j);
                                        if (runNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                            org.w3c.dom.Element runElement = (org.w3c.dom.Element) runNode;
                                            NodeList textNodes = runElement.getElementsByTagName("w:t");

                                            if (textNodes.getLength() > 0) {
                                                org.w3c.dom.Element textElement = (org.w3c.dom.Element) textNodes
                                                        .item(0);
                                                String text = textElement.getTextContent();

                                                SimpleAttributeSet attributes = new SimpleAttributeSet();
                                                NodeList colorNodes = runElement.getElementsByTagName("w:color");
                                                if (colorNodes.getLength() > 0) {
                                                    org.w3c.dom.Element colorElement = (org.w3c.dom.Element) colorNodes
                                                            .item(0);
                                                    String colorValue = colorElement.getAttribute("w:val");
                                                    Color color = Color.decode("#" + colorValue);
                                                    StyleConstants.setForeground(attributes, color);
                                                }

                                                styledDoc.insertString(styledDoc.getLength(), text, attributes);
                                            }
                                        }
                                    }
                                    styledDoc.insertString(styledDoc.getLength(), "\n", null);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            JOptionPane.showMessageDialog(this.view, "Lấy dữ liệu chữ ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(command.equals("Chọn văn bản ký")) {
        	int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.getName().endsWith(".txt")) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        this.view.model.setContent(sb.toString());
                        this.view.textPane_VanBanKy.setText(this.view.model.getContent());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (selectedFile.getName().endsWith(".docx")) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        ZipInputStream zip = new ZipInputStream(new FileInputStream(selectedFile));
                        ZipEntry entry;
                        String documentXml = null;
                        while ((entry = zip.getNextEntry()) != null) {
                            if (entry.getName().equals("word/document.xml")) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(zip));
                                String line;
                                while ((line = br.readLine()) != null) {
                                    sb.append(line);
                                }
                                br.close();
                                documentXml = sb.toString();
                                break;
                            }
                        }
                        zip.close();

                        if (documentXml != null) {
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(documentXml)));
                            doc.getDocumentElement().normalize();

                            NodeList paragraphNodes = doc.getElementsByTagName("w:p");
                            StyledDocument styledDoc = this.view.textPane_VanBanKy.getStyledDocument();
                            this.view.textPane_VanBanKy.setText("");

                            for (int i = 0; i < paragraphNodes.getLength(); i++) {
                                org.w3c.dom.Node paragraphNode = paragraphNodes.item(i);
                                if (paragraphNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                    org.w3c.dom.Element paragraphElement = (org.w3c.dom.Element) paragraphNode;
                                    NodeList runNodes = paragraphElement.getElementsByTagName("w:r");

                                    for (int j = 0; j < runNodes.getLength(); j++) {
                                        org.w3c.dom.Node runNode = runNodes.item(j);
                                        if (runNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                            org.w3c.dom.Element runElement = (org.w3c.dom.Element) runNode;
                                            NodeList textNodes = runElement.getElementsByTagName("w:t");

                                            if (textNodes.getLength() > 0) {
                                                org.w3c.dom.Element textElement = (org.w3c.dom.Element) textNodes
                                                        .item(0);
                                                String text = textElement.getTextContent();

                                                SimpleAttributeSet attributes = new SimpleAttributeSet();
                                                NodeList colorNodes = runElement.getElementsByTagName("w:color");
                                                if (colorNodes.getLength() > 0) {
                                                    org.w3c.dom.Element colorElement = (org.w3c.dom.Element) colorNodes
                                                            .item(0);
                                                    String colorValue = colorElement.getAttribute("w:val");
                                                    Color color = Color.decode("#" + colorValue);
                                                    StyleConstants.setForeground(attributes, color);
                                                }

                                                styledDoc.insertString(styledDoc.getLength(), text, attributes);
                                            }
                                        }
                                    }
                                    styledDoc.insertString(styledDoc.getLength(), "\n", null);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            JOptionPane.showMessageDialog(this.view, "Lấy dữ liệu văn bản ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(command.equals("Chọn khóa")) {
        	int result = fileChooser.showOpenDialog(null);
        	if (result == JFileChooser.APPROVE_OPTION) {
        	    File selectedFile = fileChooser.getSelectedFile();
        	    if (selectedFile.getName().endsWith(".txt")) {
        	        try {
        	            BufferedReader br = new BufferedReader(new FileReader(selectedFile));
        	            StringBuilder sb = new StringBuilder();
        	            String line;
        	            while ((line = br.readLine()) != null) {
        	                // Loại bỏ khoảng trắng thừa và ký tự xuống dòng
        	                sb.append(line.trim());
        	            }
        	            br.close();
        	            this.view.model.setContent(sb.toString());
        	            this.view.textPane_Y.setText(this.view.model.getContent());
        	        } catch (IOException ex) {
        	            ex.printStackTrace();
        	        }
        	    } else if (selectedFile.getName().endsWith(".docx")) {
        	        try {
        	            StringBuilder sb = new StringBuilder();
        	            ZipInputStream zip = new ZipInputStream(new FileInputStream(selectedFile));
        	            ZipEntry entry;
        	            String documentXml = null;
        	            while ((entry = zip.getNextEntry()) != null) {
        	                if (entry.getName().equals("word/document.xml")) {
        	                    BufferedReader br = new BufferedReader(new InputStreamReader(zip));
        	                    String line;
        	                    while ((line = br.readLine()) != null) {
        	                        sb.append(line);
        	                    }
        	                    br.close();
        	                    documentXml = sb.toString();
        	                    break;
        	                }
        	            }
        	            zip.close();

        	            if (documentXml != null) {
        	                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	                DocumentBuilder builder = factory.newDocumentBuilder();
        	                org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(documentXml)));
        	                doc.getDocumentElement().normalize();

        	                NodeList paragraphNodes = doc.getElementsByTagName("w:p");
        	                StyledDocument styledDoc = this.view.textPane_Y.getStyledDocument();
        	                this.view.textPane_Y.setText("");

        	                for (int i = 0; i < paragraphNodes.getLength(); i++) {
        	                    org.w3c.dom.Node paragraphNode = paragraphNodes.item(i);
        	                    if (paragraphNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        	                        org.w3c.dom.Element paragraphElement = (org.w3c.dom.Element) paragraphNode;
        	                        NodeList runNodes = paragraphElement.getElementsByTagName("w:r");

        	                        for (int j = 0; j < runNodes.getLength(); j++) {
        	                            org.w3c.dom.Node runNode = runNodes.item(j);
        	                            if (runNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        	                                org.w3c.dom.Element runElement = (org.w3c.dom.Element) runNode;
        	                                NodeList textNodes = runElement.getElementsByTagName("w:t");

        	                                if (textNodes.getLength() > 0) {
        	                                    org.w3c.dom.Element textElement = (org.w3c.dom.Element) textNodes.item(0);
        	                                    String text = textElement.getTextContent();

        	                                    // Loại bỏ khoảng trắng và ký tự xuống dòng thừa
        	                                    text = text.trim();

        	                                    SimpleAttributeSet attributes = new SimpleAttributeSet();
        	                                    NodeList colorNodes = runElement.getElementsByTagName("w:color");
        	                                    if (colorNodes.getLength() > 0) {
        	                                        org.w3c.dom.Element colorElement = (org.w3c.dom.Element) colorNodes.item(0);
        	                                        String colorValue = colorElement.getAttribute("w:val");
        	                                        Color color = Color.decode("#" + colorValue);
        	                                        StyleConstants.setForeground(attributes, color);
        	                                    }

        	                                    styledDoc.insertString(styledDoc.getLength(), text, attributes);
        	                                }
        	                            }
        	                        }
        	                        // Chỉ thêm xuống dòng khi cần thiết (nếu không bỏ qua)
        	                        styledDoc.insertString(styledDoc.getLength(), "", null);
        	                    }
        	                }
        	            }
        	        } catch (Exception ex) {
        	            ex.printStackTrace();
        	        }
        	    }
        	}
            JOptionPane.showMessageDialog(this.view, "Lấy dữ liệu khóa công khai thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(command.equals("Chuyển khóa")) {
        	if(this.view.jtp_Y.getText().isEmpty()) {
        		JOptionPane.showMessageDialog(this.view, "Khóa công khai không không có dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        	} else {
        		this.view.textPane_Y.setText(this.view.jtp_Y.getText());
        		JOptionPane.showMessageDialog(this.view, "Chuyển khóa công khai thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        	}
        }
        else if(command.equals("Kiểm tra")) {
        	kiemTraChuKy();
        }
	}
	
	public void sinhKhoa(){
        try {
            if (view.rdbtn_TuDong.isSelected()) {
            	view.model.sinhKhoaTuDong();
                JOptionPane.showMessageDialog(view, "Sinh khóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
            	if(view.jtp_P.getText().equals("") || view.jtp_G.getText().equals("") || view.jtp_X.getText().equals("")) {
                    JOptionPane.showMessageDialog(view, "P hoặc G hoặc X chưa có dữ liệu !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
            	} else {
            		view.model.sinhKhoaTuChon(view.jtp_P.getText(), view.jtp_G.getText(), view.jtp_X.getText());
                    JOptionPane.showMessageDialog(view, "Sinh khóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            	}
            }
            view.jtp_P.setText(view.model.getP().toString());
            view.jtp_G.setText(view.model.getG().toString());
            view.jtp_X.setText(view.model.getX().toString());
            view.jtp_Y.setText(view.model.getY().toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi sinh khóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	private String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
                "&apos;");
    }
	
	private void kyVanBan() {
        try {
            String vanBan = view.jtp_VanBanKy.getText().trim();
            String chuKy = view.model.kyVanBan(vanBan);
            if(vanBan.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Văn bản ký chưa có dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
            	view.jtp_ChuKy.setText(chuKy);
                JOptionPane.showMessageDialog(view, "Tạo chữ ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo chữ ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	private void kiemTraChuKy() {
        try {
            String vanBanKiemTra = this.view.textPane_VanBanKy.getText().trim();
            String chuKyKiemTra = this.view.textPane_ChuKy.getText().trim();
            String yKiemTra = this.view.textPane_Y.getText().trim();
            if(chuKyKiemTra.isEmpty()) {
                JOptionPane.showMessageDialog(this.view, "Vui lòng nhập hoặc chọn chữ ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            } else if(vanBanKiemTra.isEmpty()) {
                JOptionPane.showMessageDialog(this.view, "Vui lòng nhập hoặc chọn văn bản ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (yKiemTra.isEmpty()) {
                JOptionPane.showMessageDialog(this.view, "Vui lòng nhập hoặc chọn khóa công khai y!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            BigInteger y = new BigInteger(yKiemTra);
            this.view.model.setY(y);
            
            boolean ketQua = this.view.model.kiemTraChuKy(vanBanKiemTra, chuKyKiemTra);
            
            String vanBan = this.view.jtp_VanBanKy.getText().trim();
            String chuKy = this.view.jtp_ChuKy.getText().trim();
            String Y = this.view.jtp_Y.getText().trim();
            
            if(ketQua == false) {
                this.view.jtp_ThongBao.setText("Chữ ký không hợp lệ!");
                if(!vanBan.equals(vanBanKiemTra)) {
                	this.view.jtp_ThongBao.setText("Chữ ký không hợp lệ! \nVăn bản ký bị thay đổi!");
                	JOptionPane.showMessageDialog(this.view, "<html>Chữ ký không hợp lệ!<br>Văn bản ký bị thay đổi!</html>", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else if(!chuKy.equals(chuKyKiemTra)) {
                	this.view.jtp_ThongBao.setText("Chữ ký không hợp lệ! \nChữ ký bị thay đổi!");
                	JOptionPane.showMessageDialog(this.view, "<html>Chữ ký không hợp lệ!<br>Chữ ký bị thay đổi!</html>", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else if(!Y.equals(yKiemTra)) {
                	this.view.jtp_ThongBao.setText("Chữ ký không hợp lệ! \nKhóa công khai bị thay đổi!");
                	JOptionPane.showMessageDialog(this.view, "<html>Chữ ký không hợp lệ!<br>Khóa công khai bị thay đổi!</html>", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.view.jtp_ThongBao.setText("Chữ ký hợp lệ!");
            	JOptionPane.showMessageDialog(this.view, "Chữ ký hợp lệ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.view, "Lỗi khi kiểm tra chữ ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
