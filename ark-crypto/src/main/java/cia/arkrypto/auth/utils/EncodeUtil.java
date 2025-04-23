package cia.arkrypto.auth.utils;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.Map;

public class EncodeUtil {

    private static Pairing bp = PairingFactory.getPairing("a.properties");
    public static void main(String[] args) {

        int num = 0;
        Field Zr = bp.getZr();

        for(int i = 0; i < 9; i++){

            String randomStr = Integer.toBinaryString(num);
            String randomStrWithPadding = String.format("%5s", randomStr).replaceAll(" ", "0");
            Map<String, Object> encResult0 = enc0(randomStrWithPadding);
            Map<String, Object> encResult1 = enc1(randomStrWithPadding);

            System.out.println("Num: " + num);
            System.out.println(encResult0);
            System.out.println(HashUtil.hashStr2Zr(Zr, (String) encResult0.get("result"), 64));

            System.out.println(encResult1);
            System.out.println(HashUtil.hashStr2Zr(Zr, (String) encResult1.get("result"), 64) + "\n");

            num += 10;
        }
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
        if (!result.isEmpty() && ',' == result.charAt(result.length() - 1)){
            result = new StringBuilder(result.substring(0, result.length() - 1));
        }
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
        long e = System.nanoTime();

        return Map.of("result", result.toString(), "time", e-s);
    }
}
