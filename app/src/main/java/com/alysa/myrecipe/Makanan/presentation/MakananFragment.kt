package com.alysa.myrecipe.Makanan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alysa.myrecipe.R

class MakananFragment : Fragment() {
    private fun onCreate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {

        val view = inflater.inflate(R.layout.fragment_food, container, false)
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        // Cleanup references to avoid memory leaks
//        textView = findViewById<View>(R.id.text_dashboard) as TextView
//    }
}
