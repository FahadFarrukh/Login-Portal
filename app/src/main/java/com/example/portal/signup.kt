package com.example.portal

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.portal.databinding.FragmentSignupBinding



class signup : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)








        val value = arrayOf(
            "  Select a City",
            "  Karachi",
            "  Paris",
            "  Brussels",
            "  Orleans",
            "  Frankfurt"
        )
        val yourAdapter = ArrayAdapter(requireContext(), R.layout.spinner_list, value)


        binding.spinnerFirst.adapter = yourAdapter

// Set the initial selection to the "Select a superhero" hint item
        binding.spinnerFirst.setSelection(0)

        binding.spinnerFirst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val textView = view as? TextView

                val wrappedDrawableLeft = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.drawable_with_padding  // Use the wrapped drawable here
                )

                val drawableRight = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.dropdown
                )

                textView?.setCompoundDrawablesWithIntrinsicBounds(
                    wrappedDrawableLeft,
                    null,
                    drawableRight,
                    null
                )

                if (position == 0) {
                    // Set the text color for the hint item when it is selected again


                    textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))


                } else {
                    // Reset the text color for other selected items
                    (view as? TextView)?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.black
                        )
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "user-database"
        )
            .fallbackToDestructiveMigration() // This will recreate the database on schema changes
            .build()







        binding.buttonFirst.setOnClickListener {

            val email = binding.editTexte.text.toString().trim()
            val password = binding.editText4.text.toString().trim()



            val selectedValue = binding.spinnerFirst.selectedItem.toString().trim()

            val confirmpassword = binding.editText2.text.toString().trim()
            val inputText1 = binding.fa2.text.toString().trim()
            val inputText2 = binding.fa.text.toString().trim()

            if (inputText1.isEmpty()) {
                val errorMessage = "Enter First Name!"
                showErrorMessage(errorMessage)
            } else if (inputText2.isEmpty()) {
                val errorMessage = "Enter Last Name!"
                showErrorMessage(errorMessage)
            } else if (email.isEmpty()) {
                val errorMessage = "Enter your Email!"
                showErrorMessage(errorMessage)
            } else if (password.isEmpty()) {
                val errorMessage = "Enter your Passowrd!"
                showErrorMessage(errorMessage)
            } else if (confirmpassword.isEmpty()) {
                val errorMessage = "Enter Confirmed Password!"
                showErrorMessage(errorMessage)
            } else if (selectedValue == "Select a City") {
                val errorMessage = "Select a City!"
                showErrorMessage(errorMessage)
            } else if (!isValidEmail(email)) {
                val errorMessage = "Incorrect Email Format!"
                showErrorMessage(errorMessage)
            } else if (password != confirmpassword) {
                val errorMessage = "Passwords Donot Match!"
                showErrorMessage(errorMessage)
            } else {
                // Query the database to check if a user with the given email already exists


                Thread {
                    val existingUser = db.UserDao().getUserByEmail(email)

                    if (existingUser == null) {
                        // User does not exist, insert the user data into the Room database
                        val user1 = User(0,inputText1, inputText2, email, password, selectedValue,false)
                        db.UserDao().insertUser(user1)

                        val successMessage = "You have signed up!"
                        requireActivity().runOnUiThread {
                            showErrorMessage(successMessage)
                        }
                    } else {
                        val errorMessage = "User already exists!"
                        requireActivity().runOnUiThread {
                            showErrorMessage(errorMessage)
                        }
                    }
                }.start()
                clearform()

            }
    }



        val togglePassword: ToggleButton = view.findViewById(R.id.togglePassword)

        val editTextPassword: EditText = view.findViewById(R.id.editText4)

// Set a click listener to toggle the password visibility
        togglePassword.setOnClickListener {
            // Toggle the password visibility




            val inputType = if (editTextPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            } else {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            editTextPassword.inputType = inputType

            // Move the cursor to the end of the input text
            editTextPassword.setSelection(editTextPassword.text.length)
        }

        val togglePassword2: ToggleButton = view.findViewById(R.id.togglePassword2)

        val editTextPassword2: EditText = view.findViewById(R.id.editText2)

        togglePassword2.setOnClickListener {
            // Toggle the password visibility

            val inputType2 = if (editTextPassword2.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            } else {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            editTextPassword2.inputType = inputType2

            // Move the cursor to the end of the input text
            editTextPassword2.setSelection(editTextPassword2.text.length)
        }






    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
    private fun showErrorMessage(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun clearform() {
        binding.editTexte.setText("")
        binding.editText4.setText("")
        binding.editText2.setText("")
        binding.fa2.setText("")
        binding.fa.setText("")
        binding.spinnerFirst.setSelection(0)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
