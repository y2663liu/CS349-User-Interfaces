import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import kotlin.math.round

class view_statistic(
    private val model: Model
    ): GridPane(), IView {

    override fun updateView(info: update_info) {
        Number_text.text = "${info.dataset!!.data.size}"
        Minimum_text.text = "${find_data_min(info.dataset!!.data)}"
        Maximum_text.text = "${find_data_max(info.dataset!!.data)}"
        Average_text.text = "${find_data_average(info.dataset!!.data)}"
        Sum_text.text = "${find_data_sum(info.dataset!!.data)}"
    }

    val Number_title = Label("Number")
    val Number_text = Label("")

    val Minimum_title = Label("Minimum")
    val Minimum_text = Label("")

    val Maximum_title = Label("Maximum")
    val Maximum_text = Label("")

    val Average_title = Label("Average")
    val Average_text = Label("")

    val Sum_title = Label("Sum")
    val Sum_text = Label("")

    init {
        this.padding = Insets(10.0,10.0,10.0,10.0)
        this.hgap = 10.0
        this.vgap = 10.0
        this.alignment = Pos.TOP_LEFT

        this.maxWidth = 125.0
        this.minWidth = 125.0

        this.add(Number_title, 0,0)
        this.add(Number_text, 1,0)

        this.add(Minimum_title, 0,1)
        this.add(Minimum_text, 1,1)

        this.add(Maximum_title, 0,2)
        this.add(Maximum_text, 1,2)

        this.add(Average_title, 0,3)
        this.add(Average_text, 1,3)

        this.add(Sum_title, 0,4)
        this.add(Sum_text, 1,4)

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }

    fun find_data_min(data: MutableList<Int>):Int{ // min of a list of int
        var min_int = 100
        for (i in data){
            if (i < min_int){
                min_int = i
            }
        }
        return min_int
    }

    fun find_data_max(data: MutableList<Int>):Int{ // max of a list of int
        var max_int = 0
        for (i in data){
            if (i > max_int){
                max_int = i
            }
        }
        return max_int
    }

    fun find_data_sum(data: MutableList<Int>):Int{ // sum of a list of int
        return data.sum()
    }

    fun find_data_average(data: MutableList<Int>):Double{ // average of a list of int
        return round(data.average() * 10.0) / 10.0
    }
}