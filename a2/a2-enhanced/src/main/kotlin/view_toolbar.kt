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

    private var current_dataset_name = ""

    private val text = Label("Dataset:")
    private val choice_box = ChoiceBox<String>()
    private val create_new = Button("New")
    private val delete_old = Button("Delete")
    private val separator = Separator()
    private val spinner = Spinner<Int>(1, 20, 5)

    override fun updateView(info: update_info) {
        val current_data_lebel = info.data_label
        val should_delete = info.should_delete
        var new_is_exist = false // whether we need to add a new chart
        var old_still_exist = false
        for (i in choice_box.items){
            if (current_data_lebel == i){
                new_is_exist = true
            }
            if (should_delete == i){
                old_still_exist = true
            }
        }
        if (!new_is_exist && current_dataset_name != ""){
            choice_box.items.add(current_data_lebel)
        }
        if (old_still_exist){
            choice_box.items.remove(should_delete)
        }
        choice_box.items.remove("")
        delete_old.isDisable = (info.dataset_amount <= 0) // disable when no dataset left

        if (current_data_lebel != ""){ // still have data set left
            choice_box.getSelectionModel().select(current_data_lebel)
            choice_box.isDisable = false
        } else { // no dataset left
            choice_box.isDisable = true
        }

    }

    init {
        this.padding = Insets(10.0,10.0,10.0,10.0)
        this.spacing = 10.0
        this.alignment = Pos.CENTER_LEFT

        choice_box.items.addAll("Increasing", "Large", "Middle", "Single", "Range", "Percentage")
        choice_box.getSelectionModel().selectFirst()
        current_dataset_name = "Increasing"

        choice_box.getSelectionModel().selectedIndexProperty().addListener {_, old, new ->
            if (new.toInt() != -1){
                current_dataset_name = choice_box.items.get(new.toInt())
                model.switch_dataset(current_dataset_name)
            }
        }

        choice_box.prefWidth = 150.0

        separator.orientation = Orientation.VERTICAL

        create_new.alignment = Pos.CENTER
        create_new.prefWidth = 80.0

        spinner.prefWidth = 60.0

        this.children.add(text)
        this.children.add(choice_box)
        this.children.add(delete_old)
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

        delete_old.setOnMouseClicked {
            model.delete_old_dataset(current_dataset_name)
        }
    }
}