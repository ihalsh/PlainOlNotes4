package com.example.plainolnotes4

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plainolnotes4.data.NoteEntity
import com.example.plainolnotes4.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment(), NotesListAdapter.ItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: NotesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = MainFragmentBinding.inflate(inflater, container, false)

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.notesList?.observe(viewLifecycleOwner) {
            adapter = NotesListAdapter(it, this@MainFragment)
            binding.recyclerView.adapter = adapter
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
        showSnackbar("${viewModel.deleteAllNotes()} items deleted.")
        return true
    }

    private fun deleteMultipleNotes(list: List<NoteEntity>): Boolean {
        showSnackbar("${viewModel.deleteNotes(list)} items deleted.")
        updateMenu()
        return true
    }

    private fun updateMenu() {
        adapter.selectedNotes.clear()
        onItemSelectionChanged()
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
        showSnackbar("Sample data added.")
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
}