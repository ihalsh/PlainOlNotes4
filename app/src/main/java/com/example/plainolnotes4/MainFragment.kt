package com.example.plainolnotes4

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plainolnotes4.data.NoteEntity
import com.example.plainolnotes4.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlin.properties.Delegates

const val NEW_NOTE_ID = 0
const val SELECTED_LIST_KEY = "SelectionListKey"

class MainFragment : Fragment(), NotesListAdapter.ItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: NotesListAdapter
    private var noteListSize: Int by Delegates.observable(0) { _, old, new ->
        if (this::adapter.isInitialized) {
            (new - old).let { diff ->
                if (diff < 0) showSnackbar("${-diff} item(s) deleted.")
                if (diff > 0) showSnackbar("$diff item(s) added.")
            }
        }
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setHasOptionsMenu(true)

        requireActivity().title = getString(R.string.app_name)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = MainFragmentBinding.inflate(inflater, container, false)

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(activity)
        }

        binding.addNewNoteButton.setOnClickListener {
            onItemClick(NEW_NOTE_ID)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.notesList?.collect { list ->
                noteListSize = list.size// Should always stays BEFORE adapter!
                adapter = NotesListAdapter(list, this@MainFragment)
                binding.recyclerView.adapter = adapter
                if (null != savedInstanceState && !savedInstanceState.isEmpty) {
                    adapter.selectedNotes.addAll(
                        savedInstanceState.getParcelableArrayList(SELECTED_LIST_KEY) ?: emptyList()
                    )
                    savedInstanceState.clear()
                }
                onItemSelectionChanged()
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (this::adapter.isInitialized && adapter.selectedNotes.isNotEmpty()) {
            inflater.inflate(R.menu.main_menu_items_selected, menu)
        } else {
            inflater.inflate(R.menu.main_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_sample_data -> addSampleData()
            R.id.delete_note -> deleteMultipleNotes(adapter.selectedNotes)
            R.id.delete_all_notes -> deleteAllNotes()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllNotes(): Boolean {
        viewModel.deleteAllNotes()
        return true
    }

    private fun deleteMultipleNotes(list: List<NoteEntity>): Boolean {
        viewModel.deleteMultipleNotes(list)
        return true
    }

    override fun onItemClick(id: Int) {
        val action = MainFragmentDirections.actionEditNote(id)
        findNavController().navigate(action)
    }

    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }

    private fun addSampleData(): Boolean {
        viewModel.addSampleData()
        return true
    }

    private fun showSnackbar(text: String) {
        Snackbar
            .make(
                requireView(),
                text,
                Snackbar.LENGTH_SHORT
            )
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (this::adapter.isInitialized) {
            outState.putParcelableArrayList(SELECTED_LIST_KEY, adapter.selectedNotes)
        }
        super.onSaveInstanceState(outState)
    }
}