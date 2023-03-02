import java.net.*; // for Socket, ServerSocket, and InetAddress
import java.io.*; // for IOException and Input/OutputStream
import java.nio.charset.StandardCharsets;

public class TCPEchoServer {

  private static final int BUFSIZE = 32; // Size of receive buffer

  public static void main(String[] args) throws IOException {

    if (args.length != 1) // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    // Create a server socket to accept client connection requests
    ServerSocket servSock = new ServerSocket(servPort);

    int recvMsgSize; // Size of received message
    byte[] byteBuffer = new byte[BUFSIZE]; // Receive buffer

    for (;;) { // Run forever, accepting and servicing connections
      Socket clntSock = servSock.accept(); // Get client connection

      System.out.println("Handling client at " +
          clntSock.getInetAddress().getHostAddress() + " on port " +
          clntSock.getPort());

      InputStream in = clntSock.getInputStream();
      OutputStream out = clntSock.getOutputStream();

      byte[] received_msg = new byte[BUFSIZE];
      String print_msg = "";

      // Receive until client closes connection, indicated by -1 return
      while ((recvMsgSize = in.read(byteBuffer)) != -1) {
        for (int i = 0; i < received_msg.length; i++) {
          print_msg += "0x" + Integer.toHexString(byteBuffer[i]) + " ";
        }
        String sIn = new String(byteBuffer, 0, recvMsgSize, StandardCharsets.UTF_16).trim();
        System.out.println("Received: " + print_msg.replace("0x0 0x0", ""));
        System.out.println("\n" + sIn);
        try {
          Short sOut = Short.parseShort(sIn);
          byte[] bytes = new byte[BUFSIZE];
          bytes[0] = (byte) (sOut >> 8);
          bytes[1] = (byte) (sOut & 0xff);
          System.out.println("0x" + Integer.toHexString(bytes[0]) + "0x" + Integer.toHexString(bytes[1]));
          out.write(bytes, 0, recvMsgSize);
        } catch (Exception e) {
          byte[] bytes = new byte[1];
          bytes[0] = -1;
          out.write(bytes, 0, 1);
        }

      }

      clntSock.close(); // Close the socket. We are done with this client!
    }
    /* NOT REACHED */
  }
}
