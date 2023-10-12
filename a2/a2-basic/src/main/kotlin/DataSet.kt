import javafx.scene.control.Label

/*
 * A simple dataset data type
 */
data class DataSet(
    var title: String,
    var xAxis: String,
    var yAxis: String,
    var data: MutableList<Int>
)

/*
 * Create test DataSets
 */
fun createTestDataSet(name: String): DataSet? {

    return when (name) {
        "Increasing" -> DataSet(
            "Increasing from 0 to 100", "Datapoint", "Value",
            (10..100 step 10).toMutableList()
        )

        "Middle" -> DataSet(
            "Middle", "Datapoint", "Value",
            mutableListOf<Int>(20, 30, 50, 99, 50, 30, 20)
        )

        "Large" -> DataSet(
            "Testing 20 Data Points", "Datapoint", "Value",
            (20 downTo 1).toMutableList()
        )

        "Single" -> DataSet(
            "A Single Value", "XAxis", "YAxis",
            mutableListOf<Int>(50)
        )

        "Range" -> DataSet(
            "Range Test", "Test", "Value",
            mutableListOf<Int>(100, 10, 1, 0)
        )

        "Percentage" -> DataSet(
            "Test1", "Proportion", "Category",
            mutableListOf<Int>(5, 10, 25, 25, 35)
        )

        else -> null
    }
}

fun createNewDataSet(n_col: Int): DataSet? {
    val random_result = randomGenerate()
    val random_int = randomCreateInt(n_col)
    return DataSet(random_result.first, random_result.second, random_result.third, random_int)
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

// randomly generate a Triple: first -> title, second -> X, third -> Y
private fun randomGenerate(): Triple<String, String, String>{
    val sample = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    var countWord = 0
    var title = ""
    var x = ""
    var y = ""

    for (letter in sample) { // count how many word is in sample
        if (letter == ' ') {
            countWord += 1
        }
    }

    // Random title
    var randLen = 3 // # of letter for title
    for (i in 1..randLen) {
        title += titleCase(randomWord(sample, countWord)) // randomly choose a word
        if (i != randLen) {
            title += ' '
        }
    }

    randLen = 1 // # of letter for title
    x += titleCase(randomWord(sample, countWord)) // randomly choose a word

    y += titleCase(randomWord(sample, countWord)) // randomly choose a word

    return Triple(title, x, y)
}

fun randomCreateInt(n_col: Int): MutableList<Int>{
    val result: MutableList<Int> = ArrayList<Int>()
    for (i in 1..n_col) {
        result.add((0..100).random())
    }
    return result
}
