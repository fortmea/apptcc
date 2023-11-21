package br.dev.tech.clientetcc

import Classes.Jogo
import Classes.Mensagem
import Classes.Sala
import Classes.Usuario
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.dev.tech.clientetcc.Classes.Client
import br.dev.tech.clientetcc.databinding.ActivityJogoBinding
import br.dev.tech.clientetcc.databinding.ActivityMainBinding
import java.util.UUID

class JogoActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityJogoBinding
    private var idSala: Int? = null;
    private var sala: Sala? = Client.sala.value;
    var mSimbolo: Int = 0;
    lateinit var botoes: List<Button>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        idSala = intent.getIntExtra("id", 0)
        println(sala?.getSimbolo().toString() + "ZAAP")
        println(sala.toString() + "SALA")
        println(idSala)
        configurar()
        println(sala?.getSimbolo().toString())
        sala?.getSimbolo()?.forEach {
            println("ZAPZAPZAP")
            println(it.value == Client.usuario?.getId())
            if (it.value == Client.usuario?.getId()) mSimbolo = it.key
        }
    }

    override fun onStop() {
        super.onStop()
        val mensagem = Mensagem();
        mensagem.setSair(true);
        mensagem.setIdSala(idSala!!)
        Client.enviarMensagem(mensagem)
        println("Saindo")
    }

    private fun configurar() {

        binding.button00.tag = "A1"
        binding.button00.setOnClickListener(this)
        binding.button01.tag = "A2"
        binding.button01.setOnClickListener(this)
        binding.button02.tag = "A3"
        binding.button02.setOnClickListener(this)
        binding.button03.tag = "B1"
        binding.button03.setOnClickListener(this)
        binding.button04.tag = "B2"
        binding.button04.setOnClickListener(this)
        binding.button05.tag = "B3"
        binding.button05.setOnClickListener(this)
        binding.button06.tag = "C1"
        binding.button06.setOnClickListener(this)
        binding.button07.tag = "C2"
        binding.button07.setOnClickListener(this)
        binding.button08.tag = "C3"
        binding.button08.setOnClickListener(this)
        botoes = listOf<Button>(
            binding.button00,
            binding.button01,
            binding.button02,
            binding.button03,
            binding.button04,
            binding.button05,
            binding.button06,
            binding.button07,
            binding.button08
        )
        Client.data.observe(this) {
            println(it);
            if (it.getMovimento()) {
                println("MOVIMENTO")
            }

        }
        Client.sala.observe(this) {
            sala = it
            println("zapzap")
            println("NOVO JOGADOR?")
            println("SALA " + sala.toString())

            if (sala != null) {
                println("JOGADORES " + sala!!.getUsuarios())
                val copia = it.getUsuarios().toMutableList()
                if (copia.size > 1) {
                    removeUsuarioPorId(copia, Client.usuario!!.getId())
                }

                println(copia.toString())
                binding.textViewPlacarJogador1.text =
                    sala!!.getPlacar()[Client.usuario!!.getId()].toString()
                binding.textViewNomeJogador1.text = Client.usuario!!.getNome().subSequence(0, 12)
                binding.textViewNomeJogador2.text = copia[0].getNome().subSequence(0, 12)
                binding.textViewPlacarJogador2.text =
                    sala!!.getPlacar()[copia[0].getId()].toString()

                if (sala!!.getJogo().getPosicoes() != null) {
                    println(sala!!.getJogo().getPosicoes())
                    alterarTextosBotoes(sala!!.getJogo().getPosicoes()!!)
                }
            }

        }

    }

    fun removeUsuarioPorId(listaUsuarios: MutableList<Usuario>, idUsuario: UUID) {
        val iterator = listaUsuarios.iterator()
        while (iterator.hasNext()) {
            val usuario = iterator.next()
            if (usuario.getId() == idUsuario) {
                iterator.remove()
                break // Se deseja remover apenas um usuário com o ID correspondente, você pode interromper o loop aqui
            }
        }
    }

    override fun onClick(v: View?) {
        //if (Client.locked.value == true) return
        v as Button;
        println(v.tag)
        println(mSimbolo)
        if (sala?.getJogo()?.getPosicoes() == null) {
            val mapa = mutableMapOf<String, Int>();
            mapa[v.tag as String] = mSimbolo;
            sala?.getJogo()?.setPosicoes(
                mapa
            )
        }
        sala?.getJogo()?.getPosicoes()?.set(v.tag as String, mSimbolo)

        Client.enviarMovimento(idSala!!)
        var tSimbolo = ""
        if ((mSimbolo == 1)) {
            tSimbolo = "X"
        } else {
            tSimbolo = "()"
        }
        v.text = tSimbolo
        println(sala?.getJogo())


    }


    fun alterarTextosBotoes( mapaTextos: Map<String, Int>) {
        for (i in botoes.indices) {
            val chave = positionToKey(i)
            val valor = mapaTextos[chave] ?: -1 // Valor padrão se a chave não existir no mapa

            botoes[i].text = if (valor == 1) "X" else if(valor ==2) "()" else ""
        }
    }

    fun positionToKey(position: Int): String {
        val linha = position / 3 + 'A'.toInt()
        val coluna = position % 3 + 1
        return "${linha.toChar()}$coluna"
    }

}
