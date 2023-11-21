package br.dev.tech.teste.classes

import android.util.Log
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ConnectedDatagramSocket
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.SocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readFully
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.writeInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.PortUnreachableException
import java.util.concurrent.CountDownLatch
import io.ktor.utils.io.core.*


object ClienteUDP {
    lateinit var serverAddress: SocketAddress;
    lateinit var client: ConnectedDatagramSocket;
    var objUtil: ObjectUtil = ObjectUtil();
    var contador = 0;
    var tempoInicial: Long = 0;
    var tempoParcial: Long = 0;
    var tempos: MutableList<Long> = mutableListOf()
    var ping: MutableList<Long> = mutableListOf()
    fun connect() {
        //val policy = ThreadPolicy.Builder()
        //    .permitAll().build()
        //StrictMode.setThreadPolicy(policy

        val latch = CountDownLatch(1)
        Thread {
            try {
                val selectorManager = SelectorManager(Dispatchers.IO)
                client = aSocket(selectorManager)
                    .udp()
                    .connect(serverAddress)

            } catch (e: java.net.PortUnreachableException) {
                Log.i("Erro", "Não foi possível conectar-se ao servidor.")

            }
            latch.countDown()
        }.start()
        latch.await()
        GlobalScope.launch(Dispatchers.IO) {
            tempoInicial = System.currentTimeMillis();
            updater()

        }
    }

    private suspend fun updater() {
        while (true) {
            //println("TESTE!")
            //println("UPDATER")
            //try {
            //println(client.toString())
            val responsePacket = client.receive()
            val packetSize = responsePacket.packet.readInt()
            val receivedBytes = ByteArray(packetSize)
            responsePacket.packet.readFully(receivedBytes)

            val mensagem = objUtil.fromBytes(receivedBytes)
            withContext(Dispatchers.Main) {
                //Log.i("Mensagem", mensagem.mensagem)
                if(mensagem.mensagem == "Qual seu nome?"){
                    enviarMensagem(Mensagem(Constants.testString))
                }else if(contador < 4000){
                    var agora = System.currentTimeMillis();
                    tempos.add(System.currentTimeMillis())
                    ping.add(agora-tempoParcial)
                    enviarMensagem(Mensagem(""))
                    contador++;
                } else {
                    println("PING")
                    Extensions.Log.d("PING", ping.toString())
                    println("TEMPOS")
                    Extensions.Log.d("TEMPOS", tempos.toString())
                    println("MEDIA")
                    Extensions.Log.d("MEDIA", (ping.sum()/4000).toString())
                }
            }
            // } catch (e: Exception) {
            //     e.printStackTrace()
            //}
        }
    }

    fun enviarMensagem(mensagem: Mensagem) {
        tempoParcial = System.currentTimeMillis();
        Thread {
            runBlocking {
                withContext(Dispatchers.IO) {
                    try {
                        val mBytes = objUtil.toBytes(mensagem)
                        val packet = buildPacket {
                            writeInt(mBytes.size) // Grava o tamanho dos bytes
                            writeFully(mBytes)    // Grava os bytes da mensagem
                        }
                        client.send(Datagram(packet, serverAddress))
                    } catch (e: PortUnreachableException) {
                        Log.i("Erro", "Não foi possível conectar-se ao servidor.")
                    }
                }
            }
        }.start()
    }

}