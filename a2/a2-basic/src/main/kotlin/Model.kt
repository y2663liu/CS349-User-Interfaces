import kotlin.math.min

class Model {

    //region View Management

    // all views of this model
    private val views: ArrayList<IView> = ArrayList()

    private val current_info: update_info

    private val dataset_map = mutableMapOf<String, DataSet?>()
    private var current_dataset:String

    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    fun addView(view: IView) {
        views.add(view)
        view.updateView(current_info)
    }

    // the model uses this method to notify all of the Views that the data has changed
    // the expectation is that the Views will refresh themselves to display new data when appropriate
    private fun notifyObservers() {
        for (view in views) {
            view.updateView(current_info)
        }
    }

    fun edit_title(s: String) {
        current_info.dataset!!.title = s
        dataset_map.get(current_dataset)!!.title = s
        notifyObservers()
    }

    fun edit_x(s: String) {
        current_info.dataset!!.xAxis = s
        dataset_map.get(current_dataset)!!.xAxis = s
        notifyObservers()
    }

    fun edit_y(s: String) {
        current_info.dataset!!.yAxis = s
        dataset_map.get(current_dataset)!!.yAxis = s
        notifyObservers()
    }

    fun update_column(index: Int, new_value: Int) {
        current_info.dataset!!.data.set(index, new_value)
        dataset_map.get(current_dataset)!!.data.set(index, new_value)
        notifyObservers()
    }

    fun switch_dataset(s: String) {
        current_dataset = s
        current_info.change_dataset(current_dataset, dataset_map.get(current_dataset))
        notifyObservers()
    }

    fun create_new_dataset(name: String, n_col: Int) {
        var new_dataset = createNewDataSet(n_col)
        dataset_map.put(name, new_dataset)
        current_info.dataset_amount += 1
        switch_dataset(name)
    }

    init {
        var new_dataset = createTestDataSet("Increasing")
        dataset_map.put("Increasing", new_dataset)

        current_dataset = "Increasing"
        current_info = update_info("Increasing", new_dataset)

        new_dataset = createTestDataSet("Large")
        dataset_map.put("Large", new_dataset)

        new_dataset = createTestDataSet("Middle")
        dataset_map.put("Middle", new_dataset)

        new_dataset = createTestDataSet("Single")
        dataset_map.put("Single", new_dataset)

        new_dataset = createTestDataSet("Range")
        dataset_map.put("Range", new_dataset)

        new_dataset = createTestDataSet("Percentage")
        dataset_map.put("Percentage", new_dataset)

        current_info.dataset_amount = 6
    }
}

// pass to the views to tell the updated infomation
class update_info(
    var data_label:String,
    var dataset: DataSet?
){
    var dataset_amount: Int

    init {
        dataset_amount = -1
    }

    fun change_dataset(dataset_s: String, d: DataSet?){
        data_label = dataset_s
        dataset = d
    }
}
