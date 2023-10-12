import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class view_data_table(
    private val model: Model
    ): GridPane(), IView {

    override fun updateView(info: update_info) {

        this.children.removeAll(this.children)

        if (info.dataset != null){
            var index = 0 // record the index of each column
            for (i in info.dataset!!.data){
                var initialValue = i
                var spinner = Spinner<Int>(0, 100, initialValue)
                spinner.prefWidth = 100.0
                var col = index

                val spinner_label = Label("${col + 1}:") // #:
                // once value change -> notify others
                spinner.valueProperty().addListener { _, _, new ->
                    model.update_column(col, new)
                }
                this.add(spinner_label, 0, col)
                this.add(spinner, 1, col)
                index++
            }
        }

    }

    init {
        this.padding = Insets(10.0,10.0,10.0,10.0)
        this.alignment = Pos.TOP_CENTER
        this.prefWidth = 150.0
        this.hgap = 10.0
        model.addView(this)
    }
}