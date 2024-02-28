package gitlet;

import java.io.File;

public class Test {

    public static void main(String[] args) {

       File input = Utils.join(System.getProperty("user.dir"), "abcd.txt");
   /*    String a = new String("abc");
       byte [] byteArr = Utils.serialize(a);
        System.out.println(Utils.sha1(byteArr));*/
      // Utils.writeObject(input, byteArr);
       byte[] byteArr2 = Utils.readObject(input, byte[].class);
        System.out.println(Utils.sha1(byteArr2));




    }
}
