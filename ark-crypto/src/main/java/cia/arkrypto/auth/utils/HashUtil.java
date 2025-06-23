package cia.arkrypto.auth.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class HashUtil {

    public static byte[] concat(byte[] ... arr) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            for(byte[] bytes: arr){
                outputStream.write(bytes);
            }
        } catch (IOException e){
            System.out.println("IOException: concat bytes error");
        }
        return outputStream.toByteArray();
    }


    public static Element hashByte2Group(Field group, byte[] bytes){
        return group.newElementFromHash(bytes, 0, bytes.length).getImmutable();
    }


    public static Element hashStr2Group(Field group, String ... strings){
        int n = strings.length;
        byte[][] bytes = new byte[n][];
        for(int i = 0; i < n; i++){
            bytes[i] = strings[i].getBytes();
        }
        byte[] input = concat(bytes);
        return hashByte2Group(group, input);
    }

}
