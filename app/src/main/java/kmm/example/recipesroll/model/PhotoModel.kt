package kmm.example.recipesroll.model

data class PhotoModel(val file: File) {
    data class File(val url: String)
}