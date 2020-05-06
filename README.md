# README
Adopted and modified from : https://github.com/excitement-engineer/ktor-graphql

## About
This is a graphql backend for a small social networking app.

## Data model
Can be found in `schema.graphql`

## Execution strategies
For simplicity, we are using singletons to simulate our data access layers. 
In the real world, usage of dataloaders is recommended. Read more about that [here](https://www.graphql-java.com/documentation/v12/batching/)

## Technical
Although this example uses Ktor, any Kotlin server may be used.

## Schema first vs code first
This example is schema first, but there are also possibilities to write code-first resolvers 
(aka schema is defined by the code). Examples of code-first can be found [here](https://github.com/ExpediaDotCom/graphql-kotlin)


