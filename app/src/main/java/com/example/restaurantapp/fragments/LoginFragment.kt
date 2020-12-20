package com.example.restaurantapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentLoginBinding
import com.example.restaurantapp.viewmodel.UserViewModel
import java.security.MessageDigest


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.progressCircular.visibility = View.GONE

        binding.loginButton.setOnClickListener{
            //binding.progressCircular.visibility = View.VISIBLE
            login()
        }

        // when the user wants to register, navigate to the register fragment
        binding.toRegisterText.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    // function for logging in
    private fun login() {
        val username = binding.loginUsernameText.text.toString()
        val password = binding.loginPasswordText.text.toString()

        val format = validateFormat(username, password)

        // if the formats aren't valid
        if(!format) {
            return
        }

        binding.progressCircular.visibility = View.VISIBLE

        // when the users loaded from the db (users with the given username or email)
        userViewModel.usedUser.observe(viewLifecycleOwner, { user ->
            // it it's null, it means that there is no user with the given username or email
            if(user == null) {
                Log.d("-----", "$user")
                Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                binding.progressCircular.visibility = View.GONE
            }
            // if there is a match, we save out the username, email, password in shared preferences
            else{
                Log.d("-----", "$user")
                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return@observe
                if(!sharedPref.contains("username") || !sharedPref.contains("email") || !sharedPref.contains("password")) {
                    val sharedPrefEdit = sharedPref.edit()
                    sharedPrefEdit.clear()
                    sharedPrefEdit.putString("username", user.username)
                    sharedPrefEdit.putString("email", user.email)
                    sharedPrefEdit.putString("password", user.password)
                    sharedPrefEdit.apply()
                }
                // navigating to the main screen
                userViewModel.currentUser.value = user
                Toast.makeText(context, "Successful login", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_listFragment)
            }})

        userViewModel.getUsersForLogin(username, sha256(password))
    }

    // function for validating the user inputs
    private fun validateFormat(username: String, password: String): Boolean {
        if(username.isEmpty()) {
            binding.loginUsernameText.error = "Type in your username"
            return false
        }

        if (password.isEmpty()) {
            binding.loginPasswordText.error = "Type in your username"
            return false
        }
        return true
    }


    /**
     * hash function for passwords
     * @param input -the password we want to hash
     * @return the password hash
     */
    private fun sha256(input: String): String
    {
        val type = "SHA-256"
        val hexChars = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(hexChars[i shr 4 and 0x0f])
            result.append(hexChars[i and 0x0f])
        }

        return result.toString()
    }
}