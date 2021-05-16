package com.example.plainolnotes4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plainolnotes4.databinding.MainFragmentBinding
import timber.log.Timber

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

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = MainFragmentBinding.inflate(inflater, container, false)

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.notesList.observe(viewLifecycleOwner) {
            notesListAdapter = NotesListAdapter(it, this@MainFragment)
            binding.recyclerView.adapter = notesListAdapter
        }

        return binding.root
    }

    override fun onItemClick(id: Int) {
        Timber.tag("CL").d("Clicked on item $id")
        val action = MainFragmentDirections.actionEditNote(id)
        findNavController().navigate(action)
    }
}