package com.shukhaev.mydaytest.todolist.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shukhaev.mydaytest.R

class ToDoListFragment : Fragment(R.layout.fragment_todolist) {

    private val toDoListViewModel: ToDoListViewModel by viewModels()

}