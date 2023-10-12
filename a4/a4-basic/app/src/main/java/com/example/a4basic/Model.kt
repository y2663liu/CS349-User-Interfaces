package com.example.a4basic

import androidx.lifecycle.MutableLiveData

class Model {

    val allList = mutableListOf<Note>()

    val noteList = mutableListOf<Note>()

    var msg = ""

    var action = ""

    var current_title = ""
    var current_body = ""
    var current_important = false
    var current_id = -1

    var onlyImportant = false
    var SearchMsg = ""

    fun deleteNoteWithID(deleteID: Int){
        for (n in allList){
            if (n.id == deleteID){
                allList.remove(n)
                break
            }
        }
        updateNoteList()
        action = "Deleted Note #$deleteID"
    }

    fun addNote(newNote: Note){
        allList.add(newNote)
        updateNoteList()
        action = "Added Note #${newNote.id}"
    }

    fun editNoteWithID(newtitle: String, newbody:String, newImportant: Boolean, editID: Int){
        for (n in allList){
            if (n.id == editID){
                n.title = newtitle
                n.body = newbody
                n.important = newImportant
                break
            }
        }
        current_title = newtitle
        current_body = newbody
        current_important = newImportant
        updateNoteList()
        action = "Edited Note #$editID"
    }

    fun setCurrentInfo(newtitle: String, newbody:String, newImportant: Boolean, currentID: Int){
        current_title = newtitle
        current_body = newbody
        current_important = newImportant
        current_id = currentID
    }

    fun updateClick(isClicked: Boolean){
        onlyImportant = isClicked
        updateNoteList()
        action = ""
    }

    fun updateSearch(msg: String){
        SearchMsg = msg
        updateNoteList()
        action = ""
    }

    fun clearNoteList(){
        val num = noteList.size
        allList.removeAll(noteList)
        updateNoteList()
        action = "Cleared $num Notes"
    }

    fun updateNoteList(){
        noteList.clear()
        for (n in allList){
            if (onlyImportant && SearchMsg != ""){
                if (n.important && ((SearchMsg in n.title) or (SearchMsg in n.body))){
                    noteList.add(n)
                }
            } else if (onlyImportant){
                if (n.important){
                    noteList.add(n)
                }
            } else if (SearchMsg != ""){
                if ((SearchMsg in n.title) or (SearchMsg in n.body)){
                    noteList.add(n)
                }
            } else {
                noteList.add(n)
            }
        }

        val M = noteList.size
        val N = allList.size
        if (onlyImportant || SearchMsg != ""){ // filter is applied
            msg = "matching $M of $N notes"
        } else {
            msg = "$N notes"
        }
    }
}