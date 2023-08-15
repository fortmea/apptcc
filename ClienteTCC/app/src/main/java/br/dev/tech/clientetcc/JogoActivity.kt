package br.dev.tech.clientetcc

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
import br.dev.tech.clientetcc.databinding.ActivityJogoBinding
import br.dev.tech.clientetcc.databinding.ActivityMainBinding

class JogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJogoBinding
    private lateinit var gridView: GridView
    private lateinit var adapter: ArrayAdapter<String>
    private var currentPlayer = 1
    private val gameBoard = mutableMapOf<String, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val idSala: Int? = intent.getIntExtra("id",0)
        println(idSala)
        gridView = findViewById(R.id.gridView)
        adapter = ArrayAdapter(this, R.layout.grid_item, R.id.gridButton, emptyList())
        gridView.adapter = adapter

        initializeGameBoard()

        gridView.setOnItemClickListener { _, view, position, _ ->
            onCellClicked(view, position)
        }
    }


    private fun initializeGameBoard() {
        for (row in 'A'..'C') {
            for (col in 1..3) {
                val key = "$row$col"
                gameBoard[key] = 0
            }
        }
    }

    private fun onCellClicked(view: View, position: Int) {
        val button = view as Button
        val key = positionToKey(position)

        if (gameBoard[key] == 0) {
            gameBoard[key] = currentPlayer
            button.text = if (currentPlayer == 1) "X" else "O"
            checkForWin()
            currentPlayer = 3 - currentPlayer // Alternates between 1 and 2
        }
    }

    private fun positionToKey(position: Int): String {
        val row = position / 3
        val col = position % 3 + 1
        return "${('A' + row)}$col"
    }

    private fun checkForWin() {
        // Implement your win checking logic here using the gameBoard map
    }

}
