package com.shukhaev.mydaytest.todolist.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shukhaev.mydaytest.R
import com.shukhaev.mydaytest.databinding.FragmentAddEditTaskBinding
import com.shukhaev.mydaytest.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            addTodoInput.setText(viewModel.taskName)
            addTodoPriority.isChecked = viewModel.taskImportance
            addTodoPriority.jumpDrawablesToCurrentState()
            addTodoTvTime.isVisible = viewModel.task != null
            addTodoTvTime.text = "Created: ${viewModel.task?.createdDateFormatted}"

            addTodoInput.addTextChangedListener {
                viewModel.taskName = it.toString()
            }
            addTodoPriority.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            addTodoFabSave.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.addTodoInput.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}