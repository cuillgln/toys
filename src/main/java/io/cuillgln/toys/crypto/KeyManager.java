
package io.cuillgln.toys.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyManager {

	public static void main(String[] args) {
		try {
			/*KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
			SecretKey key = keyGenerator.generateKey();
			System.out.println(key.getAlgorithm());
			System.out.println(key.getFormat());
			System.out.println(key.getEncoded().length);
			System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));

			SecretKey hmacKey = KeyGenerator.getInstance("HmacSha256").generateKey();
			System.out.println(hmacKey.getAlgorithm());
			System.out.println(hmacKey.getFormat());
			System.out.println(hmacKey.getEncoded().length);
			System.out.println(Base64.getEncoder().encodeToString(hmacKey.getEncoded()));

			// secret key spec 可以自己指定，因为只是一个密码，与算法无关
			SecretKeySpec keySpec = new SecretKeySpec(hmacKey.getEncoded(), "HmacSHA256");
			Mac hmacSha256 = Mac.getInstance("HmacSHA256");
			hmacSha256.init(keySpec);

			System.out.println(keySpec.getAlgorithm());
			System.out.println(keySpec.getFormat());
			System.out.println(keySpec.getEncoded().length);
			System.out.println(Base64.getEncoder().encodeToString(keySpec.getEncoded()));

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			System.out.println("======================");
			RSAPrivateKey pk = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
			// System.out.println(pk.getAlgorithm());
			// System.out.println(pk.getFormat());
			// System.out.println(pk.getEncoded().length);
			// System.out.println(Base64.getEncoder().encodeToString(pk.getEncoded()));
			// System.out.println(pk.getModulus().toString());
			// System.out.println(pk.getPrivateExponent().toString());

			System.out.println("======================");
			System.out.println(pubKey.getAlgorithm());
			System.out.println(pubKey.getFormat());
			System.out.println(pubKey.getEncoded().length);
			System.out.println(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
			System.out.println(pubKey.getModulus().toString());
			System.out.println(pubKey.getPublicExponent().toString());

			// RSAPrivateKeySpec priKeySpec = new
			// RSAPrivateKeySpec(pk.getModulus(), pk.getPrivateExponent());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec priKeySpec = keyFactory.getKeySpec(pk, PKCS8EncodedKeySpec.class);
			RSAPublicKey puKey = (RSAPublicKey) keyFactory.generatePublic(priKeySpec);
			// System.out.println(gpk.getAlgorithm());
			// System.out.println(gpk.getFormat());
			// System.out.println(gpk.getEncoded().length);
			// System.out.println(Base64.getEncoder().encodeToString(gpk.getEncoded()));
			// System.out.println(gpk.getModulus().toString());
			// System.out.println(gpk.getPrivateExponent().toString());

			System.out.println(puKey.getAlgorithm());
			System.out.println(puKey.getFormat());
			System.out.println(puKey.getEncoded().length);
			System.out.println(Base64.getEncoder().encodeToString(puKey.getEncoded()));
			System.out.println(puKey.getModulus().toString());
			System.out.println(puKey.getPublicExponent().toString());
*/
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
		    SecretKey aesKey = keygen.generateKey(); //
			
		    SecretKey skeySpec = new SecretKeySpec(aesKey.getEncoded(), aesKey.getAlgorithm());
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			 // Our cleartext
		    byte[] cleartext = "This is just an example".getBytes();

		    // Encrypt the cleartext
		    byte[] ciphertext = aesCipher.doFinal(cleartext);

		    // Initialize the same cipher for decryption
		    aesCipher.init(Cipher.DECRYPT_MODE, skeySpec);

		    // Decrypt the ciphertext
		    byte[] cleartext1 = aesCipher.doFinal(ciphertext);
			System.out.println(new String(cleartext1));

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
