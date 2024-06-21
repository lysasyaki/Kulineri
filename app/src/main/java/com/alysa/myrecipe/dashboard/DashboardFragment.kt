package com.alysa.myrecipe.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alysa.myrecipe.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_food, container, false)

//        textView = root.findViewById(R.id.)
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        // Cleanup references to avoid memory leaks
//        textView = findViewById<View>(R.id.text_dashboard) as TextView
//    }
}
