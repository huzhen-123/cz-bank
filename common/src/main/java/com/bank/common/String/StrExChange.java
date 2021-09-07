package com.bank.common.String;

public class StrExChange {

    //将字符串里的小写转大写
    public static String exChange(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null) {
            char[] c = str.toCharArray();
            for (int i = 0; i < str.length(); i++) {
                if (c[i] >= 'a' && c[i] <= 'z') c[i] = Character.toUpperCase(c[i]);
                sb.append(c[i]);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(exChange("A13b"));
    }
}
