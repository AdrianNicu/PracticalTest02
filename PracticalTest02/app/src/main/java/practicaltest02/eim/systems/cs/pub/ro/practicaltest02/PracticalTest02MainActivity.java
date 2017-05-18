package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText portTxt;
    EditText queryTxt;
    Button startServer;
    Button connectToServer;
    Button stopBtn;
    TextView responseView;
    int serverPort;
    String serverQuery;
    ServerThread serverThread;


    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (startServer.equals((Button)view)) {
                Log.d(Constants.TAG, "apas buton");
                serverPort = Integer.parseInt(portTxt.getText().toString());


                serverThread = new ServerThread(serverPort);
                serverThread.startServer();
            }

            if (connectToServer.equals((Button)view)) {

                serverPort = Integer.parseInt(portTxt.getText().toString());
                serverQuery = queryTxt.getText().toString();
                new ClientAsyncTask(serverPort, serverQuery, responseView).execute();
            }
            if (stopBtn.equals((Button)view)) {
                serverThread.stopServer();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);


        this.portTxt = (EditText) findViewById(R.id.port);
        this.queryTxt = (EditText) findViewById(R.id.query);
        this.responseView = (TextView) findViewById(R.id.response);

        this.startServer = (Button) findViewById(R.id.button1);
        this.connectToServer = (Button) findViewById(R.id.button2);
        this.stopBtn = (Button) findViewById(R.id.stop);

        startServer.setOnClickListener(buttonClickListener);
        connectToServer.setOnClickListener(buttonClickListener);
        stopBtn.setOnClickListener(buttonClickListener);



    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
    }
}
