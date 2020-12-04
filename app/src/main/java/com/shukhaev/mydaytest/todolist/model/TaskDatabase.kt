package com.shukhaev.mydaytest.todolist.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shukhaev.mydaytest.todolist.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class CallBack @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val appScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            appScope.launch {
                dao.insert(Task("помыть посуду", completed = true))
                dao.insert(Task("убраться"))
                dao.insert(Task("сходить в магазин", important = true))
                dao.insert(Task("позвонить Илону Маску"))
                dao.insert(Task("посмотреть Андроид Академия", completed = true))
                dao.insert(Task("сделать домашку СкиллБокс", important = true))
            }
        }
    }

}