package com.example.restaurantapp.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentRegisterBinding
import com.example.restaurantapp.model.User
import com.example.restaurantapp.viewmodel.UserViewModel
import java.security.MessageDigest


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var address: String
    private lateinit var phoneNumber: String
    private lateinit var password: String
    private lateinit var passwordAgain: String

    private lateinit var imageUri: Uri
    private lateinit var imageByteArray: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.progressCircular.visibility = View.GONE

        binding.addImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        // when the user presses SIGN UP button
        binding.registerButton.setOnClickListener{
            // if everything is OK, navigate to login fragment
            register()
        }

        binding.toLoginText.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun register(){
        // get user input values

        username = binding.registerUsernameText.text.toString()
        email = binding.registerEmailText.text.toString()
        password = binding.registerPasswordText.text.toString()
        passwordAgain = binding.registerPasswordAgain.text.toString()
        address = binding.registerUserAddressText.text.toString()
        phoneNumber = binding.registerUserPhoneText.text.toString()

        imageByteArray = run {
            val inputStream = activity?.contentResolver?.openInputStream(imageUri)
            inputStream!!.readBytes()
        }

        val format = validateFormat(username, email, password, passwordAgain)

        // if the formats aren't valid
        if(!format) {
            return
        }

        binding.progressCircular.visibility = View.VISIBLE

        // when the users are loaded with the given username or password
        userViewModel.usedUser.observe(viewLifecycleOwner, { user ->
            // if the username or email is not used by other user, we save out the information on shared preferences
            if(user == null) {
                val passwordHash = sha256(password)
                val newUser = User(0, username, email, passwordHash, address, phoneNumber, imageByteArray)

                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val sharedPrefEdit = sharedPref.edit()

                sharedPrefEdit.clear()
                sharedPrefEdit.putString("username", username)
                sharedPrefEdit.putString("email", email)
                sharedPrefEdit.putString("password", passwordHash)
                sharedPrefEdit.apply()

                userViewModel.addUser(newUser)
                userViewModel.currentUser.value = newUser

                Toast.makeText(context, "Successful registration", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_listFragment)
            }
            // if already exists a user with the given username or password
            else{
                Toast.makeText(context, "Username or email is already taken", Toast.LENGTH_SHORT).show()
                binding.progressCircular.visibility = View.GONE
            }})

        userViewModel.getUsersForRegistration(username, email)
    }

    // function for picking a profile image from the device
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            binding.registerUserPicture.setImageURI(imageUri)
        }
    }

    /**
     * validates the user inputs
     * @return true -if the given inputs are valid
     * @return false -if the given inputs are invalid
     */
    private fun validateFormat(username: String, email: String, password: String, passwordAgain: String): Boolean {
        // validate username
        if(username.length < 5) {
            binding.registerUsernameText.error = "Username is too short"
            return false
        }
        // validate email
        val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.{1,})"
        if(!emailRegex.toRegex().matches(email)) {
            binding.registerEmailText.error = "Email format is not valid"
            return false
        }
        // validate password
        if(password != passwordAgain) {
            binding.registerPasswordText.error = "The given passwords don't match"
            return false
        }

        if(password.isEmpty()) {
            binding.registerPasswordText.error = "Type in your password"
            return false
        }

        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z[0-9]]{8,}$"

        if(!passwordRegex.toRegex().matches(password)) {
            binding.registerPasswordText.error = "The password must containt Minimum eight characters, " +
                    "at least one uppercase letter, one lowercase letter and one number"
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