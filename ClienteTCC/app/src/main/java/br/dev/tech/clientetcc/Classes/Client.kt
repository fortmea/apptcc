package br.dev.tech.clientetcc.Classes

import Classes.Jogo
import Classes.Mensagem
import Classes.ObjectUtil
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch


object Client {
    lateinit var serverAddress: SocketAddress;
    lateinit var client: ConnectedDatagramSocket;


    val data: MutableLiveData<Mensagem> by lazy {
        MutableLiveData<Mensagem>()
    }
    var objUtil: ObjectUtil = ObjectUtil();
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
            updater()
        }

    }

    fun joinRoom(roomId: Int) {
        val mensagem = Mensagem();
        mensagem.setEntrar(true)
        mensagem.setIdSala(roomId)
        enviarMensagem(mensagem)
    }

    fun createRoom() {
        val mensagem = Mensagem();
        mensagem.setCriarSala(true)
        enviarMensagem(mensagem)
    }

    private suspend fun updater() {
        while (true) {
            println("TESTE!")
            println("UPDATER")
            //try {
            println(client.toString())
            val responsePacket = client.receive()
            val packetSize = responsePacket.packet.readInt()
            val receivedBytes = ByteArray(packetSize)
            responsePacket.packet.readFully(receivedBytes)

            val mensagem = objUtil.fromBytes(receivedBytes)
            Log.i("Resposta do servidor", mensagem.getSalas().toString())
            Log.i("Resposta do servidor", mensagem.getEntrar().toString())
            withContext(Dispatchers.Main) {
                data.value = mensagem
            }
            // } catch (e: Exception) {
            //     e.printStackTrace()
            //}
        }
    }

    fun enviarMensagem(mensagem: Mensagem) {
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
                    } catch (e: java.net.PortUnreachableException) {
                        Log.i("Erro", "Não foi possível conectar-se ao servidor.")
                    }
                }
            }
        }.start()
    }

    fun updateMe() {
        Thread {
            runBlocking {
                withContext(Dispatchers.IO) {
                    withContext(Dispatchers.Default) {
                        try {
                            runBlocking {
                                println("Ok!")
                                var mensagem = Mensagem();
                                val mBytes = objUtil.toBytes(mensagem)
                                val packet = buildPacket {
                                    writeInt(mBytes.size) // Grava o tamanho dos bytes
                                    writeFully(mBytes)    // Grava os bytes da mensagem
                                }
                                client.send(Datagram(packet, serverAddress))
                            }
                        } catch (e: java.net.PortUnreachableException) {
                            Log.i("Erro", "Não foi possível conectar-se ao servidor.")
                        }
                    }
                }
            }
        }.start()

    }

}