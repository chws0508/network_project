package Action;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



public class RSA {
	static final int KEY_SIZE = 1028;//��Ʈ 1028�� ����
	
    /**
     * ��ȣȭ
     */
    public static String encode(String plainData, String stringPublicKey) {
        String encryptedData = null;
        try {
            //������ ���޹��� ����Ű�� ����Ű��ü�� ����� ����
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());//���ڿ��� ����Ʈ�� ��ȯ
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);//����Ʈ�� publicKey�� ���ڵ�
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);//publicKey����

            //������� ����Ű��ü�� ������� ��ȣȭ���� �����ϴ� ����
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //���� ��ȣȭ�ϴ� ����
            byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    /**
     * ��ȣȭ
     */
   public static String decode(String encryptedData, String stringPrivateKey) {
        String decryptedData = null;
        try {
            //������ ���޹��� ����Ű�� ����Ű��ü�� ����� ����
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());//���ڿ��� ����Ʈ�� ��ȯ
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);//����Ʈ�� privateKey�� ���ڵ�
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);//privateKey ����

            //������� ����Ű��ü�� ������� ��ȣȭ���� �����ϴ� ����
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //��ȣ���� ��ȭ�ϴ� ����
            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData.getBytes());
            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);
            decryptedData = new String(byteDecryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }

}



