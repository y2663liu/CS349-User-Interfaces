import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox

internal class ToggleButton(
    private val model: Model
    ) : HBox() {

    private val button = Button("!")

    init {
        // setup
        button.setMinSize(25.0, 25.0)
        button.setMaxSize(25.0, 25.0)

        button.setOnMouseClicked {
            println("Controller: changing Model (actionPerformed)")
            model.showOnlyMarked()
        }

        button.alignment = Pos.CENTER

        // add button widget to the pane
        children.add(button)

    }
}