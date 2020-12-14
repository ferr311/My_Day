package com.shukhaev.mydaytest.todolist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shukhaev.mydaytest.R
import com.shukhaev.mydaytest.databinding.FragmentTodolistBinding
import com.shukhaev.mydaytest.todolist.adapters.TaskAdapter
import com.shukhaev.mydaytest.todolist.model.SortOrder
import com.shukhaev.mydaytest.todolist.model.Task
import com.shukhaev.mydaytest.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksListFragment : Fragment(R.layout.fragment_todolist), TaskAdapter.OnItemClickListener {

    private val tasksListViewModel: TasksListViewModel by viewModels()
    private lateinit var binding: FragmentTodolistBinding
    private val taskAdapter: TaskAdapter = TaskAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentTodolistBinding.bind(view)
        binding.todoFabAdd.setOnClickListener { navigateToAddFragment() }
        initRecyclerView()
        observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            tasksListViewModel.taskEvent.collect { event ->
                when (event) {
                    is TasksListViewModel.TaskEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                tasksListViewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                }
            }
        }
    }

    override fun onItemClick(task: Task) {
        tasksListViewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        tasksListViewModel.onTaskCheckChanged(task, isChecked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_list_menu, menu)
        val searchItem = menu.findItem(R.id.todo_list_search)
        val searchView = searchItem.actionView as SearchView
        searchView.onQueryTextChanged {
            tasksListViewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.todo_list_hide_completed).isChecked =
                tasksListViewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.todo_list_sort_by_name -> {
                tasksListViewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.todo_list_sort_by_date -> {
                tasksListViewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }

            R.id.todo_list_hide_completed -> {
                item.isChecked = !item.isChecked
                tasksListViewModel.onHideCompletedClick(item.isChecked)
                true
            }
            R.id.todo_list_delete_all_completed -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeViewModel() {
        tasksListViewModel.taskList.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        binding.todoRecyclerView.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    tasksListViewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(binding.todoRecyclerView)
        }
    }

    private fun navigateToAddFragment() {
        findNavController().navigate(R.id.action_navigation_todolist_to_addTaskFragment)
    }

}