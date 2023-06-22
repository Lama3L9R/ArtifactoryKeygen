package org.jfrog.license.a;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public final class ObfuscatedString {
    private static final String psf_field_0 = new String(new char[] { 'U', 'T', 'F', '8' });

    private final String pf_field_1;

    public ObfuscatedString(long[] var1) {
        int var2 = var1.length;
        byte[] var3 = new byte[8 * (var2 - 1)];
        long var4 = var1[0];
        Random var6 = new Random(var4);

        for(int var7 = 1; var7 < var2; ++var7) {
            long var8 = var6.nextLong();
            ps_method_1(var1[var7] ^ var8, var3, 8 * (var7 - 1));
        }

        String var11;
        try {
            var11 = new String(var3, psf_field_0);
        } catch (UnsupportedEncodingException var10) {
            throw new AssertionError(var10);
        }

        int var12 = var11.indexOf(0);
        this.pf_field_1 = var12 != -1?var11.substring(0, var12):var11;
    }

    private static long ps_method_0(byte[] paramArrayOfbyte, int paramInt) {
        long l = 0L;
        int i = Math.min(paramArrayOfbyte.length, paramInt + 8);
        for (int j = i; --j >= paramInt; ) {
            l <<= 8L;
            l |= (paramArrayOfbyte[j] & 0xFF);
        }
        return l;
    }

    private static void ps_method_1(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
        int i = Math.min(paramArrayOfbyte.length, paramInt + 8);
        for (int j = paramInt; j < i; j++) {
            paramArrayOfbyte[j] = (byte)(int)paramLong;
            paramLong >>= 8L;
        }
    }

    /**
     * Renamed from a(Ljava/lang/string)Ljava/lang/String;
     * @param paramString obfuscate target string
     * @return obfuscated java string code
     */
    public static String createObfuscation(String paramString) {
        byte[] arrayOfByte;
        if (paramString.indexOf(0) != -1)
            throw new IllegalArgumentException((new ObfuscatedString(new long[] { 2598583114197433456L, -2532951909540716745L, 1850312903926917213L, -7324743161950196342L, 3319654553699491298L })).toString());
        try {
            arrayOfByte = paramString.getBytes(psf_field_0);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError(unsupportedEncodingException);
        }
        Random random = new Random();
        while (true) {
            long l = random.nextLong();
            if (l != 0L) {
                random = new Random(l);
                StringBuffer stringBuffer = new StringBuffer((new ObfuscatedString(new long[] { -6733388613909857970L, -557652741307719956L, 563088487624542180L, 5623833171491374716L, -2309350771052518321L, 2627844803624578169L })).toString());
                ps_method_2(stringBuffer, l);
                int i = arrayOfByte.length;
                for (byte b = 0; b < i; b += 8) {
                    long l1 = random.nextLong();
                    long l2 = ps_method_0(arrayOfByte, b) ^ l1;
                    stringBuffer.append(", ");
                    ps_method_2(stringBuffer, l2);
                }
                stringBuffer.append((new ObfuscatedString(new long[] { 3426903566703633623L, -2851967657861944394L, 8678768925462458602L, 3688610158019998715L })));
                stringBuffer.append(paramString
                        .replaceAll("\\\\", (new ObfuscatedString(new long[] { 7866777055383403009L, -5101749501440392498L })).toString())
                        .replaceAll("\"", (new ObfuscatedString(new long[] { -8797265930671803829L, -5738757606858957305L })).toString()));
                stringBuffer.append((new ObfuscatedString(new long[] { -4228881123273879289L, 1823585417647083411L })));
                return stringBuffer.toString();
            }
        }
    }

    private static void ps_method_2(StringBuffer paramStringBuffer, long paramLong) {
        paramStringBuffer.append("0x");
        paramStringBuffer.append(Long.toHexString(paramLong).toUpperCase());
        paramStringBuffer.append('L');
    }

    public String toString() {
        return this.pf_field_1;
    }
}