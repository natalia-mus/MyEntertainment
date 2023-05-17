package com.example.myentertainment.view.findfriends

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.myentertainment.LayoutDimensionsUtil
import com.example.myentertainment.R
import com.example.myentertainment.data.UserProfile
import com.example.myentertainment.viewmodel.Dimensions

class FriendsListAdapter(
    private val context: Context,
    private val users: ArrayList<UserProfile>,
    private val profilePictures: HashMap<String, Uri>
) : Adapter<FriendsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_tile, parent, false)
        val viewHolder = FriendsListViewHolder(view)
        viewHolder.customView(context)
        return viewHolder
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        val user = users[position]

        if (profilePictures.containsKey(user.userId)) {
            val image = profilePictures[user.userId]

            Glide.with(context)
                .load(image)
                .circleCrop()
                .into(holder.image)
        }

        holder.username.text = user.username
        holder.realName.text = user.realName
        holder.location.text = user.city + ", " + user.country
    }
}

class FriendsListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.userTile_image)
    val username: TextView = view.findViewById(R.id.userTile_username)
    val realName: TextView = view.findViewById(R.id.userTile_realName)
    val location: TextView = view.findViewById(R.id.userTile_location)

    private val userTile: ConstraintLayout = view.findViewById(R.id.userTile)

    fun customView(context: Context) {
        userTile.layoutParams.width = LayoutDimensionsUtil.calcWidth(context, Dimensions.USER_TILE_WIDTH)
        userTile.layoutParams.height = LayoutDimensionsUtil.calcHeight(context, Dimensions.USER_TILE_HEIGHT)
    }
}