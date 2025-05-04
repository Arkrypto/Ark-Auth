package cia.arkrypto.auth.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.math.BigInteger;
import java.util.Base64;
import java.util.Map;

public class EncodeUtil {


    public static String parseBigInteger2HexStr(BigInteger bi){
        return bi.toString(16);
    }

    public static BigInteger parseHexStr2BigInteger(String str){
        return new BigInteger(str, 16);
    }


    public static String parseElement2Base64Str(Element element){
        byte[] bytes = element.toBytes(); // 转为 byte[]
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Element parseBase64Str2Element(String base64, Field field){
        byte[] bytes = Base64.getDecoder().decode(base64);
        return field.newElementFromBytes(bytes).getImmutable();
    }

    public static StringBuilder subComma(StringBuilder str){
        if (!str.isEmpty() && ',' == str.charAt(str.length() - 1)){
            return new StringBuilder(str.substring(0, str.length() - 1));
        }
        return str;
    }



    public static Map<String, Object> enc0(String num) {
        long s = System.nanoTime();
        StringBuilder result = new StringBuilder();
        for (int i = num.length() - 1; i > -1; i--) {
            if ('0' == num.charAt(i)) {
                for (int j = 0; j < i; j++)
                    result.append(num.charAt(j));
                result.append('1');
                result.append(",");
            }
        }
        result = subComma(result);
        long e = System.nanoTime();

        return Map.of("result", result.toString(), "time", e-s);
    }



    public static Map<String, Object> enc1(String num) {
        long s = System.nanoTime();
        StringBuilder result = new StringBuilder();
        for (int i = num.length() - 1; i > -1; i--) {
            if ('1' == num.charAt(i)) {
                for (int j = 0; j <= i; j++)
                    result.append(num.charAt(j));
                if(i != 0) {
                    result.append(",");
                }
            }
        }
        result = subComma(result);
        long e = System.nanoTime();

        return Map.of("result", result.toString(), "time", e-s);
    }
}
