package kmm.example.recipesroll.utils

import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.TimeUnit


@RunWith(JUnit4::class)
class AnimationPacerUnitTest {

    @Test(timeout = 1000)
    fun `test multiple pacer invocation`() {
        val pacer = AnimationPacer()
        var error: Throwable? = null
        Observable
            .just(mutableListOf<Int>())
            .doOnNext { result ->
                pacer.scheduleUpdate(10) { result.add(1) }
                pacer.scheduleUpdate(5) { result.add(2) }
                pacer.scheduleUpdate(15) { result.add(3) }
            }
            .delay(20, TimeUnit.MILLISECONDS)
            .doOnNext { result ->
                Assert.assertEquals(result.size, 1)
                Assert.assertEquals(result[0], 1)
            }
            .blockingSubscribe({}, { error = it })
        if (error != null) throw error!!
    }

    @Before
    fun setUpRxSchedulers() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.single() }
    }

}