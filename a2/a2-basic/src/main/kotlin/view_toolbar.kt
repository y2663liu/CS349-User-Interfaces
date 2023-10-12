import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.text.Text

class view_toolbar(
    private val model: Model
    ) : HBox(), IView {

    private var new_column: Int
    private var new_table_name: Int

    private val text = Label("Dataset:")
    private val choice_box = ChoiceBox<String>()
    private val create_new = Button("New")
    private val separator = Separator()
    private val spinner = Spinner<Int>(1, 20, 5)

    override fun updateView(info: update_info) {
        val current_data_lebel = info.data_label
        var is_exist = false // whether we need to add a new chart
        for (i in choice_box.items){
            if (current_data_lebel == i){
                is_exist = true
                break
            }
        }
        if (!is_exist){
            choice_box.items.add(current_data_lebel)
        }
        choice_box.getSelectionModel().select(current_data_lebel)

    }

    init {
        this.padding = Insets(10.0,10.0,10.0,10.0)
        this.spacing = 10.0
        this.alignment = Pos.CENTER_LEFT

        choice_box.items.addAll("Increasing", "Large", "Middle", "Single", "Range", "Percentage")
        choice_box.getSelectionModel().selectFirst()

        choice_box.getSelectionModel().selectedIndexProperty().addListener {_, _, new ->
            println(choice_box.items.get(new as Int))
            model.switch_dataset(choice_box.items.get(new as Int))
        }

        choice_box.prefWidth = 150.0

        separator.orientation = Orientation.VERTICAL

        create_new.alignment = Pos.CENTER
        create_new.prefWidth = 80.0

        spinner.prefWidth = 60.0

        this.children.add(text)
        this.children.add(choice_box)
        this.children.add(separator)
        this.children.add(create_new)
        this.children.add(spinner)
        this.prefHeight = 20.0
        new_column = 5
        new_table_name = 1
        // can make it editable, but be sure to enter numbers!

        spinner.valueProperty().addListener { _, _, new ->
            new_column = new
        }
        // register with the model when we're ready to start receiving data
        model.addView(this)

        create_new.setOnMouseClicked {
            model.create_new_dataset("New${new_table_name}", new_column)
            new_table_name += 1
        }
    }
}