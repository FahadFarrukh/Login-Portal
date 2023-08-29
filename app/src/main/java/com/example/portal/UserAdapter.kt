package com.example.portal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val userListLiveData: LiveData<List<User>>, private val loggedInUserId: Long) :
    ListAdapter<User, UserAdapter.ViewHolder>(UserDiffCallback()) {

    init {
        userListLiveData.observeForever { userList ->
            submitList(userList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, loggedInUserId)
    }

    override fun getItemCount(): Int {
        return userListLiveData.value?.size ?: 0
    }

    fun updateUserList(userList: List<User>) {
        submitList(userList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView3: TextView = itemView.findViewById(R.id.textView3)
        private val textView4: TextView = itemView.findViewById(R.id.textView4)
        private val textView5: TextView = itemView.findViewById(R.id.textView5)

        fun bind(userData: User, loggedInUserId: Long) {
            textView1.text = "User ${userData.id}"
            textView2.text = "Name: ${userData.firstname} ${userData.lastname}"
            textView3.text = "Email: ${userData.email}"
            textView4.text = "City: ${userData.city}"
            textView5.text = "Is User Logged in? ${userData.login}"



            if (userData.id == loggedInUserId) {
                itemView.setBackgroundResource(R.drawable.green)
            } else {
                itemView.setBackgroundResource(R.drawable.red)
            }




        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
