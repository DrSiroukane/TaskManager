package taskmanager.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * @author Slimane Siroukane
 * <p>
 * Model of Task
 */
public class Task implements Comparable<Task> {
    public static final int START_DATE_INDEX = 0;
    public static final int END_DATE_INDEX = 1;
    public static final int DURATION_INDEX = 2;

    public static final String START_DATE_COLUMN = "start date";
    public static final String END_DATE_COLUMN = "end date";
    public static final String DURATION_COLUMN = "duration";

    private static int orderBy;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");

    private Date startDate;
    private Date endDate;
    private Integer duration;

    public Task(Date startDate, Date endDate, int duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public Task(String startDate, String endDate, String duration) throws ParseException {
        this.startDate = this.simpleDateFormat.parse(startDate);
        this.endDate = this.simpleDateFormat.parse(endDate);
        this.duration = Integer.parseInt(duration);
    }

    public static Task mappingTask(String[] taskData) throws ParseException {
        Task task = null;
        if (taskData.length > 2) {
            task = new Task(
                    taskData[START_DATE_INDEX],
                    taskData[END_DATE_INDEX],
                    taskData[DURATION_INDEX]
            );
        }

        return task;
    }

    /**
     * Sort a given list by a default field "start_date"
     *
     * @param taskList
     * @return
     */
    public static ArrayList<Task> sortTaskList(ArrayList<Task> taskList) {
        return sortTaskList(taskList, Task.START_DATE_INDEX);
    }

    /**
     * Sort a given list by a given orderBy field {"start_date", "end_date", "duration"}
     *
     * @param taskList
     * @param orderBy
     * @return
     */
    public static ArrayList<Task> sortTaskList(ArrayList<Task> taskList, int orderBy) {
        Task.orderBy = orderBy;
        Collections.sort(taskList);
        return taskList;
    }

    public static int getOrderByIndex(String orderByColumn) {
        switch (orderByColumn) {
            case END_DATE_COLUMN:
                return END_DATE_INDEX;
            case DURATION_COLUMN:
                return DURATION_INDEX;
            default: // Task.START_DATE_COLUMN
                return START_DATE_INDEX;
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return simpleDateFormat.format(startDate) + "," + simpleDateFormat.format(endDate) + "," + duration;
    }

    @Override
    public int compareTo(Task task) {
        switch (Task.orderBy) {
            case Task.END_DATE_INDEX:
                return this.endDate.compareTo(task.endDate);
            case Task.DURATION_INDEX:
                return this.duration.compareTo(task.duration);
            default: // Task.START_DATE_INDEX
                return this.startDate.compareTo(task.startDate);
        }
    }

    public String[] toStringArray() {
        return new String[]{
                simpleDateFormat.format(startDate),
                simpleDateFormat.format(endDate),
                duration.toString()
        };
    }
}
