package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard

val coroutinesCards = listOf(
    Flashcard(17, 1701, "What is a Coroutine?", "A light-weight thread that can be suspended and resumed later without blocking the thread it is running on."),
    Flashcard(17, 1702, "What does 'suspend' keyword do?", "It marks a function that can pause the execution of a coroutine without blocking the current thread."),
    Flashcard(17, 1703, "What is a CoroutineScope?", "It defines a lifecycle for coroutines, ensuring that all coroutines launched within it are cancelled when the scope is destroyed."),
    Flashcard(17, 1704, "What is Dispatchers.Main used for?", "It is used for executing coroutines on the main thread, primarily for UI updates."),
    Flashcard(17, 1705, "What is Dispatchers.IO used for?", "It is optimized for offloading blocking I/O tasks, such as networking or disk operations, to a shared pool of threads."),
    Flashcard(17, 1706, "What is Dispatchers.Default used for?", "It is optimized for CPU-intensive work, such as sorting large lists or parsing complex JSON."),
    Flashcard(17, 1707, "What is the purpose of 'launch'?", "It starts a new coroutine that does not return a result to the caller (fire-and-forget)."),
    Flashcard(17, 1708, "What is the purpose of 'async'?", "It starts a new coroutine that returns a 'Deferred' result, which can be awaited using .await()."),
    Flashcard(17, 1709, "What is a Job in Coroutines?", "A handle to a coroutine that can be used to control its lifecycle, such as cancelling it or checking its status."),
    Flashcard(17, 1710, "What is Structured Concurrency?", "A paradigm where child coroutines are tied to the lifecycle of their parent scope, preventing leaks and ensuring error propagation."),
    Flashcard(17, 1711, "What does withContext() do?", "It switches the execution context (Dispatcher) of a coroutine and returns the result, suspending until the block completes."),
    Flashcard(17, 1712, "What is a CoroutineContext?", "A set of elements that define the behavior of a coroutine, including its Job, Dispatcher, and name."),
    Flashcard(17, 1713, "How do you handle exceptions in 'launch'?", "By using a Try-Catch block inside the coroutine or providing a CoroutineExceptionHandler in the context."),
    Flashcard(17, 1714, "How do you handle exceptions in 'async'?", "Exceptions are encapsulated in the Deferred result and thrown when .await() is called."),
    Flashcard(17, 1715, "What is SupervisorJob?", "A type of Job where the failure of one child does not result in the cancellation of its parent or other children."),
    Flashcard(17, 1716, "What is the difference between CoroutineScope and supervisorScope?", "supervisorScope creates a new scope with a SupervisorJob, preventing failure propagation to siblings."),
    Flashcard(17, 1717, "What is a Flow in Kotlin?", "A cold asynchronous data stream that emits multiple values sequentially over time."),
    Flashcard(17, 1718, "What is a Cold Stream vs Hot Stream?", "A Cold stream (Flow) only starts emitting when collected; a Hot stream (SharedFlow/StateFlow) emits regardless of collectors."),
    Flashcard(17, 1719, "What is StateFlow?", "A hot observable flow that represents a state, holding a single value and emitting updates to collectors."),
    Flashcard(17, 1720, "What is SharedFlow?", "A hot observable flow that can have multiple collectors and emit multiple values (events) over time."),
    Flashcard(17, 1721, "What does 'yield()' do in a coroutine?", "It yields the thread to other coroutines, allowing them to run, often used to check for cancellation in long-running loops."),
    Flashcard(17, 1722, "What is 'delay()'?", "A suspending function that pauses the coroutine for a specific time without blocking the thread."),
    Flashcard(17, 1723, "What is 'runBlocking'?", "A bridge that starts a new coroutine and blocks the current thread until it completes; should be used sparingly, mostly in tests or main functions."),
    Flashcard(17, 1724, "What is 'cancelAndJoin()'?", "A function that cancels a Job and suspends until the job's cancellation process is fully completed."),
    Flashcard(17, 1725, "Best Practice: Injecting Dispatchers?", "Always inject Dispatchers into classes to make code more testable by allowing you to swap them for TestDispatchers."),
    Flashcard(17, 1726, "Best Practice: GlobalScope?", "Avoid using GlobalScope as it creates coroutines that live as long as the app process, leading to potential leaks."),
    Flashcard(17, 1727, "What is a Channel?", "A synchronization primitive that allows communication between coroutines via a stream of values."),
    Flashcard(17, 1728, "What is 'viewModelScope'?", "A built-in CoroutineScope provided by Android that is automatically cancelled when the ViewModel is cleared."),
    Flashcard(17, 1729, "What is 'lifecycleScope'?", "A built-in CoroutineScope tied to an Activity or Fragment's lifecycle, automatically cancelled when the lifecycle is destroyed."),
    Flashcard(17, 1730, "What is 'flowOn()'?", "An operator used to change the context (Dispatcher) in which the preceding Flow operators are executed.")
)
