package com.yusuf.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yusuf.myapplication.LoginTestApplication
import com.yusuf.myapplication.R
import com.yusuf.myapplication.databinding.FragmentHomeBinding
import com.yusuf.myapplication.ui.data.Constants.Companion.LOG_TAG
import com.yusuf.myapplication.ui.login.LoginViewModel
import com.yusuf.myapplication.ui.login.LoginViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory((activity?.application as LoginTestApplication).repository)
        //return@viewModels LoginViewModelFactory((activity?.application as LoginTestApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.i(LOG_TAG, "In home fragment...")
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val count = checkUserCount()
        Log.i(LOG_TAG, "In Home fragment, calling checkUserCount(), UserCount = ${count} ")
    }

    private fun checkUserCount(){
        val userCount = loginViewModel.getUserCount()

        if (userCount < 1) {
            Log.i(LOG_TAG, "1) User count is: $userCount. Going to Login fragment from Home fragment")
//            val args = Bundle()
//            args.putString("userID", "String")
//            this.findNavController()
//                .navigate(R.id.nav_login, args)
        } else {
            Log.i(LOG_TAG, "2) User count is: $userCount, staying in Home fragment.")
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.i(LOG_TAG, "*HomeFragment.onCreate()...")
//        _binding = null
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        Log.i(LOG_TAG, "*HomeFragment.onDestroyView()...")
//        _binding = null
//    }
//    override fun onStart() {
//        super.onStart()
//        Log.i(LOG_TAG, "*HomeFragment.onStart()...")
//        _binding = null
//    }
//    override fun onResume() {
//        super.onResume()
//        Log.i(LOG_TAG, "*HomeFragment.onResume()...")
//        _binding = null
//    }
//    override fun onDetach() {
//        super.onDetach()
//        Log.i(LOG_TAG, "*HomeFragment.onDetach()...")
//       }
}