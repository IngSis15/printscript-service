package edu.ingsis.printscriptService.external.asset

interface AssetAPI {
    fun getAsset(
        container: String,
        key: String,
    ): String?

    fun createAsset(
        container: String,
        key: String,
        content: String,
    )

    fun deleteAsset(
        container: String,
        key: String,
    )
}
