package cafe.adriel.hal.observer

import cafe.adriel.hal.HAL
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FlowStateObserver<S : HAL.State>(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val onStateChanged: suspend (S) -> Unit
) : HAL.StateObserver<S> {

    override fun observe(stateFlow: Flow<S>) {
        stateFlow
            .onEach(onStateChanged)
            .flowOn(dispatcher)
            .launchIn(scope)
    }
}
