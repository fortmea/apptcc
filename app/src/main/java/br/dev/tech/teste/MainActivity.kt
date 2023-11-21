package br.dev.tech.teste

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import br.dev.tech.teste.classes.ClienteTCP
import io.ktor.network.sockets.InetSocketAddress
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import br.dev.tech.teste.classes.ClienteUDP
import br.dev.tech.teste.classes.Mensagem

class MainActivity : AppCompatActivity() {
    lateinit var btnudp: Button;
    lateinit var btntcp: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configurar()
    }

    fun configurar(){
        btnudp = findViewById(R.id.udp)
        btntcp = findViewById(R.id.tcp)
        btntcp.setOnClickListener{
            lifecycleScope.launch {
                Log.i("A", "THREAD")
                ClienteTCP.serverAddress = InetSocketAddress("192.168.1.25", 9002)
                ClienteTCP.connect()


            }
        }
        btnudp.setOnClickListener{
            lifecycleScope.launch {
                Log.i("A", "THREAD")
                ClienteUDP.serverAddress = InetSocketAddress("192.168.1.25", 9002)
                ClienteUDP.connect()
                ClienteUDP.enviarMensagem(Mensagem(""))
            }
        }
    }


}