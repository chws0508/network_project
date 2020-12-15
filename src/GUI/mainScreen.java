package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Action.Protocol;

public class mainScreen extends JFrame{	
	
	JFrame frame = new JFrame("Messenger");
	JPanel myinfo = new JPanel();
	JPanel myname = new JPanel();
	JLabel name = new JLabel("");
	JPanel friend = new JPanel();
	JTextField stateMessage = new JTextField(20);
	JTextField information = new JTextField(20);
	JButton setting = new JButton("Menu");//�޴�
	JButton findFriend = new JButton("Add Friend");//ģ���߰�
	JButton groupChat = new JButton("Group Chat");//�׷�ä�û���
	JList<String> fList;
	DefaultListModel<String> list;
	JPopupMenu menu = new JPopupMenu();
    JPopupMenu user = new JPopupMenu();
    JMenuItem exit = new JMenuItem("Exit");//������
    JMenuItem addF = new JMenuItem("Add Friend");//ģ���߰�
    JMenuItem groupC = new JMenuItem("Group Chat");//�׷�ä�û���
    JMenuItem chat = new JMenuItem("Chat");//ä��
    JMenuItem info = new JMenuItem("Information");//ģ������
    JMenuItem delete=new JMenuItem("Delete");//ģ������
    JMenuItem changeTL = new JMenuItem("Change Today Line");//�������Ѹ���
    JLabel coronadeath = new JLabel();
    JLabel coronadecide = new JLabel();
    JLabel today = new JLabel();
	private BufferedReader br;
	private PrintWriter pw;
	public mainScreen(){
		
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setNamespaceAware(true);
	        DocumentBuilder builder;
	        Document doc = null;
	        Calendar now=Calendar.getInstance(); //���� ��¥�� ����Ÿ������ �޾ƿ�
	        int []death={0,0}; //����, ���� ���� �����, Ȯ���� ���� arr
	        int []decide={0,0};
	        try {
	            //OpenApiȣ��
	            String urlstr = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"//url
	                    + "?serviceKey=2xbO73yAQSwDcoTLuXbVB4DPg6qQX8%2BMWLG4Oosuvhu7cYcMXzfx41spvhcH%2FsY1CIZM0pP7IfYp6LnoiTpd9g%3D%3D"//����Ű
	                    +"&startCreateDt="+now.get(Calendar.YEAR)+(now.get(Calendar.MONTH)+1)+(now.get(Calendar.DATE)-1)//���� ��¥
	                  +"&endCreateDt="+now.get(Calendar.YEAR)+(now.get(Calendar.MONTH)+1)+now.get(Calendar.DATE);//���� ��¥
	            URL url = new URL(urlstr);
	            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection(); //url�� ����
	            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));//���� �б�
	            String result = "";
	            String line;
	            while ((line = br.readLine()) != null) {
	                result = result + line.trim();// result = URL�� XML�� ���� ��
	            }
	            br.close(); //��� XML�� �޾ƿ� �� ���۸���, url���� ����
	            urlconnection.disconnect();
	            // xml �Ľ��ϱ�
	            InputSource is = new InputSource(new StringReader(result));
	            builder = factory.newDocumentBuilder();
	            doc = builder.parse(is);
	            XPathFactory xpathFactory = XPathFactory.newInstance();
	            XPath xpath = xpathFactory.newXPath();
	            XPathExpression expr = xpath.compile("//items/item");
	            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);//�޾ƿ� ��¥�� ������ŭ ����
	            for (int i = 0; i < nodeList.getLength(); i++) { 
	                NodeList child = nodeList.item(i).getChildNodes();//�޾ƿ� �����ͼ� ���� ��Ҹ�ŭ ����
	                for (int j = 0; j < child.getLength(); j++) {
	                    Node node = child.item(j);
	                    switch(node.getNodeName()) {
	                    case "deathCnt":
	                       death[i]=Integer.parseInt(node.getTextContent()); //���� ����ڸ� ��¥���� ����
	                       break;
	                    case "decideCnt":
	                       decide[i]=Integer.parseInt(node.getTextContent()); //���� Ȯ���ڸ� ��¥���� ����
	                       break;
	                    case "stateDt":
	                       break;
	                    }
	                }
	            }
	            death[0]-=death[1]; //���� �����, Ȯ���� ���� ���� ���� �����, Ȯ���ڸ� ����
	            decide[0]-=decide[1];
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	    String date = Integer.toString(now.get(Calendar.YEAR))+ "�� "+
            		Integer.toString((now.get(Calendar.MONTH)+1))+ "�� " +Integer.toString((now.get(Calendar.DATE))) + "��";
		today.setText(date + " ������ �ڷγ� ��Ȳ"); //�ڷγ� ���� ���� ���
	    coronadeath.setText("����� : " + death[0]);
	    coronadecide.setText("Ȯ���� : " + decide[0]);
	    today.setFont(new Font("STXinwei",Font.BOLD,15)); 
	    coronadeath.setFont(new Font("STXinwei",Font.BOLD,15));
	    coronadecide.setFont(new Font("STXinwei",Font.BOLD,15));
		fList = new JList<String>(new DefaultListModel<String>());
        list = (DefaultListModel)fList.getModel();
        frame.setLayout(null);
		information.setEditable(false);
		stateMessage.setEditable(false);
		name.setFont(new Font("STXinwei",Font.BOLD,20));
		name.setHorizontalAlignment(name.CENTER);
		setting.setBounds(0, 0, 100, 50); //�� ��ư ��ġ����
		findFriend.setBounds(100, 0, 142, 50);
		groupChat.setBounds(242,0,142,50);
		name.setBounds(0, 50, 100, 100);
		information.setBounds(100,75,300,25);
		stateMessage.setBounds(100,100,300,25);
		JScrollPane p =new JScrollPane(fList); //ģ�����â
		p.setBounds(0, 150, 385, 600);
		coronadeath.setBounds(10, 810, 170, 30);
		coronadecide.setBounds(200, 810, 170, 30);
		today.setBounds(10, 770, 380, 30);
		menu.add(exit);
		menu.add(addF);
		menu.add(groupC);
		menu.add(changeTL);
		setting.setComponentPopupMenu(menu); //�˾�â
	    user.add(chat);
	    user.add(info);
	    user.add(delete);
	    fList.setComponentPopupMenu(user);
	    
	    frame.add(setting);
		frame.add(findFriend);
		frame.add(groupChat);
		frame.add(name);
		frame.add(information);
		frame.add(stateMessage);
		frame.add(p);
		frame.add(coronadeath);
		frame.add(coronadecide);
		frame.add(today);
		frame.setSize(400,900);
	    Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((res.width / 2) - 200 , (res.height / 2) - 425);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //�������� ������ ������ ����
		
	}

}

