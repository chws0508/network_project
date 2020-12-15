package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import Action.Protocol;

public class chattingScreen extends JFrame implements Runnable, ActionListener {
	
	//GUI ��ü ����
	public JFrame frame = new JFrame("");
	public JButton exit;
	public JLabel user = new JLabel("USER");
	public JTextArea messageArea = new JTextArea();
	public JTextArea partList = new JTextArea();
	public JTextField textField = new JTextField();
	public chattingScreen(String roomN) {
	///
		
		//frame ����
		frame.setTitle(roomN);
		frame.setLayout(null);
		frame.setSize(400,600);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((res.width / 2) - 200 , (res.height / 2) - 300);
		frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		//
		
		//user label ����
		user.setFont(new Font("STXinwei",Font.BOLD,20));
		user.setBounds(280,20,100,30);
		user.setHorizontalAlignment(user.CENTER);
		//
		
		//scroll pane ����
		JScrollPane p =new JScrollPane(partList);
		p.setBounds(280, 60, 100, 220);
		//
		
		
		//exit ��ư ����
		exit = new JButton("EXIT");
		exit.setBounds(280,490,100,39);
		//
		
		
		//textField ����
		textField.setBounds(20,490,250,40);
		textField.setEditable(false);
		textField.setText("");
		//
		
		
		//messageArea ����
		messageArea.setEditable(false);
		messageArea.setBounds(20,20,250,460);
		messageArea.setText("");
		//
		
		//partList TextArea ����
		partList.setEditable(false);
		partList.setText("");
		//
		
		
	
		
		//frame�� component�߰�
		frame.add(user);
		frame.add(messageArea);
		frame.add(textField);
		frame.add(exit);
		frame.add(p);
		//
		
		frame.setVisible(true);
		event();//event ����
	}
	
	public void event() {
		textField.addActionListener(this);
		exit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {//�̺�Ʈ ����
		if(e.getSource() == textField) {//ä��â�� �Է� ��
			loginScreen.printW.println(Protocol.CHATTINGSENDMESSAGE + "|" + textField.getText() + "|" + frame.getTitle()); // �޼����� ����
			loginScreen.printW.flush();
			textField.setText("");
		}
		else if (e.getSource() == exit) {//������ ��ư ���� �� 
			frame.setVisible(false);
			loginScreen.printW.println(Protocol.EXITCHATTINGROOM + "|" + "Message" + "|" + frame.getTitle());
			loginScreen.printW.flush();
			partList.setText("");
		} 
		
	}
	
	@Override
	public void run() {
		String line[] = null;
		while(true) {
			try {
				
				
			}catch (Exception io) {
				io.printStackTrace();
			}
		}
	}
}
