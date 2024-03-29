import java.net.*; // for Socket
import java.io.*; // for IOException and Input/OutputStream
import java.util.Scanner;
import java.time.*;

public class myFirstTCPClient {

    public static String byteToHex(byte[] messageBuffer, boolean trim) {
        String result = "";
        for (int i = 0; i < messageBuffer.length; i++) {
            result += "0x" + String.format("%02x", messageBuffer[i]);
            if (i != messageBuffer.length - 1) {
                result += " ";
            }
        }
        if (trim) {
            result = result.replace(" 0x00", "");
        }
        return result;
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println(args.length);
            throw new IllegalArgumentException("Parameter(s): <Server> [<Port>]");
        }

        String server = args[0]; // Server name or IP address
        int servPort = (args.length == 2) ? Integer.parseInt(args[1]) : 7;

        // Create socket that is connected to server on specified port
        Socket socket = new Socket(server, servPort);
        System.out.println("Connected to server...sending echo string");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        Scanner scan = new Scanner(System.in);

        String message = "-1";
        int int_message = -1;

        while (true) {
            System.out.print("Enter an integer in the range [0-32767]: ");
            try {
                message = scan.nextLine();
                int_message = Integer.parseInt(message);
                if (int_message <= 32767 && int_message >= 0) {
                    break;
                }
            } catch (Exception e) {
                break;
            }
            System.out.println("Invalid input please try again...");
        }

        // Convert string array into byte array.
        byte[] messageBuffer = message.getBytes("UTF-16");

        System.out.println("Sending : " + byteToHex(messageBuffer, false));

        long startTime = -1;
        long endTime = -1;

        startTime = System.currentTimeMillis();
        out.write(messageBuffer);
        scan.close(); // Close the Scanner object

        // Receive the same string back from the server
        int totalBytesRcvd = 0; // Total bytes received so far
        int bytesRcvd; // Bytes received in last read
        while (totalBytesRcvd < messageBuffer.length) {
            if ((bytesRcvd = in.read(messageBuffer, totalBytesRcvd,
                    messageBuffer.length - totalBytesRcvd)) == -1)
                throw new SocketException("Connection close prematurely");
            totalBytesRcvd += bytesRcvd;
        }

        System.out.println("Received : " + byteToHex(messageBuffer, true));

        endTime = System.currentTimeMillis();
        System.out.println("Round-Trip Time: " + (endTime - startTime) + "ms");
        socket.close(); // Close the socket and its streams
    }
}
