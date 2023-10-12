import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.*
import javafx.scene.paint.Color
import java.beans.beancontext.BeanContextChild
import javax.lang.model.type.NullType

internal class NoteArray(
    model: Model,
    statusBar: StatusBar
    ) : FlowPane(), IView{

    var isInit = false

    val myStatusBar = statusBar

    var mySearchMsg = ""

    val myModel = model

    // switch between two array for toggleButton
    private val allNotes: ArrayList<Note> = ArrayList()

    private var isToggle = false
    private var isSearch = false

    override fun updateView(reason:String, id:Int, counter: Int, searchMsg:String) {
        if (reason == "Random"){
            val newNote = Note(true, id, Label(""), Label(""), 0, myModel)
            allNotes.add(newNote)

            // display
            display()
        } else if (reason == "Clean"){
            val removeLen = children.size
            for (element in children){
                if (element is Note){
                    if (element.id == id){ // delete the note that is select
                        myModel.disableDelete()
                    }
                    allNotes.remove(element)
                }
            }
            display()
            myStatusBar.cleanupMsg(removeLen)
            myModel.decreaseCounter(removeLen)
        } else if (reason == "Show"){
            isToggle = true
            display()
        } else if (reason == "FinishShow"){
            isToggle = false
            display()
        } else if (reason == "Search"){
            isSearch = (searchMsg != "")

            if (isSearch){
                mySearchMsg = searchMsg
            }

            display()
        } else if (reason == "Delete"){
            for (n in allNotes) {
                if (n.id == id){
                    allNotes.remove(n)
                    break
                }
            }

            display()
        } else if (reason == "Deselect"){
            for (n in allNotes) {
                if (n.id == id){ // clean the border
                    println("Should clean")
                    n.border = null
                    break
                }
            }
            display()
        } else if (reason == "Select" || reason == "Edit"){
            for (n in allNotes) {
                if (n.id == id){ // clean the border
                    println("Should clean")
                    n.border = Border(BorderStroke(javafx.scene.paint.Color.DARKBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0, 1.0, 1.0, 1.0)))
                } else {
                    n.border = null
                }
            }
            display()
        }
    }

    init {
        // register with the model when we're ready to start receiving date
        isToggle = false
        isSearch = false
        model.addView(this)
        this.hgap = 10.0
        this.vgap = 10.0
        this.padding = Insets(10.0, 10.0, 10.0, 10.0)
    }

    // add a note to allNotes
    fun addChild(newChild: Note){
        allNotes.add(newChild)

        // display
        display()
    }

    // return a Triple containing all the key info of children targetid
    fun getChildInfo(targetid: Int): Triple<String, String, Int>{
        var childTitle = ""
        var childBody = ""
        var childIsImportant = -1

        for (c in this.children) {
            if ((c is Note) && (c.id == targetid)) {
                childTitle = c.title.text
                childBody = c.body.text
                childIsImportant = c.isImportant
                break
            }
        }
        return Triple(childTitle, childBody, childIsImportant)
    }

    // update a note with given info
    fun editChild(childid: Int, newTitle: String, newBody: String, newImportant: Int){
        for (c in children) {
            if ((c is Note) && (c.id == childid)) {
                println("Successfully edit Note ${childid}")
                c.title.text = newTitle
                c.body.text = newBody
                c.isImportant = newImportant
                c.setColor(c.isImportant)
                break
            }
        }

        // reconstruct children
        display()
    }

    // display the changes by selecting all the notes that match the filter
    private fun display(){
        var reason = (isToggle || isSearch)
        this.children.clear()

        if (isSearch){ // search one more time for new adding chilren
            for (n in allNotes) {
                n.checkMatch(mySearchMsg)
            }
        }

        for (element in allNotes){
            if (isToggle && isSearch){
                if (element.isImportant == 1 && element.ismatch == true){
                    this.children.add(element)
                }
            } else if (isToggle && !isSearch){
                if (element.isImportant == 1){
                    this.children.add(element)
                }
            } else if (!isToggle && isSearch){
                if (element.ismatch == true){
                    this.children.add(element)
                }
            } else {
                this.children.add(element) // base case (all the notes)
            }
        }
        myStatusBar.updateInfo(reason, children.size, allNotes.size)
    }
}