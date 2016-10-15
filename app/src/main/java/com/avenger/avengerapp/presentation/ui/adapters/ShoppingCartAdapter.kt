package com.avenger.avengerapp.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.avenger.avengerapp.R
import com.avenger.avengerapp.domain.models.OrderItem
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder
import timber.log.Timber

/**
 * Created by RobGThai on 10/8/16.
 */
class ShoppingCartAdapter(val inflater: LayoutInflater): RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>(),
        SwipeableItemAdapter<ShoppingCartAdapter.ViewHolder> {

    private val OPTIONS_AREA_PROPORTION = 0.3f
    private val REMOVE_ITEM_THRESHOLD = 0.6f
    internal var pinnedPosition = -1

    override fun onSwipeItem(holder: ShoppingCartAdapter.ViewHolder?, position: Int, result: Int): SwipeResultAction {
        if (result == SwipeableItemConstants.RESULT_SWIPED_LEFT) {
            if (holder!!.lastSwipeAmount < -REMOVE_ITEM_THRESHOLD) {
                return SwipeLeftRemoveAction(this, position)
            } else {
                return SwipeLeftPinningAction(this, position)
            }
        } else {
            return SwipeCancelAction(this, position)
        }
    }

    override fun onGetSwipeReactionType(holder: ShoppingCartAdapter.ViewHolder?, position: Int, x: Int, y: Int): Int {
        return SwipeableItemConstants.REACTION_CAN_SWIPE_LEFT
    }

    override fun onSetSwipeBackground(holder: ShoppingCartAdapter.ViewHolder?, position: Int, type: Int) {
        if (type == SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND) {
//            holder!!.root.setBackgroundColor(Color.RED)
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    var items: MutableList<OrderItem> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onBindViewHolder(holder: ShoppingCartAdapter.ViewHolder?, position: Int) {
        val order: OrderItem = getItem(position)
        holder?.apply {
            txtLabel.text = order.displayName
            txtPrice.text = order.price.toString() + " THB"
            txtQty.text = "1"
            root.setTag(R.id.item, order)
            root.setOnClickListener {
                Timber.d("Root click item $position")
            }
            txtQty.setOnClickListener { Timber.d("Qty click") }

            deleteImage.setOnClickListener {
                removeItem(position)
            }

            maxLeftSwipeAmount = -OPTIONS_AREA_PROPORTION
            maxRightSwipeAmount = 0f
            swipeItemHorizontalSlideAmount = if(position == pinnedPosition) -OPTIONS_AREA_PROPORTION else 0f
        }
    }


    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ShoppingCartAdapter.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.shopping_cart_item, parent, false) as ViewGroup)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    private fun getItem(position: Int): OrderItem {
        return this.items[position]
    }

    inner class ViewHolder(view: ViewGroup): AbstractSwipeableItemViewHolder(view) {

        override fun getSwipeableContainerView(): View {
            return swipeableContainer
        }

        var root = view
        var txtLabel = view.findViewById(R.id.txtLabel) as TextView
        var txtPrice = view.findViewById(R.id.txtPrice) as TextView
        var txtQty = view.findViewById(R.id.txtQty) as TextView

        var swipeableContainer: FrameLayout = view.findViewById(R.id.swipeable_container) as FrameLayout
        var optionDelete = view.findViewById(R.id.option_delete) as FrameLayout
        var deleteImage = view.findViewById(R.id.option_delete_image)


        var lastSwipeAmount = 0f

        override fun onSlideAmountUpdated(horizontalAmount: Float, verticalAmount: Float, isSwiping: Boolean) {
            val itemWidth = itemView.width
            Timber.d("itemWidth $itemWidth")
            val optionItemWidth = itemWidth * OPTIONS_AREA_PROPORTION
            Timber.d("optionItemWidth $optionItemWidth")
            val offset = (optionItemWidth + 0.5f).toInt()
            val p = Math.max(0f, Math.min(OPTIONS_AREA_PROPORTION, -horizontalAmount)) / OPTIONS_AREA_PROPORTION

            Timber.d("offset ${offset}")
            Timber.d("p ${p}")
            Timber.d("optionDelete ${deleteImage.width}")
            if (deleteImage.width == 0) {
                Timber.d("setLayoutWidth ${(optionItemWidth + 0.5f)}")
                setLayoutWidth(deleteImage, (optionItemWidth + 0.5f).toInt())
            }

            deleteImage.translationX = (-(p * optionItemWidth * 1f + 0.5f).toInt() + offset).toFloat()
            Timber.d("optionDelete.translationX ${(-(p * optionItemWidth * 1f + 0.5f).toInt() + offset).toFloat()}")
            if (horizontalAmount < -REMOVE_ITEM_THRESHOLD) {
                swipeableContainer.visibility = View.INVISIBLE
                optionDelete.visibility = View.INVISIBLE
            } else {
                swipeableContainer.visibility = View.VISIBLE
                optionDelete.visibility = View.VISIBLE
            }

            lastSwipeAmount = horizontalAmount
        }

        fun setLayoutWidth(view: View, width: Int) {
            var params:ViewGroup.LayoutParams = view.layoutParams
            params.width = width
            view.layoutParams = params
        }
    }


    inner class SwipeLeftRemoveAction(var adapter: ShoppingCartAdapter, var position: Int) : SwipeResultActionRemoveItem() {

        override fun onPerformAction() {
            Timber.d("Left Remove Action")
            adapter.items.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    inner class SwipeLeftPinningAction(var adapter: ShoppingCartAdapter, var position: Int) : SwipeResultActionMoveToSwipedDirection() {

        override fun onPerformAction() {
            Timber.d("Left Pinning Action")
            pinnedPosition = position
            adapter.notifyItemChanged(position)
        }
    }


    inner class SwipeCancelAction(var adapter: ShoppingCartAdapter, var position: Int) : SwipeResultActionDefault() {

        override fun onPerformAction() {
            Timber.d("Left Cancel Action")
            pinnedPosition = -1
            adapter.notifyItemChanged(position)
        }
    }

}
