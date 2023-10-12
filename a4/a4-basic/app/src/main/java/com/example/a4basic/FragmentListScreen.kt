package com.example.a4basic

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class FragmentListScreen : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // layout is defined in "res/layouts/fragment_1.xml"
        val root = inflater.inflate(R.layout.fragment_list_screen, container, false)

        // add UI handles for navigation here

        val buttonRandom = root.findViewById<Button>(R.id.random)
        buttonRandom.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
            viewModel.randomNote()
        }

        val buttonClear = root.findViewById<Button>(R.id.clear)
        buttonClear.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
            viewModel.clearAll()
        }
        viewModel.canClear.observe(this) { cando ->
            buttonClear.isEnabled = cando
        }

        val buttonAdd = root.findViewById<Button>(R.id.add)
        buttonAdd.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
            findNavController().navigate(R.id.action_list_to_add)
        }

        val buttonSwitch = root.findViewById<Switch>(R.id.Switch)
        buttonSwitch.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
            println("clicked")
            viewModel.notifySwitch(buttonSwitch.isChecked)
        }

        val searchField = root.findViewById<EditText>(R.id.searchField)
        searchField.addTextChangedListener {
            println("search")
            viewModel.notifySearch(searchField.text.toString())
        }
        // add UI handlers that call your viewmodel here

        // observe viewModel properties here

        val wl = root.findViewById<LinearLayout>(R.id.noteList)
        viewModel.notes.observe(this) { notes ->
            // first clean up
            wl.removeAllViews()

            // update UI
            for (n in notes){
                val view = inflater.inflate(R.layout.note_item, null)
                val tt = view.findViewById<TextView>(R.id.titleText)
                tt.text = n.title

                val bt = view.findViewById<TextView>(R.id.bodyText)
                bt.text = n.body

                if (n.important){
                    view.setBackgroundColor(Color.rgb(255, 182, 193)) // pink
                }

                val deleteButton = view.findViewById<Button>(R.id.deleteButton)
                deleteButton.setOnClickListener {
                    viewModel.deleteNote(n.id)
                }

                view.setOnClickListener {
                    val action = FragmentListScreenDirections.actionListToDisplay()
                    findNavController().navigate(action)
                    viewModel.setCurrentNote(n.title, n.body, n.important, n.id)
                }

                wl.addView(view)
            }

        }


        return root
    }
}