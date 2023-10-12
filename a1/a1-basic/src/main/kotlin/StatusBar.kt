import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

internal class StatusBar(
    private val model: Model
    ) : HBox(), IView{

    private var selectOne = -1 // the one that is selected

    private var firstHalf = Label("")
    private var secondHalf = Label("")

    private var first = ""
    private var second = ""

    // When notified by the model that things have changed,
    // update to display the new value
    // note that we don't the # of notes, so do it in a seperate function
    override fun updateView(reason:String, id:Int, counter: Int, searchMsg:String) {
        if (reason == "Ini") {
            first = counter.toString()
        } else if (reason == "finishAddSuccess"){
            first = counter.toString()
            second = "Added Note #${id}"
        } else if (reason == "Random"){
            first = counter.toString()
            second = "Added Note #${id}"
        } else if (reason == "Delete"){
            selectOne = -1
            first = counter.toString()
            second = "Deleted Note #${id}"
        } else if (reason == "Edit"){
            selectOne = id
            first = counter.toString()
            second = "Edited Note #${id}"
        } else if (reason == "Select"){
            selectOne = id
            first = counter.toString()
        } else if (reason == "Deselect"){
            selectOne = -1
        } else if (reason == "DisableDelete"){
            selectOne = -1
        }
        updateText(first, second)
    }

    init {
        // setup
        this.setMinSize(100.0, 40.0)
        this.setMaxSize(100.0, 40.0)
        // set label properties
        firstHalf.isWrapText = true
        secondHalf.isWrapText = true

        this.padding = Insets(10.0,10.0,10.0,10.0)
        firstHalf.alignment = Pos.CENTER
        secondHalf.alignment = Pos.CENTER
        firstHalf.setMinSize(100.0, 20.0)
        firstHalf.setMaxSize(100.0, 20.0)
        secondHalf.setMinSize(125.0, 20.0)
        secondHalf.setMaxSize(125.0, 20.0)

        // add label widget to the pane
        children.add(firstHalf)
        children.add(secondHalf)

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }

    // Display things like: 1 (of 10)
    fun updateInfo(isFilter:Boolean, display: Int, total:Int) {

        if (isFilter){
            first = "${display} (of ${total})"
        } else {
            first = "${total}"
        }

        // display # of notes
        updateText(first, second)
    }

    // Display when clear is pressed
    fun cleanupMsg(total: Int){
        first = "0"
        second = "Cleared ${total} Notes"
        updateText(first, second)
    }

    private fun updateText(msg1: String, msg2: String){
        if (selectOne > 0){
            firstHalf.text = "#${selectOne} | " + msg1
        } else {
            firstHalf.text = msg1
        }
        secondHalf.text = msg2
    }
}