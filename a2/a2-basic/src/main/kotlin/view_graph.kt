import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Color.*
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment

class view_graph(
    private val model: Model
    ): StackPane(), IView {

    private var Xmin = 0.0
    private var Ymin = 0.0
    private var Xmax = 300.0
    private var Ymax = 200.0

    private var canvas_width = 360.0
    private var canvas_height = 270.0

    private var v_gap20 = 20.0
    private var v_gap40 = 40.0

    private var width_scale = 1.0
    private var height_scale = 1.0

    private var title = ""
    private var x_name = ""
    private var y_name = ""

    private var max_y = 100

    private var all_data: MutableList<Int> = ArrayList()


    override fun updateView(info: update_info) {
        val cur_dataset = info.dataset!!
        max_y = find_max(cur_dataset.data)
        title = cur_dataset.title
        x_name = cur_dataset.xAxis
        y_name = cur_dataset.yAxis

        all_data = cur_dataset.data

        draw_xy_plane()
    }


    init {

        this.padding = Insets(50.0,50.0,50.0,50.0)
        this.background = Background(BackgroundFill(WHITE, null, null))
        model.addView(this)

        this.widthProperty().addListener{ _, old, new ->
            if (old.toDouble() != 0.0) {
                width_scale = new.toDouble() / old.toDouble()
                Xmin *= width_scale
                Xmax *= width_scale
                v_gap20 *= width_scale
                v_gap40 *= width_scale
                canvas_width *= width_scale
            }
            draw_xy_plane()
        }

        this.heightProperty().addListener{ _, old, new ->
            if (old.toDouble() != 0.0){
                height_scale = new.toDouble() / old.toDouble()
                Ymin *= height_scale
                Ymax *= height_scale
                canvas_height *= height_scale
            }
            draw_xy_plane()
        }

        draw_xy_plane()
    }


    fun draw_xy_plane(){
        println("enter draw")
        this.children.removeAll(this.children)

        val group = Group()

        val yline = Line(Xmin, Ymin, Xmin, Ymax)
        yline.stroke = BLACK
        yline.strokeWidth = 1.0
        group.children.add(yline)

        val xline = Line(Xmin, Ymax, Xmax, Ymax)
        xline.stroke = BLACK
        xline.strokeWidth = 1.0
        group.children.add(xline)

        val x_top = Line(Xmin, Ymin, Xmax, Ymin)
        x_top.stroke = LIGHTGRAY
        x_top.strokeWidth = 1.0
        group.children.add(x_top)

        val v_top = Line(Xmax, Ymin, Xmax, Ymax)
        v_top.stroke = LIGHTGRAY
        v_top.strokeWidth = 1.0
        group.children.add(v_top)

        val y_zero = Text(Xmin - v_gap20, Ymax, "0")
        group.children.add(y_zero)

        val y_top = Text(Xmin - v_gap20, Ymin, "${max_y}")
        group.children.add(y_top)

        // use gc to draw labels
        val canvas = Canvas(canvas_width, canvas_height)

        // Use the graphics context to draw on a canvas
        val gc = canvas.graphicsContext2D

        gc.textAlign = TextAlignment.CENTER
        gc.textBaseline = VPos.BOTTOM
        gc.fillText(x_name, canvas_width/2, canvas_height)

        gc.textAlign = TextAlignment.CENTER
        gc.textBaseline = VPos.TOP
        gc.fillText(title, canvas_width/2, 0.0)

        gc.rotate(90.0)
        gc.textAlign = TextAlignment.CENTER
        gc.textBaseline = VPos.CENTER
        gc.fillText(y_name, 100.0, canvas_height/4)

//        val x_label = Text((Xmin + Xmax)/2, Ymax + y_zero_gap, x_name)
//        x_label.textAlignment = TextAlignment.CENTER
//        group.children.add(x_label)

        val y_label = Text(Xmin - v_gap40, (Ymin + Ymax)/2, y_name)
        y_label.rotate = -90.0
        group.children.add(y_label)

//        val title_label = Text((Xmin + Xmax)/2, Ymin, title)
//        title_label.textAlignment = TextAlignment.CENTER
//        group.children.add(title_label)

        // draw bar
        var data_n = all_data.size

        var total_len = Xmax - Xmin - 5.0 * data_n - 5.0

        var y_height = Ymax - Ymin

        var cur_x = Xmin + 5.0

        var bar_len = total_len / data_n

        // draw the bar
        if (data_n != 0 && max_y != 0){
            var i = 0
            while (i < data_n) {
                var i_height = all_data.get(i)
                var ratio_height = i_height * y_height / max_y
                val rectangle = Rectangle(bar_len, ratio_height)
                rectangle.fill = Color.hsb(i * 360.0 / data_n, 1.0, 1.0)
                rectangle.x = cur_x
                rectangle.y = Ymax - ratio_height

                i += 1
                cur_x += (bar_len + 5.0)

                group.children.add(rectangle)
            }
        }

        this.children.add(canvas)
        this.children.add(group)
    }
}

fun find_max(data: MutableList<Int>): Int{
    var max_int = 0
    for (i in data){
        if (i > max_int){
            max_int = i
        }
    }
    return max_int
}

