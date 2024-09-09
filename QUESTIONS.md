# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
Yes, of course I do refactoring. Below is why,
The Store entity class extends from the PanacheEntity, which means it inherits all the active record style methods. However, it doesn't seem to have a corresponding repository class for handling database operations. 
It might be beneficial to have a separate repository (ex: StoreRepository) to handle complex database operations similar to the WarehouseRepository and ProductRepository we have.

I suggest to follow the pattern implemented in the WarehouseRepository because it uses Panache methods for basic CRUD operations which reduce boilerplate codes and implementing WarehouseStore further helps separate concerns by keeping business logic separate from raw database access. 
This separation allows easier testing and flexibility, as the business logic can be reused or mocked in service layers without needing direct database interaction.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
To begin with, using OpenAPI for API design definitely brings several advantages to the business. One of the most important reasons behind this is Standardization. It ensures that APIs are designed in a consistent manner , provides automatic generation of documentation and design first approach. Moreover, It enables design API before writing any code which leads to better collaboration and further refine the business use case to identify potential issues early in the development.On the other hand, using OpenAPI adds some learning curve to the developers, overhead to the business. Sometimes you need to provide custom implementation to support your business needs.

Coding directly gives you more flexibility and simplicity which allow you to write it anyway that suits your need and coding style. Disadvantages are like No standardization enforced, No automatic documentation are generated resulting you need to write more boilerplate codes.

In Terms of choice, it depends on the specific needs of the project. However I value standardization, design first approach and more importantly collaboration with the team. So I go with OpenAPI specification for designing APIs.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
Testing is essential for ensuring software quality, However, due to time and resource constraints, it's  important to prioritize and focus on the most critical parts of the application. Here's the test strategy for bringing the right balance to the product you build.
Start with unit tests as they are the foundation of your test suite. Easy to write and you can catch issues early. More focus on testing the business logic and repositories. 
Next write an integration test to make sure different components of your system are functioning properly. 
If time permits, you can focus on end to end tests to simulate real user scenarios and will help you to find some bugs which you had missed with Unit and Integration tests. However writing end to end tests can be time consuming which comes under the cost.
```