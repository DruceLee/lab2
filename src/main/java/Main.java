import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private final String fileName = "information.xml";
    static private ServerSocket serverSocket;
    static private Socket socket;
    static private ObjectOutputStream objectOutputStream;
    static private ObjectInputStream objectInputStream;

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
        TreeMap<String, String> map = new TreeMap<>();
        TreeMap<String, ArrayList<Task>> map1 = new TreeMap<>();

        /*map.put("aa", "ba");
        map.put("ab", "bb");
        map.put("ac", "bc");
        map.put("ad", "bd");
        map.put("ae", "be");

        ArrayList<Task> arrayList = new ArrayList<>();
        arrayList.add(new Task("ata", dateFormat.parse("1999-12-12/12:12:12"), false));
        map1.put("aa", arrayList);
        map1.put("ab", arrayList);
        map1.put("ac", arrayList);
        map1.put("ad", arrayList);
        map1.put("ae", arrayList);*/

        Main main = new Main();

        main.readData(map1, map);

        try {
            serverSocket = new ServerSocket(1111);
            while (true) {
                socket = serverSocket.accept();
                new ServerThread(socket, map, map1).start();
                //
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        main.writeData(map1, map);
    }

    public Main() {
    }

    private void readData(TreeMap<String, ArrayList<Task>> information, TreeMap<String, String> usersList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fileName);
            NodeList userList = document.getElementsByTagName("user");

            for (int i = 0; i < userList.getLength(); i++) {
                Node node = userList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element user = (Element) node;
                    String login = user.getAttribute("login");
                    String password = user.getAttribute("password");
                    usersList.put(login, password);

                    NodeList tasks = user.getChildNodes();
                    ArrayList<Task> taskArrayList = new ArrayList<>();
                    for (int j = 0; j < tasks.getLength(); j++) {
                        Node node1 = tasks.item(j);

                        if (node1.getNodeType() == Node.ELEMENT_NODE) {
                            Element task = (Element) node1;

                            NodeList attributes = task.getChildNodes();
                            String attributesString = "";

                            for (int k = 0; k < attributes.getLength(); k++) {
                                Node node2 = attributes.item(k);

                                if (node2.getNodeType() == Node.ELEMENT_NODE) {
                                    Element attribute = (Element) node2;

                                    attributesString += attribute.getAttribute("value") + " ";
                                }
                            }

                            taskArrayList.add(parserStroki(attributesString));
                        }
                    }
                    information.put(login, taskArrayList);
                }

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Task parserStroki(String attr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
        Task task;

        String title = attr.substring(0, attr.indexOf(" "));
        attr = attr.substring(attr.indexOf(" ") + 1);

        String time = attr.substring(0, attr.indexOf(" "));
        attr = attr.substring(attr.indexOf(" ") + 1);

        String start = attr.substring(0, attr.indexOf(" "));
        attr = attr.substring(attr.indexOf(" ") + 1);

        String end = attr.substring(0, attr.indexOf(" "));
        attr = attr.substring(attr.indexOf(" ") + 1);

        String interval = attr.substring(0, attr.indexOf(" "));
        attr = attr.substring(attr.indexOf(" ") + 1);

        String active = attr.substring(0, attr.indexOf(" "));
        attr = attr.substring(attr.indexOf(" ") + 1);

        if ("".equals(time)) {
            task = new Task(title, dateFormat.parse(start), dateFormat.parse(end), Integer.getInteger(interval), Boolean.getBoolean(active));
        } else {
            task = new Task(title, dateFormat.parse(time), Boolean.getBoolean(active));
        }

        return task;
    }

    private void writeData(TreeMap<String, ArrayList<Task>> information, TreeMap<String, String> usersList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element users = document.createElement("users");
            document.appendChild(users);

            Map.Entry<String, String> a = usersList.firstEntry();
            Element user1 = document.createElement("user");
            users.appendChild(user1);
            user1.setAttribute("login", a.getKey());
            user1.setAttribute("password", a.getValue());
            ArrayList<Task> list1 = information.get(a.getKey());
            addElements(user1, list1, document);

            while (!a.equals(usersList.lastEntry())) {
                a = usersList.higherEntry(a.getKey());
                Element user = document.createElement("user");
                users.appendChild(user);
                user.setAttribute("login", a.getKey());
                user.setAttribute("password", a.getValue());

                ArrayList<Task> list = information.get(a.getKey());
                addElements(user, list, document);
            }

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(document), new StreamResult(new FileOutputStream(fileName)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void addElements(Element user, ArrayList<Task> list, Document document) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
        for (Task task : list) {
            Element task1 = document.createElement("task");

            Element title = document.createElement("title");
            user.appendChild(task1);
            task1.appendChild(title);
            title.setAttribute("value", task.getTitle());

            Element time = document.createElement("time");
            user.appendChild(task1);
            task1.appendChild(time);
            if (task.getTime() != null)
                time.setAttribute("value", dateFormat.format(task.getTime()));
            else
                time.setAttribute("value", "");

            Element start = document.createElement("start");
            user.appendChild(task1);
            task1.appendChild(start);
            if (task.getStart() != null)
                start.setAttribute("value", dateFormat.format(task.getStart()));
            else
                start.setAttribute("value", "");

            Element end = document.createElement("end");
            user.appendChild(task1);
            task1.appendChild(end);
            if (task.getEnd() != null)
                end.setAttribute("value", dateFormat.format(task.getEnd()));
            else
                end.setAttribute("value", "");

            Element interval = document.createElement("interval");
            user.appendChild(task1);
            task1.appendChild(interval);
            interval.setAttribute("value", String.valueOf(task.getInterval()));

            Element active = document.createElement("active");
            user.appendChild(task1);
            task1.appendChild(active);
            active.setAttribute("value", String.valueOf(task.isActive()));
        }
    }
}
