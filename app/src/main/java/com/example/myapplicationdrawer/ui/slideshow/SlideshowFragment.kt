package com.example.myapplicationdrawer.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplicationdrawer.AppDatabase
import com.example.myapplicationdrawer.R
import com.example.myapplicationdrawer.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "user-database"
        ).build()

        // Observe the list of all users and update UI individually
        db.UserDao().getAllUsers().observe(viewLifecycleOwner) { userList ->
            binding.userContainer.removeAllViews() // Clear existing views

            for (user in userList) {
                val userInfo = "User ID: ${user.id}\nName: ${user.firstname} ${user.lastname}\nEmail: ${user.email}\nCity: ${user.city}\n\n\n\n"
                val userTextView = TextView(requireContext())
                userTextView.text = userInfo
                userTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.userContainer.addView(userTextView)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
