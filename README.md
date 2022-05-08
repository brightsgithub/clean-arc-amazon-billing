Clean Architecture Amazon IAB API Example
------------------------------------------------------------

Libraries used
------------------
* **amazon-appstore-sdk** - So we can communicate with the Amazon API for billing
* Koin - for dependency injection


Ability to Consume In App Products as Consumables
-------------------------------------------------

This is a simple application that accesses an API to display:

1) A User's CV with summary information.
2) A list of previous work experiences.
3) When a previous experience has been clicked on, the detail view is shown.
4) **Offline mode supported**

Architecture
------------------
* The Presentation Layer is an MVVM implementation.

* The **Application architecture** is 'Clean Architecture'
I have decided to go with modules to enforce a separation of concerns using the Clean Architecture approach to enforce
'The Dependency Rule' where dependencies should point inwards. **PresentationLayer -> DomainLayer <- Datalayer**

By utilizing modules, each module is its own independent unit, and each module has its own:



Presentation Layer
------------------

**View** -  The View does nothing but simply render the view to the user and is made as dumb as possible with little logic

**ViewModel** - is responsible for fetching data as per the user's request, formatting the data and maintaining state for configuration changes.


Domain Layer
------------------

Is a pure Kotlin implementation, and its responsibility is to perform business logic. Its repository interfaces are implemented by the data layer.
Once business logic has been performed, the response is sent back up the chain to the Presentation layer



Data Layer
------------------
This is simply used to obtain and listen for data changes inside and outside of the app. This currently where **Amazon Billing** classes are implemented
The **BillingRepository** is kept s generic as possible, where its rules should apply to any billing implementation. The detail specifics are within the DataSources that are vendor specific.


------------------


(to test in Android studio, be sure to select the **mockServerDebug** variant for all modules)




![alt tag](https://github.com/brightsgithub/clean-arc-amazon-billing/blob/master/screen_shot_1.png)
![alt tag](https://github.com/brightsgithub/clean-arc-amazon-billing/blob/master/screen_shot_2.png)