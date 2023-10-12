package cs349.a3battleship

import cs349.a3battleship.model.Display
import cs349.a3battleship.model.Game
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.stage.Stage

class Battleship : Application() {
    override fun start(stage: Stage) {

        var game = Game(10, true)
        var computer = AI(game)
        // var player = ...
        game.startGame()

        stage.title = "A3 Battleship (y2663liu)"
        val main = Display(10, game, stage)
        val scene = Scene(main, 875.0, 375.0)


        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}