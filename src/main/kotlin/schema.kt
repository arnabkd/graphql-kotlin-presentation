import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import com.google.common.io.Resources

val schema = SchemaParser
        .newParser()
        .schemaString(Resources.toString(Resources.getResource("schema.graphql"), Charsets.UTF_8))
        .resolvers(Query(), Mutation())
        .build()
        .makeExecutableSchema()

private class Query: GraphQLQueryResolver {
    fun allPeople() = PeopleRepository.all()
    fun findById(id: Int) = PeopleRepository.findById(id)
}

private class Mutation: GraphQLMutationResolver {
    fun addFriend(firstFriendId: Int, secondFriendId: Int): Boolean {
        PeopleRepository.addFriendConnection(firstFriendId, secondFriendId)
        return true
    }
}