package br.dev.tech.teste.classes

import android.util.Log
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ConnectedDatagramSocket
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.SocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.connection
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readByteBuffer
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readFully
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.PortUnreachableException
import java.util.concurrent.CountDownLatch
import kotlin.system.exitProcess

object ClienteTCP {
    lateinit var serverAddress: SocketAddress;
    lateinit var client: Socket;
    var objUtil: ObjectUtil = ObjectUtil();
    var contador = 0;
    var tempoInicial: Long = 0;
    var tempoParcial: Long = 0;
    var tempos: MutableList<Long> = mutableListOf()
    var ping: MutableList<Long> = mutableListOf()
    lateinit var receiveChannel: ByteReadChannel
    lateinit var sendChannel: ByteWriteChannel
    fun connect() {
        //val policy = ThreadPolicy.Builder()
        //    .permitAll().build()
        //StrictMode.setThreadPolicy(policy)
        Thread {
            try {
                runBlocking {
                    val selectorManager = SelectorManager(Dispatchers.IO)
                    client = aSocket(selectorManager).tcp().connect(serverAddress)
                    receiveChannel = client.openReadChannel()
                    sendChannel = client.openWriteChannel(autoFlush = true)
                    tempoInicial = System.currentTimeMillis();
                    enviarMensagem(Mensagem("Zé"))
                    CoroutineScope(Dispatchers.IO).launch {
                        println("AAAAAAAAAAAAAAA")
                        updater()
                    }.start()
                }
            } catch (e: java.net.PortUnreachableException) {
                Log.i("Erro", "Não foi possível conectar-se ao servidor.")

            }

        }.start()


    }

    /*
        private suspend fun receive() {
            while (true) {
                try {
                    //println("INICIO")

                    // Leitura do tamanho do pacote
                    val packetSize = receiveChannel.readInt()
                    //println(packetSize)
                    // Leitura dos dados do pacote com base no tamanho
                    val byteReadPacket = receiveChannel.readPacket(packetSize)

                    //println("RECEBENDO DADOS")
                     val mensagem = objUtil.fromBytes(byteReadPacket.readBytes())
                     //println(mensagem.mensagem)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    */
    private suspend fun updater() {
        //println("UPDATER")
        while (true) {

            val responsePacket = receiveChannel.readPacket(receiveChannel.readInt())
            val mensagem = objUtil.fromBytes(responsePacket.readBytes())
            //println("AQUI")
            withContext(Dispatchers.Main) {

                //Log.i("Mensagem", mensagem.mensagem)
                if (mensagem.mensagem == "Qual seu nome?") {
                    enviarMensagem(Mensagem(Constants.testString))
                } else if (contador < 4000) {
                    var agora = System.currentTimeMillis();
                    tempos.add(System.currentTimeMillis())
                    ping.add(agora - tempoParcial)
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
                        sendChannel.writePacket(packet)
                    } catch (e: PortUnreachableException) {
                        Log.i("Erro", "Não foi possível conectar-se ao servidor.")
                    }
                }
            }
        }.start()
    }
}