package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class addFriends{
	
	
	JFrame frame = new JFrame("Add Friend");//frame����
	JTextField input = new JTextField();//textField ����
	JLabel userList = new JLabel("UserList");//UserList �� ����
	JList<String> wList;
	DefaultListModel<String> list;
	JButton summit = new JButton("Summit");
	JPopupMenu add = new JPopupMenu();
	JMenuItem addF = new JMenuItem("Add Friend");
	
	addFriends(){
		
		
		wList = new JList<String>(new DefaultListModel<String>());//JList ��ü ���� 
		wList.setComponentPopupMenu(add);//Jlist�� pop�Ŵ� �߰� 
		
		//Scroll pane ����
		JScrollPane p =new JScrollPane(wList);
		p.setBounds(0,100,385,400);
       //
		
		list = (DefaultListModel)wList.getModel();//defaultlist�� ��ü ����
        
        
		//Search Text ����
		input.setText("Search Box");
		input.setBounds(0,20,385, 30);
		//
		
		
		
		summit.setBounds(285,530,100,30);//Summit��ư ����
		
		//userList ����
		userList.setFont(new Font("STXinwei",Font.BOLD,20));
		userList.setBounds(0,50,385,30);
		//
		
		//Popmenu�� ADD Friend �߰�
		add.add(addF);
		
		
		
		//frameũ�� �� layout����
		frame.setLayout(null);
		frame.setSize(400,600);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((res.width / 2) - 200 , (res.height / 2) - 300);
		frame.setResizable(false);
		//
		
		//frame�� component�߰�
		frame.add(input);
		frame.add(userList);
		frame.add(p);
		frame.add(summit);
		//
	}

}