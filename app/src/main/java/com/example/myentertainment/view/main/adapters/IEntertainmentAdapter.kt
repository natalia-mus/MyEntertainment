package com.example.myentertainment.view.main.adapters

import com.example.myentertainment.data.IEntertainment

interface IEntertainmentAdapter {

    fun dataSetChanged(newDataSet: List<IEntertainment>)
}