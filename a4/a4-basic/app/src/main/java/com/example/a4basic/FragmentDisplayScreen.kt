package com.example.a4basic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip

class FragmentDisplayScreen : Fragment() {
    private val viewModel: MyViewModel by activityViewModels()

    var title = ""
    var body = ""
    var important = false
    var cur_id = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // layout is defined in "res/layouts/fragment_2.xml"
        val root = inflater.inflate(R.layout.fragment_display_screen, container, false)

        val dl = root.findViewById<TextView>(R.id.displayTitle)
        viewModel.current_title.observe(this) { t ->
            title = t
            dl.text = t
        }

        val db = root.findViewById<TextView>(R.id.displayBody)
        viewModel.current_body.observe(this) { b ->
            body = b
            db.text = b
        }

        val c = root.findViewById<Chip>(R.id.chip)
        viewModel.current_important.observe(this) { i ->
            important = i
            c.isVisible = i
        }

        val curID = root.findViewById<TextView>(R.id.displayLabel)
        viewModel.current_id.observe(this) { id ->
            cur_id = id
            curID.text = "Note #$id"
        }

        val buttonEdit = root.findViewById<Button>(R.id.displayEdit)
        buttonEdit.setOnClickListener {
            val action = FragmentDisplayScreenDirections.actionDisplayToEdit(title, body, important, cur_id)
            findNavController().navigate(action)
        }

        return root
    }
}