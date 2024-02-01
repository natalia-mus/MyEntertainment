package com.example.myentertainment.interfaces

import com.example.myentertainment.data.IEntertainment

interface OnItemClickAction {

    fun onItemClicked(item: IEntertainment)
    fun onItemLongClicked(id: String?)
}