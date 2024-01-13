package com.example.myentertainment.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Music
import com.example.myentertainment.interfaces.OnItemClickAction
import com.example.myentertainment.view.add.AddActivity
import com.example.myentertainment.view.main.adapters.MusicAdapter
import com.example.myentertainment.viewmodel.main.EntertainmentViewModel

class MusicFragment : Fragment(), OnItemClickAction {

    private lateinit var fragmentView: View
    private lateinit var viewModel: EntertainmentViewModel

    private lateinit var musicList: RecyclerView
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noMusicLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_music, container, false)
        initView()
        viewModel = EntertainmentViewModel.create(CategoryObject.MUSIC, this)
        setObservers()
        viewModel.fetchItems()
        return fragmentView
    }

    override fun onItemClicked(id: String?) {
        editSong(id)
    }

    override fun onItemLongClicked(id: String?) {
        viewModel.deleteItem(id)
    }

    private fun editSong(id: String?) {
        if (id != null) {
            val intent = Intent(activity, AddActivity::class.java)
            intent.putExtra(Constants.CATEGORY, CategoryObject.MUSIC.categoryName)
            intent.putExtra(Constants.ID, id)
            startActivity(intent)
        }
    }

    private fun initView() {
        musicList = fragmentView.findViewById(R.id.music_list)
        loadingSection = fragmentView.findViewById(R.id.music_loadingSection)
        noMusicLabel = fragmentView.findViewById(R.id.music_noMusicLabel)
    }

    private fun setObservers() {
        viewModel.entertainmentList.observe(this) { updateView(it as List<Music>) }
    }

    private fun updateView(music: List<Music>) {
        loadingSection.visibility = View.GONE
        if (music.isEmpty()) {
            musicList.visibility = View.GONE
            noMusicLabel.visibility = View.VISIBLE
        } else {
            if (viewModel.itemDeleted.value == true) {
                musicAdapter.dataSetChanged(music)
            } else {
                musicList.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                musicAdapter = MusicAdapter(requireContext(), music, this)
                musicList.adapter = musicAdapter
            }
        }
    }

}