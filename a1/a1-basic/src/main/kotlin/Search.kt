import javafx.geometry.Pos
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox

internal class Search(
    private val model: Model
    ) : VBox(){

    private val text = TextField("") // field for user to enter

    init {
        // set label properties
        text.isEditable = true

        // setup
        text.setMinSize(50.0, 25.0)
        text.setMaxSize(200.0, 25.0)
        text.prefWidth = 200.0

        // add a listener whenever the field changed
        text.textProperty().addListener(){ _, _, new ->
            model.searchMsg = new.toString()
        }

        // add label widget to the pane
        children.add(text)

    }
}