package Action;

public class Protocol {
	public static final String REGISTER = "100"; // ȸ������(request)

	public static final String IDSEARCH = "110"; // IDã�� Join (request)

	public static final String IDSEARCHCHECK = "111"; // (using ȸ������)IDã�� �ߺ�Ȯ��(request)

	public static final String IDSEARCHCHECK_OK = "112"; // (using ȸ������)ID �ߺ�Ȯ�� (��밡��) (ACK)

	public static final String IDSEARCHCHECK_NO = "113"; // (using ȸ������)ID �ߺ�Ȯ�� (��� �Ұ���) (NACK)

	public static final String ENTERLOGIN = "120"; // �α���(request)

	public static final String ENTERLOGIN_OK = "121"; // �α��� ����(ACK)

	public static final String ENTERLOGIN_NO = "122"; // �α��� ����(NACK)

	public static final String ROOMMAKE_OK = "201"; // �游���_ACK 

	public static final String ROOMMAKE_OK1 = "202"; // �游���_ACK(����������)

	public static final String EXITMAINROOM = "203"; // �α׾ƿ�

	public static final String ENTERROOM_OK = "301"; // ������ ����

	public static final String ENTERROOM_OK1 = "302"; // ������ ���� //�����ϴ� �����

	public static final String ENTERROOM_NO = "303"; // ������ ����

	public static final String ENTERROOM_USERLISTSEND = "304"; // �濡 ��������� ������

	public static final String EXITCHATTINGROOM = "305"; // �泪���� (ä�ù� ������)

	public static final String CHATTINGSENDMESSAGE = "420"; // ä�ù濡�� �޼��� ������ (Request)

	public static final String CHATTINGSENDMESSAGE_OK = "430"; // ������ ä�ó��� �ش� ä�ù濡 ����
	
	public static final String REQUEST_FRIEND_LIST = "560";	//ģ���߰� â ���� ��ü ���� ��� ��û
	
	public static final String REQUEST_FRIEND_LIST_CONFIRM = "561"; //������ �������� ���� ��� ����
	
	public static final String REQUEST_FRIEND_ADD = "562"; //ģ���߰��� ���� ���� ����

	public static final String CHANGE_TODAY_LINE = "570"; //������ �Ѹ��� ����
	
	public static final String UPDATE_ME = "571"; //������ �Ѹ��� ���� �����
	
	public static final String UPDATED = "580"; //������ �Ѹ��𺯰�, ģ���߰� ��� ���� �׼� �߻��� �ֽ�ȭ ���� �������� �������� ������Ʈ �Ǿ��ٰ� ����
	
	public static final String UPDATE_PLZ = "581"; //UPDATED ������ ������ ������Ʈ�� �׸� �ֽ�ȭ ������ ��û
	
	public static final String UPDATE_CONFIRM = "582";//������ UPDATE_PLZ ������ ������ �������� ����
	
	public static final String CHECK_FRIEND_INFO = "590"; //ģ���� ���� Ȯ�� ��û
	
	public static final String CONFIRM_FRIEND_INFO = "591"; //ģ���� ���� ������ �������� ����
	
	public static final String REQUEST_GROUPCHAT_LIST = "600"; //�׷�ä��â ���� ���� ģ�� ��� ��û
	
	public static final String GROUPCHAT_LIST = "601"; //ģ�� ��� ����
	
	public static final String REQUEST_MAKE_GROUPCHAT = "602"; //�ش� ���� ������ ä��â ����� ��û
	
	public static final String JOINROOM_REQUEST = "610"; //������ ä�� �ʴ��ϸ� �ʴ� �޼��� ����
	
	public static final String JOINROOM_YES = "611"; //ä�� ��û ����
	
	public static final String JOINROOM_NO = "612"; //ä�� ��û ����
	
	public static final String ENTERROOM_REJECT = "620"; //���� ������ �������ִ� �����鿡��
	
	public static final String EXIT_CHATTINGROOM = "621"; //ä��â�� ������ ��������
	
	public static final String REQUEST_FRIEND_DELETE="630"; //ģ������
	
	public static final String CONFIRM_FRIEND_DELETE="631";//����Ȯ��
	
}
