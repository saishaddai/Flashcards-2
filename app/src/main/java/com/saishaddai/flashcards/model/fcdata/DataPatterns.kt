package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard

val patternsCards = listOf(
    Flashcard(16, 1601, "What is the MVC pattern?", "Model-View-Controller: The Model manages data, the View displays the UI, and the Controller (often Activity/Fragment) handles user input and updates the Model."),
    Flashcard(16, 1602, "What is the MVP pattern?", "Model-View-Presenter: The Presenter acts as an intermediary, retrieving data from the Model and updating the View through an interface, making it more testable than MVC."),
    Flashcard(16, 1603, "What is the MVVM pattern?", "Model-View-ViewModel: The ViewModel exposes data through observables (LiveData/Flow). The View binds to these, allowing for a decoupled, lifecycle-aware architecture."),
    Flashcard(16, 1604, "What is the MVI pattern?", "Model-View-Intent: A reactive pattern where the UI sends Intents (actions), the State is processed immutably, and a single State object is rendered by the View."),
    Flashcard(16, 1605, "What is the main advantage of MVVM over MVP?", "MVVM uses data binding or observables, reducing boilerplate code in the View and better handling Activity recreation via ViewModels."),
    Flashcard(16, 1606, "What is the Singleton pattern?", "Ensures a class has only one instance and provides a global point of access to it (e.g., a database instance or a network client)."),
    Flashcard(16, 1607, "What is the Factory pattern?", "Defines an interface for creating an object, but lets subclasses decide which class to instantiate."),
    Flashcard(16, 1608, "What is the Abstract Factory pattern?", "Provides an interface for creating families of related or dependent objects without specifying their concrete classes."),
    Flashcard(16, 1609, "What is the Builder pattern?", "Separates the construction of a complex object from its representation, allowing the same construction process to create different representations."),
    Flashcard(16, 1610, "What is the Adapter pattern in Android?", "Acts as a bridge between an underlying data source and a UI component like RecyclerView or ListView."),
    Flashcard(16, 1611, "What is the Observer pattern?", "A one-to-many dependency where when one object changes state, all its dependents are notified automatically (e.g., LiveData or RxJava)."),
    Flashcard(16, 1612, "What is the Strategy pattern?", "Defines a family of algorithms, encapsulates each one, and makes them interchangeable at runtime."),
    Flashcard(16, 1613, "What is the Command pattern?", "Encapsulates a request as an object, thereby letting you parameterize clients with different requests (e.g., Runnable or Action)."),
    Flashcard(16, 1614, "What is the Decorator pattern?", "Attaches additional responsibilities to an object dynamically, providing a flexible alternative to sub-classing for extending functionality."),
    Flashcard(16, 1615, "What is the Facade pattern?", "Provides a unified interface to a set of interfaces in a subsystem, making the subsystem easier to use (e.g., a high-level API for a complex library)."),
    Flashcard(16, 1616, "What is the Repository pattern?", "Mediates between the domain and data mapping layers, acting like an in-memory collection of domain objects."),
    Flashcard(16, 1617, "What is the Proxy pattern?", "Provides a surrogate or placeholder for another object to control access to it (e.g., Retrofit's dynamic proxy for network calls)."),
    Flashcard(16, 1618, "What is the Template Method pattern?", "Defines the skeleton of an algorithm in an operation, deferring some steps to subclasses (e.g., Activity lifecycle methods)."),
    Flashcard(16, 1619, "What is the Composite pattern?", "Composes objects into tree structures to represent part-whole hierarchies, allowing clients to treat individual objects and compositions uniformly."),
    Flashcard(16, 1620, "What is the Bridge pattern?", "Decouples an abstraction from its implementation so that the two can vary independently."),
    Flashcard(16, 1621, "What is Unidirectional Data Flow (UDF)?", "A pattern where data flows in one direction (State down, Events up), common in MVI and modern Compose architectures."),
    Flashcard(16, 1622, "What is the Service Locator pattern?", "An anti-pattern where a central registry provides dependencies, often hiding class dependencies and making testing harder."),
    Flashcard(16, 1623, "What is the Dependency Inversion Principle?", "High-level modules should not depend on low-level modules; both should depend on abstractions."),
    Flashcard(16, 1624, "What is the Single Responsibility Principle (SRP)?", "A class should have only one reason to change, meaning it should perform only one job or function."),
    Flashcard(16, 1625, "What is the Open/Closed Principle?", "Software entities (classes, modules, etc.) should be open for extension but closed for modification."),
    Flashcard(16, 1626, "What is the Liskov Substitution Principle?", "Objects of a superclass should be replaceable with objects of its subclasses without breaking the application."),
    Flashcard(16, 1627, "What is the Interface Segregation Principle?", "Clients should not be forced to depend on interfaces they do not use (split large interfaces into smaller ones)."),
    Flashcard(16, 1628, "What is the State pattern?", "Allows an object to alter its behavior when its internal state changes, appearing to change its class."),
    Flashcard(16, 1629, "What is the Flyweight pattern?", "Uses sharing to support large numbers of fine-grained objects efficiently (e.g., String pool or Integer caching)."),
    Flashcard(16, 1630, "What is the Chain of Responsibility pattern?", "Avoids coupling the sender of a request to its receiver by giving more than one object a chance to handle the request.")
)
