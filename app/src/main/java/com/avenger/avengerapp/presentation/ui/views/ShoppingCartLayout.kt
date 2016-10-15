package com.avenger.avengerapp.presentation.ui.views

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.avenger.avengerapp.R
import com.avenger.avengerapp.domain.models.OrderItem
import com.avenger.avengerapp.presentation.ui.adapters.ShoppingCartAdapter
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import kotlinx.android.synthetic.main.shopping_cart_layout.view.*

/**
 * Created by RobGThai on 9/21/16.
 */
class ShoppingCartLayout: LinearLayoutCompat {

    val swipeManager: RecyclerViewSwipeManager = RecyclerViewSwipeManager()

    fun displayOrders(selection: MutableList<OrderItem>) {
        adapter.apply {
            items.apply {
                clear()
                addAll(selection)
            }
            notifyDataSetChanged()

        }
        rv.smoothScrollToPosition(0)
//        rv.addOnItemTouchListener()


        if(selection.size > 0) {
            rv.visibility = View.VISIBLE
            emptyImage.visibility = View.GONE
        }else {
            rv.visibility = View.GONE
            emptyImage.visibility = View.VISIBLE
        }
        requestLayout()
    }

    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int):
        super(context, attributeSet, defStyle){
        initLayout(context)
    }

    lateinit private var adapter: ShoppingCartAdapter
    lateinit private var swipeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    fun initLayout(context: Context) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.shopping_cart_layout, this, true)
        adapter = ShoppingCartAdapter(inflater)
        swipeAdapter = swipeManager.createWrappedAdapter(adapter)

        this.rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@ShoppingCartLayout.swipeAdapter
            itemAnimator = SwipeDismissItemAnimator()
        }
        swipeManager.attachRecyclerView(rv)
    }
}
