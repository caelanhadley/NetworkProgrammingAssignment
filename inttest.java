import java.net.*; // for Socket, ServerSocket, and InetAddress
import java.io.*; // for IOException and Input/OutputStream

public class inttest {
    public static void main(String[] args) {
        String message = "16735";
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            int ascii = (int) c;
            System.out.println(Integer.toHexString(ascii));
        }

    }
}
