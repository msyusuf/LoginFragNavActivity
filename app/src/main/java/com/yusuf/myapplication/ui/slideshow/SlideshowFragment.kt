package com.yusuf.myapplication.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yusuf.myapplication.databinding.FragmentSlideshowBinding
import com.yusuf.myapplication.ui.data.Constants

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(Constants.LOG_TAG, "***GalleryFragment.onCreate()...")
        _binding = null
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        Log.i(Constants.LOG_TAG, "***GalleryFragment.onDestroyView()...")
//        _binding = null
//    }
//    override fun onStart() {
//        super.onStart()
//        Log.i(Constants.LOG_TAG, "***GalleryFragment.onStart()...")
//        _binding = null
//    }
//    override fun onResume() {
//        super.onResume()
//        Log.i(Constants.LOG_TAG, "***GalleryFragment.onResume()...")
//        _binding = null
//    }
//    override fun onDetach() {
//        super.onDetach()
//        Log.i(Constants.LOG_TAG, "***GalleryFragment.onDetach()...")
//    }
}