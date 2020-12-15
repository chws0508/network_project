package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class newAccount extends JFrame{
	
	//GUI ���� �κ�
	JFrame frame = new JFrame("New Account");
	JLabel newAccount = new JLabel("New Account");
	JLabel id = new JLabel ("ID");
	JLabel pw = new JLabel ("PassWord");
	JLabel nn = new JLabel ("NickName");
	JLabel name = new JLabel ("Name");
	JLabel email = new JLabel ("Email");
	JLabel birth = new JLabel ("Birth");
	JTextField idField = new JTextField(20);
	JPasswordField pwField = new JPasswordField(20);
	JTextField nnField = new JTextField(20);
	JTextField nameField = new JTextField(20);
	JTextField emailField = new JTextField(20);
	JButton isDuplicate = new JButton("CHECK");
	JButton createOK = new JButton("CREATE");
	JButton checkEmail = new JButton("SEND");
	JLabel emailadductionL = new JLabel("E-mail Check");
	JTextField emailadductionT = new JTextField(10);
	JButton emailokB = new JButton("Check Number");
	JComboBox<String> emailC ,ageYearC, ageMonthC ,ageDayC;
	//
	
	public newAccount(){
		
		//e-mail �ڽ� �κ� �ʱ�ȭ
		String[] emailT = { "naver.com", "gmail.com" , "hanmail.net"}; 
		//
	
		//ageYear �ڽ� �κ� �ʱ�ȭ
		String[] ageYear = { "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90",
				"91", "92", "93", "94", "95", "96", "97", "98", "99", "00", "01", "02", "03",
				"04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
				"17", "18", "19" , "20"};
		//
		
		//ageMonth �ڽ� �κ� �ʱ�ȭ
		String[] ageMonth = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		//
		
		//ageDay �ڽ� �κ� �ʱ�ȭ
		String[] ageDay = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
		//
		
		
		//frame ����
		frame.setLayout(null);
		frame.setSize(400,600);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((res.width / 2) - 200 , (res.height / 2) - 200);
		frame.setResizable(false);
		//
		
		//GUI ��ü ���� �κ�
		emailC = new JComboBox<String>(emailT);
		ageYearC = new JComboBox<String>(ageYear);
		ageMonthC = new JComboBox<String>(ageMonth);
		ageDayC = new JComboBox<String>(ageDay);
		//
		
		//password field ����
		pwField.setEchoChar('*');
		pwField.setBounds(140,150,150,30);
		//
		
		//label ����
		newAccount.setFont(new Font("STXinwei",Font.BOLD,40));
		newAccount.setBounds(30,20,300,30);
		newAccount.setHorizontalAlignment(newAccount.CENTER);
		//
		
		//id �κ� ����
		id.setFont(new Font("STXinwei",Font.BOLD,20));
		id.setBounds(20,100,100,30);
		idField.setBounds(140,100,150,30);
		//
		
		//password �κ� ����
		pw.setFont(new Font("STXinwei",Font.BOLD,20));
		pw.setBounds(20,150,100,30);
		//
		
		//�г��� �κ� ����
		nn.setFont(new Font("STXinwei",Font.BOLD,20));
		nn.setBounds(20,200,100,30);
		nnField.setBounds(140,200,150,30);
		//
		
		//�̸� �κ� ����
		name.setFont(new Font("STXinwei",Font.BOLD,20));
		name.setBounds(20,250,100,30);
		nameField.setBounds(140,250,150,30);
		//
		
		//email�κ� ����
		email.setFont(new Font("STXinwei",Font.BOLD,20));
		email.setBounds(20,300,100,30);
		emailField.setBounds(140,300,75,30);
		emailC.setBounds(225,300,50,30);
		checkEmail.setBounds(300, 300, 70, 30);
		emailadductionL.setBounds(20, 350, 100, 30);
		emailadductionT.setBounds(140,350, 75, 30);
		emailadductionL.setFont(new Font("STXinwei",Font.BOLD,15));
		emailokB.setBounds(225,350,150,30);
		
		//
		
		//������� �κ� ����
		birth.setFont(new Font("STXinwei",Font.BOLD,20));
		birth.setBounds(20,400,100,30);
		//
		
		
		//age �ڽ� �κ� ����
		ageYearC.setBounds(140,400,50,30);
		ageMonthC.setBounds(200,400,50,30);
		ageDayC.setBounds(260,400,50,30);
		//
		
		//create��ư �κ� ����
		createOK.setBounds(300,450,80,30);
		//
		
		//�ߺ�üũ ��ư ����
		isDuplicate.setBounds(300,100,80,30);
		//
		
		
		//frame�� component �߰�
		frame.add(newAccount);
		frame.add(id);
		frame.add(pw);
		frame.add(nn);
		frame.add(name);
		frame.add(email);
		frame.add(birth);
		frame.add(idField);
		frame.add(pwField);
		frame.add(nnField);
		frame.add(nameField);
		frame.add(emailField);
		frame.add(emailC);
		frame.add(checkEmail);
		frame.add(ageYearC);
		frame.add(ageMonthC);
		frame.add(ageDayC);
		frame.add(createOK);
		frame.add(isDuplicate);
		frame.add(emailadductionL);
		frame.add(emailadductionT);
		frame.add(emailokB);
		//
		
	}
}
