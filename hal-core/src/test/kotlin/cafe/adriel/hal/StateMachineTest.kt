package cafe.adriel.hal

import cafe.adriel.hal.util.FakeStateMachine
import cafe.adriel.hal.util.TestCoroutineScopeRule
import cafe.adriel.hal.util.TurnstileAction
import cafe.adriel.hal.util.TurnstileState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class StateMachineTest {

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private lateinit var stateMachine: FakeStateMachine

    @Before
    fun setup() {
        stateMachine = FakeStateMachine(testScopeRule, testScopeRule.dispatcher) { action, _ ->
            when (action) {
                is TurnstileAction.InsertCoin -> +TurnstileState.Unlocked
                is TurnstileAction.Push -> +TurnstileState.Locked
            }
        }.apply {
            observeState(testScopeRule, testScopeRule.dispatcher) {}
        }
    }

    @Test
    fun `when start HAL then set the initial state`() {
        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Locked
    }

    @Test
    fun `when emit one single action then transition to expected state`() {
        stateMachine += TurnstileAction.InsertCoin

        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Unlocked
    }

    @Test
    fun `when emit multiples actions then transition to expected states`() {
        stateMachine += TurnstileAction.InsertCoin
        stateMachine += TurnstileAction.Push

        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Locked
    }
}
