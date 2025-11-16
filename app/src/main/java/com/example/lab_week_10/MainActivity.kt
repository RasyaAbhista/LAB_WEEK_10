package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.*
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    private fun updateText(value: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, value)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { totalObject ->
            updateText(totalObject.value)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()   // ← AGAR AMAN SAAT DEBUG
            .build()
    }

    private fun initializeValueFromDatabase() {
        val list = db.totalDao().getTotal(ID)

        if (list.isEmpty()) {
            db.totalDao().insert(
                Total(
                    id = 1,
                    total = TotalObject(0, Date().toString())
                )
            )
        } else {
            val t = list.first()
            viewModel.setTotal(t.total.value, t.total.date)
        }
    }

    override fun onPause() {
        super.onPause()
        val t = viewModel.total.value ?: return

        db.totalDao().update(
            Total(
                id = ID,
                total = TotalObject(t.value, Date().toString())
            )
        )
    }

    override fun onStart() {
        super.onStart()
        val date = viewModel.total.value?.date ?: ""
        Toast.makeText(this, "Last updated: $date", Toast.LENGTH_LONG).show()
    }

    companion object {
        const val ID = 1L
    }
}
