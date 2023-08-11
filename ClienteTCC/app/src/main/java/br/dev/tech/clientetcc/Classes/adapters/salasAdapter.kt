package br.dev.tech.clientetcc.Classes.adapters

import Classes.Sala
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.dev.tech.clientetcc.R

class salasAdapter(private val salas: MutableList<Sala>) :
    RecyclerView.Adapter<salasAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val button: Button
        init {
            textView = view.findViewById(R.id.textViewSala)
            button = view.findViewById(R.id.buttonSala)

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
        viewHolder.button.setOnClickListener {

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = salas.size

}
