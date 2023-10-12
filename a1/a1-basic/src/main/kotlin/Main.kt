import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.*
import javafx.stage.Stage
import javafx.stage.WindowEvent
import kotlin.system.exitProcess

// Adjust from Demo MVC2
class Main : Application() {
    override fun start(stage: Stage) {

        // create and initialize the Model
        val model = Model()

        // create each view, and tell them about the model
        // the views will register themselves with the model

        val topLine = HBox()
        val middlePart = ScrollPane()
        val bottomLine = HBox()

        // button for toolbar
        val toolbar = HBox()

        val addOneButton = AddOne(model)
        val randomButton = RandomNote(model)
        val deleteOneButton = DeleteOne(model)
        val clearAllButton = CleanAll(model)

        val toggleButton = ToggleButton(model)
        val search = Search(model)

        // setup toolbar
        toolbar.spacing = 10.0
        toolbar.children.add(addOneButton)
        toolbar.children.add(randomButton)
        toolbar.children.add(deleteOneButton)
        toolbar.children.add(clearAllButton)

        // button for status bar
        val statusBar = StatusBar(model)

        bottomLine.children.add(statusBar) // bottom-view

        // pane for Note
        val noteArray = NoteArray(model, statusBar)

        // setup
        middlePart.content = noteArray
        middlePart.isFitToWidth = true

        // pane for topLine
        topLine.spacing = 10.0
        topLine.padding = Insets(10.0, 10.0, 10.0, 10.0)
        topLine.children.add(toolbar)
        topLine.children.add(toggleButton)
        topLine.children.add(search)


        // create a Pane who has children: toolbar
        //                                 middlePart
        //                                 statusBar
        val motherPane = MotherPane(topLine, middlePart, bottomLine, model)

        // StackPane: questionnaire on the top of motherPane
        val rootPane = StackPane()
        rootPane.children.add(motherPane)

        // set size for questionnaire
        val questionnaire = Questionnaire(model, noteArray)

        // setup questionnaire
        questionnaire.setPrefSize(300.0, 300.0)
        questionnaire.setMinSize(250.0, 250.0)
        questionnaire.setMaxSize(350.0, 350.0)
        questionnaire.alignment = Pos.CENTER

        rootPane.children.add(questionnaire)

        // Add rootPane to a scene (and the scene to the stage)
        val scene = Scene(rootPane, 800.0, 600.0)
        stage.scene = scene
        stage.title = "A1 Notes (y2663liu)"
        stage.minHeight = 400.0
        stage.minWidth = 400.0
        stage.show()
    }
}