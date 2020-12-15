package Action;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DO.Room;
import DO.User;

public class MainHandler extends Thread {
	private BufferedReader br;
	private PrintWriter printW;
	private Socket socket;
	private Connection conn;
	private PreparedStatement pstmt;
	private User user;
	
	private ArrayList<MainHandler>  connUserList; //���� �����
	private ArrayList<String> friendUserList; // ģ��
	private ArrayList<Room> roomtotalList; // ��ü �渮��Ʈ
	private Room priRoom; // ����ڰ� �ִ� ��
	private String fileName;
	private String privateKey;//�缳Ű ����
    private String publicKey;//����Ű ����
	// ����, ��ü�����,����,�渮��Ʈ,JDBC
	public MainHandler(Socket socket, ArrayList<String>  friendUserList, ArrayList<MainHandler>  connUserList,
			ArrayList<Room> roomtotalList, Connection conn) throws IOException {
		this.user = new User();
		this.priRoom = new Room();
		this.socket = socket;
		this.connUserList = connUserList;
		this.friendUserList =  friendUserList;
		this.roomtotalList = roomtotalList;
		this.conn = conn;

		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		//RSA
				String filepath = "./key2.txt";//���� ���
				String filepath2="./key.txt";
				BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(filepath )));
				BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream(filepath2 )));
				privateKey=br2.readLine();
				publicKey=br3.readLine();
		//RSA����
		
	}

	@Override
	public void run() {
		// ������ �Է¹��� �������Ľ� -> ��� �����������
		try {

			String[] line = null;
			while (true) {
				line = br.readLine().split("\\|");

				if (line == null) {
					break;
				}
				if (line[0].compareTo(Protocol.REGISTER) == 0) // [ȸ������]
				{
					String userContent[] = line[1].split("%");
					
					String sql2="UPDATE id_generator SET seq_currval=LAST_INSERT_ID(seq_currval+1)";
					pstmt = conn.prepareStatement(sql2);
                    pstmt.executeUpdate();
                    
                    String sql = "Insert into UserContent(priNumber,id,password,nickName, name ,email, age, today_line, state) "
                            + "values(LAST_INSERT_ID(),?,?,?,?,?,?,?,?)";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userContent[0]);
                    pstmt.setString(2, userContent[1]);
                    pstmt.setString(3, userContent[2]);
                    pstmt.setString(4, userContent[3]);
                    pstmt.setString(5, userContent[4]);
                    pstmt.setString(6, userContent[5]);
                    pstmt.setString(7, userContent[6]);
                    pstmt.setString(8, userContent[7]);
					int su = pstmt.executeUpdate(); // �׻� ��� ����(CRUD)���� ������ return
					System.out.println(su + "ȸ������[DB]");

				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK) == 0) // ȸ������ ID �ߺ�üũ
				{
					System.out.println(line[0] + "/" + line[1]);
					String sql = "select * from UserContent where id = '" + line[1] + "'"; //�Է��� ID�� ���� DB�� �����ϴ��� Ȯ��
					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery(sql);
					String name = null;
					int count = 0;
					while (rs.next()) {
						name = rs.getString("id");
						if (name.compareTo(line[1]) == 0) {
							count++;
						}
					}
					System.out.println(count);
					if (count == 0) // ID�� �ߺ� �ȵǸ� ���� ����
					{
						printW.println(Protocol.IDSEARCHCHECK_OK + "|" + "MESSAGE");
						printW.flush();
					} else { //ID�� �ߺ��ȴٸ� ���� �Ұ���
						printW.println(Protocol.IDSEARCHCHECK_NO + "|" + "MESSAGE");
						printW.flush();
					}
				} else if (line[0].compareTo(Protocol.ENTERLOGIN) == 0) // [login]
				{

					boolean con = true; // ������ �α��εǾ��ִ��� �ȵǾ��ִ��� ����
					System.out.println("login");
					String userContent[] = line[1].split("%");

					System.out.println(userContent[0] + "/" + userContent[1]);

					for (int i = 0; i < connUserList.size(); i++) {
						if ((connUserList.get(i).user.getIdName()).compareTo(userContent[0]) == 0) {
							con = false;
						}
					}
					if (con) { //���� �α����� �ȵǾ��־��ٸ�
						String sql = "select * from UserContent where id = '" + userContent[0]+"'";

						pstmt = conn.prepareStatement(sql);
						ResultSet rs = pstmt.executeQuery(sql);
						String pw=null;
						int count = 0;
						
						while (rs.next()) { //�Է��� ID�� ���� row�� �޾ƿ�
							user.setPassword(userContent[1]);
							user.setPryNumber(rs.getInt("priNumber"));
							user.setIdName(rs.getString("ID"));
				            user.setPassword("secret");
							user.setnickName(rs.getString("nickName"));
							user.setName(rs.getString("NAME"));
							user.setEmail(rs.getString("email"));
							user.setAge(rs.getString("AGE"));
							user.setToday_line(rs.getString("today_line"));
							user.setState(rs.getInt("state"));
							pw=RSA.decode(rs.getString("password"), privateKey); //�н����带 RSA�˰������� ��ȣȭ
						}
						if(pw==null);
						else if(pw.compareTo(RSA.decode(userContent[1], privateKey))==0) //��ȣȭ�� �н����尡 ���� ����� �Ͱ� ���ٸ�
                        	 count++;
						System.out.println(count);

						if (count == 0) // ID,printW Ʋ���� ������ user���� �ʱ�ȭ
						{
							printW.println(Protocol.ENTERLOGIN_NO + "|" + "�α��ο� �����Ͽ����ϴ�");
							printW.flush();

							user.setPryNumber(0);
							user.setIdName("");
							user.setPassword("");
							user.setnickName("");
							user.setName("");
							user.setEmail("");
							user.setAge("");
							user.setToday_line("");
							user.setState(0);

						} else { // �α��� �Ǿ�����
							String sql1= "UPDATE usercontent SET state = 1 where id = '" + user.getIdName() + "'"; //������� ���¸� online���� �ٲ�
		                    pstmt=conn.prepareStatement(sql1);
		                    pstmt.executeUpdate();
		                    
		                    sql1= "UPDATE friendList SET friendState = 1 where friendid = '" + user.getIdName() + "'";
		                    pstmt=conn.prepareStatement(sql1);
		                    pstmt.executeUpdate();
		                    
							connUserList.add(this); // ������ �ο��� �߰�
							String userline = "";
							String sql2 = "select friendId, friendName, friendNickname, friendtoday_line , friendState from friendList where id = '" + user.getIdName()
									+ "'";
							pstmt = conn.prepareStatement(sql2);
							ResultSet rs1 = pstmt.executeQuery(sql2);
							
								while(rs1.next()) { //�޾ƿ� ģ�� �� ��ŭ �ݺ�
									String s = "offline";
									if(rs1.getString("friendState").equalsIgnoreCase("1")) { //friendState�� 1�� ���
										s = "online"; //online���� ǥ��
									}
									userline += (rs1.getString("friendId") + "&" + rs1.getString("friendName") + "&" + 
											 rs1.getString("friendNickname") + "&" + rs1.getString("friendtoday_line") + "&" + s +":");
								}
								if(userline.length() == 0) {
									userline = "null&null&null&null&null";
								}
							System.out.println(Protocol.ENTERLOGIN_OK + "|" + user.getName() + "|"+ user.getIdName()  + "|"+ user.getToday_line() +
									"|" + user.getEmail() +"|" + userline);
							printW.println(Protocol.ENTERLOGIN_OK + "|" + user.getName() + "|"+ user.getIdName()  + "|"+ user.getToday_line() +
									"|" + user.getEmail() +"|" + userline);
							printW.flush();
							
							for (int i = 0; i < connUserList.size(); i++) {
								connUserList.get(i).printW.println(Protocol.UPDATED);
								connUserList.get(i).printW.flush();
							}
						}
						System.out.println(user.toString());
						
					} else {
						printW.println(Protocol.ENTERLOGIN_NO + "|" + "�̹� �α��� ���Դϴ�.");
						printW.flush();
					}

				} else if (line[0].compareTo(Protocol.EXITMAINROOM) == 0) { // ���ο��� �α���������(logout);
					
					String sql2= "UPDATE usercontent SET state = 0 where id = '" + user.getIdName() + "'"; //logout�� user state�� 0���� ����� offlineǥ��
                    pstmt=conn.prepareStatement(sql2);
                    pstmt.executeUpdate();
                    
                    sql2= "UPDATE friendList SET friendState = 0 where friendid = '" + user.getIdName() + "'";
                    pstmt=conn.prepareStatement(sql2);
                    pstmt.executeUpdate();
                    
					connUserList.remove(this); //���� ������ ����Ʈ���� ����
					user.setPryNumber(0);
					user.setIdName("");
					user.setPassword("");
					user.setnickName("");
					user.setName("");
					user.setEmail("");
					user.setAge("");
					user.setToday_line("");
					user.setState(0);
					System.out.println(Protocol.EXITMAINROOM);
					
					for (int i = 0; i < connUserList.size(); i++) { //���� ������ ����Ʈ�� ���¸� ������Ʈ �� �ٸ� ����ڿ��� ǥ��
						connUserList.get(i).printW.println(Protocol.UPDATED);
						connUserList.get(i).printW.flush();
					}
				} 
				else if (line[0].compareTo(Protocol.REQUEST_MAKE_GROUPCHAT) == 0) { // �游���
					int l = line[1].length();
					line[1] = line[1].substring(1,l - 1);
					System.out.println(line[1]);
					
					String userContent[] = line[1].split(", ");
					String[][] person= new String[userContent.length][];
					for(int i = 0 ; i < userContent.length ; i++) {
						person[i] = userContent[i].split("/");
						for(int j = 0 ; j < person[i].length ; j++) {
							System.out.println(person[i][j]);
						}
					}
					Room tempRoom = new Room(); //�� ����
                    
					tempRoom.setUserCount(1); //�� ������ �� 1�� ����
					tempRoom.setMasterName(user.getIdName()); //���� ID�� �� ���� ����� ID�� ����
					
					String sql2="UPDATE room_number SET room_num = LAST_INSERT_ID(room_num + 1);"; 
					pstmt = conn.prepareStatement(sql2);
                    pstmt.executeUpdate();
                    String sql = "select room_num from room_number";
                    pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery(sql);
                    int priNumber = 0;
                    while(rs.next()) {
                    	priNumber = rs.getInt("room_num");
                    }
					System.out.println("userContent Length : " + userContent.length);
					if (priNumber != 0) { //�� �ѹ��� �ҷ�������
						tempRoom.setrID(priNumber); //���� ���� ��ȣ�� �������ְ�
						tempRoom.setRoomInUserList(user.getIdName()); //����ڸ���Ʈ�� ���� ������� ID�� �ִ´�
						roomtotalList.add(tempRoom);
						priRoom = tempRoom; // ���� ���� ������
					}
					System.out.println(roomtotalList.size());
					printW.println(Protocol.ROOMMAKE_OK + "|" + tempRoom.getMasterName() + "|" + priNumber);
					printW.flush();
					for (int i = 0; i < connUserList.size(); i++) { //���� ���� ����Ʈ�� �����ŭ �ݺ�
						System.out.println("IDDDd" + connUserList.get(i).user.getIdName());
						for(int j = 0 ; j < userContent.length ; j++) {
							if (connUserList.get(i).user.getIdName().equals(person[j][0])) {
								connUserList.get(i).printW.println(Protocol.JOINROOM_REQUEST + "|" + tempRoom.getMasterName() 
										+ "|" + priNumber);
								connUserList.get(i).printW.flush();
								System.out.println(Protocol.JOINROOM_REQUEST);
							} 
						}
					}
				}  
		
				else if (line[0].compareTo(Protocol.JOINROOM_YES) == 0) { // [�� �����ư]

					String thisName = connUserList.get(connUserList.indexOf(this)).user.getIdName(); //������ ����Ʈ���� ������� ID�� �޾ƿ�.

					int roomid = Integer.parseInt(line[2]); // ��ID
					System.out.println(roomid);
					String roomUser = "";
					int index = 0;
					for (int i = 0; i < roomtotalList.size(); i++) { //��ü �� ������ŭ �ݺ�
						System.out.println("Room ID L:" + roomtotalList.get(i).getrID());
						if (roomtotalList.get(i).getrID() == roomid) { //�� ID�� ���� ���
							int c = roomtotalList.get(i).getUserCount(); //���� ������ ���� �޾ƿ���
							roomtotalList.get(i).setUserCount(c + 1); //+1
							roomUser = roomtotalList.get(i).getRoomInUserList();
							roomtotalList.get(i).setRoomInUserList(roomUser + "%" +user.getIdName()); //������ ����Ʈ�� ���� �����ڸ� �߰��Ѵ�.
							priRoom = roomtotalList.get(i);
							index = i;
							System.out.println(priRoom.toString());
						}
					}
					
					System.out.println(thisName); 
					System.out.println("Index : " + index);
					System.out.println(connUserList.size());
					printW.println(Protocol.ENTERROOM_OK1  + "|" +  line[1] + "|" +line[2]);
					printW.flush();
					String roomMember[] = roomtotalList.get(index).getRoomInUserList().split("%"); //�� �ο� arr
					System.out.println(roomMember.length);
					for (int i = 0; i < roomMember.length; i++) {
						for(int j = 0 ; j < connUserList.size() ; j++) { 
							if(connUserList.get(j).user.getIdName().equalsIgnoreCase(roomMember[i])) {
								connUserList.get(j).printW.println(Protocol.ENTERROOM_USERLISTSEND + "|"
										+ roomtotalList.get(index).getRoomInUserList() + "|" + user.getIdName() + "���� �����ϼ̽��ϴ�." + "|" + line[2]); //��ȭ ���濡�� ���� ����ڰ� ���������� �˸�
								connUserList.get(j).printW.flush();
							}
						}
					}
				} 
				else if (line[0].compareTo(Protocol.EXITCHATTINGROOM) == 0) // �� ������
				{

					int roomIndex = 0;
					boolean con = true;

					for (int i = 0; i < roomtotalList.size(); i++) {
						if (roomtotalList.get(i).getrID() == Integer.parseInt(line[2])) {

							if (roomtotalList.get(i).getUserCount() == 2) // ���Ë� �ڱⰡ �������� ��.
							{
								roomIndex = i;
								String roomUser = roomtotalList.get(roomIndex).getRoomInUserList();
								String roomMember[] = roomUser.split("%");
								for (int k = 0; k < roomtotalList.get(roomIndex).getUserCount(); k++) {
									for(int j = 0 ; j < connUserList.size() ; j++) {
										if(connUserList.get(j).user.getIdName().equalsIgnoreCase(roomMember[k])) {
											connUserList.get(j).printW.println(Protocol.EXIT_CHATTINGROOM + "|" + roomtotalList.get(i).getrID());
											connUserList.get(j).printW.flush(); //�� ��ü ����Ʈ �߿� ���� ���� ���°�� ��ġ�ϴ��� �˻�
										}
									}
								}
								
								System.out.println("���ö� ���� �������϶�");
								Room tempRoom = roomtotalList.get(i); 
								roomtotalList.remove(tempRoom); //������� remove
								tempRoom = new Room();
								con = false;
								

							} else { // �ּ� 2���� ��
								System.out.println("���ö� ���� �������ƴҶ�");
								Room tempRoom = roomtotalList.get(i); 
								roomtotalList.get(i).setUserCount(roomtotalList.get(i).getUserCount() - 1);; // �濡 ���� ����
								tempRoom = new Room();// ���� �� ����
								roomIndex = i;
							}

						}
						
						if (con) // �����ִ¹濡 �ּ� 2���̻��϶�
						{
							String roomUser = roomtotalList.get(roomIndex).getRoomInUserList(); //���� �� �ο��� �޾ƿ�
							String roomMember[] = roomUser.split("%");
							System.out.println("Ư���濡 ����� : " + roomtotalList.get(roomIndex).getUserCount());
							System.out.println(roomUser);
							for (int k = 0; k < roomtotalList.get(roomIndex).getUserCount(); k++) { //���� ��� �ο����� ������ ���������� �˸�
								for(int j = 0 ; j < connUserList.size() ; j++) {
									if(connUserList.get(j).user.getIdName().equalsIgnoreCase(roomMember[k])) {
										connUserList.get(j).printW.println(Protocol.ENTERROOM_USERLISTSEND + "|"
												+ roomUser + "|" + user.getIdName() + "���� �����ϼ̽��ϴ�." + "|" + roomtotalList.get(i).getrID());
										connUserList.get(j).printW.flush();
									}
								}
							}
						}

						String roomListMessage = "";

						System.out.println(roomListMessage);
					}
					
					
					
				} else if (line[0].compareTo(Protocol.CHATTINGSENDMESSAGE) == 0) // ä�ù濡�� �޼��� ������
				{
					int index = 0;
					for(int i = 0 ; i < roomtotalList.size() ; i++) {
						if(roomtotalList.get(i).getrID() == Integer.parseInt(line[2])){
							index = i; //���� �� ������ �޾ƿ�
						}
					}
					System.out.println("RoomNumber : " + line[2]);
					System.out.println("Index =" + index);
					String roomUser = roomtotalList.get(index).getRoomInUserList(); //���� ���� ������ ����Ʈ�� �޾ƿ�
					String roomMember[] = roomUser.split("%");
					for (int i = 0; i < roomMember.length; i++) {
						for(int j = 0 ; j < connUserList.size() ; j++) {
							if(connUserList.get(j).user.getIdName().equalsIgnoreCase(roomMember[i])) { //�� �����ڵ鿡�� �޼����� ������
								connUserList.get(j).printW.println(Protocol.CHATTINGSENDMESSAGE_OK + 
										"|" + user.getIdName() + "|" + line[1] + "|" +roomtotalList.get(index).getrID()); 
								connUserList.get(j).printW.flush();
							}
						}
					}

				}
				else if(line[0].compareTo(Protocol.CHANGE_TODAY_LINE) == 0) { //������ �Ѹ��� ����
					String sql2="UPDATE usercontent SET today_line= '" + line[1]  + "' where id = '" + user.getIdName() + "'";
                    pstmt=conn.prepareStatement(sql2);
                    pstmt.executeUpdate(); 
                    String sql1="UPDATE friendList SET friendtoday_line= '" + line[1] + " 'where friendId= '" + user.getIdName() + "'";
                    pstmt=conn.prepareStatement(sql1);
                    pstmt.executeUpdate(); //db�� ����� ������ ����

                    for (int i = 0; i < connUserList.size(); i++) { //���� ������ ����Ʈ���� ������ �Ѹ��� ������Ʈ �Ǿ��ٰ� ����
						connUserList.get(i).printW.println(Protocol.UPDATED);
						connUserList.get(i).printW.flush();
					}
                    printW.println(Protocol.UPDATE_ME + "|" + line[1]);
                    printW.flush();
				}
				else if(line[0].compareTo(Protocol.UPDATE_PLZ) == 0) { //������Ʈ�� ������ ������ ���� ��û
					  	String userline = "";
	                    String sql= "select friendId, friendName, friendNickname, friendtoday_line, friendState from friendList where id = '" + user.getIdName()
						+ "'";
	                    pstmt = conn.prepareStatement(sql);
	                    ResultSet rs1 = pstmt.executeQuery(sql);
	                   
	                    while(rs1.next()) { //�޾ƿ� ������ ģ����� ���ۼ�
	                    	String s = "offline";
	                 		if(rs1.getString("friendState").equalsIgnoreCase("1")) {
	                 			s = "online";
	                 		}
	                    	userline += (rs1.getString("friendId") + "&" + rs1.getString("friendName") + "&" 
	                    			+ rs1.getString("friendNickname") + "&" + rs1.getString("friendtoday_line") + "&" + s + ":");
	                    }
	                    if(userline.length() == 0) {
	                    	userline = "null&null&null&null&null";
	                    }
	                    System.out.println(Protocol.UPDATE_CONFIRM + "|" + userline);
	                    printW.println(Protocol.UPDATE_CONFIRM + "|" + userline);
	                    printW.flush();
				}
				else if(line[0].compareTo(Protocol.REQUEST_FRIEND_LIST) == 0) { //ģ���߰� â ��� ��
					String userline = "";
					if(line[1].equalsIgnoreCase("ALL!@#")) {
	                    String sql= "select id, name, nickName, state from usercontent";
	                    pstmt = conn.prepareStatement(sql);
	                    ResultSet rs1 = pstmt.executeQuery(sql);
	                    while(rs1.next()) { //���� DB�� �ִ� ��� ������� ������ �޾ƿ���
	                    	if(rs1.getString("id").equalsIgnoreCase(user.getIdName())) {
	                    	}
	                    	else {
	                    		String s = "offline"; //ģ�� ��Ͽ� ���� �߰�
	                    		if(rs1.getString("state").equalsIgnoreCase("1")) {
	                    			s = "online";
	                    		}
	                    		userline += (rs1.getString("id") + "&" + rs1.getString("name") + "&" + 
		                    			rs1.getString("nickName") + "&" + s + ":");
	                    	}
	                    	
	                    }  
					}
					else { //���� ������ ����� ������ �޾ƿ���
	                    String sql= "select id, name, nickName from usercontent where id like'%" + line[1] + "%'";
	                    pstmt = conn.prepareStatement(sql);
	                    ResultSet rs1 = pstmt.executeQuery(sql);
	                    while(rs1.next()) {
	                    	if(rs1.getString("id").equalsIgnoreCase(user.getIdName())) {
	                    	}
	                    	else {
	                    		String s = "offline"; //ģ�� ��Ͽ� ���� �߰�
	                    		if(rs1.getString("state").equalsIgnoreCase("1")) {
	                    			s = "online";
	                    		}
	                    		userline += (rs1.getString("id") + "&" + rs1.getString("name") + "&" + 
		                    			rs1.getString("nickName") + "&" + s + ":");
	                    	}
	                    	
	                    }  
					}
					if(userline.length() == 0) {
                    	userline = "null&null&null&null";
                    }
					printW.println(Protocol.REQUEST_FRIEND_LIST_CONFIRM + "|" + userline);
					printW.flush();
				}
				else if(line[0].compareTo(Protocol.REQUEST_FRIEND_ADD) == 0) {//ģ�� �߰��� ���� ���� ����
					String text[] = line[1].split(" ");
					String sql= "select id, name, nickName, today_line, state from usercontent where id ='" + text[0] + "'";//id�� �Ȱ��� ����� ���� �޾ƿ�	
			
                    pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery(sql);
                    String sql1 = "";
                    
                   
                    while(rs.next()) {//�޾ƿ� ����� ������ friendlist DB table�� ����
                    	sql1 = "Insert into friendList values('" + user.getIdName() + "','" + rs.getString("id") + "','" + rs.getString("name")
    					+ "','" + rs.getString("nickName") + "','" + rs.getString("today_line") + "','" + rs.getString("state") + "')";
                    }
	                pstmt=conn.prepareStatement(sql1);
	                pstmt.executeUpdate();
	                
	                for (int i = 0; i < connUserList.size(); i++) { //�������� ������ ��ο��� 
						connUserList.get(i).printW.println(Protocol.UPDATED); //������Ʈ ������ ������ �˸�
						connUserList.get(i).printW.flush();
					}
                    printW.println(Protocol.UPDATED + "|" + line[1]);
                    printW.flush();
				}
				else if(line[0].compareTo(Protocol.REQUEST_FRIEND_DELETE) == 0) {//ģ�� ����
					String text[] = line[1].split("/");
					String sql= "delete from friendlist where id='"+ user.getIdName()+"' and friendId='"+text[0]+ "'"; //ģ�� ���� ������
					
                    pstmt = conn.prepareStatement(sql);
                    pstmt.executeUpdate();
                    
                   System.out.println(text[1]+" is deleted");
                   String sql2= "select id, name, nickName, age, email, today_line , state from usercontent where id ='" + text[0] + "'";
                   pstmt = conn.prepareStatement(sql2);
                   ResultSet rs = pstmt.executeQuery(sql2);
                   String id ="";
                   String nickname = "";
                   String name = "";
                   String email = "";
                   String age = "";
                   String today_line = "";
                   int state = 0;
					while (rs.next()) { //������ ģ���� ������ �޾ƿ� ����
						id = (rs.getString("ID"));
						nickname = (rs.getString("nickName"));
						name = (rs.getString("NAME"));
						email = (rs.getString("email"));
						age = (rs.getString("AGE"));
						today_line = (rs.getString("today_line"));
						state = (rs.getInt("state"));
					}
					
					 printW.println(Protocol.CONFIRM_FRIEND_DELETE + "|" + id + "&" + nickname + "&"
							 + name + "&" + email + "&" + age + "&" + today_line + "&" + state); //ģ���� �����Ǿ����� �˸�
	                 printW.flush();
				}	
				
				else if(line[0].compareTo(Protocol.CHECK_FRIEND_INFO) == 0) { //ģ�� ���� �޾ƿ���
					String text[] = line[1].split("/");
					String sql= "select id, name, nickName, age, email, today_line , state from usercontent where id ='" + text[0] + "'"; //ģ�� ���� �޾ƿ��� ������
                    pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery(sql);
                    String id ="";
                    String nickname = "";
                    String name = "";
                    String email = "";
                    String age = "";
                    String today_line = "";
                    int state = 0;
					while (rs.next()) { //ģ���� ������ �޾ƿ� ����
						id = (rs.getString("ID"));
						nickname = (rs.getString("nickName"));
						name = (rs.getString("NAME"));
						email = (rs.getString("email"));
						age = (rs.getString("AGE"));
						today_line = (rs.getString("today_line"));
						state = (rs.getInt("state"));
					}
					 printW.println(Protocol.CONFIRM_FRIEND_INFO + "|" + id + "&" + nickname + "&"
							 + name + "&" + email + "&" + age + "&" + today_line + "&" + state); //ģ���� ������ �������� ����ϱ� ���� ����
	                 printW.flush();
				}
				else if(line[0].compareTo(Protocol.REQUEST_GROUPCHAT_LIST) == 0) { //�׷�ä���� �������� ģ����� ��û
					String userline = "";
                    String sql= "select friendId, friendName, friendNickname, friendtoday_line, friendState from friendList where id = '" + user.getIdName()
					+ "'"; //���� ������� ģ�� ����Ʈ���� �޾ƿ�
                    pstmt = conn.prepareStatement(sql);
                    ResultSet rs1 = pstmt.executeQuery(sql);
                   
                    while(rs1.next()) { //ģ�� ��� ����
                    	String s = "offline";
                 		if(rs1.getString("friendState").equalsIgnoreCase("1")) {
                 			s = "online";
                 		}
                    	userline += (rs1.getString("friendId") + "&" + rs1.getString("friendName") + "&" 
                    			+ rs1.getString("friendNickname") + "&" + rs1.getString("friendtoday_line") + "&" + s + ":");
                    }
                    if(userline.length() == 0) {
                    	userline = "null&null&null&null&null";
                    }
                    System.out.println(Protocol.GROUPCHAT_LIST + "|" + userline);
                    printW.println(Protocol.GROUPCHAT_LIST + "|" + userline); //ģ�� ��� ���
                    printW.flush();
				}
				else if(line[0].compareTo(Protocol.JOINROOM_NO) == 0) { //ä�� �ʴ� ����
					int index = 0;
					for (int i = 0; i < roomtotalList.size(); i++) {
						if (roomtotalList.get(i).getrID() == Integer.parseInt(line[2])) {
							index = i; //�������� ä�ù��� �ε����� ����
							System.out.println(priRoom.toString());
						}
					}
					String roomMember[] = roomtotalList.get(index).getRoomInUserList().split("%");
					System.out.println(roomMember.length);
					for (int i = 0; i < roomMember.length; i++) {
						for(int j = 0 ; j < connUserList.size() ; j++) {
							if(connUserList.get(j).user.getIdName().equalsIgnoreCase(roomMember[i])) {
								connUserList.get(j).printW.println(Protocol.ENTERROOM_REJECT + "|" + user.getIdName() + "|" + line[2]); //ä�ù� �ȿ� �ִ� �����鿡�� ������������ �˸�
								connUserList.get(j).printW.flush();
							}
						}
					}
				}
			} // while		
		} catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				br.close(); //���� ����� �Է� ���۸� �ݴ´�.
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printW.close();
			try {
				socket.close(); //���� ����� ������ �ݴ´�.
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		}
		
	}
}
