/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package id.passage.android.model

import id.passage.android.model.ProtocolCredentialAssertionResponse

import com.squareup.moshi.Json

/**
 * 
 *
 * @param handshakeId 
 * @param handshakeResponse 
 * @param magicLink 
 * @param userId 
 */


data class ApimagicLinkLoginWebAuthnFinishRequest (

    @Json(name = "handshake_id")
    val handshakeId: kotlin.String? = null,

    @Json(name = "handshake_response")
    val handshakeResponse: ProtocolCredentialAssertionResponse? = null,

    @Json(name = "magic_link")
    val magicLink: kotlin.String? = null,

    @Json(name = "user_id")
    val userId: kotlin.String? = null

)

