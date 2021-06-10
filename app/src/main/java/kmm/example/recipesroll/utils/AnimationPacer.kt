package kmm.example.recipesroll.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


open class AnimationPacer {
    var disposable: Disposable? = null
    var latestScheduleStartTime: Long? = null

    open fun scheduleUpdate(
        afterTimeDelayMillis: Long,
        callback: (passedTimeMillis: Long) -> Unit,
    ) {
        disposable?.apply { dispose() }


        val startTime = if (latestScheduleStartTime != null) latestScheduleStartTime!! else now()
        latestScheduleStartTime = startTime


        disposable = Observable
            .timer(afterTimeDelayMillis, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                latestScheduleStartTime = null
                callback(now() - startTime)
            }, { Timber.e(it) })
    }

    private fun now() = System.currentTimeMillis()
}


abstract class AnimationProgression<InterpolatedValue>(private val animationDurationMillis: Long) {
    private var runtimeMillis = 0.0f

    val progress: Float
        get() = 1.0f.coerceAtMost(runtimeMillis / animationDurationMillis)

    val interpolation: InterpolatedValue
        get() = onComputeAnimatedValueInterpolation(progress)

    fun applyAndCheckCompletion(timeDeltaMillis: Long): Boolean {
        runtimeMillis += timeDeltaMillis
        return isCompleted()
    }

    private fun isCompleted(): Boolean = runtimeMillis >= animationDurationMillis

    abstract fun onComputeAnimatedValueInterpolation(progress: Float): InterpolatedValue
}
