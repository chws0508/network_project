package Action;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import DO.Room;

public class server {
	private ServerSocket ss; // ���� ����
	private ArrayList<MainHandler> allUserList; // ��ü �����
	private ArrayList<String> friendList; // ģ��
	private ArrayList<MainHandler> connUserList; // ���� �����
	private ArrayList<Room> roomtotalList;// ��ü �渮��Ʈ

	private Connection conn;
	private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost/network?serverTimezone=UTC ";
    private String user = "root";
    private String password = "ws0508";

	public server() {

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password); // DB ����

			ss = new ServerSocket(9500);
			System.out.println("�����غ�Ϸ�");

			allUserList = new ArrayList<MainHandler>(); // ��ü �����
			friendList = new ArrayList<String>(); // ���� �����
			connUserList = new ArrayList<MainHandler>(); // ���� �����
			roomtotalList = new ArrayList<Room>(); // ��ü �渮��Ʈ
			while (true) {
				Socket socket = ss.accept();
				MainHandler handler = new MainHandler(socket, friendList, connUserList, roomtotalList, conn);// ������ ����
				handler.start();// ������ ����
			} // while
		} catch (IOException io) {
			io.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new server();
	}
}
