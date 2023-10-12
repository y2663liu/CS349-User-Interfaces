package cs349.a3battleship.model

import cs349.a3battleship.model.ships.ShipType
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.scene.transform.Affine
import javafx.scene.transform.MatrixType
import java.security.cert.X509CRL

class DisplayShip(val length: Int, x: Double, y: Double, val shipType: ShipType) {
    var cell = Cell(-1, -1) // initialize with -1, -1
    var rect = Rectangle()

    var t_matrix = Affine()

    var is_vertical = true
    var can_move = true
    var is_selected = false

    val originX: Double
    val originY: Double

    var last_cursorX = 0.0
    var last_cursorY = 0.0

    val width = 10.0
    val height: Double
    // x & y are the coordinates of the top left corner
    init {
        rect.isDisable = false

        originX = x + 10.0
        originY = y + 10.0

        rect = Rectangle(originX, originY, 10.0, 30.0 * length - 20.0)
        height = 30.0 * length - 20.0
    }

    fun draw(pane: Pane){
        rect.transforms.removeAll(rect.transforms)
        rect.transforms.add(t_matrix)
        pane.children.add(rect)
    }

    fun rotate_ship(cursorX: Double, cursorY: Double){
        if (this.is_selected == true){
            if (this.is_vertical){

                val new_matrix = createTranslationMatrix(cursorX, cursorY)
                new_matrix.appendRotation(90.0)
                new_matrix.appendTranslation(-cursorX, -cursorY)
                new_matrix.append(t_matrix)
                t_matrix = new_matrix

                this.is_vertical = false
            } else {

                val new_matrix = createTranslationMatrix(cursorX, cursorY)
                new_matrix.appendRotation(-90.0)
                new_matrix.appendTranslation(-cursorX, -cursorY)
                new_matrix.append(t_matrix)
                t_matrix = new_matrix

                this.is_vertical = true
            }
        }
    }


    fun check_clicked(x: Double, y: Double): Boolean {
        val transform_p = t_matrix.createInverse().transform(Point2D(x, y))

        if (rect.contains(transform_p) && this.can_move){
            this.is_selected = true
            last_cursorX = x
            last_cursorY = y
            return true
        }

        return false
    }

    fun check_move(cursorX: Double, cursorY: Double){
        if (this.is_selected){

            val new_matrix = createTranslationMatrix(cursorX - last_cursorX, cursorY - last_cursorY)
            new_matrix.append(t_matrix)
            t_matrix = new_matrix
//            t_matrix.appendTranslation(cursorX - last_cursorX, cursorY - last_cursorY)

            this.last_cursorX = cursorX
            this.last_cursorY = cursorY
        }
    }

    fun is_valid_cell(): Boolean{
        return !((this.cell.x == -1) || (this.cell.y == -1))
    }

}

fun createTranslationMatrix(x:Double, y:Double): Affine{
    val t = Affine()
    t.appendTranslation(x, y)
    return t
}
