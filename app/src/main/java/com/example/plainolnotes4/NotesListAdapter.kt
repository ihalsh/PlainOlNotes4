package com.example.plainolnotes4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.plainolnotes4.data.NoteEntity
import com.example.plainolnotes4.databinding.ListItemBinding

class NotesListAdapter(
    private val listener: ItemClickListener
) : ListAdapter<NoteEntity, NotesListAdapter.ViewHolder>(NoteEntityDiffCallback()) {

    val selectedNotes = arrayListOf<NoteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.bind(getItem(position))
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteEntity) {
            with(binding) {
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
    }

    interface ItemClickListener {
        fun onItemClick(id: Int)
        fun onItemSelectionChanged()
    }
}

class NoteEntityDiffCallback : DiffUtil.ItemCallback<NoteEntity>() {
    override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return newItem == oldItem
    }
}