package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Adrian on 5/18/2017.
 */

public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;
    private int port;

    private EditText serverTextEditText;

    public ServerThread(int port) {

        this.port = port;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();

        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }

    @Override
    public void run() {

        HttpClient httpClient;
        String pageSourceCode;
        String internetURL = "http://autocomplete.wunderground.com/aq?query=";
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    BufferedReader br = Utilities.getReader(socket);
                    String queryStr = br.readLine().trim();

                    httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(internetURL +  queryStr);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    pageSourceCode = httpClient.execute(httpGet, responseHandler);

                    String toSend = "Nume: ";
                    int idx = pageSourceCode.indexOf("name");
                    if (idx == -1) {
                        Log.d(Constants.TAG, "Eroare index");
                    } else {
                        idx += 8;
                        pageSourceCode = pageSourceCode.substring(idx);
                        idx = 0;
                        toSend = "";
                        while (idx < pageSourceCode.length() && pageSourceCode.charAt(idx) != '\"') {
                            toSend += pageSourceCode.charAt(idx);
                            idx++;
                        }
                    }

                    PrintWriter pw = Utilities.getWriter(socket);
                    pw.write(toSend + "\n");
                    Log.d(Constants.TAG, "Sending from server: " + toSend);
                    pw.flush();
                    socket.close();

                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();

        }
    }
}