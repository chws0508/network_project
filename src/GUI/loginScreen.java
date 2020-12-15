package GUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import Action.Protocol;
import Action.RSA;
import FunctionTest.Email.SendMail;


public class loginScreen extends JFrame implements ActionListener, Runnable, ListSelectionListener, PopupMenuListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//ȭ�� ����
    private JFrame frame = new JFrame("Login");
    private JLabel name = new JLabel("Messenger");
    private JLabel id = new JLabel("ID");
    private JLabel pw = new JLabel("PW");
    private JTextField idField = new JTextField(20);
    private JPasswordField pwField = new JPasswordField(20);
    private JButton create = new JButton("CREATE");
    private JButton summit = new JButton("LOGIN");
    //ȭ�� ���� ��
    
    private String publicKey;//����Ű ����
    
    //�ٸ� GUI ����
	newAccount createAccount;
	mainScreen mainS;
	addFriends aF;
	makeGroupChat mGC;
	//�ٸ� GUI ���� ��
	Socket socket;
	private BufferedReader br;
	private BufferedReader br2;
	static PrintWriter printW;
	
	private String sNumber = "><^^"; // default ��ũ���ѹ�
	private boolean condition_S = false; // �̸��� ����Ȯ��
	private boolean condition_Id = false; // ID �ߺ�üũ
	private static Map<Integer, chattingScreen> chat_room=new HashMap<>(); //ä�ù� ����
	public loginScreen() throws IOException{
		//���� ����
		network();
		
		mainS = new mainScreen();
		createAccount = new newAccount();
		aF = new addFriends();
		mGC = new makeGroupChat();
		
		
		 ////RSA�κ�
		String filepath = "./key.txt";//���� ���
		br2 = new BufferedReader(new InputStreamReader(new FileInputStream(filepath )));
		publicKey=br2.readLine();
         ///
		
		//�α���â GUI ���� �� ��ġ ,ũ��, ���� ��Ʈ ����
		frame.setLayout(null);
		name.setFont(new Font("STXinwei",Font.BOLD,50));
		id.setFont(new Font("STXinwei",Font.BOLD,20));
		pw.setFont(new Font("STXinwei",Font.BOLD,20));
		create.setFont(new Font("STXinwei",Font.BOLD,15));
		summit.setFont(new Font("STXinwei",Font.BOLD,15));
		name.setBounds(55,0,300,75);
		id.setBounds(60,100,50,50);
		pw.setBounds(60,160,50,50);
		idField.setBounds(140,100,175,50);
		pwField.setBounds(140,160,175,50);
		create.setBounds(25,260,100,50);
		summit.setBounds(275,260,100,50);
		pwField.setEchoChar('*');
		frame.add(name);
		frame.add(id);
		frame.add(pw);
		frame.add(idField);
		frame.add(pwField);
		frame.add(create);
		frame.add(summit);
		frame.setSize(400,350);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((res.width / 2) - 200 , (res.height / 2) - 175);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//�׼Ǹ����� ����
		event();
	}

	public void event() {
		//ȸ������ ��ư ������
		create.addActionListener(this);
		//�α��� Ȯ�� ��ư ������
		summit.addActionListener(this);
		//��й�ȣ �Է�â ���� ������
		pwField.addActionListener(this);
		//ȸ������ Ȯ�� ��ư ������
		createAccount.createOK.addActionListener(this);
		//ȸ������ ���̵� �ߺ� ���� üũ ��ư ������
		createAccount.isDuplicate.addActionListener(this);
		//ȸ������ �̸��� ������ȣ ���� ��ư ������
		createAccount.checkEmail.addActionListener(this);
		//ȸ������ �̸��� ������ȣ Ȯ�� ��ư ������
		createAccount.emailokB.addActionListener(this);
		//���ν�ũ�� �׷�ä�� ��ư ������
		mainS.groupChat.addActionListener(this);
		//���ν�ũ�� ���� ��ư ������
		mainS.setting.addActionListener(this);
		//���ν�ũ�� ģ���߰� ��ư ������
		mainS.findFriend.addActionListener(this);
		//���ν�ũ�� EXIT �˾��޴� ������
		mainS.exit.addActionListener(this);
		//���ν�ũ�� ģ���߰� �˾��޴� ������
		mainS.addF.addActionListener(this);
		//���ν�ũ�� �׷�ä�� �˾��޴� ������
		mainS.groupC.addActionListener(this);
		//���ν�ũ�� ������ �Ѹ��� ���� �˾��޴� ������
		mainS.changeTL.addActionListener(this);
		//���ν�ũ�� ģ�� Ŭ���� ������ ���� �˾��޴� ������
		mainS.info.addActionListener(this);
		//���ν�ũ�� ģ�� Ŭ���� ������ 1��1 ä�� �˾��޴� ������
		mainS.chat.addActionListener(this);
		//���ν�ũ�� ģ�� Ŭ���� ������ ģ�� ���� ������
		mainS.delete.addActionListener(this);
		//ģ���߰�â �����˻�â ������
		aF.input.addActionListener(this);
		//ģ���߰�â ģ���߰� �˾��޴� ������
		aF.addF.addActionListener(this);
		//ģ���߰�â Ȯ�� ��ư ������
		aF.summit.addActionListener(this);
		//�׷�ä�ù� ����â ���� ��ư ������
		mGC.create.addActionListener(this);
		//���ν�ũ�� ģ��Ŭ���� ��Ÿ���� �˾��޴� ����
		initPopupListener(mainS.fList, mainS.user);
		//���ν�ũ�� ���� Ŭ���� ��Ÿ���� �˾��޴� ����
		initPopupListener(mainS.setting, mainS.menu);
		//ģ���߰� â���� ����Ŭ���� ��Ÿ���� �˾��޴� ����
		initPopupListener(aF.wList, aF.add);
	}
	public void network() throws IOException {
		
		// ���� ����
		try {
			String serverInfo[] = readServerInfo();
			socket = new Socket(serverInfo[0], Integer.parseInt(serverInfo[1]));
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (UnknownHostException e) {
			System.out.println("������ ã�� �� �����ϴ�");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("������ ������ �ȵǾ����ϴ�");
			e.printStackTrace();
			System.exit(0);
		}

		// �̺�Ʈ

		// ������ ����
		Thread t = new Thread(this);
		t.start();
	}
	
	//������ IP�ּ�, Port Number�� �ҷ���
	public static String[] readServerInfo() throws IOException {
  		//�����̸��� serverinfo.dat���� �����Ѵ�.
  		String fileName = "serverinfo.dat";
  		Scanner inputStream = null;
  		String[] serverInfo = new String[2];
  		String input;
  		try {//������ ������ serverinfo.dat���� �޾ƿ´�.
  			inputStream = new Scanner(new File(fileName));
  			input = inputStream.nextLine();
  			serverInfo = input.split(" ");	
  		}//���� ������ �������� �ʴ´ٸ�, �����ּҸ� 127.0.0.1, ��Ʈ��ȣ��  9500 �⺻ �����μ� �������ش�.
  		catch(FileNotFoundException e) {
  			serverInfo[0] = "127.0.0.1";
  			serverInfo[1] = "9500";
  		}
  		return serverInfo;
  	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == create) { //ȸ������ ��ư ������
			createAccount.frame.setVisible(true);
		}
		else if(e.getSource() == summit) { //�α��� ��ư ������
			String id = idField.getText();
			String pwss = pwField.getText();
			//��й�ȣ RSA�̿� ��ȣȭ
			pwss=RSA.encode(pwss, publicKey);
			if (id.length() == 0 || pwss.length() == 0) {
				JOptionPane.showMessageDialog(this, "��ĭ�� �Է����ּ���");
			} else {
				String line = id + "%" + pwss;
				//�������� �α��� ��û
				printW.println(Protocol.ENTERLOGIN + "|" + line);
				printW.flush();
			}
			idField.setText("");
			pwField.setText("");
		}
		else if(e.getSource() == pwField) { //��й�ȣ ġ�� ���� ������
			String id = idField.getText();
			String pwss = pwField.getText();
			//��й�ȣ RSA�̿� ��ȣȭ
			pwss=RSA.encode(pwss, publicKey);
			if (id.length() == 0 || pwss.length() == 0) {
				JOptionPane.showMessageDialog(this, "��ĭ�� �Է����ּ���");
			} else {
				String line = id + "%" + pwss;
				//�������� �α��� ��û
				printW.println(Protocol.ENTERLOGIN + "|" + line);
				printW.flush();
			}
			idField.setText("");
			pwField.setText("");
		}
		else if(e.getSource() == createAccount.createOK) { //ȸ������ �Ϸ� ��ư Ŭ��
			//������ �Է��� ���� �ʱ� �⺻������ ����
			String name = createAccount.nameField.getText();
			String id = createAccount.idField.getText();
			String pw1 = createAccount.pwField.getText();
			//��й�ȣ ��ȣȭ
			pw1=RSA.encode(pw1, publicKey);
			String ageYear = (String) createAccount.ageYearC.getSelectedItem();
			String ageMonth = (String) createAccount.ageMonthC.getSelectedItem();
			String ageDay = (String)createAccount.ageDayC.getSelectedItem();
			String email = createAccount.emailField.getText();
			String nickname = createAccount.nnField.getText();
			String today_line = "Enter Today Message";
			int state = 0;
			//ȸ�����Խ� �Է� ���� �� �ִٸ�
			if(name.length() == 0 || id.length() == 0 || pw1.length() == 0 || email.length() == 0 || nickname.length() == 0) {
				JOptionPane.showMessageDialog(this, "Please enter a blank space.");
			}//ȸ�����Խ� �ߺ�üũ �ϰ� �̸��� ������ȣ�� Ȯ�� ������
			else if(condition_S && condition_Id) {
				String line = "";
				line = (createAccount.idField.getText() + "%" + pw1 + 
						"%" + createAccount.nnField.getText() + "%" + createAccount.nameField.getText() + 
						"%" + createAccount.emailField.getText() + "@" + createAccount.emailC.getSelectedItem() + 
						"%" + createAccount.ageYearC.getSelectedItem() + createAccount.ageMonthC.getSelectedItem()
						+ createAccount.ageDayC.getSelectedItem() + "%" + today_line + "%" + state);
				System.out.println(line);
				//�������� ��Ͽ�û ��
				printW.println(Protocol.REGISTER + "|" + line);
				printW.flush();
				
				JOptionPane.showMessageDialog(this, "Create Account Success.");
				createAccount.idField.setText("");
				createAccount.pwField.setText("");
				createAccount.nnField.setText("");
				createAccount.nameField.setText("");
				createAccount.emailField.setText("");
				createAccount.emailC.setSelectedIndex(0);
				createAccount.ageYearC.setSelectedIndex(0);
				createAccount.ageMonthC.setSelectedIndex(0);
				createAccount.ageDayC.setSelectedIndex(0);
				condition_S = false;
				condition_Id = false;
				sNumber = "><^^";
				createAccount.frame.setVisible(false);
				this.frame.setVisible(true);
			}//���� ID�ߺ�üũ ���ߴٸ�
			else if (!condition_Id && condition_S) {
				JOptionPane.showMessageDialog(this, "Check ID Duplicated");
			}//���� �̸��� �ߺ�üũ ���ߴٸ�
			else if (!condition_S && condition_Id) {
				JOptionPane.showMessageDialog(this, "Check Email");
			}//�Ѵ� ���ߴٸ�
			else if (!condition_Id && !condition_S) {
				JOptionPane.showMessageDialog(this, "Check ID Duplicated , Check Email");
			}
			
		}
		else if(e.getSource() == createAccount.isDuplicate) {//ID �ߺ� üũ
			if (createAccount.idField.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "Input ID");
			} else {
				printW.println(Protocol.IDSEARCHCHECK + "|" + createAccount.idField.getText());
				printW.flush();
			}
		}
		else if (e.getSource() == createAccount.checkEmail) // ȸ������ ������ -----------> ������ȣ ����
		{
			if (createAccount.emailField.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "Input Email");
			} else {
				JOptionPane.showMessageDialog(this, "Send the Check number");
				String emailString = createAccount.emailField.getText() + "@"
						+ (String) createAccount.emailC.getSelectedItem();
				System.out.println(emailString);
				sNumber = String.valueOf(SendMail.SendMail(emailString));
			}
		}else if (e.getSource() == createAccount.emailokB) { // ȸ������ ������ -----------> ������ȣȮ��
			if (sNumber.compareTo(createAccount.emailadductionT.getText()) == 0) {
				JOptionPane.showMessageDialog(this, "Confirm");
				condition_S = true;
			} else {
				JOptionPane.showMessageDialog(this, "Wrong Number");
			}
		}//���ν�ũ�� ���� Ŭ����
		else if(e.getSource() == mainS.setting) {
			mainS.menu.show(mainS.setting, 0, 50);
		}//���ν�ũ�� ģ���߰� ��ư Ŭ����
		else if(e.getSource() == mainS.findFriend) {
			aF.input.setText("");
			//�������� ��ü������� ��û
			printW.println(Protocol.REQUEST_FRIEND_LIST + "|" + "ALL!@#");
			printW.flush();
			aF.frame.setVisible(true);
		}//���ν�ũ�� �׷�ä�ù� ��ư Ŭ����
		else if(e.getSource() == mainS.groupChat) {//�游��� ������ ����
			mGC.frame.setVisible(true);
			//�׷�ä��â ���� ���� ģ�� ��� ��û
			printW.println(Protocol.REQUEST_GROUPCHAT_LIST);
			printW.flush();
		}//���ν�ũ������ ������ �˾��޴� Ŭ����
		else if(e.getSource() == mainS.exit) {
			mainS.frame.setVisible(false);
  			this.frame.setVisible(true);
  			//�������� ���� �α׾ƿ� ��û
  			printW.println(Protocol.EXITMAINROOM + "|" + "message");
  			printW.flush();
		}//���ν�ũ������ ģ���߰� �˾��޴� Ŭ����
		else if(e.getSource() == mainS.addF) {
			aF.input.setText("");
			//�׷�ä��â ���� ���� ģ�� ��� ��û
			printW.println(Protocol.REQUEST_FRIEND_LIST + "|" + "ALL!@#");
			printW.flush();
			aF.frame.setVisible(true);
		}//���ν�ũ�� �׷�ä�ù� �˾��޴� Ŭ����
		else if(e.getSource() == mainS.groupC) {
			mGC.frame.setVisible(true);
			//�׷�ä��â ���� ���� ģ�� ��� ��û
			printW.println(Protocol.REQUEST_GROUPCHAT_LIST);
			printW.flush();
		}//���ν�ũ�� ������ �Ѹ��� ���� �˾��޴� Ŭ����
		else if(e.getSource() == mainS.changeTL) {
			//����� ���ڿ� �޾ƿ�
			String cTL = JOptionPane.showInputDialog("Enter Today Line");
			//�������� ������ �Ѹ��� ���� ��û
			printW.println(Protocol.CHANGE_TODAY_LINE + "|" + cTL);
			printW.flush();
		}//���ν�ũ������ ģ�� Ŭ���� ������ chat �˾��޴� Ŭ����
		else if(e.getSource() == mainS.chat) {
			List line = mainS.fList.getSelectedValuesList();
			//�������� ä�ù� ���� ��û
			printW.println(Protocol.REQUEST_MAKE_GROUPCHAT + "|" + line);
			printW.flush();
		}//���ν�ũ������ ģ�� Ŭ���� ������ ���� �˾��޴� Ŭ����
		else if(e.getSource() == mainS.info) {
			//�������� ģ�� ���� ��û
			printW.println(Protocol.CHECK_FRIEND_INFO + "|" + mainS.fList.getSelectedValue());
			printW.flush();
		}//�ֿ켮:������� �߰�
		else if(e.getSource()==mainS.delete) {
			//�������� ģ�� ���� ��û ����
			printW.println(Protocol.REQUEST_FRIEND_DELETE+"|"+ mainS.fList.getSelectedValue());
			printW.flush();
		}//ģ�� �߰� â���� Ȯ�� ��ư Ŭ����
		else if (e.getSource() == aF.summit) {
			aF.frame.setVisible(false);
		}//ģ�� �߰�â���� ���� �˻���
		else if (e.getSource() == aF.input) {
			//���� �����̸� ��ü ���� ��û
			if(aF.input.getText().equalsIgnoreCase("")) {
				printW.println(Protocol.REQUEST_FRIEND_LIST + "|" + "ALL!@#");
				printW.flush();
			}
			//�ƴϸ� �������� �ش� ���ڿ������� ������� ��û
			else {
				printW.println(Protocol.REQUEST_FRIEND_LIST + "|" + aF.input.getText());
				printW.flush();
			}
		}//ģ�� �߰�â���� ���� Ŭ���� ������ ģ���߰� �˾��޴� Ŭ����
		else if(e.getSource() == aF.addF) {
			//�������� ģ���߰� ��û
			printW.println(Protocol.REQUEST_FRIEND_ADD + "|" + aF.wList.getSelectedValue());
			printW.flush();
		}//�׷�ä�ù� ���� â���� ����� ��ư Ŭ����
		else if(e.getSource() == mGC.create) {
			//������ ģ�� ��� �� �޾Ƽ� �������� ä��â Open ��û
			List line = mGC.friendList.getSelectedValuesList();
			printW.println(Protocol.REQUEST_MAKE_GROUPCHAT + "|" + line);
			printW.flush();
			mGC.frame.setVisible(false);
		}
	}
	
	private void initPopupListener(JButton setting, JPopupMenu menu) {
		menu.addPopupMenuListener(new PopupMenuListener() {
	          @Override
	          public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	        	  //�˾��޴� ������ ��ġ ����
	        	  Object obj = e.getSource();
	        	  String l = obj.toString();
	        	  int x = l.indexOf("desiredLocationX");
	        	  int y = l.indexOf("desiredLocationY");
	        	  int label = l.indexOf("label");
	        	  int rX = Integer.parseInt(l.substring(x + 17, y - 1));
	        	  int rY = Integer.parseInt(l.substring(y + 17 ,label - 1));
	        	  Point p = new Point(rX,rY);
	              SwingUtilities.convertPointToScreen(p, setting);
	              menu.setLocation(rX,rY);
	          }

	          @Override
	          public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

	          }

	          @Override
	          public void popupMenuCanceled(PopupMenuEvent e) {

	          }
	      });
	}

	private void initPopupListener(JList<String> list, JPopupMenu m) {
		m.addPopupMenuListener(new PopupMenuListener() {
	          @Override
	          public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	        	//�˾��޴� ������ ��ġ ����
	        	  Object obj = e.getSource();
	        	  String l = obj.toString();
	        	  int x = l.indexOf("desiredLocationX");
	        	  int y = l.indexOf("desiredLocationY");
	        	  int label = l.indexOf("label");
	        	  int rX = Integer.parseInt(l.substring(x + 17, y - 1));
	        	  int rY = Integer.parseInt(l.substring(y + 17 ,label - 1));
	        	  Point p = new Point(rX,rY);
	              SwingUtilities.convertPointToScreen(p, list);
	              m.setLocation(rX,rY);
	          }

	          @Override
	          public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

	          }

	          @Override
	          public void popupMenuCanceled(PopupMenuEvent e) {

	          }
	      });
		
	}

	@Override
	public void run() {
		// �޴���
		String line[] = null;
		while (true) {
			try {
				line = br.readLine().split("\\|");
				if (line == null) {
					br.close();
					printW.close();
					socket.close();

					System.exit(0);
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_OK) == 0) { // ȸ������ ID �ߺ� �ȵ�
					JOptionPane.showMessageDialog(this, "Available");
					condition_Id = true;
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_NO) == 0) { // ȸ������ ID �ߺ� ��
					JOptionPane.showMessageDialog(this, "Unavailable");
					condition_Id = false;
				} else if (line[0].compareTo(Protocol.ENTERLOGIN_OK) == 0) // �α��� ����
				{
					this.frame.setVisible(false);
					mainS.frame.setVisible(true);
					System.out.println(line);
					mainS.name.setText(line[1]);
					//�ڽ��� ��������
					mainS.information.setText(line[2] + " / " + line[4]);
					mainS.stateMessage.setText(line[3]);
					mainS.list.removeAllElements();
					//ģ����� ����
					String text[] = line[5].split(":");
					for (int i = 0; i < text.length; i++) {
						String t[] = text[i].split("&");
						if(t[0].equalsIgnoreCase("null") && t[1].equalsIgnoreCase("null") 
								&& t[2].equalsIgnoreCase("null") && t[3].equalsIgnoreCase("null") && t[4].equalsIgnoreCase("null")) {
						}
						else {
							mainS.list.addElement(t[0] +  "/" +  t[1] + "/" + t[2] + "/" + t[3] + "/" + t[4]);
						}
					}
				} 
				else if (line[0].compareTo(Protocol.ENTERLOGIN_NO) == 0) // �α��� ����
				{
					JOptionPane.showMessageDialog(this, line[1]);
					System.out.println("Login Failed");
				}//������ �Ѹ��� ���� �Ǿ�����
				else if(line[0].compareTo(Protocol.UPDATE_ME) == 0) {
					mainS.stateMessage.setText(line[1]);
				}//�������� �ֽ�ȭ �ʿ��ϴٰ� ��û��
				else if(line[0].compareTo(Protocol.UPDATED) == 0) {
					//�������� �ֽ�ȭ �ش޶�� ��û
					printW.println(Protocol.UPDATE_PLZ);
					printW.flush();//�ֿ켮: �̰� �����ִ°� �߰���
				}//������ �������� �ֽ�ȭ ������ ��������
				else if(line[0].compareTo(Protocol.UPDATE_CONFIRM) == 0) {
					//ģ�� ��� �ٽ� ����
					mainS.list.removeAllElements();
					String text[] = line[1].split(":");
					//���� ģ�� ������ �ȶ��
					for (int i = 0; i < text.length; i++) {
						String t[] = text[i].split("&");
						if(t[0].equalsIgnoreCase("null") && t[1].equalsIgnoreCase("null") && t[2].equalsIgnoreCase("null")
								&& t[3].equalsIgnoreCase("null") && t[4].equalsIgnoreCase("null")) {
						}
						else {
							mainS.list.addElement(t[0] +  "/" +  t[1] + "/" + t[2] + "/" + t[3] + "/" + t[4]);
						}
					}
				}//������ ģ���߰���ûâ�� ������� ��û�� ���� ���� �ٽ�
				else if(line[0].compareTo(Protocol.REQUEST_FRIEND_LIST_CONFIRM) == 0) {
					aF.list.removeAllElements();
					String text[] = line[1].split(":");
					//���� ���������� �ȶ��
					for (int i = 0; i < text.length; i++) {
						String t[] = text[i].split("&");
						if(t[0].equalsIgnoreCase("null") && t[1].equalsIgnoreCase("null") 
								&& t[2].equalsIgnoreCase("null") && t[3].equalsIgnoreCase("null")) {
						}
						else {
							aF.list.addElement(t[0] +  "  " +  t[1] + "  " + t[2] + " " + t[3]);
						}
					}
				}//ģ���� ����Ȯ�� ��û�� ���� ���� ���� ��
				else if(line[0].compareTo(Protocol.CONFIRM_FRIEND_INFO) == 0) {
					String text[] = line[1].split("&");
					String s = "offline";
					if(text[6].equalsIgnoreCase("1")) {
						s = "online";
					}
					JOptionPane.showMessageDialog(null, "ID : " + text[0] + "\nNICKNAME : " + text[1] + "\nNAME : " + text[2]
							 + "\nEMAIL : " + text[3] + "\nAGE : " + text[4] + "\nTODAY_LINE : " + text[5] + "\nSTATE : " + s);
				}//////�ֿ켮: ģ�� ������� �߰�
				else if(line[0].compareTo(Protocol.CONFIRM_FRIEND_DELETE) == 0) {
					String text[] = line[1].split("&");

					JOptionPane.showMessageDialog(null, text[0]+" is deleted!");
					printW.println(Protocol.UPDATE_PLZ);
					printW.flush();
					
				}//�׷�ä�ù� ���� ģ�� ��� ���޿� ���� ���� ���� ��
				else if(line[0].compareTo(Protocol.GROUPCHAT_LIST) == 0) {
					mGC.list.removeAllElements();
					String text[] = line[1].split(":");
					for (int i = 0; i < text.length; i++) {
						String t[] = text[i].split("&");
						if(t[0].equalsIgnoreCase("null") && t[1].equalsIgnoreCase("null") 
								&& t[2].equalsIgnoreCase("null") && t[3].equalsIgnoreCase("null") && t[4].equalsIgnoreCase("null")) {
						}
						else {
							mGC.list.addElement(t[0] +  "/" +  t[1] + "/" + t[2] + "/" + t[3] + "/" + t[4]);
						}
					}
				}//ä�ù� ���� ��û ������
				else if(line[0].compareTo(Protocol.JOINROOM_REQUEST) == 0) {
					int check = JOptionPane.showConfirmDialog(null, line[1] + " invited you. Do you Join Chat?", "REQUEST JOIN" ,JOptionPane.YES_NO_OPTION);
					if(check == 0) {
						//�����ϸ� �����ߴٰ� �������� �˸�
						printW.println(Protocol.JOINROOM_YES + "|" + line[1] + "|" +line[2]);
						printW.flush();
					}
					else {
						//�����ߴٰ� �������� �˸�
						printW.println(Protocol.JOINROOM_NO + "|" +  line[1] + "|" +line[2]);
						printW.flush();
					}
				}
				else if (line[0].compareTo(Protocol.ROOMMAKE_OK) == 0) // �游����� 
				{
					//ä�ý�ũ���� ���Ӱ� ����� ä�ù� ������ ����
					System.out.println(Protocol.ROOMMAKE_OK);
					chattingScreen chat = new chattingScreen(line[2]);
					chat_room.put(Integer.parseInt(line[2]), chat);
					
				}
				else if (line[0].compareTo(Protocol.ENTERROOM_OK1) == 0) // ������ ��û���� ������ �����
				{
					//ä�ý�ũ���� ���Ӱ� ����� ä�ù� ������ ����
					chattingScreen chat = new chattingScreen(line[2]);
					chat_room.put(Integer.parseInt(line[2]), chat);
				} 
				else if (line[0].compareTo(Protocol.ENTERROOM_USERLISTSEND) == 0) // ä�ù� ���� ����Ʈ ���ΰ�ħ
				{

					String roomMember[] = line[1].split("%");// �뿡 ���»����
					String lineList = "";
					for (int i = 0; i < roomMember.length; i++) {
						lineList += (roomMember[i] + "\n");
					}
					System.out.println(lineList);
					//ä�ù� ��Ͽ��� �ش�� ������ ��������
					chattingScreen temp = chat_room.get(Integer.parseInt(line[3]));
					temp.textField.setEditable(true);
					temp.partList.setText(lineList);
					temp.messageArea.append(line[2] + "\n");
				}//������ ä��ģ ������ ������ ���� �Ѿ�ý�
				else if (line[0].compareTo(Protocol.CHATTINGSENDMESSAGE_OK) == 0) {
					//ä�ù� ��Ͽ��� �ش�� ������ ��������
					chattingScreen temp = chat_room.get(Integer.parseInt(line[3]));
					temp.messageArea.append("[" + line[1] + "] :" + line[2] + "\n");
				}//������ ������ ��û ������
				else if(line[0].compareTo(Protocol.ENTERROOM_REJECT) == 0) {
					//ä�ù� ��Ͽ��� �ش�� ������ ��������
					chattingScreen temp = chat_room.get(Integer.parseInt(line[2]));
					temp.frame.setVisible(false);
					temp.partList.setText("");
					JOptionPane.showMessageDialog(null, line[1] + " reject join chat.");
				}//������ ä�ù��� ������
				else if(line[0].compareTo(Protocol.EXIT_CHATTINGROOM) == 0) {
					//ä�ù� ��Ͽ��� �ش�� ������ ��������
					chattingScreen temp = chat_room.get(Integer.parseInt(line[1]));
					temp.frame.setVisible(false);
					temp.partList.setText("");
					temp.textField.setEditable(false);
					JOptionPane.showMessageDialog(null, "All user exit.");
				}

			} catch (IOException io) {
				io.printStackTrace();
			}

		} // while

		
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}