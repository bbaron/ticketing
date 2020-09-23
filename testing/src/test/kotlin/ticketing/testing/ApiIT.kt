package ticketing.testing

import com.github.javafaker.Faker
import com.google.gson.GsonBuilder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.json.GsonTester
import retrofit2.Call
import java.util.*

@TestMethodOrder(OrderAnnotation::class)
class ApiIT {
    private val api = Api.create()
    private val faker = Faker()
    private val userRequest = randomUser()
    private lateinit var userResponse: UserResponse
    private lateinit var userToken: String
    private lateinit var invalidGrantResponseTester: GsonTester<InvalidGrantResponse>

    companion object {
        const val PASSWORD = "asdf"
        val CLIENT_CREDENTIALS = "Basic " + Base64.getEncoder().encodeToString("client:secret".toByteArray())
    }

    @BeforeAll
    internal fun setUp() {
        try {
            val gson = GsonBuilder().create()
            GsonTester.initFields(this, gson)
            api.getCurrentUser().execute()
        } catch (e: Throwable) {
            assumeThat(false).isTrue()
        }
    }

    @Test
    @Order(10)
    @DisplayName("currentuser with no auth info returns null user")
    @Disabled
    internal fun `currentuser with no auth info returns null user`() {
        val response = api.getCurrentUser().execute()
        assertThat(response.isSuccessful).isTrue()
        val body: CurrentUserResponse? = response.body()
        assertThat(body?.currentUser).isNull()
    }

    @Test
    @Order(20)
    @DisplayName("get all tickets does not require auth")
    internal fun `get all tickets does not require auth`() {
        val response = api.getTickets().execute()
        assertThat(response.isSuccessful).isTrue()
    }

    @Test
    @Order(24)
    @DisplayName("get all tickets are all available")
    internal fun `get all tickets are all available`() {
        val response = api.getTickets().execute()
        assertThat(response.isSuccessful).isTrue()
        val tickets = response.body()?.tickets ?: fail("GET tickets failed")
        assertThat(tickets).noneMatch { it.reserved }

    }

    @Test
    @Order(30)
    @DisplayName("unregistered user denied signin")
    @Disabled
    internal fun `unregistered user denied signin`() {
        val user = randomUser()
        val response = api.signin(user).execute()
        assertThat(response.code()).isEqualTo(401)
    }

    @Test
    @Order(40)
    @DisplayName("random user may signup and signin")
    internal fun `random user may signup`() {
        val response = api.signup(userRequest).execute()
        assertThat(response.code()).isEqualTo(201)
        val userResponse = response.body() ?: fail("Signup failed")
        assertAll(
                { assertThat(userResponse.currentUser.email).isEqualTo(userRequest.email) },
                { assertThat(userResponse.currentUser.id).isNotBlank() }
        )
        this.userResponse = userResponse
        val response2 = oauthToken(userRequest).execute()
        assertThat(response2.code()).isEqualTo(200)
        val oauthResponse = response2.body() ?: fail("/oauth/token failed")
        assertAll(
                { assertThat(oauthResponse.currentUser.email).isEqualTo(userRequest.email) },
                { assertThat(oauthResponse.currentUser.id).isNotBlank() },
                { assertThat(oauthResponse.access_token).isNotBlank() },
                { assertThat(oauthResponse.refresh_token).isNotBlank() }
        )
        userToken = oauthResponse.access_token
    }


    @Nested
    @TestMethodOrder(OrderAnnotation::class)
    @DisplayName("given registered user")
    inner class RequiresAuth {
        private lateinit var ticketResponse: TicketResponse
        private lateinit var orderResponse: OrderResponse
//        private lateinit var ticketSeller: UserResponse
        private lateinit var ticketSellerToken: String
//        private lateinit var ticketBuyer: UserResponse
        private lateinit var ticketBuyerToken: String
//        private lateinit var anotherUser: UserResponse
        private lateinit var anotherUserToken: String

        @BeforeAll
        internal fun setUp() {
            assumeThat(this@ApiIT::userResponse.isInitialized).isTrue()
//            ticketSeller = userResponse
            ticketSellerToken = userToken
//            ticketBuyer = signupRandomUser()
//            anotherUser = signupRandomUser()
            ticketBuyerToken = oauthRandomUser()
            ticketSellerToken = oauthRandomUser()
            anotherUserToken = oauthRandomUser()
        }

//        @Test
//        @Order(10)
//        @DisplayName("oauth token is successful")
//        internal fun `oauth token is successful`() {
//            val response = oauthToken(userRequest).execute()
//            assertThat(response.code()).isEqualTo(200)
//            val oauthResponse = response.body() ?: fail("/oauth/token failed")
//            assertAll(
//                    { assertThat(oauthResponse.currentUser.email).isEqualTo(userRequest.email) },
//                    { assertThat(oauthResponse.currentUser.id).isNotBlank() },
//                    { assertThat(oauthResponse.access_token).isNotBlank() },
//                    { assertThat(oauthResponse.refresh_token).isNotBlank() }
//            )
//        }

        @Test
        @Order(11)
        @DisplayName("oauth refresh is successful")
        internal fun `oauth refresh is successful`() {
            val response1 = oauthToken(userRequest).execute()
            val oauthResponse1 = response1.body()!!
            val response = oauthRefresh(oauthResponse1.refresh_token).execute()
            assertThat(response.code()).isEqualTo(200)
            val oauthResponse = response.body() ?: fail("/oauth/token failed")
            assertAll(
                    { assertThat(oauthResponse.currentUser.email).isEqualTo(userRequest.email) },
                    { assertThat(oauthResponse.currentUser.id).isNotBlank() },
                    { assertThat(oauthResponse.access_token).isNotBlank() },
                    { assertThat(oauthResponse.refresh_token).isNotBlank() }
            )
        }

        @Test
        @Order(12)
        @DisplayName("oauth token returns 400 on bad credentials")
        internal fun `oauth token returns 400 on bad credentials`() {
            val bogusRequest = UserRequest("asdf@asdf.com", "bogus")
            val response = oauthToken(bogusRequest).execute()
            assertThat(response.code()).isEqualTo(400)
            val text = response.errorBody()?.string() ?: ""
            assertThat(text).isNotBlank()
            val details = invalidGrantResponseTester.parse(text).`object`
            assertAll(
                    { assertThat(details.error).isEqualTo("invalid_grant") },
                    { assertThat(details.error_description).isEqualTo("Bad credentials") })

        }

        @Test
        @Order(15)
        @DisplayName("oauth check is successful")
        internal fun `oauth check is successful`() {
            val response1 = oauthToken(userRequest).execute()
            val oauthResponse = response1.body()!!
            val response2 = oauthCheckToken(oauthResponse.access_token).execute()
            assertThat(response2.code()).isEqualTo(200)
            val checkResponse = response2.body() ?: fail("/oauth/check failed")
            assertAll(
                    { assertThat(checkResponse.currentUser.email).isEqualTo(userRequest.email) },
                    { assertThat(checkResponse.currentUser.id).isNotBlank() },
                    { assertThat(checkResponse.active).isTrue() },
                    { assertThat(checkResponse.user_name).isEqualTo(userRequest.email) }
            )
        }

        @Test
        @Order(20)
        @DisplayName("create new ticket is successful")
        internal fun `create new ticket is successful`() {
            val ticketRequest = randomTicketRequest()
            val ticketResponse = createTicket(ticketSellerToken, ticketRequest)
            assertAll(
                    { assertThat(ticketResponse.title).isEqualTo(ticketRequest.title) },
                    { assertThat(ticketResponse.price).isEqualTo(ticketRequest.price) },
                    { assertThat(ticketResponse.id).isNotBlank() }
            )
            this.ticketResponse = ticketResponse
        }

        @Test
        @Order(30)
        @DisplayName("may get the ticket")
        internal fun `may get the ticket`() {
            assumeThat(this::ticketResponse.isInitialized).isTrue()
            val response = api.getTicket(ticketResponse.id).execute()
            assertThat(response.code()).isEqualTo(200)
            val gottenTicket = response.body() ?: fail("GET ticket failed")
            assertThat(gottenTicket.id).isEqualTo(this.ticketResponse.id)
        }

        @Test
        @Order(40)
        @DisplayName("may update the ticket")
        internal fun `may update the ticket`() {
            assumeThat(this::ticketResponse.isInitialized).isTrue()
            val ticketRequest = randomTicketRequest()
            val response = api.putTicket(ticketResponse.id, ticketRequest, authHeader(ticketSellerToken)).execute()
            assertThat(response.code()).isEqualTo(200)
            val putTicket = response.body() ?: fail("PUT ticket failed")
            assertThat(putTicket.title).isEqualTo(ticketRequest.title)
            assertThat(putTicket.price).isEqualTo(ticketRequest.price)
        }

        @Test
        @Order(50)
        @DisplayName("another user MAY NOT update the ticket")
        internal fun `another user MAY NOT update the ticket`() {
            assumeThat(this::ticketResponse.isInitialized).isTrue()
            val ticketRequest = TicketRequest(faker.rockBand().name(), faker.number().numberBetween(10, 1000))
            val response = api.putTicket(ticketResponse.id, ticketRequest, authHeader(anotherUserToken)).execute()
            assertThat(response.code()).isEqualTo(403)
        }

        @Test
        @Order(60)
        @DisplayName("create new order is successful")
        @Disabled
        internal fun `create new order is successful`() {
//            val ticket = createTicket(ticketSeller)
//            val orderResponse = createOrder(ticket, ticketBuyer)
//            assertAll(
//                    { assertThat(orderResponse.status).isEqualTo("Created") },
//                    { assertThat(orderResponse.userId).isEqualTo(ticketBuyer.currentUser.id) },
//                    { assertThat(orderResponse.ticket.id).isEqualTo(ticket.id) },
//                    { assertThat(orderResponse.id).isNotBlank() }
//            )
//            sleep(1000)
//            val orderedTicket = api.getTicket(ticket.id).execute().body()!!
//            assertThat(orderedTicket.reserved).isTrue()
//            this.orderResponse = orderResponse
        }

        @Test
        @Order(70)
        @DisplayName("may get the order")
        @Disabled
        internal fun `may get the order`() {
//            assumeThat(this::orderResponse.isInitialized).isTrue()
//            val response = api.getOrder(orderResponse.id, ticketBuyer.jwt).execute()
//            assertThat(response.code()).isEqualTo(200)
//            val gottenOrder = response.body() ?: fail("GET order failed")
//            assertThat(gottenOrder.id).isEqualTo(this.orderResponse.id)
        }

        @Test
        @Order(80)
        @DisplayName("another user MAY NOT get the order")
        @Disabled
        internal fun `another user MAY NOT get the order`() {
//            assumeThat(this::orderResponse.isInitialized).isTrue()
//            val response = api.getOrder(orderResponse.id, anotherUser.jwt).execute()
//            assertThat(response.code()).isEqualTo(403)
        }

        @Test
        @Order(90)
        @DisplayName("may cancel the order")
        @Disabled
        internal fun `may cancel the order`() {
//            val order = createOrder(createTicket(ticketSeller), ticketBuyer)
//            val response = api.cancelOrder(order.id, ticketBuyer.jwt).execute()
//            assertThat(response.code()).isEqualTo(200)
//            val cancelledOrder = response.body() ?: fail("DELETE order failed")
//            assertThat(cancelledOrder.id).isEqualTo(order.id)
//            assertThat(cancelledOrder.status).isEqualTo("Cancelled")
        }

        @Test
        @Order(100)
        @DisplayName("another user MAY NOT cancel the order")
        @Disabled
        internal fun `another user MAY NOT cancel the order`() {
//            val order = createOrder(createTicket(ticketSeller), ticketBuyer)
//            val response = api.cancelOrder(order.id, anotherUser.jwt).execute()
//            assertThat(response.code()).isEqualTo(403)
        }

        @Test
        @Order(110)
        @DisplayName("may get own orders only")
        @Disabled
        internal fun `may get own orders only`() {
//            val user = signupRandomUser()
//            val user2 = signupRandomUser()
//            createOrder(createTicket(ticketSeller), user)
//            createOrder(createTicket(ticketSeller), user)
//            createOrder(createTicket(ticketSeller), user)
//            createOrder(createTicket(ticketSeller), user2)
//            createOrder(createTicket(ticketSeller), user2)
//            val response = api.getOrders(user.jwt).execute()
//            assertThat(response.code()).isEqualTo(200)
//            val orders = response.body()?.orders ?: fail("GET orders failed")
//            assertThat(orders).allMatch { it.userId == user.currentUser.id }
        }

        @Test
        @Order(120)
        @DisplayName("may not order ticket already reserved")
        @Disabled
        internal fun `may not order ticket already reserved`() {
//            val ticket = createTicket(ticketSeller)
//            createOrder(ticket, ticketBuyer)
//            val response = api.postOrder(OrderRequest(ticket.id), anotherUser.jwt).execute()
//            assertThat(response.code()).isEqualTo(400)
        }

        @Test
        @Order(130)
        @DisplayName("order and wait for it to expire")
        @Disabled
        internal fun `order and wait for it to expire`() {
//            val orderId = createOrder(createTicket(ticketSeller), ticketBuyer, "1").id
//            val millis = 2000L
//            println("waiting for order $orderId to expire")
//            sleep(millis)
//            println("waited $millis millis for order $orderId to expire")
//            val order = api.getOrder(orderId, ticketBuyer.jwt)
//                    .execute()
//                    .body()!!
//            assertThat(order.status).isEqualTo("Cancelled")
        }

        @Test
        @Order(135)
        @DisplayName("order a random ticket from the landing page")
        @Disabled
        internal fun `order a random ticket from the landing page`() {

//            val tickets = api.getTickets().execute().body()?.tickets ?: fail("GET tickets failed")
//            val ticket = tickets[Random.nextInt(tickets.size)]
//            val orderId = createOrder(ticket, ticketBuyer, "1").id
//            val millis = 2000L
//            println("waiting for order $orderId to expire")
//            sleep(millis)
//            println("waited $millis millis for order $orderId to expire")
//            val order = api.getOrder(orderId, ticketBuyer.jwt)
//                    .execute()
//                    .body()!!
//            assertThat(order.status).isEqualTo("Cancelled")
        }

        @Test
        @Order(140)
        @DisplayName("make payment on order")
        @Disabled
        internal fun `make payment on order`() {
//            val paymentApi = Api.create("http://localhost:8080")
//            val orderId = createOrder(createTicket(ticketSeller), ticketBuyer).id
//            println("make payment for order $orderId")
//            val paymentRequest = PaymentRequest("tok_visa", orderId)
//            val response = paymentApi.postPayment(paymentRequest, ticketBuyer.jwt).execute()
//            assertThat(response.code()).isEqualTo(201)
//            val order = api.getOrder(orderId, ticketBuyer.jwt)
//                    .execute()
//                    .body()!!
//            assertThat(order.status).isEqualTo("Completed")
        }
    }

    private fun randomUser(): UserRequest {
        val email = "${faker.name().username()}@${faker.internet().domainName()}"
        return UserRequest(email, PASSWORD)
    }

    private fun randomTicketRequest(): TicketRequest = TicketRequest(faker.rockBand().name(), faker.number().numberBetween(10, 1000))

    private fun signupRandomUser(): UserResponse {
        val userRequest = randomUser()
        val response = api.signup(userRequest).execute()
        return response.body() ?: fail("Signup failed")
    }

    private fun oauthRandomUser(): String {
        val userRequest = randomUser()
        api.signup(userRequest).execute()
        return oauthToken(userRequest).execute().body()!!.access_token
    }

    private fun createTicket(token: String,
                             ticketRequest: TicketRequest = randomTicketRequest()): TicketResponse {
        val response = api.postTicket(ticketRequest, authHeader(token)).execute()
        assertThat(response.code()).isEqualTo(201)
        return response.body() ?: fail("POST ticket failed")
    }

    private fun createOrder(ticket: TicketResponse, user: UserResponse, expiration: String? = null): OrderResponse {
        val request = OrderRequest(ticket.id)
        val response = if (expiration == null)
            api.postOrder(request, user.jwt).execute()
        else
            api.postOrder(request, user.jwt, expiration).execute()
        assertThat(response.code()).isEqualTo(201)
        return response.body() ?: fail("POST order failed")
    }

    private fun authHeader(token: String) : String =
            "Bearer $token"


    fun oauthToken(userRequest: UserRequest): Call<OAuthTokenResponse> =
            api.oauthToken(CLIENT_CREDENTIALS, userRequest.email, userRequest.password)

    fun oauthRefresh(token: String): Call<OAuthTokenResponse> =
            api.oauthRefresh(CLIENT_CREDENTIALS, token)


    fun oauthCheckToken(token: String): Call<OAuthCheckTokenResponse> =
            api.oauthCheckToken(CLIENT_CREDENTIALS, token)

}