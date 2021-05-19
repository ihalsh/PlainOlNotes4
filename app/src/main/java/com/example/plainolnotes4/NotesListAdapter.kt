package com.example.plainolnotes4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plainolnotes4.NotesListAdapter.ViewHolder
import com.example.plainolnotes4.data.NoteEntity
import com.example.plainolnotes4.databinding.ListItemBinding

class NotesListAdapter(
    private val dataSet: List<NoteEntity>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<ViewHolder>() {

    val selectedNotes = arrayListOf<NoteEntity>()

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val note = dataSet[position]
        with(holder.binding) {
            noteText.text = note.text
            root.setOnClickListener {
                listener.onItemClick(note.id)
            }
            fab.setOnClickListener {
                if (selectedNotes.contains(note)) {
                    selectedNotes.remove(note)
                    fab.setImageResource(R.drawable.ic_note)
                } else {
                    selectedNotes.add(note)
                    fab.setImageResource(R.drawable.ic_check)
                }
                listener.onItemSelectionChanged()
            }
            fab.setImageResource(
                when {
                    selectedNotes.contains(note) -> R.drawable.ic_check
                    else -> R.drawable.ic_note
                }
            )
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = dataSet.size

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ListItemBinding = ListItemBinding.bind(view)
    }

    interface ItemClickListener {
        fun onItemClick(id: Int)
        fun onItemSelectionChanged()
    }
}
