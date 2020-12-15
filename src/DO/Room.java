package DO;

public class Room {
	//���� ID
	private int rID;
	//�濡 �ִ� ���� ��
	private int userCount;
	//���� ���� ���
	private String masterName;
	//�濡 ������ ������ ID ���
	public String roomInUserList;

	public Room() {
		this.rID = 0;
		this.userCount = 0;
		this.masterName = "";
		this.roomInUserList = "";
	}

	public Room(int rID, int userCount, String masterName, String roomInUserList) {
		this.rID = rID;
		this.userCount = userCount;
		this.masterName = masterName;
		this.roomInUserList = roomInUserList;
	}

	public int getrID() {
		return rID;
	}

	public void setrID(int rID) {
		this.rID = rID;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getRoomInUserList() {
		return roomInUserList;
	}

	public void setRoomInUserList(String roomInUserList) {
		this.roomInUserList = roomInUserList;
	}

	@Override
	public String toString() {
		return "Room [rID=" + rID + ", userCount=" + userCount + ", masterName=" + masterName + ", roomUserList=" + roomInUserList +"]";
	}
}
