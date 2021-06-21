package com.example.plainolnotes4

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plainolnotes4.data.NoteEntity
import com.example.plainolnotes4.data.NoteEntityItem
import com.example.plainolnotes4.data.toNoteEntityItem
import com.example.plainolnotes4.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.Delegates

const val NEW_NOTE_ID = 0
const val SELECTED_LIST_KEY = "SelectionListKey"

class MainFragment : Fragment(), NoteEntityItem.ItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var groupAdapter: GroupAdapter<GroupieViewHolder>
    private var newListSize: Int by Delegates.observable(0) { _, old, new ->
        if (this::groupAdapter.isInitialized) {
            (new - old).let { diff ->
                if (diff < 0) {
                    showSnackbar("${-diff} item(s) deleted.")
                }
                if (diff > 0) {
                    showSnackbar("$diff item(s) added.")
                }
            }
        }
    }
    private val selectedNotes = arrayListOf<NoteEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate.")
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        lifecycleScope.launch {
            viewModel.notesList?.collect { list ->
                newListSize = list.size// should always stays BEFORE adapter initialization!
                when (::groupAdapter.isInitialized) {
                    false -> {
                        Timber.d("Create adapter.")
                        groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
                            addAll(list.toNoteEntityItem(this@MainFragment))
                        }
                        binding.recyclerView.adapter = groupAdapter
                    }
                    true -> {
                        Timber.d("Update adapter.")
                        groupAdapter.update(list.toNoteEntityItem(this@MainFragment))
                    }
                }
                selectedNotes.clear()
                if (null != savedInstanceState && savedInstanceState.isEmpty.not()) {
                    selectedNotes.addAll(
                        savedInstanceState.getParcelableArrayList(SELECTED_LIST_KEY) ?: emptyList()
                    )
                    savedInstanceState.clear()
                }
                onItemSelectionChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView.")
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setHasOptionsMenu(true)

        requireActivity().title = getString(R.string.app_name)

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

        //Necessary to process returning to MainFragment after empty note creation
        if (::groupAdapter.isInitialized && null == binding.recyclerView.adapter) {
            binding.recyclerView.adapter = groupAdapter
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (::groupAdapter.isInitialized && selectedNotes.size > 0) {
            inflater.inflate(R.menu.main_menu_items_selected, menu)
        } else {
            inflater.inflate(R.menu.main_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_sample_data -> addSampleData()
            R.id.delete_note -> deleteMultipleNotes(selectedNotes)
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

    override fun isSelected(note: NoteEntity): Boolean = selectedNotes.contains(note)

    override fun addToSelected(note: NoteEntity) {
        selectedNotes.add(note)
    }

    override fun removeFromSelected(note: NoteEntity) {
        selectedNotes.remove(note)
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
        if (::groupAdapter.isInitialized) {
            outState.putParcelableArrayList(SELECTED_LIST_KEY, selectedNotes)
        }
        super.onSaveInstanceState(outState)
    }
}