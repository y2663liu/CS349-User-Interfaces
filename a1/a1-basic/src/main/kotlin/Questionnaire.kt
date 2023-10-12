import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color.LIGHTYELLOW
import java.awt.Color
import java.awt.TextField
import kotlin.random.Random

// a pane for adding/editing note
internal class Questionnaire(
    model: Model,
    noteArray: NoteArray
) : VBox(), IView{
    var isEdit = false // flag for add/edit

    val topPane = HBox()
    val paneName = Label("Add New Note")

    val middlePane = GridPane()

    val titleLabel = Label("Title")
    var title = javafx.scene.control.TextField("")

    val bodyLabel = Label("Body")
    var body = TextArea("")

    val checkbox = CheckBox("Important")

    val bottonPane = HBox()

    val saveBotton = Button("Save")
    val cancelButton = Button("Cancel")

    // key Info
    var isImportant = -1
    var currentid = -1 // the note id that we are going to add/edit

    val myNoteArray = noteArray

    init {
        //setup
        this.isVisible = false
        this.isDisable = true

        saveBotton.setOnMouseClicked {
            isImportant =
                if (checkbox.isSelected == true){ // important depend on checkbox
                    1
                } else {
                    0
                }
            this.isVisible = false
            this.isDisable = true

            // create note
            if (isEdit == false){
                val newChild = Note(false, currentid, Label(title.text), Label(body.text), isImportant, model)
                noteArray.addChild(newChild)
                model.finishAdd(true)
            } else {
                noteArray.editChild(currentid, title.text, body.text, isImportant) // edit note through note array
                model.finishEdit()
            }
        }

        cancelButton.setOnMouseClicked {
            if (isEdit == false){
                isImportant = -1
                this.isVisible = false
                this.isDisable = true
                model.finishAdd(false)
            } else {
                isImportant = -1
                this.isVisible = false
                this.isDisable = true
                model.finishEdit()
            }
        }

        // setupsize
        this.setMaxSize(350.0, 350.0)

        paneName.setPrefSize(100.0,20.0)
        paneName.setMaxSize(100.0,20.0)
        paneName.alignment = Pos.CENTER_LEFT
        topPane.children.add(paneName)
        topPane.alignment = Pos.CENTER_LEFT

        titleLabel.setPrefSize(50.0,30.0)
        titleLabel.setMaxSize(75.0,30.0)
        titleLabel.alignment = Pos.CENTER_LEFT

        title.setPrefSize(300.0,30.0)
        title.setMaxSize(325.0,30.0)

        body.isWrapText = true
        bodyLabel.setPrefSize(50.0,200.0)
        bodyLabel.setMaxSize(75.0,250.0)
        bodyLabel.alignment = Pos.CENTER_LEFT

        body.setPrefSize(300.0,200.0)
        body.setMaxSize(325.0,250.0)

        middlePane.add(titleLabel, 0, 0)
        middlePane.add(title, 1, 0)
        middlePane.add(bodyLabel, 0, 1)
        middlePane.add(body, 1, 1)

        checkbox.setPrefSize(125.0,30.0)
        checkbox.setMaxSize(150.0,30.0)
        checkbox.alignment = Pos.CENTER_LEFT
        middlePane.add(checkbox, 1, 2)
        middlePane.vgap = 10.0

        bottonPane.spacing = 10.0
        saveBotton.alignment = Pos.CENTER
        saveBotton.setPrefSize(75.0,30.0)
        saveBotton.setMaxSize(75.0,30.0)

        cancelButton.alignment = Pos.CENTER
        cancelButton.setPrefSize(75.0,30.0)
        cancelButton.setMaxSize(75.0,30.0)

        bottonPane.children.add(saveBotton)
        bottonPane.children.add(cancelButton)
        bottonPane.alignment = Pos.CENTER_RIGHT

        children.add(topPane)
        children.add(middlePane)
        children.add(bottonPane)

        this.padding = Insets(0.0, 10.0, 0.0, 10.0)
        this.background = Background(BackgroundFill(javafx.scene.paint.Color.LIGHTGRAY, null, null))

        // observer
        model.addView(this)
    }

    // make it visible when adding/editing note
    // make it invisible when finishing
    override fun updateView(reason:String, id:Int, counter: Int, searchMsg:String) {
        if (reason == "Add"){
            // set up key field
            paneName.text = "Add New Note"
            title.clear()
            body.clear()
            checkbox.isSelected = false
            isEdit = false
            currentid = id

            // make it show up on the screen
            this.isVisible = true
            this.isDisable = false

        } else if (reason == "Edit"){
            paneName.text = "Edit Note"
            title.clear()
            body.clear()
            checkbox.isSelected = false
            isEdit = true
            currentid = id

            // receive the child info from child array and save them
            val receiveInfo = myNoteArray.getChildInfo(id)
            title.text = receiveInfo.first
            body.text = receiveInfo.second
            checkbox.isSelected = (receiveInfo.third == 1)

            // make it show up on the screen
            this.isVisible = true
            this.isDisable = false
        }
    }

}