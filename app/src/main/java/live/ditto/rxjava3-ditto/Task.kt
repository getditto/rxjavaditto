package live.ditto.rxjava3-ditto

import java.util.UUID
import live.ditto.DittoDocument

data class Task(
    var _id: String = UUID.randomUUID().toString(),
    var body: String,
    var isCompleted: Boolean
) {
    constructor(document: DittoDocument) :
            this(
                document["_id"].stringValue,
                document["body"].stringValue,
                document["isCompleted"].booleanValue
            )
}
