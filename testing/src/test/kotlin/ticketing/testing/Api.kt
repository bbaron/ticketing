package ticketing.testing

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

data class CurrentUser(val id: String, val email: String, val iat: Date)
data class CurrentUserResponse(val currentUser: CurrentUser?)
data class TicketResponse(val id: String, val title: String, val price: Int, val reserved: Boolean)
data class TicketsResponse(val tickets: List<TicketResponse>)
data class TicketRequest(val title: String, val price: Int)

data class UserRequest(val email: String, val password: String)
data class UserResponse(val jwt: String, val currentUser: CurrentUser)

data class OrderRequest(val ticketId: String)
data class OrderResponse(
        val id: String,
        val version: Long,
        val status: String,
        val userId: String,
        val expiresAt: String,
        val ticket: TicketResponse
) {
    data class TicketResponse(
            val id: String,
            val title: String,
            val price: Int
    )
}

data class OAuthTokenResponse(
        val access_token: String,
        val currentUser: CurrentUser,
        val expires_in: Long,
        val jti: String,
        val refresh_token: String,
        val scope: String,
        val token_type: String
)

data class OAuthCheckTokenResponse(
        val active: Boolean,
        val authorities: List<String>,
        val client_id: String,
        val currentUser: CurrentUser,
        val exp: Long,
        val jti: String,
        val scope: List<String>,
        val user_name: String
)

data class InvalidGrantResponse(
        val error: String,
        val error_description: String
)

data class OrdersResponse(val orders: List<OrderResponse>)
data class PaymentRequest(
        val token: String,
        val orderId: String
)

data class PaymentResponse(val id: String)

interface Api {

    @GET("/api/users/currentuser")
    fun getCurrentUser(): Call<CurrentUserResponse>

    @GET("/api/tickets")
    fun getTickets(): Call<TicketsResponse>

    @GET("/api/tickets/{id}")
    fun getTicket(@Path("id") id: String): Call<TicketResponse>

    @POST("/api/users/signin")
    fun signin(@Body userRequest: UserRequest): Call<UserResponse>

    @POST("/api/users/signup")
    fun signup(@Body userRequest: UserRequest): Call<UserResponse>

    @POST("/api/tickets")
    fun postTicket(@Body ticketRequest: TicketRequest,
                   @Header("x-auth-info") authInfo: String): Call<TicketResponse>

    @PUT("/api/tickets/{id}")
    fun putTicket(@Path("id") id: String,
                  @Body ticketRequest: TicketRequest,
                  @Header("x-auth-info") authInfo: String): Call<TicketResponse>

    @POST("/api/orders")
    fun postOrder(@Body orderRequest: OrderRequest,
                  @Header("x-auth-info") authInfo: String): Call<OrderResponse>

    @POST("/api/orders")
    fun postOrder(@Body orderRequest: OrderRequest,
                  @Header("x-auth-info") authInfo: String,
                  @Query("expiration") expiration: String): Call<OrderResponse>

    @GET("/api/orders")
    fun getOrders(@Header("x-auth-info") authInfo: String): Call<OrdersResponse>

    @GET("/api/orders/{id}")
    fun getOrder(@Path("id") id: String,
                 @Header("x-auth-info") authInfo: String): Call<OrderResponse>

    @DELETE("/api/orders/{id}")
    fun cancelOrder(@Path("id") id: String,
                    @Header("x-auth-info") authInfo: String): Call<OrderResponse>

    @POST("/api/payments")
    fun postPayment(@Body paymentRequest: PaymentRequest,
                    @Header("x-auth-info") authInfo: String): Call<PaymentResponse>

    @POST("/oauth/token?grant_type=password&scope=read")
    fun oauthToken(@Header("authorization") authorization: String,
                   @Query("username") username: String,
                   @Query("password") password: String): Call<OAuthTokenResponse>

    @POST("/oauth/token?grant_type=refresh_token&scope=read")
    fun oauthRefresh(@Header("authorization") authorization: String,
                     @Query("refresh_token") refreshToken: String): Call<OAuthTokenResponse>

    @POST("/oauth/check_token")
    fun oauthCheckToken(@Header("authorization") authorization: String,
                        @Query("token") token: String): Call<OAuthCheckTokenResponse>

    companion object {
        private const val DEFAULT_BASE_URL = "http://localhost:8080"
        fun create(baseUrl: String? = null): Api {
            val resolvedUrl = baseUrl
                    ?: System.getProperty("ticketing.base-url")
                    ?: System.getenv("TICKETING_BASE_URL")
                    ?: DEFAULT_BASE_URL
            val retrofit = Retrofit.Builder()
                    .baseUrl(resolvedUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(Api::class.java)
        }
    }
}