package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    /** clientSocket для общения с клиентом*/
    private Socket clientSocket;
    /** serverSocket */
    private ServerSocket serverSocket;
    /** Поток чтения из сокета */
    private BufferedReader in;
    /** Поток записи в сокет */
    private BufferedWriter out;
    /** Массив для записи файла клиента */
    private static ArrayList<String> list;

    /**
     * Конструктор, который отвечает за запуск и работу сервера
     */
    public Server() {
        try {
            this.serverSocket = new ServerSocket(4004);
            System.out.println("Server is running!");
            clientSocket = serverSocket.accept();
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String str = null;
                list = new ArrayList<String>();
                System.out.println("Server message: Retrieving a file...");
                do {
                    str = in.readLine();
                    list.add(str);
                } while(!str.contains(".txt"));
                String path = createFile(list.get(list.size()-1));
                System.out.println("Server message: The file was received and saved at:" + path + "\n");
            } finally {
                clientSocket.close();
                System.out.println("Server message: The server is closed!");
                in.close();
                out.close();
            }

        } catch (IOException ioException) {
            System.out.println("Server message: The server is not running!");
        }

    }

    /**
     * Функция для создания и записи данных в файл
     * @param fileName имя файла
     * @return путь к файлу
     */
    private String createFile(String fileName) {
        String path = "E:/Documents/Projects/Work/Lab 1/src/main/resources/files/" + fileName;
        File file = new File (path);
        try {
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < list.size()-1; i++) {
                writer.write(list.get(i) + "\n");
            }
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return path;
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}