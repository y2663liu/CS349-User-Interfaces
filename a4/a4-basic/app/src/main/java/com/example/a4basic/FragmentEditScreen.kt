package com.example.a4basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip

class FragmentEditScreen: Fragment() {
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // layout is defined in "res/layouts/fragment_2.xml"
        val root = inflater.inflate(R.layout.fragment_edit_screen, container, false)

        var title = arguments!!["title"].toString()
        var body = arguments!!["body"].toString()
        val important = arguments!!["important"]
        var id = arguments!!["id"].toString()

        val dl = root.findViewById<TextView>(R.id.editTitle)
        dl.text = title

        val db = root.findViewById<TextView>(R.id.editBody)
        db.text = body

        val importantSwitch = root.findViewById<Switch>(R.id.editImportant)
        importantSwitch.isChecked = important as Boolean

        val displayID = root.findViewById<TextView>(R.id.editLabel)
        displayID.text = "Edit Note #$id"

        val buttonEdit = root.findViewById<Button>(R.id.editSave)
        buttonEdit.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
            var newTitle = root.findViewById<TextView>(R.id.editTitle).text.toString()

            var newBody = root.findViewById<TextView>(R.id.editBody).text.toString()

            var newImportant = false

            if (root.findViewById<Switch>(R.id.editImportant).isChecked){
                newImportant = true
            }
            println("Why Always Me?")
            viewModel.editNote(newTitle, newBody, newImportant, id.toInt())

            findNavController().navigate(R.id.action_edit_back_display)
        }

        return root
    }
}