package com.example.plainolnotes4.data

import com.example.plainolnotes4.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.list_item.view.*

class NoteEntityItem(
    private val note: NoteEntity,
    private val listener: ItemClickListener
) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            note_text.text = note.text
            setOnClickListener {
                listener.onItemClick(note.id)
            }
            fab.setOnClickListener {
                if (listener.isSelected(note)) {
                    listener.removeFromSelected(note)
                    fab.setImageResource(R.drawable.ic_note)
                } else {
                    listener.addToSelected(note)
                    fab.setImageResource(R.drawable.ic_check)
                }
                listener.onItemSelectionChanged()
            }
            fab.setImageResource(
                when {
                    listener.isSelected(note) -> R.drawable.ic_check
                    else -> R.drawable.ic_note
                }
            )
        }
    }

    override fun getLayout(): Int = R.layout.list_item

    interface ItemClickListener {
        fun onItemClick(id: Int)
        fun onItemSelectionChanged()
        fun isSelected(note: NoteEntity) : Boolean
        fun addToSelected(note: NoteEntity)
        fun removeFromSelected(note: NoteEntity)
    }
}

fun List<NoteEntity>.toNoteEntityItem(listener: NoteEntityItem.ItemClickListener): List<NoteEntityItem> {
    return this.map {
        NoteEntityItem(it, listener)
    }
}