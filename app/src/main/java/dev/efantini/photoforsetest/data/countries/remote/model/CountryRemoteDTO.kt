package dev.efantini.photoforsetest.data.countries.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryRemoteDTO(
    @SerialName("iso")
    val iso: Int? = null,
    @SerialName("isoAlpha2")
    val isoAlpha2: String? = null,
    @SerialName("isoAlpha3")
    val isoAlpha3: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("phonePrefix")
    val phonePrefix: String? = null,
)
