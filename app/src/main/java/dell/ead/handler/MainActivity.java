package dell.ead.handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    Handler handler;
    private static final int INICIO_TAREFA = 0, FIM_TAREFA = 1;
    private static int contadorTarefas = 1;
    private EditText logMensagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logMensagens = findViewById(R.id.txt_log_messages);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case INICIO_TAREFA:
                        logMensagens.append(">> Nova tarefa " + msg.arg1 + "\n");
                        logMensagens.append("* Tempo de execução: " + msg.arg2
                                + "ms \n\n");
                        break;
                    case FIM_TAREFA:
                        logMensagens.append("# Fim da tarefa " + msg.arg1 + "\n\n");
                        break;
                }
            }
        };
    }

    public void novaTarefa(View v) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                int tempoAleatorioEspera = (int) (Math.random() * 10000);
                Message msg = handler.obtainMessage();
                int indiceTarefa = contadorTarefas++;
                msg.what = INICIO_TAREFA;
                msg.arg1 = indiceTarefa;
                msg.arg2 = tempoAleatorioEspera;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(tempoAleatorioEspera);
                    msg = handler.obtainMessage();
                    msg.arg1 = indiceTarefa;
                    msg.what = FIM_TAREFA;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * FIQUE LIGADO!
         * Conhecendo o ciclo de vida das aplicações Android, você
         * é capaz de dizer qual a função do trecho de código a seguir?
         * Porque colocamos estas instruções no metodo onResume?
         */
        contadorTarefas = 1;
        logMensagens.setText("");
    }

}