package ticketing.testing

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

data class CurrentUser(val id: String, val userId: String, val iat: Date)
data class CurrentUserResponse(val currentUser: CurrentUser?)
data class TicketResponse(val id: String, val title: String, val price: Int, val reserved: Boolean)
data class TicketsResponse(val tickets: List<TicketResponse>)
data class TicketRequest(val title: String, val price: Int)

data class UserRequest(val email: String, val password: String)
data class UserResponse(val id: String, val email: String, val jwt: String)

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

data class OrdersResponse(val orders: List<OrderResponse>)

interface Api {

    @GET("/api/ping")
    fun ping(): Call<String>

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