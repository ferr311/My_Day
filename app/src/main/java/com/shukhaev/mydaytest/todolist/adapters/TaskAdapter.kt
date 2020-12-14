package com.shukhaev.mydaytest.todolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shukhaev.mydaytest.databinding.ItemListTodoCardBinding
import com.shukhaev.mydaytest.todolist.model.Task

class TaskAdapter (private val listener:OnItemClickListener): ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            ItemListTodoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemListTodoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {

                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
                todoItemCheckbox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onCheckBoxClick(task,todoItemCheckbox.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                todoItemCheckbox.isChecked = task.completed
                todoItemTvTask.text = task.name
                todoItemTvTask.paint.isStrikeThruText = task.completed
                todoItemIvPriority.isVisible = task.important
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked:Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem


    }
}