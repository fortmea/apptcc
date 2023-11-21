package br.dev.tech.clientetcc.Classes

import Classes.*
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
    var usuario: Usuario? = null;
    val onroom: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val locked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val sala: MutableLiveData<Sala> by lazy {
        MutableLiveData<Sala>()
    }

    val data: MutableLiveData<Mensagem> by lazy {
        MutableLiveData<Mensagem>()
    }
    val usuarioLiveData: MutableLiveData<Usuario> by lazy {
        MutableLiveData<Usuario>()
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
        println("Entrando em sala")
        if (locked.value == null) {
            locked.value = true;
            val mensagem = Mensagem();
            mensagem.setEntrar(true)
            mensagem.setIdSala(roomId)
            enviarMensagem(mensagem)
        }
    }

    suspend fun receberMovimento(mensagem: Mensagem) {

        withContext(Dispatchers.Main) {
            sala.value = mensagem.getSala()
            locked.value = !locked.value!!
        }

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
            if (mensagem.getMovimento()) {
                receberMovimento(mensagem)
            }
            withContext(Dispatchers.Main) {
                data.value = mensagem
                if (mensagem.getUsuario() != null) {
                    usuario = mensagem.getUsuario()
                    usuarioLiveData.value = mensagem.getUsuario()
                    println(mensagem.getUsuario())
                } else if (mensagem.getSala() != null) {
                    sala.value = mensagem.getSala()
                    println("atualizando sala")
                } else {
                    sala.value = mensagem.getSalas()[mensagem.getIdSala()]
                }
            }
            // } catch (e: Exception) {
            //     e.printStackTrace()
            //}
        }
    }

    fun enviarMovimento(idSala: Int) {

        var mensagem = Mensagem();
        mensagem.setIdSala(idSala)
        mensagem.setMovimento(true)
        mensagem.setSala(sala.value!!)
        locked.value = true
        enviarMensagem(mensagem)
    }

    fun enviarMensagem(mensagem: Mensagem) {
        Thread {
            runBlocking {
                withContext(Dispatchers.IO) {
                    try {

                        if (usuario != null) {
                            println(usuario!!.getId().toString())
                            mensagem.setUsuario(usuario!!)
                        }
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
        enviarMensagem(Mensagem())
    }

}