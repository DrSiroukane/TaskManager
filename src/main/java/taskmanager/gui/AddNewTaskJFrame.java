package taskmanager.gui;

import taskmanager.models.Task;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * @author Slimane Siroukane
 * <p>
 * Add new task Frame
 */
public class AddNewTaskJFrame extends JFrame {
    private JPanel fieldsPanel;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField durationField;

    private JPanel buttonsPanel;
    private JButton addButton;
    private JButton cancelButton;

    public AddNewTaskJFrame() {
        initComponents();
    }

    public static void main(String[] args) {
        AddNewTaskJFrame addNewTaskJFrame = new AddNewTaskJFrame();

        addNewTaskJFrame.setVisible(true);
    }

    private void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        {
            JLabel startDateLabel = new JLabel("start date");
            fieldsPanel.add(startDateLabel);

            startDateField = new JTextField("", 10);
            fieldsPanel.add(startDateField);

            JLabel endDateLabel = new JLabel("end date");
            fieldsPanel.add(endDateLabel);

            endDateField = new JTextField("", 10);
            fieldsPanel.add(endDateField);

            JLabel durationLabel = new JLabel("duration");
            fieldsPanel.add(durationLabel);

            durationField = new JTextField("", 10);
            fieldsPanel.add(durationField);
        }
        contentPane.add(fieldsPanel, BorderLayout.CENTER);

        buttonsPanel = new JPanel();
        {
            addButton = new JButton("Add");
            addButton.addActionListener(eventAction -> {
                System.out.println("Add");

                boolean success = false;
                try {
                    success = addTask();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (success) {
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong, please try again !!!");
                }
            });
            buttonsPanel.add(addButton);

            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(eventAction -> {
                System.out.println("Cancel");
                this.dispose();
            });
            buttonsPanel.add(cancelButton);
        }
        contentPane.add(buttonsPanel, BorderLayout.PAGE_END);

        setSize(400, 200);
        setLocationRelativeTo(null);
    }

    /**
     * Get fields input and add a new task
     *
     * @return true|false
     * @throws ParseException
     */
    private boolean addTask() throws ParseException {
        if (!validateTaskFields()) {
            return false;
        }

        String[] newTaskFields = new String[]{
                startDateField.getText(),
                endDateField.getText(),
                durationField.getText()
        };
        Task newTask = Task.mappingTask(newTaskFields);
        TaskManagerJFrame.taskList.add(newTask);
        Task.sortTaskList(TaskManagerJFrame.taskList, TaskManagerJFrame.orderBy);
        int newTaskIndex = TaskManagerJFrame.taskList.indexOf(newTask);
        TaskManagerJFrame.taskTableModel.insertRow(newTaskIndex, newTaskFields);
        return true;
    }

    /**
     * validate task fields input
     *
     * @return true|false
     */
    private boolean validateTaskFields() {
        Pattern datePattern = Pattern.compile("\\d{2}\\/\\d{2}\\/\\d{4}");
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        Pattern numberPattern = Pattern.compile("\\d+");
        String duration = durationField.getText();

        return datePattern.matcher(startDate).matches()
                && datePattern.matcher(endDate).matches()
                && numberPattern.matcher(duration).matches();
    }
}
