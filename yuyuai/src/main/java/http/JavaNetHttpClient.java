package http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import util.Cnst;

// http://d.hatena.ne.jp/Kazuhira/20131026/1382796711
public class JavaNetHttpClient {

    public static void executeGet(String pUrl) {
        System.out.println("===== HTTP GET Start =====");
        try {
            URL url = new URL(pUrl);

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(Cnst.HttpMethodGET);

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                        StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===== HTTP GET End =====");
    }

    public static void executePost(String pUrl, ArrayList<String> pList) {
        System.out.println("===== HTTP POST Start =====");
        try {
            URL url = new URL(pUrl);

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod(Cnst.HttpMethodPOST);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),
                    StandardCharsets.UTF_8));

                for(String str: pList) {
                    writer.write(str);
                }
                writer.flush();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                        StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===== HTTP POST End =====");
    }
}
