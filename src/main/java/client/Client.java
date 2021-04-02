package client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Класс Client, передает файл серверу
 */
public class Client {
    /** Сокет для чтения сообщений */
    private Socket clientSocket;
    /** Поток чтения из clientSocket */
    private BufferedReader in;
    /** Поток записи в  clientSocket */
    private BufferedWriter out;
    /** Массив для записи из файла*/
    private static ArrayList<String> list;

    /**
     * Функция для чтения данных из файла
     * @return true - файл найден, false - файл не существует
     */
    public boolean enterFile () {
        Scanner scanner = new Scanner(System.in);
        Path path = Path.of(scanner.nextLine());
        File file = new File(String.valueOf(path));
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String str;

            list = new ArrayList<String>();
            while((str = reader.readLine()) != null ){
                if(!str.isEmpty()){
                    list.add(str);
                }}
            list.add(String.valueOf(path.getFileName()));
        } catch (IOException e) {
            return false;
        }
       return true;
    }

    /**
     * Функция устанавливающая соединение с Server
     */
    public void connection () {
        System.out.println("Server connection...");
        try {
            this.clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException ioException) {
            System.out.println("Connection not established!");
            return;
        }
        System.out.println("Connection established!");
    }

    /**
     * Функция, которая передает содержимое файла
     */
    public void transfer() {
        System.out.println("File transfer...");
        try {
            for(String str : list) {
                out.write(str + "\n");
                out.flush();
            }
            System.out.println("File transfer complete!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * Функция закрывающая Client
     * @throws IOException exception
     */
    public void сlosing() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
        System.out.println("The client has been closed!");
    }


    public static void main(String[] args) {
        Client client = new Client();
        System.out.println("Enter the path to the file: ");
        while(!client.enterFile()) {
            System.out.println("The file path is not correct, re-enter: ");
        }
        client.connection();
        client.transfer();
    }
}