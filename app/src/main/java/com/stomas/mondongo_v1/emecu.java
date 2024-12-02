package com.stomas.mondongo_v1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
//librerias para MQTT y Formulario
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;


public class emecu extends AppCompatActivity {

    // Variables de la conexion a MQTT
    private static String mqttHost = "tcp://irontoe438.cloud.shiftr.io:1883"; //Ip del Servidor MQTT
    private static String IdUsuario = "AppAndroid"; //Nombre del dispositivo que se conectara

    private static String Topico = "Mensaje"; //Topico al que se suscribira
    private static String User = "irontoe438"; //Usuario
    private static String Pass = "jhgGT4BiFNPGzpbd"; //Contraseña o Token

    // Variable que se utilizara para imprimir los datos del sensor
    private TextView textView;
    private EditText editTextMessage;
    private Button botonEnvio;

    //Libreria MQTT
    private MqttClient mqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        // un enlace de la variable del ID que esta en el activity main donde imprimiremos los datos
        textView = findViewById(R.id.textView);
        editTextMessage = findViewById(R.id.txtMensaje);
        botonEnvio = findViewById(R.id.botonEnvioMensaje);
        try {
            //creacion de un cliente MQTT
            mqttClient = new MqttClient(mqttHost, IdUsuario, null);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(User);
            options.setPassword(Pass.toCharArray());
            // conexion al server MQTT
            mqttClient.connect(options);
            // si se conecta imprimira un mensaje de MQTT
            Toast.makeText(this, "Aplicacion conectada al server MQTT", Toast.LENGTH_SHORT).show();
            //manejo de entrega de datos y perdida de conexion
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("MQTT", "lo perdimos :(");
                }
                //Metodo para enviar el mensaje a MQTT
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String payload = new String(message.getPayload());
                    runOnUiThread(() -> textView.setText(payload));
                }
                //Metodo para verificar si el envio fue exitoso
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("MQTT", "Entrega completa");
                }
            });

        }catch (MqttException e){
            e.printStackTrace();
        }



        //Al dar click en el boton enviar el mensajito del topico
        botonEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el mensaje ingresando por el usuario
                String mensaje = editTextMessage.getText().toString();
                try {
                    //Verifica si la conexion MQTT esta activa
                    if (mqttClient != null && mqttClient.isConnected()){
                        //publicar el mensaje en el topico especificado
                        mqttClient.publish(Topico, mensaje.getBytes(), 0,false);
                        //mostrar el mensaje enviado en el TextView
                        textView.append("\n - "+ mensaje);
                        Toast.makeText(emecu.this, "ya se envio manito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(emecu.this, "Error: no je pudo manito, active el MqTt.", Toast.LENGTH_SHORT).show();
                    }

                }catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
