package com.heershingenmosiken.assertions

import java.util.*

internal object Utils {
    internal fun <T> filter(set: TreeSet<T>, filter: (T) -> Boolean) {
        val itemsToRemove = ArrayList<T>()
        set.forEach { if (filter.invoke(it)) itemsToRemove.add(it) }
        set.removeAll(itemsToRemove);
    }
}