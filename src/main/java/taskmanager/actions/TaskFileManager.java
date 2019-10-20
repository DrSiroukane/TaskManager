package taskmanager.actions;

import taskmanager.models.Task;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Slimane Siroukane
 * <p>
 * Class to handle read/write actions
 */
public class TaskFileManager {
    public static final String FILE_NAME = "task-manager.csv";
    public static final String FILE_PATH = "";

    private String fileName;
    private String filePath;

    public TaskFileManager() {
        this.fileName = FILE_NAME;
        this.filePath = FILE_PATH;
    }

    public TaskFileManager(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFullFileSoure() {
        return this.filePath + this.fileName;
    }

    /**
     * Method to write the given Tasks into file
     *
     * @param taskList task list
     * @return true or false
     */
    public boolean setData(ArrayList<Task> taskList) {
        PrintWriter printWriter = null;
        try {
            // create new file or get an existing file
            FileWriter fileWriter = new FileWriter(this.getFullFileSoure());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            // add tasks to file
            for (Task task : taskList) {
                printWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }

        return true;
    }

    /**
     * Method to return stocked task on a file as an ArrayList<Task>
     *
     * @return list of tasks
     */
    public ArrayList<Task> getData() {
        ArrayList<Task> taskList = new ArrayList<Task>();

        int attempts = 0;
        boolean fileExists = false;

        FileReader fileReader = null;
        do {
            try {
                // read file
                fileReader = new FileReader(this.getFullFileSoure());
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(fileReader);

                    //Reading file content
                    String currentLine;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        String[] taskData = currentLine.split(",");

                        Task task = Task.mappingTask(taskData);
                        taskList.add(task);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }

                fileExists = true;
            } catch (FileNotFoundException e1) {
                PrintWriter printWriter = null;
                try {
                    // create new file or get an existing file
                    FileWriter fileWriter = new FileWriter(this.getFullFileSoure());
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    printWriter = new PrintWriter(bufferedWriter);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (printWriter != null) {
                        printWriter.close();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            attempts++;
        } while (!fileExists && attempts < 10);

        return taskList;
    }
}
