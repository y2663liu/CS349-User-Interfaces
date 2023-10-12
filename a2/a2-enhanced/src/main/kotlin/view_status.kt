import javafx.animation.AnimationTimer
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal class view_status(
    private val model: Model
    ) : BorderPane(), IView{

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

        // from 06.clock.advanced
        val time = Label()
        val timePane = StackPane(time)
        timePane.setPrefSize(width, 75.0)

        // timer fires every 1/60 seconds, and fetches time
        val dateFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
        val timer: AnimationTimer = object : AnimationTimer() {
            override fun handle(now: Long) {
                time.text = dateFormat.format(Calendar.getInstance().time)
            }
        }
        timer.start()

        // add label widget to the pane
        this.left = text
        this.right = time

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }

}
