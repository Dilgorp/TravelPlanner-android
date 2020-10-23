package ru.dilgorp.android.travelplanner.ui.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

class DeleteItemCallback(
    private val deleteAction: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        deleteAction(viewHolder.adapterPosition)
    }
}