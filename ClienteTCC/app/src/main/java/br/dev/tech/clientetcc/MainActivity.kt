package br.dev.tech.clientetcc

import Classes.Jogo
import Classes.Sala
import android.content.Intent
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
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var salas: MutableMap<Int, Sala> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        configurar()


    }

    fun configurar() {
        val recycler = binding.recyclerView
        binding.button.setOnClickListener {
            lifecycleScope.launch { Client.createRoom() }
        }

        binding.button3.setOnClickListener {
            lifecycleScope.launch {
                Client.updateMe()
            }

        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = salasAdapter(salas)
        lifecycleScope.launch {
            Log.i("A", "THREAD")
            Client.serverAddress = InetSocketAddress("192.168.1.25", 9002)
            Client.connect()
            Client.updateMe()

        }
        Client.data.observe(this) {
            if(it.getEntrar()){
                val intent = Intent(this, JogoActivity::class.java)
                intent.putExtra("sala", it.getSala())
                intent.putExtra("id", it.getIdSala())
                this.startActivity(intent)
            }
            binding.textView.text = it.getSalas().size.toString()
            salas.clear()
            salas.putAll(it.getSalas())
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
