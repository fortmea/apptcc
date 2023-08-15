package br.dev.tech.clientetcc.Classes.adapters

import Classes.Sala
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.dev.tech.clientetcc.Classes.Client
import br.dev.tech.clientetcc.JogoActivity
import br.dev.tech.clientetcc.R


class salasAdapter(private val salas: MutableMap<Int, Sala>) :
    RecyclerView.Adapter<salasAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val button: Button
        val jogadores: TextView

        init {
            textView = view.findViewById(R.id.textViewSala)
            button = view.findViewById(R.id.buttonSala)
            jogadores = view.findViewById(R.id.textViewjogadores)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerlayout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = "Sala " + position
        viewHolder.jogadores.text =
            salas[salas.keys.toList()[position]]!!.getJogadores().toString() + "/2"
        viewHolder.button.setOnClickListener {
            Client.joinRoom(salas.keys.toList()[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = salas.size

}
