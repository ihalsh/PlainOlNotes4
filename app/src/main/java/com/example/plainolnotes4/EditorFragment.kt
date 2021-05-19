package com.example.plainolnotes4

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.plainolnotes4.databinding.EditorFragmentBinding

class EditorFragment : Fragment() {

    private lateinit var viewModel: EditorViewModel
    private val args: EditorFragmentArgs by navArgs()
    private lateinit var binding: EditorFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
        }

        setHasOptionsMenu(true)

        requireActivity().title =
            if (args.noteId == NEW_NOTE_ID) {
                getString(R.string.add_new_note)
            } else {
                getString(R.string.edit_note)
            }

        binding = EditorFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(EditorViewModel::class.java)
        viewModel.getNoteById(args.noteId)
        viewModel.currentNote.observe(viewLifecycleOwner) {
            binding.editor.setText(it.text)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveAndReturn()
                }
            }
        )

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> saveAndReturn()
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun saveAndReturn(): Boolean {
        val imm = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)


        viewModel.currentNote.value?.text = binding.editor.text.toString().trim()
        viewModel.updateCurrentNote()

        findNavController().navigateUp()
        return true
    }
}