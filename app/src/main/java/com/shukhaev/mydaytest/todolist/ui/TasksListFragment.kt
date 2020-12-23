package com.shukhaev.mydaytest.todolist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
import com.shukhaev.mydaytest.util.exhaustive
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
    private lateinit var searchView:SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentTodolistBinding.bind(view)
        binding.todoFabAdd.setOnClickListener { tasksListViewModel.onAddNewTaskClick() }

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
                    is TasksListViewModel.TaskEvent.NavigateToAddTaskScreen -> {
                        val action =
                            TasksListFragmentDirections.actionNavigationTodolistToAddTaskFragment(
                                null,
                                "New Task"
                            )
                        findNavController().navigate(action)
                    }
                    is TasksListViewModel.TaskEvent.NavigateToEditTaskScreen -> {
                        val action =
                            TasksListFragmentDirections.actionNavigationTodolistToAddTaskFragment(
                                event.task,
                                "Edit Task"
                            )
                        findNavController().navigate(action)
                    }
                    is TasksListViewModel.TaskEvent.ShowTaskSavedMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is TasksListViewModel.TaskEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action =
                            TasksListFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            tasksListViewModel.onAddEditResult(result)
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
        searchView = searchItem.actionView as SearchView

        val pendingQuery = tasksListViewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()){
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery,false)
        }

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
                tasksListViewModel.onDeleteAllCompletedClick()
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

    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
    }

}