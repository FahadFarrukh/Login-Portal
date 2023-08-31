package com.example.myapplicationdrawer.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myapplicationdrawer.AppDatabase
import com.example.myapplicationdrawer.UserAdapter
import com.example.myapplicationdrawer.databinding.FragmentGalleryBinding
import com.example.myapplicationdrawer.SessionManager

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var userAdapter: UserAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "user-database"
        ).build()

        val userListLiveData = db.UserDao().getAllUsers()

        sessionManager = SessionManager(requireContext()) // Initialize sessionManager

        // Pass the loggedInUserId from the session manager to the UserAdapter constructor
        userAdapter = UserAdapter(userListLiveData, sessionManager.loggedInUserId)

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = userAdapter

        userListLiveData.observe(viewLifecycleOwner, Observer { userList ->
            userAdapter.updateUserList(userList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
