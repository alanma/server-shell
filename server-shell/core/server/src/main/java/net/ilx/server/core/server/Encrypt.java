package net.ilx.server.core.server;

import org.jasypt.util.password.BasicPasswordEncryptor;

public class Encrypt {

	public static void main(String[] args) {
		BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
		encryptor.encryptPassword("password");
	}
}
