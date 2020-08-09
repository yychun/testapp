package com.derekyu.testapp.data.model

data class AppInfoDTO(
    private val _appID: AppID,
    private val _name: AppName,
    private val _category: AppCategory,
    val artworkUrl: String?,
    val rating: Float?,
    val ratingCount: Int?
) {
    constructor(appInfo: AppInfo, appLookupResult: AppLookupResult) :
            this(
                appInfo.appID,
                appInfo.name,
                appInfo.category,
                appInfo.image.maxBy { it.attributes.height }?.label,
                appLookupResult.averageUserRating,
                appLookupResult.userRatingCount
            )

    constructor(appInfo: AppInfo) :
            this(
                appInfo.appID,
                appInfo.name,
                appInfo.category,
                appInfo.image.maxBy { it.attributes.height }?.label,
                null,
                null
            )

    val appID: String
        get() = _appID.attributes.id
    val name: String
        get() = _name.label
    val category: String
        get() = _category.attributes.label
    val hasRating: Boolean
        get() = rating != null && ratingCount != null
}