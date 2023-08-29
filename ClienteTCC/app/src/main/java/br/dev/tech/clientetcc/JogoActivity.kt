package br.dev.tech.clientetcc

import Classes.Jogo
import Classes.Mensagem
import Classes.Sala
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.dev.tech.clientetcc.Classes.Client
import br.dev.tech.clientetcc.databinding.ActivityJogoBinding
import br.dev.tech.clientetcc.databinding.ActivityMainBinding

class JogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJogoBinding
    private var idSala: Int? = null;
    private var sala: Sala? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        idSala = intent.getIntExtra("id", 0)
        println(idSala)
        configurar()

    }

    override fun onStop() {
        super.onStop()
        val mensagem = Mensagem();
        mensagem.setSair(true);
        mensagem.setIdSala(idSala!!)
        Client.enviarMensagem(mensagem)
    }

    private fun configurar() {
        Client.data.observe(this) {
            println(it);
        }
        Client.sala.observe(this) {
            sala = it
            println("zapzap")
            if (sala != null) {
                val copia = it.getUsuarios().toMutableList()
                copia.remove(Client.usuario)
                binding.textViewPlacarJogador1.text =
                    sala!!.getPlacar()[Client.usuario!!.getId()].toString()
                binding.textViewNomeJogador1.text = Client.usuario!!.getNome().subSequence(0, 12)
                binding.textViewNomeJogador2.text = copia[0].getNome().subSequence(0, 12)
                binding.textViewPlacarJogador2.text =  sala!!.getPlacar()[copia[0].getId()].toString()
            }
        }
    }


}
