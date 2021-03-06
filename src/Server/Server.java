/*package Server;

//import java.io.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.*;

public class Server implements Runnable
{
    /**
     *The class represents a server containing a serverStrategy that will handle different client requests.
     * each client is being manipulated as a thread - as part of a threadPool.


    private IServerStrategy strategy;
    private int listenTime;
    private int port;
    private volatile boolean stop;
    private ExecutorService executor;
    public static String ThreadPoolSize;


    public Server(int port, int listenTime , IServerStrategy strategy)
    {
        try {
            Configurations.propertiesCreation();// init the properties by which the server will work (threadPool size, searching algorithm etc.)
        }
        catch(IOException e){
            e.printStackTrace();
        }
        this.strategy = strategy;
        this.listenTime = listenTime;
        this.port = port;
        this.stop = false;
        //this.executor= Executors.newFixedThreadPool(Integer.parseInt(ThreadPoolSize));
        this.executor= Executors.newFixedThreadPool(3);
    }


    public void start ()  {

        new Thread(()->
        {
            run();
        }).start();
    }


    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listenTime);

            while (!stop)
            {
                try
                {
                    Socket clientSocket = serverSocket.accept();

                    Runnable r = new Thread(() ->
                    {
                        handleClient(clientSocket);
                    });
                    executor.execute(r);
                }

                catch (IOException e)
                {
                    System.out.println("Where are the clients?");
                }

            }

            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        catch (SocketTimeoutException | SocketException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public synchronized void stop()
    {
        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        this.stop = true;
        executor.shutdown();
    }


    public void handleClient(Socket clientSocket)
    {
        /**
         * handles the client request using a specific server strategy


        try
        {
            OutputStream outToClient = clientSocket.getOutputStream();
            InputStream inFromClient = clientSocket.getInputStream();

            strategy.handleClient(inFromClient, outToClient);

            inFromClient.close();
            outToClient.close();
            clientSocket.close();
        }

        catch(IOException e){
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Static class that sets the configurations of the server.
     * each property will be assigned into a static string, related to a specific class.

    public static class Configurations
    {
        public static void propertiesCreation() throws IOException
        {
            /**
             * writes the properties to the config file

            try (OutputStream output = new FileOutputStream("resources\\config.properties")) {

                Properties prop = new Properties();

                // set the properties value
                prop.setProperty("ThreadPoolSize", "3");
                prop.setProperty("ASearchingAlgorithm", "BestFirstSearch");
                prop.setProperty("AMazeGenerator", "MyMazeGenerator");

                // save properties to project root folder
                prop.store(output, null);


                /**
                 * reads the properties from the config file
                 * static variables init

            try
            {
                InputStream is = Configurations.class.getClassLoader().getResourceAsStream("config.properties");
                Properties properties = new Properties();
                properties.load(is);

                Server.ThreadPoolSize = properties.getProperty("ThreadPoolSize");
                //ServerStrategySolveSearchProblem.searchingAlgorithmString = properties.getProperty("ASearchingAlgorithm");
                ServerStrategyGenerateMaze.mazeGeneratorString = properties.getProperty("AMazeGenerator");

            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }


    }
}}*/

package Server;

//import java.io.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.*;

public class Server implements Runnable
{
    /**
     *The class represents a server containing a serverStrategy that will handle different client requests.
     * each client is being manipulated as a thread - as part of a threadPool.
     */

    private IServerStrategy strategy;
    private int listenTime;
    private int port;
    private volatile boolean stop;
    private ExecutorService executor;
    public static String ThreadPoolSize;


    public Server(int port, int listenTime , IServerStrategy strategy)
    {
        try {
            Server.Configurations.propertiesCreation();
        } catch (IOException var5) {
            var5.printStackTrace();
        }
        //Configurations.propertiesCreation();// init the properties by which the server will work (threadPool size, searching algorithm etc.)
        this.strategy = strategy;
        this.listenTime = listenTime;
        this.port = port;
        this.stop = false;
        this.executor= Executors.newFixedThreadPool(Integer.parseInt(ThreadPoolSize));
        //this.executor= Executors.newFixedThreadPool(5);
    }


    public void start ()  {

        new Thread(()->
        {
            run();
        }).start();
    }


    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listenTime);

            while (!stop)
            {
                try
                {
                    Socket clientSocket = serverSocket.accept();

                    Runnable r = new Thread(() ->
                    {
                        handleClient(clientSocket);
                    });
                    executor.execute(r);
                }

                catch (IOException e)
                {
                    System.out.println("Where are the clients?");
                }

            }

            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        catch (SocketTimeoutException | SocketException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public synchronized void stop()
    {
        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        this.stop = true;
        executor.shutdown();
    }


    public void handleClient(Socket clientSocket)
    {
        /**
         * handles the client request using a specific server strategy
         */

        try
        {
            OutputStream outToClient = clientSocket.getOutputStream();
            InputStream inFromClient = clientSocket.getInputStream();

            strategy.handleClient(inFromClient, outToClient);

            inFromClient.close();
            outToClient.close();
            clientSocket.close();
        }

        catch(IOException e){
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setProprties(String proprty,String value)
    {
        if(proprty.equals("Threapool"))
        {
            Configurations.Threapool = value;
        }
        else if(proprty.equals("SearchingAlgorithm"))
        {
            Configurations.SearchingAlgorithm = value;
        }
        else if(proprty.equals("MazeGenartor"))
        {
            Configurations.MazeGenartor = value;
        }

        try {
            Server.Configurations.propertiesCreation();
        } catch (IOException var5) {
            var5.printStackTrace();
        }
    }

    public static class Configurations
    {

        private static String Threapool = "3";
        private static String SearchingAlgorithm = "BestFirstSearch";
        private static String MazeGenartor = "MyMazeGenerator";



        public static void propertiesCreation() throws IOException
        {
            /**
             * writes the properties to the config file
             */
            try (OutputStream output = new FileOutputStream("resources\\config.properties")) {

                Properties prop = new Properties();

                // set the properties value
                prop.setProperty("ThreadPoolSize", Threapool);
                prop.setProperty("ASearchingAlgorithm",SearchingAlgorithm);
                prop.setProperty("AMazeGenerator", MazeGenartor);

                // save properties to project root folder
                prop.store(output, null);


                /**
                 * reads the properties from the config file
                 * static variables init
                 */
                try
                {
                    InputStream is = Configurations.class.getClassLoader().getResourceAsStream("config.properties");
                    Properties properties = new Properties();
                    properties.load(is);


                    Server.ThreadPoolSize = properties.getProperty("ThreadPoolSize");
                    ServerStrategySolveSearchProblem.searchingAlgorithmString = properties.getProperty("ASearchingAlgorithm");
                    ServerStrategyGenerateMaze.mazeGeneratorString = properties.getProperty("AMazeGenerator");

                }
                catch(FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }


        }
    }}
