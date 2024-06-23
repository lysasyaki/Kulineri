package com.alysa.myrecipe.minuman.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alysa.myrecipe.R

class MinumanFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_drink, container, false)

        textView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        // Cleanup references to avoid memory leaks
//        textView = findViewById<View>(R.id.text_notifications) as TextView
//    }
}
