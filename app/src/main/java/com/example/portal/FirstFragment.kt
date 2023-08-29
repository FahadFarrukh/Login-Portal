package com.example.portal

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.portal.AppDatabase
import com.example.portal.databinding.FragmentFirstBinding
import com.google.android.material.navigation.NavigationView

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var sessionManager: SessionManager // Add this line

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "user-database"
        ).build()

//        val navigationView = requireView().findViewById<NavigationView>(R.id.nav_view)
//        val headerView = navigationView.getHeaderView(0)
//
//        val usernameTextView = headerView.findViewById<TextView>(R.id.textViewff)
//        val emailTextView = headerView.findViewById<TextView>(R.id.textViewee)

        sessionManager = SessionManager(requireContext()) // Initialize sessionManager

        binding.buttonFirst.setOnClickListener {
            val email = binding.editTexte.text.toString()
            val password = binding.editText4.text.toString()

            Thread {
                val user = db.UserDao().getUser(email, password)
                val firstname = db.UserDao().firstname() ?: "Unknown Firstname"
                val lastname = db.UserDao().lastname() ?: "Unknown Lastname"
                val email1 = db.UserDao().email() ?: "Unknown Email"

                requireActivity().runOnUiThread {
                    if (user != null) {
                        // User found, update session and navigate
                        sessionManager.login(requireContext(), email)
                //        usernameTextView.text = "$firstname $lastname"
                //        emailTextView.text = email1
                        loginSuccess(user)
                    } else {
                        // User not found, show error toast
                        Toast.makeText(requireContext(), "Invalid Email or Password!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()






    }

        binding.textview4.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SignupFragment)
        }

        val togglePassword: ToggleButton = view.findViewById(R.id.togglePassword)
        val editTextPassword: EditText = view.findViewById(R.id.editText4)

        togglePassword.setOnClickListener {
            val inputType = if (editTextPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            } else {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            editTextPassword.inputType = inputType
            editTextPassword.setSelection(editTextPassword.text.length)
        }
    }
    private fun loginSuccess(user: User) {
        // Save login status and user ID to session
        sessionManager.isLoggedIn = true
        sessionManager.loggedInUserId = user.id

        // Proceed to the main fragment or any other action
        findNavController().navigate(R.id.action_FirstFragment_to_HomeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
