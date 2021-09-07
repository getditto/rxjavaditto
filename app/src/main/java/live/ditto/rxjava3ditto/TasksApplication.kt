package live.ditto.rxjava3ditto

import android.app.Application
import android.content.Context
import live.ditto.Ditto
import live.ditto.android.DefaultAndroidDittoDependencies

object DittoManager {

    lateinit var ditto: Ditto

    fun bootstrapDitto(context: Context) {
        val androidDependencies = DefaultAndroidDittoDependencies(context)
        this.ditto = Ditto(androidDependencies);
    }
}

class TasksApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DittoManager.bootstrapDitto(this);
    }

}
