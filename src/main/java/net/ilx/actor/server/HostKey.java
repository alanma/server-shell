package net.ilx.actor.server;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.mina.util.Base64;
import org.apache.sshd.common.KeyPairProvider;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.keyprovider.PEMGeneratorHostKeyProvider;
import org.bouncycastle.util.encoders.Hex;
import org.jboss.logging.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class HostKey {
	private static final Logger LOG = Logger.getLogger(HostKey.class);

	@Option(name = "-g", usage = "generate key")
	private boolean generate;

	@Option(name = "-i", usage = "info on key")
	private boolean info;

	@Option(name = "-v", usage = "calculate key fingerpring")
	private boolean verify;

	@Option(name = "-f", usage = "key file ", required = true)
	private String file;

	// receives other command line parameters than options
	@Argument
	private List<String> arguments = new ArrayList<String>();

	public static void main(final String[] args) throws NoSuchAlgorithmException, IOException {
		new HostKey().doMain(args);
	}

	public void doMain(final String[] args) throws NoSuchAlgorithmException, IOException {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);

			execute();
		} catch (CmdLineException ex) {
			LOG.errorv(ex, "unable to parse parameters");
			parser.printUsage(System.err);
		}

	}

	private void execute() throws NoSuchAlgorithmException, IOException {
		if (generate && file != null) {
			simpleKeyGenerator(file);
		}
		if (info && file != null) {
			info(file);
		}
		if (verify && file != null) {
			verify(file);
		}
	}

	private void verify(final String filePath) throws IOException, NoSuchAlgorithmException {
		File file = new File(filePath);
		byte[] data = FileUtils.readFileToByteArray(file);
		byte[] decodedData = Base64.decodeBase64(data);

		fingerprint(decodedData);

	}

	private void info(final String file) throws NoSuchAlgorithmException
	{
		FileKeyPairProvider provider = new FileKeyPairProvider(new String[] {file});
		KeyPair key = provider.loadKey(KeyPairProvider.SSH_DSS);

		PrivateKey privateKey = key.getPrivate();
		PublicKey publicKey = key.getPublic();

		dumpKey(privateKey.getEncoded());
		fingerprint(privateKey.getEncoded());

		dumpKey(publicKey.getEncoded());
		fingerprint(publicKey.getEncoded());

	}

	private void fingerprint(final byte[] key) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(key);
		byte[] digest = md.digest();
		byte[] fingerPrint = Hex.encode(digest);
		System.out.println("Fingerprint: " + new  String(fingerPrint));
	}

	private void dumpKey(final byte[] key) {
		byte[] base64Value = Base64.encodeBase64(key);
		System.out.println(new String(base64Value));
	}

	private void simpleKeyGenerator(final String path)
	{
		PEMGeneratorHostKeyProvider keyProvider = new PEMGeneratorHostKeyProvider();
		keyProvider.setAlgorithm("DSA");
		keyProvider.setKeySize(512);
		keyProvider.setPath(path);

		// loadkey ce kreirati datoteku ukoliko ona vec ne postoji
		keyProvider.loadKeys();
	}

}
