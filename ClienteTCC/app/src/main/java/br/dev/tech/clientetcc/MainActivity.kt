package br.dev.tech.clientetcc

import Classes.Sala
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.dev.tech.clientetcc.Classes.Client
import br.dev.tech.clientetcc.Classes.adapters.salasAdapter
import br.dev.tech.clientetcc.databinding.ActivityMainBinding
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var salas: MutableList<Sala> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        configurar()


    }

    fun configurar() {

        binding.button.setOnClickListener {
            lifecycleScope.launch {
                Thread {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            withContext(Dispatchers.Default) { Client.enviarMensagem() }
                        }
                    }

                }.start()
            }
        }
        binding.button3.setOnClickListener {

            lifecycleScope.launch {
                Thread {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            withContext(Dispatchers.Default) { Client.updateMe() }
                        }
                    }

                }.start()
            }

        }
        val recycler = binding.recyclerView

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = salasAdapter(salas)
        lifecycleScope.launch {
            Log.i("A", "THREAD")
            Client.serverAddress = InetSocketAddress("192.168.1.27", 9002)
            Client.connect()
            Client.updateMe()

        }
        Client.data.observe(this) {
            binding.textView.text = it.getSalas().size.toString()
            salas.clear()
            salas.addAll(it.getSalas())
            recycler.adapter!!.notifyDataSetChanged()
            if (salas.size < 1) {
                recycler.visibility = View.GONE
                binding.textView.text = "Nenhuma sala"
            } else {
                recycler.visibility = View.VISIBLE
                recycler.adapter = salasAdapter(salas)
                recycler.layoutManager = LinearLayoutManager(this)
            }
        }
    }


}