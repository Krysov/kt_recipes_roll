package kmm.example.recipesroll.remote

class RecipesApiFactory {
    fun create(type: ApiType = ApiType.LIVE): RecipesApi {
        return when (type) {
            ApiType.LIVE -> RemoteRecipesApi
            ApiType.TEST -> DummyRecipesApi.apply { recipesCount = 32 }
        }
    }

    enum class ApiType {
        LIVE,
        TEST,
    }
}