import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Client Class is used to perform all the client side actions
 */
public class MorampudiClient {
    private static int logical_clock = 0;
    private int berkely_clock_offset = 0;
    private static int port_number = 25000;
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
    private static InputStream is_obj;
    private static InputStreamReader isr_obj;
    private static BufferedReader br_obj;


    /**
     * send_message Method is used to send the data to the server
     */
    public void send_message(){
        logical_clock++;
        System.out.println("Send Message Clock:" +logical_clock);
        encrypt(logical_clock);

    }


    /**
     * receive_message Method is used to receive the data from the server
     */
    public  void receive_message(){
        logical_clock++;
        
        decrypt();
        System.out.println("Receive Message Clock:" +logical_clock);
    }

    /**
     * internal_event method is used to perform some internal event within the client. I am implementing a simple clock increment
     */
    public void internal_event(){
        logical_clock = logical_clock + 1;
        System.out.println("Internal Event Clock:" +logical_clock);
        //encrypt(logical_clock);
    }

    /**
     * byzantine_failure Method is used to implement Byzantine failures in the system
     * @return
     */
    public void byzantine_failure(){
        logical_clock = logical_clock + 100;

        System.out.println("Byzantine Clock:" +logical_clock);
        encrypt(logical_clock);
        //return logical_clock;
    }

    /**
     * This method is used to encrypt the clock values sent to the server
     * @return
     */
    public int encrypt(int clock){
        clock = clock * 10;
        try {
            os_obj = socket_obj.getOutputStream();
            osw_obj = new OutputStreamWriter(os_obj);
            bw_obj = new BufferedWriter(osw_obj);

            int message = clock;
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
        int message = 0;
       
        try {
            is_obj = socket_obj.getInputStream();
            isr_obj = new InputStreamReader(is_obj);
            br_obj = new BufferedReader(isr_obj);
            
            message = br_obj.read();
            
            //System.out.println("Proper Clock value received from Server is: " +message);
      		//return message/10;      
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
        	return message/10;
	
      
    }

    public static void main(String args[]){
        //Scanner sc = new Scanner(System.in);
        //System.out.println("Enter the Ip Address of the Server where you want the Client to connect to: ");
        //ip = sc.next();
        //System.out.println("Enter the port number of the Server!");
        //port_number = sc.nextInt();


        try {
            socket_obj = new Socket(ip, port_number);
            System.out.println("hello, i'm Connected!");

            // br_obj = new BufferedReader(new InputStreamReader(socket_obj.getInputStream()));
			
			
			
			
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        MorampudiClient Client_obj = new MorampudiClient();
        int number = 0;
        while(number < 1000)
        {
            Random r_obj = new Random();
            int probability = r_obj.nextInt(20);// getting the probability with which each event can happen
            // System.out.println(probability);
            /**
             * checking for the designated probabilities for each event in the process object to happen
             */
            if(probability >= 0 && probability < 7)
            {
                Client_obj.send_message();
            }
            else if(probability > 7 && probability < 14){
                Client_obj.receive_message();
            }
            else if(probability >= 14 && probability < 19)
            {
                Client_obj.internal_event();
            }
            else{
                Client_obj.byzantine_failure();
            }
            number++;
        }
        System.out.println(logical_clock);
        System.out.println("hello");


        /**
         * object for the Client class
         */
        
    }


}
