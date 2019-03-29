package model;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Class for saving tasks in the array
 * @author Andrey Sherstyuk
 */
public class ArrayTaskList extends TaskList {
    /** Array for storing tasks */
    private Task[] tasks;
    /** The number of elements in the array */
    private int index = 0;

    /**
     * Method to add tasks
     * @param task task to add
     */
    @Override
    public void add(Task task) {
        if (index == 0)
            tasks = new Task[10];
        if (tasks.length == index) {
            Task[] tasks1 = new Task[index * 2];
            for (int i = 0; i < index; i++)
                tasks1[i] = tasks[i];
            tasks = tasks1;
        }
        tasks[index++] = task;
    }

    /**
     * Method to delete a task from an array
     * @param task task to be deleted
     * @return returns a boolean value if the task existed
     */
    @Override
    public boolean remove(Task task) {
        boolean existence = false;
        for (int i = 0; i < index; i++) {
            if (tasks[i].equals(task)) {
                existence = true;
                for (int j = i; j < index; j++) {
                    tasks[j] = tasks[j + 1];
                }
                index--;
                break;
            }
        }
        return existence;
    }

    /**
     * Method that returns the number of elements in the arrayList
     * @return returns the number of elements in an arrayList
     */
    @Override
    public int size() {
        return index;
    }

    /**
     * Method to get the task by index
     * @param index1 Task number
     * @return returns the task that number index1
     */
    @Override
    public Task getTask(int index1) {
        return tasks[index1];
    }

    /*
     * Метод для нахождения активных задач в промежутке
     * @param from - значение от которого нужно начать поиск
     * @param to - значение до которого нужно продолжать поиск
     * @return возвращает список задач по критериям поиска
     */
    /*public TaskList incoming(Date from, Date to) {
        TaskList arrayTaskList = new ArrayTaskList();
        Date date = new Date();

        for (int i = 0; i < size(); i++) {
            if (!(getTask(i).nextTimeAfter(from).equals(date)) && getTask(i).nextTimeAfter(from).before(to)) {
                arrayTaskList.add(getTask(i));
            }
        }
        return arrayTaskList;
    }*/

    @Override
    public Iterator<Task> iterator() {
        Iterator<Task> it = new Iterator<>() {
            private int i = 0;
            private Task task = null;

            @Override
            public boolean hasNext() {
                return i < index;
            }

            @Override
            public Task next() {
                if (hasNext()) {
                    task = getTask(i++);
                    return task;
                }
                return null;
            }

            @Override
            public void remove() {
                if (i > 0) {
                    i--;
                    ArrayTaskList.this.remove(task);
                } else {
                    throw new IllegalStateException();
                }
            }
        };
        return it;
    }

    /**
     * Method to return the task list as a string
     * @return returns a string consisting of objects of type Task
     */
    @Override
    public String toString() {
        return Arrays.toString(tasks);
    }

    /**
     * A method that returns a clone of the task list that calls the method.
     * @return returns a clone of this task list
     */
    @Override
    public ArrayTaskList clone() {
        ArrayTaskList ar = new ArrayTaskList();

        for (int i = 0; i < index; i++) {
            ar.add(tasks[i].clone());
        }

        return ar;
    }
}