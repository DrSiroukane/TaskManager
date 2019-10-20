package taskmanager.gui;

import taskmanager.actions.TaskFileManager;
import taskmanager.models.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Slimane Siroukane
 * <p>
 * Task manager Frame
 */
public class TaskManagerJFrame extends JFrame {
    public static ArrayList<Task> taskList = null;
    public static DefaultTableModel taskTableModel = null;
    public static int orderBy = Task.START_DATE_INDEX;

    private JPanel orderByPanel;
    private JComboBox<String> orderByComboBox;
    private JButton refrechButton;

    private JPanel taskListPanel;
    private JScrollPane taskListScrollPane;
    private JTable taskListTable;

    private JPanel buttonsPanel;
    private JButton addButton;
    private JButton saveButton;
    private JButton quitButton;

    public TaskManagerJFrame() {
        initComponents();
    }

    public static void main(String[] args) {
        TaskManagerJFrame taskManagerJFrame = new TaskManagerJFrame();
        taskManagerJFrame.setVisible(true);
    }

    public static ArrayList<Task> getTaskList() {
        TaskFileManager taskFileManager = new TaskFileManager();
        taskList = taskFileManager.getData();
        Task.sortTaskList(taskList, orderBy);
        return taskList;
    }

    public static boolean setTaskList() {
        TaskFileManager taskFileManager = new TaskFileManager();
        return taskFileManager.setData(taskList);
    }

    public static String[][] getTaskData() {
        if (taskList == null) {
            getTaskList();
        }

        String[][] data = new String[taskList.size()][3];
        for (int i = 0; i < taskList.size(); i++) {
            data[i] = taskList.get(i).toStringArray();
        }
        return data;
    }

    private void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        orderByPanel = new JPanel();
        {
            JLabel orderByLabel = new JLabel("Order By");
            orderByPanel.add(orderByLabel);

            orderByComboBox = new JComboBox<>(getTaskColumns());
            orderByPanel.add(orderByComboBox);

            refrechButton = new JButton("Refresh");
            refrechButton.addActionListener(eventAction -> {
                System.out.println("Refresh");
                if (orderByComboBox.getSelectedItem() != null) {
                    orderBy = Task.getOrderByIndex(orderByComboBox.getSelectedItem().toString());
                    Task.sortTaskList(taskList, orderBy);

                    String[][] data = getTaskData();
                    taskTableModel = new DefaultTableModel(data, getTaskColumns());
                    taskListTable.setModel(taskTableModel);
                }
            });
            orderByPanel.add(refrechButton);
        }
        contentPane.add(orderByPanel, BorderLayout.PAGE_START);

        taskListPanel = new JPanel();
        {
            getTaskList();
            String[][] data = getTaskData();
            taskTableModel = new DefaultTableModel(data, getTaskColumns());
            taskListTable = new JTable();
            taskListTable.setModel(taskTableModel);

            taskListScrollPane = new JScrollPane();
            taskListScrollPane.getViewport().add(taskListTable);

            taskListPanel.add(taskListScrollPane);
        }
        contentPane.add(taskListPanel, BorderLayout.CENTER);

        buttonsPanel = new JPanel();
        {
            addButton = new JButton("Add");
            addButton.addActionListener(eventAction -> {
                System.out.println("Add");
                AddNewTaskJFrame addNewTaskJFrame = new AddNewTaskJFrame();
                addNewTaskJFrame.setVisible(true);
            });
            buttonsPanel.add(addButton);

            saveButton = new JButton("Save");
            saveButton.addActionListener(eventAction -> {
                System.out.println("Save");
                setTaskList();
            });
            buttonsPanel.add(saveButton);

            quitButton = new JButton("Quit");
            quitButton.addActionListener(eventAction -> {
                System.out.println("Quit");
                System.exit(0);
            });
            buttonsPanel.add(quitButton);
        }
        contentPane.add(buttonsPanel, BorderLayout.PAGE_END);

        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private String[] getTaskColumns() {
        return new String[]{
                Task.START_DATE_COLUMN,
                Task.END_DATE_COLUMN,
                Task.DURATION_COLUMN,
        };
    }
}
