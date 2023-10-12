package cs349.a3battleship.model

import cs349.a3battleship.model.ships.Ship
import cs349.a3battleship.model.ships.ShipType
import javafx.beans.binding.When
import javafx.event.EventType
import javafx.geometry.Point2D
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Affine
import javafx.stage.Stage
import java.text.NumberFormat
import kotlin.math.floor
import kotlin.math.roundToInt

class Display (var demension: Int, val game: Game, val stage: Stage): Pane(), IView {
    var Ships = mutableListOf<DisplayShip>()
    var is_ship_selected = false
    var can_game_start = false
    var is_game_start = false
    var is_game_end = false

    var AI_attackedCells = Array(10){i -> Array(10){i -> CellState.Ocean}}
    var Human_attackedCells = Array(10){i -> Array(10){i -> CellState.Ocean}}

    override fun updateView() {
        TODO("Not yet implemented")
    }

    init {
        // add 5 ships
        val y = 25.0
        val Destroyer = DisplayShip(2, 362.5, y, ShipType.Destroyer)
        val Cruiser = DisplayShip(3, 392.5, y, ShipType.Cruiser)
        val Submarine = DisplayShip(3, 422.5, y, ShipType.Submarine)
        val Battleship = DisplayShip(4, 452.5, y, ShipType.Battleship)
        val Carrier = DisplayShip(5, 482.5, y, ShipType.Carrier)

        Ships.add(Destroyer)
        Ships.add(Cruiser)
        Ships.add(Submarine)
        Ships.add(Battleship)
        Ships.add(Carrier)

        Ships.forEach {it.draw(this)}

        val player_board = board(true)
        val ai_board = board(false)
        val player_fleet = fleet()

        // initialize start_button
        val start_button = create_start_button()

        // initialize exit_button
        val exit_button = create_exit_button()

        this.children.add(player_board)
        this.children.add(ai_board)
        this.children.add(player_fleet)
        this.children.add(start_button)
        this.children.add(exit_button)

        this.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { e ->
            if (is_game_end){
                // do_nothing
            } else if (is_game_start){ // find the attack_point
                val attack_point = Point2D(e.x, e.y)
                val receive_cell = find_attack_grid(attack_point)
                if (receive_cell.x != -1 && receive_cell.y != -1){ // a valid attack
                    println("Attack")
                    game.attackCell(find_attack_grid(attack_point))

                    // update info in AI board
                    AI_attackedCells = game.getBoard(Player.AI)
                    Human_attackedCells = game.getBoard(Player.Human)

                    if (game.is_finish()){
                        println("END")
                        is_game_end = true
                        val unsink_ShipType = game.unsunk_ships()

                        this.Ships.forEach { ship ->
                            if (ship.shipType in unsink_ShipType){
                                ship.t_matrix = Affine()
                            }
                        }
                        draw_winner(game.return_winner())
                    }
                }
                if (!is_game_end){
                    redraw()
                }
            } else {
                if (e.button == MouseButton.SECONDARY){ // it is a right click
                    for (ship in Ships) {
                        ship.rotate_ship(e.x, e.y) // disable all the other ships except this ship
                    }
                } else { // it is a left click
                    if (is_ship_selected){ // drop the ship
                        for (ship in Ships) {
                            if (ship.is_selected){ // find the one that is being selected
                                var orientation = Orientation.VERTICAL // find the orientation
                                if (!ship.is_vertical){
                                    orientation = Orientation.HORIZONTAL
                                }

                                val bowPoint2D = ship.t_matrix.transform(Point2D(ship.originX, ship.originY))
                                val grid_index = point_to_grid(bowPoint2D, ship.is_vertical, ship.length, true)
                                val bowcell = Cell(grid_index.first, grid_index.second)
                                val newship = game.placeShip(Player.Human, ship.shipType, orientation, bowcell)

                                if (newship == null){ // remove from Board by call function in Game
                                    ship.t_matrix = Affine()
                                    ship.is_vertical = true
                                    this.can_game_start = false
                                    println("NOT OK")
                                } else { // adjust board
                                    ship.t_matrix = adjust_posi(bowPoint2D, grid_index, ship.t_matrix, ship.is_vertical, ship.length)
                                    ship.cell = bowcell

                                    // if we have enough ship, then start
                                    this.can_game_start = (game.getShipsPlacedCount(Player.Human) == 5)
                                }
                                ship.is_selected = false
                                ship.can_move = true
                                is_ship_selected = false
                            } else { // make other ships able to move
                                ship.can_move = true
                            }
                        }
                        is_ship_selected = false
                    } else{
                        for (ship in Ships) {
                            if (ship.check_clicked(e.x, e.y)){
                                disable_other_ships(ship.shipType) // disable all the other ships except this ship
                                is_ship_selected = true
                                if (ship.is_valid_cell()){ // remove from board
                                    game.removeShip(Player.Human, ship.cell)
                                }
                                break
                            }
                        }
                    }
                }
                redraw()
            }
        }

        this.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_MOVED) { e ->
            if (is_game_end ||is_game_start){
                // do_nothing
            } else {
                for (ship in Ships) {
                    ship.check_move(e.x, e.y)
                }

                redraw()
            }
        }
    }

    fun disable_other_ships(type: ShipType){
        Ships.forEach { if (it.shipType != type) {it.can_move = false}}
    }


    fun create_start_button(): Button{
        val start_button = Button("Start Game")
        start_button.isDisable = !can_game_start || is_game_start
        start_button.layoutX = 362.5
        start_button.layoutY = 290.0
        start_button.prefWidth = 150.0
        start_button.prefHeight= 20.0
        start_button.setOnMouseClicked {
            this.is_game_start = true
            start_button.isDisable = true
            game.startGame()
        }
        return start_button
    }

    fun create_exit_button(): Button{
        val exit_button = Button("Exit Game")
        exit_button.layoutX = 362.5
        exit_button.layoutY = 325.0
        exit_button.prefWidth = 150.0
        exit_button.prefHeight= 20.0
        exit_button.setOnMouseClicked {
            stage.close()
        }
        return exit_button
    }

    fun redraw(){
        this.children.removeAll(this.children)
        val player_board = board(true)
        val ai_board = board(false)
        val player_fleet = fleet()
        val start_button = create_start_button()
        val exit_button = create_exit_button()
        this.children.add(player_board)
        this.children.add(ai_board)
        this.children.add(player_fleet)
        this.children.add(start_button)
        this.children.add(exit_button)

        Ships.forEach {it.draw(this)}
    }

    fun draw_winner(winner: Player){
        this.children.removeAll(this.children)
        val player_board = board(true)
        val ai_board = board(false)
        val player_fleet = final_fleet(winner)
        val start_button = create_start_button()
        val exit_button = create_exit_button()
        this.children.add(player_board)
        this.children.add(ai_board)
        this.children.add(player_fleet)
        this.children.add(start_button)
        this.children.add(exit_button)

        Ships.forEach {it.draw(this)}
    }

    fun board(is_player: Boolean): Group {

        var title: String

        var x: Double
        var y: Double

        var middle_x: Double


        if (is_player){
            title = "My Formation"
            x = 25.0
            y = 50.0
            middle_x = 120.0
        } else {
            title = "Opponent's Formation"
            x = 550.0
            y = 50.0
            middle_x = 610.0
        }

        val group = Group()

        val t = Text(title)
        t.textOrigin = VPos.TOP
        t.translateX = middle_x
//    t.translateX = x + 150.0 - (t.layoutBounds.width / 2.5)
        t.translateY = 0.0

        t.font = Font.font(null, FontWeight.BOLD, 16.0)

        group.children.add(t)

        x_label(x + 12.5, y - 12.5, group) // top
        x_label(x + 12.5, y + 325 - 12.5, group) // bottom
        y_label(x - 12.5, y + 12.5, group) // left
        y_label(x + 325 - 12.5, y + 12.5, group) // right

        if (!is_game_end || !is_player){
            grid(x, y, group) // board
        } else {
            final_grid(x, y, group)
        }

        return group
    }

    fun fleet(): Group {

        val t = Text("My Fleet")

        t.textOrigin = VPos.TOP
        t.translateX = 875.0 / 2.0 - (t.layoutBounds.width / 2.0)
        t.translateY = 0.0
        t.font = Font.font(null, FontWeight.BOLD, 16.0)
        val group = Group()

        group.children.add(t)

        return group
    }

    fun final_fleet(winner: Player): Group {

        val t = when(winner){
            Player.AI -> Text("You were defeated!")
            Player.Human -> Text("You Win!")
        }

        t.textOrigin = VPos.TOP
        t.translateX = 875.0 / 2.0 - (t.layoutBounds.width / 2.0)
        t.translateY = 0.0
        t.font = Font.font(null, FontWeight.BOLD, 16.0)
        val group = Group()

        group.children.add(t)

        return group
    }

    fun grid(x: Double, y: Double, group: Group) {

        val wh = 300.0 // as given
        val s = 30.0 // as given

        var target_Array: Array<Array<CellState>>

        if (x <= 875.0 / 2.0){
            target_Array = Human_attackedCells
        } else {
            target_Array = AI_attackedCells
        }
        var x_value = 0.0

        var x_index = 0
        while (x_value < wh) {
            var y_value = 0.0
            var y_index = 0
            while (y_value < wh) {
                val hgrid = Rectangle(x + x_value, y + y_value, s, s)
                hgrid.stroke = Color.BLACK
                hgrid.strokeWidth = 1.0

                hgrid.fill = when(target_Array[y_index][x_index]){
                    CellState.Ocean -> Color.LIGHTBLUE
                    CellState.Attacked -> Color.LIGHTGRAY
                    CellState.ShipHit -> Color.CORAL
                    CellState.ShipSunk -> Color.DARKGRAY
                }

                group.children.add(hgrid)
                y_index += 1
                y_value += s
            }
            x_index += 1
            x_value += s

        }
    }

    fun final_grid(x: Double, y: Double, group: Group) {

        val wh = 300.0 // as given
        val s = 30.0 // as given

        var target_Array = Human_attackedCells

        var x_value = 0.0

        var x_index = 0
        while (x_value < wh) {
            var y_value = 0.0
            var y_index = 0
            while (y_value < wh) {
                val hgrid = Rectangle(x + x_value, y + y_value, s, s)
                hgrid.stroke = Color.BLACK
                hgrid.strokeWidth = 1.0

                hgrid.fill = when(target_Array[y_index][x_index]){
                    CellState.ShipSunk -> Color.DARKGRAY
                    else -> Color.LIGHTBLUE
                }

                group.children.add(hgrid)
                y_index += 1
                y_value += s
            }
            x_index += 1
            x_value += s

        }
    }
}

fun find_attack_grid(p: Point2D): Cell{
    val left_corner_x = 550.0
    val left_corner_y = 50.0
    val right_corner_x = 850.0
    val right_corner_y = 350.0

    val p_x = p.x
    val p_y = p.y

    var H_index = -1
    var V_index = -1

    if (left_corner_x <= p_x && p_x <= right_corner_x && left_corner_y <= p_y && p_y <= right_corner_y){ // valid
        H_index = floor((p_x - left_corner_x) / 30.0).roundToInt()
        V_index = floor((p_y - left_corner_y) / 30.0).roundToInt()

    }

    return Cell(H_index, V_index)
}

fun adjust_posi(old_p: Point2D, new_index: Pair<Int, Int>, old_matrix: Affine, is_vertical: Boolean, length: Int): Affine{
    val t = Affine()

    var new_p_x = new_index.first * 30.0 + 25.0 + 10.0
    var old_p_x = old_p.x
    if (!is_vertical){
        old_p_x -= (length * 30.0 - 20.0)
    }
    val new_p_y = new_index.second * 30.0 + 50.0 + 10.0
    var old_p_y = old_p.y
    t.appendTranslation(new_p_x - old_p_x, new_p_y - old_p_y)
    t.append(old_matrix)
    return t
}

fun point_to_grid(p: Point2D, is_vertical:Boolean, length: Int, is_player: Boolean): Pair<Int, Int>{
    var left_corner_x = 25.0
    var left_corner_y = 50.0
    var right_corner_x = 325.0
    var right_corner_y = 350.0

    var H_index = -1
    var V_index = -1

    if (!is_player){
        left_corner_x = 550.0
        left_corner_y = 50.0
        right_corner_x = 850.0
        right_corner_y = 350.0
    }

    var p_x = p.x
    var p_y = p.y
    if (!is_vertical){
        p_x -= (length * 30.0 - 20.0)
    }
    if (left_corner_x <= p_x && p_x <= right_corner_x && left_corner_y <= p_y && p_y <= right_corner_y){ // valid
        H_index = floor((p_x - left_corner_x) / 30.0).roundToInt()
        V_index = floor((p_y - left_corner_y) / 30.0).roundToInt()

    }

    val result = Pair<Int, Int>(H_index, V_index)

    return result
}

fun x_label(x: Double, y: Double, group: Group) {
    val s = 30.0 // as given

    var x_value = x

    var count = 1
    while (count <= 10) {
        val text = Text("${count}")
        text.textOrigin = VPos.CENTER
        text.translateX = x_value - text.layoutBounds.width / 2.5
        text.translateY = y
        text.font = Font.font(12.0)
        group.children.add(text)
        x_value += s

        count += 1
    }
}

fun y_label(x: Double, y: Double, group: Group) {
    val s = 30.0 // as given

    var y_value = y
    for (letter in "ABCDEFGHIJ") {
        val text = Text(letter.toString())
        text.textOrigin = VPos.CENTER
        text.translateX = x - text.layoutBounds.width / 2.0
        text.translateY = y_value
        text.font = Font.font(12.0)
        group.children.add(text)
        y_value += s

    }
}
