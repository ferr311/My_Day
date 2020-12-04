package com.shukhaev.mydaytest.todolist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shukhaev.mydaytest.R
import com.shukhaev.mydaytest.databinding.FragmentTodolistBinding
import com.shukhaev.mydaytest.todolist.adapters.TaskAdapter
import com.shukhaev.mydaytest.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todolist.*

@AndroidEntryPoint
class TasksListFragment : Fragment(R.layout.fragment_todolist) {

    private val tasksListViewModel: TasksListViewModel by viewModels()
    private lateinit var binding: FragmentTodolistBinding
    private val taskAdapter: TaskAdapter = TaskAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentTodolistBinding.bind(view)
        binding.todoFabAdd.setOnClickListener { navigateToAddFragment() }
        initRecyclerView()
        observeViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_list_menu, menu)
        val searchItem  = menu.findItem(R.id.todo_list_search)
        val searchView = searchItem.actionView as SearchView
        searchView.onQueryTextChanged {
            tasksListViewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.todo_list_sort_by_name -> {

                true
            }
            R.id.todo_list_sort_by_date -> {

                true
            }

            R.id.todo_list_hide_completed -> {
                item.isChecked = !item.isChecked
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
        }
    }

    private fun navigateToAddFragment() {
        findNavController().navigate(R.id.action_navigation_todolist_to_addTaskFragment)
    }

}