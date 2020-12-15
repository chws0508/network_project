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
	
	
	JFrame frame = new JFrame("Add Friend");//frame정의
	JTextField input = new JTextField();//textField 정의
	JLabel userList = new JLabel("UserList");//UserList 라벨 정의
	JList<String> wList;
	DefaultListModel<String> list;
	JButton summit = new JButton("Summit");
	JPopupMenu add = new JPopupMenu();
	JMenuItem addF = new JMenuItem("Add Friend");
	
	addFriends(){
		
		
		wList = new JList<String>(new DefaultListModel<String>());//JList 객체 생성 
		wList.setComponentPopupMenu(add);//Jlist에 pop매뉴 추가 
		
		//Scroll pane 정의
		JScrollPane p =new JScrollPane(wList);
		p.setBounds(0,100,385,400);
       //
		
		list = (DefaultListModel)wList.getModel();//defaultlist모델 객체 생성
        
        
		//Search Text 정의
		input.setText("Search Box");
		input.setBounds(0,20,385, 30);
		//
		
		
		
		summit.setBounds(285,530,100,30);//Summit버튼 정의
		
		//userList 정의
		userList.setFont(new Font("STXinwei",Font.BOLD,20));
		userList.setBounds(0,50,385,30);
		//
		
		//Popmenu에 ADD Friend 추가
		add.add(addF);
		
		
		
		//frame크기 및 layout설정
		frame.setLayout(null);
		frame.setSize(400,600);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((res.width / 2) - 200 , (res.height / 2) - 300);
		frame.setResizable(false);
		//
		
		//frame에 component추가
		frame.add(input);
		frame.add(userList);
		frame.add(p);
		frame.add(summit);
		//
	}

}