package com.example.myentertainment.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainment.R
import com.example.myentertainment.data.Music
import com.example.myentertainment.view.main.adapters.MusicAdapter
import com.example.myentertainment.viewmodel.MusicFragmentViewModel

class MusicFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: MusicFragmentViewModel

    private lateinit var musicList: RecyclerView
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var noMusicLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_music, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(MusicFragmentViewModel::class.java)
        setObservers()
        viewModel.fetchMusic()
        return fragmentView
    }

    private fun initView() {
        musicList = fragmentView.findViewById(R.id.music_list)
        loadingSection = fragmentView.findViewById(R.id.music_loadingSection)
        noMusicLabel = fragmentView.findViewById(R.id.music_noMusicLabel)
    }

    private fun setObservers() {
        viewModel.music.observe(this, { updateView(it) })
    }

    private fun updateView(music: List<Music>) {
        if (music.isEmpty()) {
            loadingSection.visibility = View.INVISIBLE
            noMusicLabel.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
            musicList.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            musicList.adapter = MusicAdapter(requireContext(), music)
        }
    }
}