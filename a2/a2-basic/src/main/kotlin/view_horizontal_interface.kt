import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.*

// a pane for adding/editing note
internal class view_horizontal_interface(
    model: Model,
) : HBox(), IView{

    val Title_title = Label("Title:")
    var Title_text = javafx.scene.control.TextField("")

    val X_Axis_title = Label("X-Axis:")
    var X_Axis_text = javafx.scene.control.TextField("")

    val Y_Axis_title = Label("Y-Axis:")
    var Y_Axis_text = javafx.scene.control.TextField("")

    init {
        this.padding = Insets(10.0,10.0,10.0,10.0)
        this.spacing = 10.0

        this.prefHeight = 20.0

        this.alignment = Pos.CENTER_LEFT

        Title_text.isEditable = true
        X_Axis_text.isEditable = true
        Y_Axis_text.isEditable = true

        Title_text.prefWidth = 120.0
        X_Axis_text.prefWidth = 120.0
        Y_Axis_text.prefWidth = 120.0

        this.children.add(Title_title)
        this.children.add(Title_text)
        this.children.add(X_Axis_title)
        this.children.add(X_Axis_text)
        this.children.add(Y_Axis_title)
        this.children.add(Y_Axis_text)

        model.addView(this)

        // once changed -> notify others
        Title_text.textProperty().addListener(){ _, _, new ->
            model.edit_title(new.toString())
        }

        X_Axis_text.textProperty().addListener(){ _, _, new ->
            model.edit_x(new.toString())
        }

        Y_Axis_text.textProperty().addListener(){ _, _, new ->
            model.edit_y(new.toString())
        }
    }

    override fun updateView(info: update_info){

        this.Title_text.text = info.dataset!!.title
        this.X_Axis_text.text = info.dataset!!.xAxis
        this.Y_Axis_text.text = info.dataset!!.yAxis
    }
}