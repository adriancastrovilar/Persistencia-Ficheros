package es.upm.miw.editordetexto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MiW";
    private EditText etTextoIntroducido;
    private Button btBotonEnviar;
    private TextView tvContenidoFichero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTextoIntroducido = findViewById(R.id.etTextoIntroducido);
        btBotonEnviar = findViewById(R.id.btBotonEnviar);
        tvContenidoFichero = findViewById(R.id.tvContenidoFichero);
    }

    @Override
    protected  void onResume() {
        super.onResume();
        this.mostrarContenido();
    }

    private String obtenerNombreFichero() {
        return getResources().getString(R.string.default_NombreFich);
    }

    public void accionAnhadir(View view) {
        try {
            FileOutputStream fos;

            fos = openFileOutput(obtenerNombreFichero(), Context.MODE_APPEND);
            fos.write(etTextoIntroducido.getText().toString().getBytes());
            fos.write('\n');
            fos.close();
            this.mostrarContenido();
        } catch (Exception e) {
            Log.i(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
        }
        etTextoIntroducido.setText(null);

    }

    private void mostrarContenido() {
        boolean hayContenido = false;
        BufferedReader fin;
        tvContenidoFichero.setText("");

        try {
            fin = new BufferedReader(new InputStreamReader(openFileInput(obtenerNombreFichero())));
            String linea = fin.readLine();
            while(linea != null) {
                hayContenido = true;
                tvContenidoFichero.append(linea + '\n');
                linea = fin.readLine();
            }
            fin.close();
            Log.i(LOG_TAG, "MOSTRAR fichero");
        } catch (Exception e) {
            Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        if(!hayContenido) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "No hay contenido en el fichero",
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }
}