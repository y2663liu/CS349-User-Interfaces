import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color

// a pane that collects everything except the questionnaire, in a way that we can easily disable everything
internal class MotherPane(
    private val topLine: HBox,
    private val middlePart: ScrollPane,
    private val bottomLine: HBox,
    private val model: Model
    ): BorderPane(), IView {

    init {
        this.top = topLine
        this.center = middlePart
        this.bottom = bottomLine

        this.isDisable = false
        model.addView(this)
    }

    // disable everything when adding/editing a note
    override fun updateView(reason:String, id:Int, counter: Int, searchMsg:String) {
        if (reason == "finishAddSuccess" || reason == "finishAddFail"){
            this.isDisable = false
            this.background = Background(BackgroundFill(Color.WHITE, null, null))
        } else if (reason == "finishEdit"){
            this.isDisable = false
            this.background = Background(BackgroundFill(Color.WHITE, null, null))
        } else if (reason == "Add"){
            this.isDisable = true
            this.background = Background(BackgroundFill(javafx.scene.paint.Color.DARKGRAY, null, null))
        } else if (reason == "Edit"){
            this.isDisable = true
            this.background = Background(BackgroundFill(javafx.scene.paint.Color.DARKGRAY, null, null))
        }
    }
}