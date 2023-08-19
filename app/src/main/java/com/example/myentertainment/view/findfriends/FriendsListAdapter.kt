package com.example.myentertainment.view.findfriends

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
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
    private val userTileClickListener: UserTileClickListener
) : Adapter<FriendsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_tile, parent, false)
        val viewHolder = FriendsListViewHolder(view)
        viewHolder.customView(context)
        return viewHolder
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        val user = users[position].userProfileData

        if (user != null) {
            val image = users[position].userProfilePicture
            Glide.with(context)
                .load(image)
                .circleCrop()
                .placeholder(ResourcesCompat.getDrawable(context.resources, R.drawable.placeholder_user, null))
                .into(holder.image)

            holder.username.text = user.username
            holder.realName.text = user.realName
            holder.location.text = user.city + ", " + user.country

            holder.userTile.setOnClickListener {
                userTileClickListener.onUserTileClicked(user.userId)
            }
        }
    }
}

class FriendsListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val userTile: ConstraintLayout = view.findViewById(R.id.user)
    val image: ImageView = view.findViewById(R.id.user_image)
    val username: TextView = view.findViewById(R.id.user_username)
    val realName: TextView = view.findViewById(R.id.user_realName)
    val location: TextView = view.findViewById(R.id.user_location)

    fun customView(context: Context) {
        image.layoutParams.width = LayoutDimensionsUtil.calcWidth(context, Dimensions.USER_TILE_PROFILE_PICTURE_SIZE)
        image.layoutParams.height = LayoutDimensionsUtil.calcHeight(context, Dimensions.USER_TILE_PROFILE_PICTURE_SIZE)

        val usernameTextSize = LayoutDimensionsUtil.calcTextSize(context, Dimensions.USER_TILE_USERNAME_TEXT_SIZE)
        val realNameTextSize = LayoutDimensionsUtil.calcTextSize(context, Dimensions.USER_TILE_REAL_NAME_TEXT_SIZE)
        val locationTextSize = LayoutDimensionsUtil.calcTextSize(context, Dimensions.USER_TILE_LOCATION_TEXT_SIZE)
        username.setTextSize(TypedValue.COMPLEX_UNIT_PX, usernameTextSize)
        realName.setTextSize(TypedValue.COMPLEX_UNIT_PX, realNameTextSize)
        location.setTextSize(TypedValue.COMPLEX_UNIT_PX, locationTextSize)
    }
}

interface UserTileClickListener {
    fun onUserTileClicked(userId: String?)
}