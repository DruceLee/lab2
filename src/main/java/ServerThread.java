import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ServerThread extends Thread {
    private Socket socket = null;
    private TreeMap<String, String> loginPassword;
    private TreeMap<String, ArrayList<Task>> loginTaskList;

    public ServerThread(Socket socket, TreeMap<String, String> loginPassword, TreeMap<String, ArrayList<Task>> loginTaskList) {
        this.socket = socket;
        this.loginPassword = loginPassword;
        this.loginTaskList = loginTaskList;
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

                if (title.equals("Login:")) {
                    String login = response.substring(0, response.indexOf(" "));
                    String password = response.substring(response.indexOf(" ") + 1);

                    if (loginPassword.get(login) == null) {
                        printWriter.println("Login entered incorrectly");
                        printWriter.flush();
                    }

                    if (password.equals(loginPassword.get(login))) {
                        Gson gson = new Gson();
                        response = gson.toJson(loginTaskList.get(login));
                        printWriter.println(response);
                        printWriter.flush();
                    } else {
                        printWriter.println("Password entered incorrectly");
                        printWriter.flush();
                    }
                } else if (title.equals("Registration:")) {
                    String login = response.substring(0, response.indexOf(" "));
                    String password = response.substring(response.indexOf(" ") + 1);

                    if (loginPassword.get(login) != null) {
                        printWriter.println("Such a login is already busy");
                        printWriter.flush();
                    } else {
                        loginPassword.put(login, password);
                        loginTaskList.put(login, new ArrayList<Task>());
                    }
                } else if (title.equals("End of work:")) {
                    Gson gson = new Gson();
                    String login = response.substring(0, response.indexOf(" "));
                    ArrayList<Task> taskList = gson.fromJson(response.substring(response.indexOf(" ") + 1), new TypeToken<List<Task>>() {
                    }.getType());
                    //map1.get(login) = taskList;

                    loginTaskList.remove(login);
                    loginTaskList.put(login, taskList);
                }

                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
