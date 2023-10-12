import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage) {
        // create and initialize the Model
        val model = Model()
        val rootPane = BorderPane()

        val top_toolbar = view_toolbar(model)
        val horizontal_interface = view_horizontal_interface(model)
        val data_table = view_data_table(model)
        val data_graph = view_graph(model)
        val data_statistics = view_statistic(model)
        val status_bar = view_status(model)

        val top_pane = VBox()
        top_pane.children.add(top_toolbar)
        top_pane.children.add(horizontal_interface)

        val data_pane = ScrollPane()
        data_pane.content = data_table
        data_pane.isFitToWidth = true

        rootPane.top = top_pane
        rootPane.left = data_pane
        rootPane.center = data_graph
        rootPane.right = data_statistics
        rootPane.bottom = status_bar
        // Add rootPane to a scene (and the scene to the stage)
        val scene = Scene(rootPane, 800.0, 600.0)
        stage.scene = scene
        stage.title = "A2 Grapher (y2663liu)"
        stage.minWidth = 600.0
        stage.minHeight = 400.0
        stage.show()
    }
}