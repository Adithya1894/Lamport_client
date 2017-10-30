import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Client Class is used to perform all the client side actions
 */
public class Client {
    private static int logical_clock = 0;
    private int berkely_clock_offset = 0;
    private static int port_number = 8423;
    private static String ip = "localhost";
    private static Socket socket_obj;
    /**
     * static variables for outputStream or sending the messages to the server
     */
    private static OutputStream os_obj;
    private static OutputStreamWriter osw_obj;
    private static BufferedWriter bw_obj;
    /**
     * static variables for receiving the messages from the server
     */
    private static BufferedReader br_obj = null;
    private static DataInputStream din = null;
    //private static InputStream is_obj;
    //private static InputStreamReader isr_obj;
    // private static InputStream is = null;

    Thread t;



    /**
     * send_message Method is used to send the data to the server
     */
    public void send_message(){
        logical_clock++;
        //System.out.println("Send Message Clock:" +logical_clock);
        encrypt(logical_clock);

    }


    /**
     * receive_message Method is used to receive the data from the server
     */
    public  void receive_message(){
        int receive = 0;

        logical_clock = logical_clock + decrypt() + 1;
        System.out.println("Receive Message Clock:" +logical_clock);
    }

    /**
     * internal_event method is used to perform some internal event within the client. I am implementing a simple clock increment
     */
    public void internal_event() throws InterruptedException {
        logical_clock = logical_clock + 1;
        t.sleep(100);
        // System.out.println("Internal Event Clock:" +logical_clock);
        //encrypt(logical_clock);
    }

    /**
     * byzantine_failure Method is used to implement Byzantine failures in the system
     * @return
     */
    public void byzantine_failure(){
        logical_clock = logical_clock + 3;
        logical_clock++;
        //System.out.println("Byzantine Clock:" +logical_clock);
        encrypt(logical_clock);
        //return logical_clock;
    }

    /**
     * This method is used to encrypt the clock values sent to the server
     * @return
     */
    public int encrypt(int clock){
        clock = clock;
        try {
            os_obj = socket_obj.getOutputStream();
            osw_obj = new OutputStreamWriter(os_obj);
            bw_obj = new BufferedWriter(osw_obj);

            //int message = clock;
            int y = 3*clock;
            //int encrypted_data = Math.max(y, 26);
            String message = Integer.toString(y) + "\n";
            bw_obj.write(message);
            bw_obj.flush();

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }


        return 0;
    }

    /**
     * This method is used to Decrypt the clock values received from the server
     * This method performs the Decryption on the messages which are encrypted by the server
     * @return
     */
    public int decrypt(){
        String message;
        int offser_number_t = 0;
        int decrypted_data = 0;

        try {

            if(br_obj.ready())
            {
                message = br_obj.readLine();
                offser_number_t = Integer.parseInt(message);
                decrypted_data = offser_number_t/3;
                //System.out.println(offser_number_t);
            }

            //System.out.println("Proper Clock value received from Server is: " +message);
            //return message/10;
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
        return decrypted_data;


    }

    /**
     * Main Method of the client class. This method has all the functionality required for the
     * programm to run properly, I am using a Random number generator to get the random values
     * as probabilities and call different methods in the process based on probability
     * @param args
     * @throws InterruptedException
     * @throws NullPointerException
     */
    public static void main(String args[]) throws InterruptedException, NullPointerException{

        try {
            socket_obj = new Socket(ip, port_number);
            System.out.println("hello, i'm Connected!");

            // br_obj = new BufferedReader(new InputStreamReader(socket_obj.getInputStream()));

            Client Client_obj = new Client();
            //int number = 0;

            //latch.await();
            int l = 0;

            while(l < 10000) {
                if (true)
                {
                    Random r_obj = new Random();
                    int probability = r_obj.nextInt(20);// getting the probability with which each event can happen
                    // System.out.println(probability);
                    /**
                     * checking for the designated probabilities for each event in the process object to happen
                     */
                    if (probability >= 0 && probability < 7) {
                        Client_obj.send_message();
                    } else if (probability > 7 && probability < 14) {
                        Client_obj.receive_message();
                    } else if (probability >= 14 && probability < 19) {
                        Client_obj.internal_event();
                    } else {
                        Client_obj.byzantine_failure();
                    }

                }
                //din = new DataInputStream(socket_obj.getInputStream());
                //Client_obj.send_message();
                //Client_obj.receive_message();
                br_obj = new BufferedReader(new InputStreamReader(socket_obj.getInputStream(), "ISO-8859-1"));
                //is = socket_obj.getInputStream();


                l++;
            }
            //System.out.println(logical_clock);
            //System.out.println("hello");

            CountDownLatch latch = new CountDownLatch(1000);


        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Closing the connection once the program is done executing
         */
        finally {
            try {
                socket_obj.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }





    }


}