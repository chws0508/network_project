package DO;

public class User {
	//������ ������ȣ
	private int pryNumber;
	//������ ID
	private String idName;
	//������ ��й�ȣ
	private String password;
	//������ ����
	private String nickName;
	//������ �̸�
	private String name;
	//������ �̸���
	private String email;
	//������ ����
	private String age;
	//������ ������ �Ѹ���
	private String today_line;
	//������ ���� ����
	private int state;
	
	
	public User() {
		this(0, "", "", "", "", "", "", "Enter Today Message", 0);
	}

	public User(int pryNumber, String idName, String password, String nickName, String name, String email ,String age,
			String today_line, int state) {
		super();
		this.pryNumber = pryNumber;
		this.idName = idName;
		this.password = password;
		this.name = name;
		this.age = age;
		this.email = email;
		this.today_line = today_line;
		this.state = state;
	}

	public int getPryNumber() {
		return pryNumber;
	}

	public void setPryNumber(int pryNumber) {
		this.pryNumber = pryNumber;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}
	
	public String getnickName() {
		return nickName;
	}

	public void setnickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToday_line() {
		return today_line;
	}

	public void setToday_line(String today_line) {
		this.today_line = today_line;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "User [pryNumber=" + pryNumber + ", idName=" + idName + ", password=" + password + ", nickName=" + nickName
				+ ", name=" + name + ", email=" + email + ", age=" + age + ", today_line=" + today_line +  ", state=" + state + "]";
	}
}
