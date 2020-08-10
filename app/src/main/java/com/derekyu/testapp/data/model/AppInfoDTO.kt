package com.derekyu.testapp.data.model

data class AppInfoDTO(
    val appArtist: String,
    val appID: String,
    val category: String,
    val name: String,
    val summary: String,
    val artworkUrl: String?,
    val rating: Float?,
    val ratingCount: Int?
) {
    constructor(appInfo: AppInfo, appLookupResult: AppLookupResult) :
            this(
                appInfo.appArtist.label,
                appInfo.appID.attributes.id,
                appInfo.category.attributes.label,
                appInfo.name.label,
                appInfo.summary.label,
                appInfo.image.maxBy { it.attributes.height }?.label,
                appLookupResult.averageUserRating,
                appLookupResult.userRatingCount
            )

    constructor(appInfo: AppInfo) :
            this(
                appInfo.appArtist.label,
                appInfo.appID.attributes.id,
                appInfo.category.attributes.label,
                appInfo.name.label,
                appInfo.summary.label,
                appInfo.image.maxBy { it.attributes.height }?.label,
                null,
                null
            )

    val hasRating: Boolean
        get() = rating != null && ratingCount != null

    fun matchQuery(query: String) =
        appArtist.contains(query, true) or
                category.contains(query, true) or
                name.contains(query, true) or
                summary.contains(query, true)
}