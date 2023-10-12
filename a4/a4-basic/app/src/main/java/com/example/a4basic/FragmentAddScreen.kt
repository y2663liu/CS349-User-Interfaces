package com.example.a4basic

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class FragmentAddScreen : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // layout is defined in "res/layouts/fragment_2.xml"
        val root = inflater.inflate(R.layout.fragment_add_screen, container, false)

        // add UI handlers that call your viewmodel here

        // observe viewModel properties here

        val buttonSave = root.findViewById<Button>(R.id.addSave)
        buttonSave.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
            var newTitle = root.findViewById<TextView>(R.id.addTitle).text.toString()

            var newBody = root.findViewById<TextView>(R.id.addBody).text.toString()

            var newImportant = false

            if (root.findViewById<Switch>(R.id.addImportant).isChecked){
                newImportant = true
            }
            viewModel.addNote(newTitle, newBody, newImportant)
            findNavController().navigate(R.id.action_add_back_list)
        }

        return root
    }
}