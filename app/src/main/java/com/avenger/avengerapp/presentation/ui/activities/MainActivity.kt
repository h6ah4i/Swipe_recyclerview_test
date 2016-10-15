package com.avenger.avengerapp.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.avenger.avengerapp.R
import com.avenger.avengerapp.domain.models.OrderItem
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var selectedOrder: MutableList<OrderItem> = mutableListOf()

    @DebugLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.btnAdd.setOnClickListener {
            val item: OrderItem = OrderItem((selectedOrder.size + 1).toLong(), "Name", 20.00, selectedOrder.size + 1)
            selectedOrder.add(item)
            this@MainActivity.summary_pane.displayOrders(selectedOrder)
        }

        Timber.tag("MainActivity")
        Timber.d("OnCreate")

        summary_pane.displayOrders(selectedOrder)
    }
}
