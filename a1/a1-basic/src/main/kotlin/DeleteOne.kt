import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox

internal class DeleteOne(
    private val model: Model
    ) : HBox(), IView{

    private val button = Button("Delete")


    init {
        // setup
        button.setPrefSize(100.0, 25.0)
        button.setMaxSize(100.0, 25.0)
        button.alignment = Pos.CENTER
        button.isDisable = true

        // set mouse clicks
        button.setOnMouseClicked {
            println("Controller: changing Model (actionPerformed)")
            model.decrementCounter()
        }

        // add button widget to the pane
        children.add(button)

        // observer
        model.addView(this)

    }

    // Disable itself when no note is selected
    override fun updateView(reason: String, id: Int, counter: Int, searchMsg: String) {
        if (reason == "Select"){
            println("Delete is available 1")
            button.isDisable = false
        } else if (reason == "Edit"){
            println("Delete is available 2")
            button.isDisable = false
        } else if (reason == "Deselect"){
            println("Delete back to sleep 1")
            button.isDisable = true
        } else if (reason == "DisableDelete"){
            println("Special Disable")
            button.isDisable = true
        } else if (reason == "Delete"){
            println("Delete back to sleep 2")
            button.isDisable = true
        }
    }
}