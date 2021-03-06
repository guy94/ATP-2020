/*package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ServerStrategySolveSearchProblem implements IServerStrategy
{
    public static String searchingAlgorithmString;//config file data member
    private HashMap<byte[], Solution> SolutionsMap = new  HashMap<byte[], Solution>();
    private Solution solved;

    /**
     data members used for the file creation inorder to store SolutionsMap HashMap


    private String tempDirectoryPath = System.getProperty("java.io.tmpdir");
    private FileOutputStream fos;
    private FileInputStream fis;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private File file;
    private boolean isFile = false;


    @Override
    public void handleClient(InputStream inputStream, OutputStream outputStream){

        /**
         * receiving a maze from the client inputStream.
         * checks in a file (storing a HashMap<byte[], Solution>) if the current maze was already solved earlier by the server.
         *  if so, we pull the solution of the current maze from the same file.
         * else, convert the maze into a searchable maze and solves it with a given ASearchingAlgorithm.

        try
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            //ISearchingAlgorithm searchAlgorithm = ASearchingAlgorithm.algorithmType(searchingAlgorithmString);
            ISearchingAlgorithm searchAlgorithm = new BestFirstSearch();

            Maze maze = (Maze)objectInputStream.readObject();
            byte[] mazeByteArr = maze.toByteArray();

            byte[] returned = null;
            if(isFile)
            {
                readFromFile();
                ArrayList<byte[]> keysArrayList = new ArrayList<byte[]>(this.SolutionsMap.keySet());
                returned = isExist(keysArrayList, mazeByteArr);
            }

            if( returned != null )
            {
                solved = SolutionsMap.get(returned);
            }
            else
            {
                ISearchable searchablemaze = new SearchableMaze(maze);
                solved = searchAlgorithm.solve(searchablemaze);
                SolutionsMap.put(mazeByteArr, solved);
                writeToFile();
            }

            objectOutputStream.writeObject(solved);
            objectOutputStream.flush();
        }


        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }

    private synchronized byte[] isExist(ArrayList<byte[]> mazeSolutions , byte[] tocheck)
    {
        /**
         * checks whether the current maze has been solved before


        for(int i=0; i < mazeSolutions.size(); i++)
        {
            if(Arrays.equals(tocheck,mazeSolutions.get(i)))
            {
                return mazeSolutions.get(i);
            }
        }
        return null;
    }


    public synchronized void readFromFile() throws IOException, ClassNotFoundException {
        /**
         * reads the HashMap from the temp file.

        try
        {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            SolutionsMap = (HashMap<byte[], Solution>) ois.readObject();


            ois.close();
            fis.close();
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void writeToFile() throws IOException
    {
        /**
         * writes the HashMap to the temp file.

        try
        {
            file = File.createTempFile(tempDirectoryPath, null);
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            isFile = true;

            if(file.length() != 0)
            {
                file.delete();
            }
            oos.writeObject(SolutionsMap);

            oos.flush();
            oos.close();
            fos.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}*/
package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import sun.awt.Mutex;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerStrategySolveSearchProblem implements IServerStrategy {

    public static String searchingAlgorithmString;
    private static ConcurrentHashMap SolutionMap;
    private AtomicInteger num;
    private Mutex mutex;

    public ServerStrategySolveSearchProblem() {
        SolutionMap = new ConcurrentHashMap<String,String>();
        this.num = new AtomicInteger(1);
        this.mutex = new Mutex();
    }
    @Override
    public void handleClient(InputStream IStream, OutputStream OStream) {
        try {
            ObjectOutputStream OOS = new ObjectOutputStream(OStream);
            ObjectInputStream OINS = new ObjectInputStream(IStream);
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");

            try {
                Maze toSolveMaze = (Maze) OINS.readObject();
                Solution solution = null;
                //byte[] MazeAsByteArray = toSolveMaze.toByteArray();


////////////////////////
                ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                OutputStream out = new MyCompressorOutputStream(BAOS);
                out.write(toSolveMaze.toByteArray());
                byte[] MazeAsByteArray = BAOS.toByteArray();
////////////////////////




                mutex.lock();
                if(SolutionMap.containsKey(Arrays.toString(MazeAsByteArray))){

                    FileInputStream SolutionInputFile = new FileInputStream(tempDirectoryPath+"\\"+SolutionMap.get(Arrays.toString(MazeAsByteArray)));
                    ObjectInputStream SolutionObject = new ObjectInputStream(SolutionInputFile);
                    solution = (Solution) SolutionObject.readObject();
                }
                else{
                    SearchableMaze searchableMaze = new SearchableMaze(toSolveMaze);
                    ISearchingAlgorithm searchAlgo =ASearchingAlgorithm.algorithmType(searchingAlgorithmString);
                    solution = searchAlgo.solve(searchableMaze);


                    SolutionMap.put(Arrays.toString(MazeAsByteArray),"Sol"+ num.toString());
                    File SolFile = new File(tempDirectoryPath+"\\"+"Sol"+ num.toString());
                    File MazeFile = new File(tempDirectoryPath+"\\"+"Maze"+ num.toString());
                    num.incrementAndGet();


                    FileOutputStream fileOutForMaze = new FileOutputStream(MazeFile);
                    ObjectOutputStream objectMazeOut = new ObjectOutputStream(fileOutForMaze);
                    objectMazeOut.writeObject(toSolveMaze);
                    objectMazeOut.close();




                    FileOutputStream fileOutForSolution = new FileOutputStream(SolFile);
                    ObjectOutputStream objectSolutionOut = new ObjectOutputStream(fileOutForSolution);
                    objectSolutionOut.writeObject(solution);
                    objectSolutionOut.close();

                }
                mutex.unlock();



                OOS.writeObject(solution);
                OOS.flush();


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            OOS.close();
            OINS.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

