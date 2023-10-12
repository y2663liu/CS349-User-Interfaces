import com.sun.javafx.scene.text.TextLayout
import com.sun.source.tree.ImportTree
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextArea
import javafx.scene.layout.*
import javafx.scene.paint.Color.LIGHTYELLOW
import javafx.scene.text.Text
import java.awt.Color
import java.awt.TextField
import kotlin.random.Random

internal class Note(
    isRandom: Boolean,
    idNumber:Int,
    newTitle: Label,
    newBody: Label,
    newIsImportant: Int,
    model: Model
    ) : VBox(){

    var title = Label("")

    var body = Label("")

    // key Info
    var isImportant = -1 // -1: create fail, 1: important, 0: normal
    var ismatch = false // flag for search
    var id = -1 // id for the note

    init {
        // set label properties
        id = idNumber

        if (isRandom == false) { // create a note that is not marked
            title = newTitle
            body = newBody
            isImportant = newIsImportant
        } else { // create a random note
            val titleBodyPair = randomGenerate()
            title = titleBodyPair.first
            body = titleBodyPair.second
            isImportant = titleBodyPair.third
        }

        // setup
        title.isWrapText = false
        body.isWrapText = true

        this.setPrefSize(150.0, 200.0)
        this.setMaxSize(150.0, 200.0)

        title.padding = Insets(10.0, 10.0, 10.0, 10.0)
        body.padding = Insets(0.0, 10.0, 10.0, 10.0)

        title.setMinSize(150.0, 200.0/12 + 20.0)
        title.setMaxSize(150.0, 200.0/12 + 20.0)
        body.setMinSize(150.0, 200.0 - 200.0/12 - 20.0)
        body.setMaxSize(150.0, 200.0 - 200.0/12 - 20.0)

        setColor(isImportant) // true: yellow, false: white

        title.alignment = Pos.TOP_LEFT
        body.alignment = Pos.TOP_LEFT

        children.add(title)
        children.add(body)

        // mouse click
        this.setOnMouseClicked {
            if (it.clickCount == 2){ // edit
                model.editNote(id)
            } else if (it.clickCount == 1){ // select
                model.selectNote(id)
            }
        }

    }

    // helper function: make a word into title case
    private fun titleCase(input: String): String {
        return input.take(1).uppercase() + input.drop(1).lowercase()
    }

    // helper function: randomly select a word from msg
    private fun randomWord(msg:String, wordNumber: Int): String{
        var result = msg.split(' ')[(0..wordNumber).random()]
        val lastIndex = result.length - 1
        if (result[lastIndex] == '.' || result[lastIndex] == ','){
            result =  result.substring(0,lastIndex) // get rid of . and , at the End
        }
        return result
    }

    // randomly generate a Triple: first -> title, second -> body, third -> isImportant
    private fun randomGenerate(): Triple<Label, Label, Int>{
        val rand = (0..4).random()
        var isImportant = -1
        if (rand == 0){
            isImportant = 1 // 1 -> important
        } else {
            isImportant = 0 // 0 -> not important
        }

        val sample = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        var countWord = 0
        var title = ""

        for (letter in sample) { // count how many word is in sample
            if (letter == ' ') {
                countWord += 1
            }
        }

        // Random title
        val randTitle = (1..3).random() // # of letter for title
        for (i in 1..randTitle) {
            title += titleCase(randomWord(sample, countWord)) // randomly choose a word
            if (i != randTitle) {
                title += ' '
            }
        }

        val randSentence = (2..5).random() // # of sentences for body
        var body = ""
        for (i in 1..randSentence){
            var sentence = ""
            if (i != 1){
                sentence += ' ' // extra space
            }

            var randWord = (3..10).random() // # of words in each sentence
            for (j in 1..randWord){
                var tempWord = randomWord(sample, countWord) // randomly choose a word
                if (j == 1){
                    tempWord = titleCase(tempWord) // titleCase
                } else if (j == randWord) {
                    tempWord = ' ' + tempWord.lowercase() + '.' // period
                } else {
                    tempWord = ' ' + tempWord.lowercase() // lower case
                }
                sentence += tempWord
            }
            body += sentence
        }

        return Triple(Label(title), Label(body), isImportant)
    }

    // helper function: set color for label depend on importance
    fun setColor(important: Int){
        if (important == 1){
            title.background = Background(BackgroundFill(javafx.scene.paint.Color.LIGHTYELLOW, null, null))
            body.background = Background(BackgroundFill(javafx.scene.paint.Color.LIGHTYELLOW, null, null))
        } else if (important == 0){
            title.background = Background(BackgroundFill(javafx.scene.paint.Color.WHITE, null, null))
            body.background = Background(BackgroundFill(javafx.scene.paint.Color.WHITE, null, null))
        }

    }

    // mutate this.ismatch: true -> contain the string in the search field
    fun checkMatch(searchMsg: String){
        val lsearch = searchMsg.lowercase()
        val titleText = title.text.lowercase()
        val bodyText = body.text.lowercase()

        ismatch = (lsearch in titleText) || (lsearch in bodyText)
    }
}