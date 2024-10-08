package icu.lama.artifactory.agent;

import icu.lama.artifactory.agent.patches.PatcherLicenseParser;
import icu.lama.artifactory.agent.patches.PublicKeyOverrider;

import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AgentMain {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static void premain(String args, Instrumentation ins) {
        if (args != null) {
            // Hex string is shell friendly
            var argsRaw = Base64.getDecoder().decode(hexStringToByteArray(args));
            try {
                var doc = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().parse(new ByteArrayInputStream(argsRaw));

                var key = doc.getElementsByTagName("publicKey");
                if (key.getLength() > 1) {
                    System.out.println("Illegal config! Multiple PublicKey found but 1 is the only allowed amounts.");
                } else if (key.getLength() != 0) {
                    Constants.PUBLIC_KEY = key.item(0).getFirstChild().getNodeValue();
                    System.out.println("Artifactory Agent :: Overriding default PUBLIC_KEY to" + Constants.PUBLIC_KEY);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        System.out.println("Artifactory Agent :: =====================================");
        System.out.println("Artifactory Agent ::   Artifactory Agent | by lamadaemon  ");
        System.out.println("Artifactory Agent ::   Is now LOADED!                     ");
        System.out.println("Artifactory Agent ::                                      ");
        System.out.println("Artifactory Agent ::   ALERT! NONE-COMMERCIAL USAGE ONLY! ");
        System.out.println("Artifactory Agent ::   ALERT! USE AT YOUR OWN RISK!       ");
        System.out.println("Artifactory Agent :: =====================================");

        //ins.addTransformer(new PatcherLicenseParser());
        ins.addTransformer(new PublicKeyOverrider());
    }

    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("This can encrypt arguments into Base64 + hex string format");
        } else {
            System.out.println(bytesToHex(Base64.getEncoder().encode(String.join(" ", args).getBytes(StandardCharsets.UTF_8))));
        }
    }

    // source: https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    // source: https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
