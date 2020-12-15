package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.*;

public class makeGroupChat extends JFrame{

	//GUI ������
	JFrame frame = new JFrame("Make Group Chat");
	JLabel fL = new JLabel("FriendList");
	JButton create = new JButton("CREATE");
	JList<String> friendList;
	DefaultListModel<String> list;
	//
	
	public makeGroupChat() {
		
		//JList ����
		friendList = new JList<String>(new DefaultListModel<String>());
        list = (DefaultListModel)friendList.getModel();
        friendList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//
        
        
        //label ����
		fL.setFont(new Font("STXinwei",Font.BOLD,30));
		fL.setBounds(10, 10, 300, 30);
		//
		
		//create��ư ����
		create.setBounds(280, 10, 100, 30);
		//
		
		//scrollpane ����
		JScrollPane p =new JScrollPane(friendList);
		p.setBounds(10, 50, 370, 440);
		//
		
		//frame ����
		frame.setSize(400,500);
		frame.setLayout(null);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((res.width / 2) - 200 , (res.height / 2) - 250);
		frame.setResizable(false);
		//
		
		//frame�� component �߰�
		frame.add(fL);
		frame.add(create);
		frame.add(p);
		//
	}
}
