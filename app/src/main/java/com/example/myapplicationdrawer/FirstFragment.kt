package com.example.myapplicationdrawer

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.myapplicationdrawer.databinding.FragmentFirstBinding
import com.google.android.material.navigation.NavigationView

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var sessionManager: SessionManager

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


        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        // Find views within the fragment's layout
        val togglePassword: ToggleButton = binding.togglePassword
        val editTextPassword: EditText = binding.editText4

        // Initialize database and session manager
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "user-database"
        ).fallbackToDestructiveMigration().build()

        sessionManager = SessionManager(requireContext())

        binding.buttonFirst.setOnClickListener {
            val email = binding.editTexte.text.toString()
            val password = binding.editText4.text.toString()

            Thread {
                val user = db.UserDao().getUser(email, password)

                val email1 = db.UserDao().email() ?: "Unknown Lastname"
                val user2 = db.UserDao().getUserByEmail(email)
                requireActivity().runOnUiThread {
                    if (user != null) {
                        // User found, update session
                        sessionManager.login(requireContext(), email)

                        // Update the header TextViews in the NavigationView
                        val navHeaderView = requireActivity().findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
                        val usernameTextView = navHeaderView.findViewById<TextView>(R.id.textViewff)
                        val emailTextView = navHeaderView.findViewById<TextView>(R.id.textViewee)

                        val firstname = user.firstname
                        val lastname = user.lastname
                        usernameTextView.text = "$firstname $lastname"
                        emailTextView.text = user.email

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

        // Set the flag to indicate successful login
        findNavController().previousBackStackEntry?.savedStateHandle?.set("loginSuccess", true)

        // Proceed to the HomeFragment
        findNavController().navigate(R.id.action_FirstFragment_to_HomeFragment)
    }
    override fun onResume() {
        super.onResume()

        // Check if the login was successful
        val loginSuccess = findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>("loginSuccess")

        if (loginSuccess == true) {
            // Clear the flag
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>("loginSuccess")

            // Navigate to the HomeFragment without adding it to the back stack
            findNavController().navigate(R.id.action_FirstFragment_to_HomeFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        _binding = null
    }
}
