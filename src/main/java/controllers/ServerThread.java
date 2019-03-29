package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Task;
import model.Tasks;
import model.User;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerThread extends Thread {
    private Socket socket = null;
    private ArrayList<User> usersList;
    private TreeMap<String, ArrayList<Task>> tasksList;

    public ServerThread(Socket socket, ArrayList<User> usersList, TreeMap<String, ArrayList<Task>> tasksList) {
        this.socket = socket;
        this.usersList = usersList;
        this.tasksList = tasksList;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

            while (true) {

                String response = in.readLine();
                System.out.println(response);

                String title = response.substring(0, response.indexOf(":") + 1);
                response = response.substring(response.indexOf(":") + 2);

                if ("Login:".equals(title)) {
                    String login = response.substring(0, response.indexOf(" "));
                    String password = response.substring(response.indexOf(" ") + 1);

                    for (User user : usersList) {
                        if (user.getLogin().equals(login)) {
                            if (user.getPassword().equals(password)) {
                                if (user.isBanned()) {
                                    printWriter.println("You are banned");
                                    printWriter.flush();
                                } else {
                                    Gson gson = new Gson();
                                    response = gson.toJson(tasksList.get(login));
                                    printWriter.println(response);
                                    printWriter.flush();
                                }
                            } else {
                                printWriter.println("Password entered incorrectly");
                                printWriter.flush();
                            }
                        }
                    }

                    printWriter.println("Login entered incorrectly");
                    printWriter.flush();
                } else if ("Registration:".equals(title)) {
                    String login = response.substring(0, response.indexOf(" "));
                    String password = response.substring(response.indexOf(" ") + 1);

                    for (User user : usersList) {
                        if (user.getLogin().equals(login)) {
                            printWriter.println("Such a login is already busy");
                            printWriter.flush();
                        }
                    }
                    usersList.add(new User(login, password, false));
                    tasksList.put(login, new ArrayList<>());
                } else if ("Delete:".equals(title)) {
                    Gson gson = new Gson();
                    String login = response.substring(0, response.indexOf(" "));
                    Task task = gson.fromJson(response.substring(response.indexOf(" ") + 1), new TypeToken<Task>() {
                    }.getType());

                    tasksList.get(login).remove(task);
                } else if ("Add:".equals(title)) {
                    Gson gson = new Gson();
                    String login = response.substring(0, response.indexOf(" "));
                    Task task = gson.fromJson(response.substring(response.indexOf(" ") + 1), new TypeToken<Task>() {
                    }.getType());

                    tasksList.get(login).add(task);
                } else if ("Change:".equals(title)) {
                    String login = response.substring(0, response.indexOf(" "));
                    response = response.substring(response.indexOf(" ") + 1);
                    Gson gson = new Gson();
                    int index = Integer.parseInt(response.substring(0, response.indexOf(" ")));

                    Task task = gson.fromJson(response.substring(response.indexOf(" ") + 1), new TypeToken<Task>() {
                    }.getType());

                    tasksList.get(login).set(index, task);
                } else if ("Calendar:".equals(title)) {
                    Gson gson = new Gson();
                    String login = response.substring(0, response.indexOf(" "));
                    response = response.substring(response.indexOf(" ") + 1);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
                    Date date1 = simpleDateFormat.parse(response.substring(0, response.indexOf(" ")));
                    Date date2 = simpleDateFormat.parse(response.substring(response.indexOf(" ") + 1));

                    SortedMap<Date, Set<Task>> sortedMap = Tasks.calendar(tasksList.get(login), date1, date2);
                    printWriter.println(gson.toJson(sortedMap));
                    printWriter.flush();
                } else if ("Banned list:".equals(title)) {
                    Gson gson = new Gson();
                    printWriter.println(gson.toJson(usersList));
                }

                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
