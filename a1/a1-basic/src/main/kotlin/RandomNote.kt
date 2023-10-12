import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox

internal class RandomNote(
    private val model: Model
    ) : HBox() {

    private val button = Button("Random")

    init {
        // setup
        button.setPrefSize(100.0, 25.0)
        button.setMaxSize(100.0, 25.0)
        button.alignment = Pos.CENTER

        // set mouse clicks
        button.setOnMouseClicked {
            println("Controller: changing Model (actionPerformed)")
            model.randomIncrementCounter()
        }

        // add button widget to the pane
        children.add(button)

    }
}