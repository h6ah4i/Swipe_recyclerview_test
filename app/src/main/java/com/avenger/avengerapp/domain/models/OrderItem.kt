package com.avenger.avengerapp.domain.models

/**
 * Created by RobGThai on 9/21/16.
 */
data class OrderItem(val id: Long, val displayName: String = "",
                     val price: Double = 0.0,
                     val displayOrder: Int)