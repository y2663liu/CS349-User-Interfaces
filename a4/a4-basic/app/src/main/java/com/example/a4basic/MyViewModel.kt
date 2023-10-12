package com.example.a4basic

import androidx.lifecycle.*

class MyViewModel() : ViewModel() {

    val model = Model()

    // add all observable properties here
    val notes: MutableLiveData<MutableList<Note>> = MutableLiveData<MutableList<Note>>(model.noteList)
    val canClear: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val subtitle: MutableLiveData<String> = MutableLiveData<String>("")
    val actionMsg: MutableLiveData<String> = MutableLiveData<String>("")

    val current_title: MutableLiveData<String> = MutableLiveData<String>()
    val current_body: MutableLiveData<String> = MutableLiveData<String>()
    val current_important: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val current_id: MutableLiveData<Int> = MutableLiveData<Int>(-1)

    var idNumber = 0

    init {
    }

    // add modelview update functions here

    fun updateVM(){
        notes.value = model.noteList
        canClear.value = !(model.noteList.isEmpty())
        subtitle.value = model.msg
        if (model.action != ""){
            actionMsg.value = model.action
        }
    }

    fun randomNote(){
        val n = createRandomNote()
        idNumber += 1
        model.addNote(n)
        updateVM()
    }

    fun addNote(t: String, b: String, isImportant: Boolean){
        val n = Note(idNumber, t, b, isImportant)
        idNumber += 1
        model.addNote(n)
        updateVM()
    }

    fun clearAll(){
        model.clearNoteList()
        updateVM()
    }

    fun deleteNote(id: Int){
        model.deleteNoteWithID(id)
        updateVM()
    }

    fun editNote(t: String, b: String, isImportant: Boolean, id: Int){
        model.editNoteWithID(t, b, isImportant, id)

        current_title.value = model.current_title
        current_body.value = model.current_body
        current_important.value = model.current_important

        updateVM()
    }

    fun notifySwitch(isClicked: Boolean){
        model.updateClick(isClicked)

        updateVM()
    }

    fun notifySearch(target: String){
        model.updateSearch(target)

        updateVM()
    }

    fun setCurrentNote(t: String, b: String, isImportant: Boolean, id: Int){
        model.setCurrentInfo(t, b, isImportant, id)

        current_title.value = model.current_title
        current_body.value = model.current_body
        current_important.value = model.current_important
        current_id.value = model.current_id
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

    // randomly generate a Note
    private fun createRandomNote(): Note{
        val rand = (0..4).random()
        var isImportant = false
        if (rand == 0){
            isImportant = true // important
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

        return Note(idNumber, title, body, isImportant)
    }

}