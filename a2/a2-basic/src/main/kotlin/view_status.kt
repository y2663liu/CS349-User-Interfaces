import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.paint.Color

internal class view_status(
    private val model: Model
    ) : HBox(), IView{

    private var old_dataset_amount = -1

    private val text = Label("")

    override fun updateView(info: update_info) {
        if (info.dataset_amount != old_dataset_amount){
            old_dataset_amount = info.dataset_amount // dataset being added/removed
            var remain_text = " dataset"
            if (info.dataset_amount > 1){
                remain_text += "s"
            }
            text.text = "${info.dataset_amount}" + remain_text // # datasets
        }
    }

    init {
        // set label properties
        text.isWrapText = true

        this.padding = Insets(10.0,10.0,10.0,10.0)
        this.background = Background(BackgroundFill(Color.LIGHTGRAY, null, null))
        text.alignment = Pos.CENTER
        text.prefHeight = 20.0
        this.alignment = Pos.CENTER_LEFT

        // add label widget to the pane
        children.add(text)

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }

}