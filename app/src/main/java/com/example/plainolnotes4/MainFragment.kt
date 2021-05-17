package com.example.plainolnotes4

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plainolnotes4.data.NoteEntity
import com.example.plainolnotes4.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment(), NotesListAdapter.ItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var notesListAdapter: NotesListAdapter

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
            notesListAdapter = NotesListAdapter(it, this@MainFragment)
            binding.recyclerView.adapter = notesListAdapter
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_fragment_menu -> addSampleData()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(id: Int) {
        val action = MainFragmentDirections.actionEditNote(id)
        findNavController().navigate(action)
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