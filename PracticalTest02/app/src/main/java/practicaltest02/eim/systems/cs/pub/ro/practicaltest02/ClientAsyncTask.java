package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Adrian on 5/18/2017.
 */

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    String serverAddr = "127.0.0.1";
    int port;
    TextView responseView;
    String queryStr;

    public ClientAsyncTask(int port,  String queryStr, TextView responseView) {
        this.serverAddr = serverAddr;
        this.port = port;
        this.queryStr = queryStr;
        this.responseView = responseView;
    }

    @Override
    protected Void doInBackground(String... params) {

        Socket socket;
        String response = "";
        try {
            socket = new Socket(serverAddr, port);
            PrintWriter clientPr = Utilities.getWriter(socket);
            clientPr.write(queryStr+"\n");
            clientPr.flush();

            BufferedReader clientBr = Utilities.getReader(socket);
            //while (!socket.isClosed()) {
            response += clientBr.readLine();
            //}
            this.publishProgress(response);

            socket.close();

        } catch (Exception e) {e.printStackTrace();}

        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(String... progress) {
        responseView.setText(progress[0]);
    }

    @Override
    protected void onPostExecute(Void result) {}
}