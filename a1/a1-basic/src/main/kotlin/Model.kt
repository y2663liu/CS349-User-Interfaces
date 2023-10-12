import java.awt.DisplayMode
import kotlin.math.max
import kotlin.math.min

class Model {
    //region View Management

    // all views of this model
    private val views: ArrayList<IView> = ArrayList()

    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    fun addView(view: IView) {
        views.add(view)
        view.updateView("Ini", newid, counter, searchMsg)
    }

    // the model uses this method to notify all of the Views that the data has changed
    // Extra field are used for extra information when needed
    private fun notifyObservers(reason:String, id:Int, counter:Int, searchMsg:String) {
        for (view in views) {
            view.updateView(reason, id, counter, searchMsg)
        }
    }

    // A counter for # of total notes
    var counter = 0
        private set

    var newid = 0 // id for new notes

    // used for search text field (notify IView when changed)
    var searchMsg = ""
        set(value) {
            field = value.toString()
            notifyObservers("Search", newid, counter, searchMsg)
        }

    // used for select/deselect/delete/edit, record last selected note
    var curSelectid = -1

    // flag for toggle-button
    var isShow = false

    // Add button is pressed (may not cancelled so does not increase counter)
    fun incrementCounter() {
        notifyObservers("Add", newid, counter, searchMsg)
    }

    // Add button is pressed (isSuccess: whether save or cancel)
    // increase counter & newid when save
    fun finishAdd(isSuccess: Boolean){
        if (isSuccess == true){
            println("Model: increment counter to ${max(0,counter + 1)}")
            counter++
            notifyObservers("finishAddSuccess", newid, counter, searchMsg)
            newid++
        } else {
            notifyObservers("finishAddFail", newid, counter, searchMsg)
        }
    }

    // called when a page is edited
    fun finishEdit(){
        notifyObservers("finishEdit", curSelectid, counter, searchMsg)
    }

    // called when a page is created using random
    // increase counter & newid by 1
    fun randomIncrementCounter() {
        println("Model: randomly increment counter to ${counter + 1}")
        counter++
        notifyObservers("Random", newid, counter, searchMsg)
        newid++
    }


    fun cleanCounter() {
        println("Model: clean counter to 0")
        notifyObservers("Clean", curSelectid, counter, searchMsg) // if we delete the one we select, disable delete
    }

    fun decrementCounter() {
        println("Model: decrement counter to ${max(0,counter - 1)}")
        counter = max(counter - 1, 0)
        notifyObservers("Delete", curSelectid, counter, searchMsg)
    }

    fun showOnlyMarked() {
        println("Only show marked notes")
        if (isShow == false){
            notifyObservers("Show", newid, counter, searchMsg)
            isShow = true
        } else {
            notifyObservers("FinishShow", newid, counter, searchMsg)
            isShow = false
        }
    }

    fun selectNote(id: Int){
        if (id != curSelectid){
            println("Select Note ${id}")
            curSelectid = id
            notifyObservers("Select", curSelectid, counter, searchMsg)
        } else { // select the same one twice
            println("Deselect Note ${id}")
            notifyObservers("Deselect", curSelectid, counter, searchMsg)
            curSelectid = -1
        }
    }

    fun editNote(id: Int){
        println("Edit Note ${id}")
        curSelectid = id
        notifyObservers("Edit", curSelectid, counter, searchMsg)
    }

    fun disableDelete(){
        println("specially case: clean select")
        notifyObservers("DisableDelete", curSelectid, counter, searchMsg)
        curSelectid = -1
    }

    // used by clear when filter
    fun decreaseCounter(amount: Int){
        counter -= amount
    }

}