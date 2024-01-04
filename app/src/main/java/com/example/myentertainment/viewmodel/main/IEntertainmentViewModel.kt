package com.example.myentertainment.viewmodel.main

import com.example.myentertainment.data.IEntertainment

interface IEntertainmentViewModel {

    fun orderByCreationDate(array: ArrayList<IEntertainment>): ArrayList<IEntertainment> {
        array.sortBy { it.creationDate }
        return array
    }
}