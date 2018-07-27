import java.io.*;
import java.util.*;
import java.net.*;

public class ChatClient {
    Socket s;
    DataOutputStream dout;
    DataInputStream din;

    public ChatClient() {
        String host = "localhost";
        try {
            s = new Socket(host, 30);
            dout = new DataOutputStream(s.getOutputStream());
            din = new DataInputStream(s.getInputStream());

            clientChat();
        } catch (Exception e) {
            System.out.println("Error while user creation \n");
            e.printStackTrace();
        }
    }

    public void clientChat() {
        // start the reading thread
        Runnable runnableJob = new ReadTask(din);
        Thread readThread = new Thread(runnableJob);
        readThread.start();

        // writing job of this user
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//conversion
        String data = "";
        do {
            try {
                data = br.readLine();
                dout.writeUTF(data);
                dout.flush();
            } catch (Exception e) {
                System.out.println("Error while reading from keyboard --> client side");
            }

        } while (!data.equals("stop"));

    }

    public static void main(String[] args) {
        new ChatClient();
    }

}

class ReadTask implements Runnable {
    DataInputStream din;

    public ReadTask(DataInputStream din) {
        this.din = din;
    }

    @Override
    public void run() {
        String data = "";
        do {
            try {
                data = din.readUTF();
                System.out.println(data); // print on client  side
            } catch (Exception e) {
                System.out.println("Error while readng form server");
            }
        } while (!data.equals("stop"));
    }
}
