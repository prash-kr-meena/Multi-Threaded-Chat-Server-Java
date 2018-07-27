import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    ArrayList al = new ArrayList();
    ServerSocket ss;

    Socket s;

    public ChatServer() {
        try {
            ss = new ServerSocket(30);
            while (true) {
                System.out.println("hello from the other side");
                s = ss.accept();
                System.out.println("client connected!!");
                al.add(s);

                // now servers's job is to just start a new thread
                Runnable runnableJob = new Task(s, al);
                Thread thrd = new Thread(runnableJob);

                // start the thread
                thrd.start();
            }
        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    public static void main(String[] args) {
        new ChatServer();
    }
}

class Task implements Runnable {
    Socket s;
    ArrayList al;

    public Task(Socket s, ArrayList al) {
        this.s = s;
        this.al = al;
    }

    @Override
    public void run() {
        String data;
        try {
            DataInputStream din = new DataInputStream(s.getInputStream());
            do {
                data = din.readUTF();
                System.out.println(data); // print on server side
                if (!data.equals("stop")) {
                    tellEveryOne(data);
                } else {
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.writeUTF(data);
                    dout.flush();
                    al.remove(s); // remove the current user
                }
            } while (!data.equals("stop"));
        } catch (Exception e) {
            System.out.println("Exception while handling user");
            e.printStackTrace();
        }
    }

    public void tellEveryOne(String data){
        Iterator i = al.iterator();
        while (i.hasNext()) {
            try{
                        Socket s = (Socket) i.next();   
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            dout.writeUTF(data);
            dout.flush();
            // System.out.println("");
            }catch(Exception e){
                System.out.println("Error while telling every one !! \n");
                e.printStackTrace();
            }
        }
    }
}
