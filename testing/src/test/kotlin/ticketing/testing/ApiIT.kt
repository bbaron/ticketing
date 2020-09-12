package ticketing.testing

import com.github.javafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import java.lang.Thread.sleep

@TestMethodOrder(OrderAnnotation::class)
class ApiIT {
    private val api = Api.create()
    private val faker = Faker()
    private val userRequest = randomUser()
    private lateinit var userResponse: UserResponse

    @BeforeAll
    internal fun setUp() {
        try {
            api.getCurrentUser().execute()
        } catch (e: Throwable) {
            assumeThat(false).isTrue()
        }
    }

    @Test
    @Order(1)
    @DisplayName("currentuser with no auth info returns null user")
    internal fun `currentuser with no auth info returns null user`() {
        val response = api.getCurrentUser().execute()
        assertThat(response.isSuccessful).isTrue()
        val body: CurrentUserResponse? = response.body()
        assertThat(body?.currentUser).isNull()
    }

    @Test
    @Order(2)
    @DisplayName("get all tickets does not require auth")
    internal fun `get all tickets does not require auth`() {
        val response = api.getTickets().execute()
        assertThat(response.isSuccessful).isTrue()
        println(response.body())
    }

    @Test
    @Order(3)
    @DisplayName("unregistered user denied signin")
    internal fun `unregistered user denied signin`() {
        val user = randomUser()
        val response = api.signin(user).execute()
        assertThat(response.code()).isEqualTo(401)
    }

    @Test
    @Order(4)
    @DisplayName("random user may signup")
    internal fun `random user may signup`() {
        val response = api.signup(userRequest).execute()
        assertThat(response.code()).isEqualTo(201)
        val userResponse = response.body() ?: fail("Signup failed")
        assertAll(
                { assertThat(userResponse.currentUser.email).isEqualTo(userRequest.email) },
                { assertThat(userResponse.jwt).isNotBlank() },
                { assertThat(userResponse.currentUser.id).isNotBlank() }
        )
        this.userResponse = userResponse
    }


    @Nested
    @TestMethodOrder(OrderAnnotation::class)
    @DisplayName("given registered user")
    inner class RequiresAuth {
        private lateinit var ticketResponse: TicketResponse
        private lateinit var orderResponse: OrderResponse
        private lateinit var ticketSeller: UserResponse
        private lateinit var ticketBuyer: UserResponse
        private lateinit var anotherUser: UserResponse

        @BeforeAll
        internal fun setUp() {
            assumeThat(this@ApiIT::userResponse.isInitialized).isTrue()
            ticketSeller = userResponse
            ticketBuyer = signupRandomUser()
            anotherUser = signupRandomUser()
        }

        @Test
        @Order(1)
        @DisplayName("signin is successful")
        internal fun `signin is successful`() {
            val response = api.signin(userRequest).execute()
            assertThat(response.code()).isEqualTo(200)
            val userResponse = response.body() ?: fail("Signin failed")
            assertAll(
                    { assertThat(userResponse.currentUser.email).isEqualTo(userRequest.email) },
                    { assertThat(userResponse.jwt).isNotBlank() },
                    { assertThat(userResponse.currentUser.id).isNotBlank() }
            )
        }

        @Test
        @Order(2)
        @DisplayName("create new ticket is successful")
        internal fun `create new ticket is successful`() {
            val ticketRequest = randomTicketRequest()
            val ticketResponse = createTicket(ticketSeller, ticketRequest)
            assertAll(
                    { assertThat(ticketResponse.title).isEqualTo(ticketRequest.title) },
                    { assertThat(ticketResponse.price).isEqualTo(ticketRequest.price) },
                    { assertThat(ticketResponse.id).isNotBlank() }
            )
            this.ticketResponse = ticketResponse
        }

        @Test
        @Order(3)
        @DisplayName("may get the ticket")
        internal fun `may get the ticket`() {
            assumeThat(this::ticketResponse.isInitialized).isTrue()
            val response = api.getTicket(ticketResponse.id).execute()
            assertThat(response.code()).isEqualTo(200)
            val gottenTicket = response.body() ?: fail("GET ticket failed")
            assertThat(gottenTicket.id).isEqualTo(this.ticketResponse.id)
        }

        @Test
        @Order(4)
        @DisplayName("may update the ticket")
        internal fun `may update the ticket`() {
            assumeThat(this::ticketResponse.isInitialized).isTrue()
            val ticketRequest = randomTicketRequest()
            val response = api.putTicket(ticketResponse.id, ticketRequest, ticketSeller.jwt).execute()
            assertThat(response.code()).isEqualTo(200)
            val putTicket = response.body() ?: fail("PUT ticket failed")
            assertThat(putTicket.title).isEqualTo(ticketRequest.title)
            assertThat(putTicket.price).isEqualTo(ticketRequest.price)
        }

        @Test
        @Order(5)
        @DisplayName("another user MAY NOT update the ticket")
        internal fun `another user MAY NOT update the ticket`() {
            assumeThat(this::ticketResponse.isInitialized).isTrue()
            val ticketRequest = TicketRequest(faker.rockBand().name(), faker.number().numberBetween(10, 1000))
            val response = api.putTicket(ticketResponse.id, ticketRequest, anotherUser.jwt).execute()
            assertThat(response.code()).isEqualTo(403)
        }

        @Test
        @Order(6)
        @DisplayName("create new order is successful")
        internal fun `create new order is successful`() {
            val ticket = createTicket(ticketSeller)
            val orderResponse = createOrder(ticket, ticketBuyer)
            assertAll(
                    { assertThat(orderResponse.status).isEqualTo("Created") },
                    { assertThat(orderResponse.userId).isEqualTo(ticketBuyer.currentUser.id) },
                    { assertThat(orderResponse.ticket.id).isEqualTo(ticket.id) },
                    { assertThat(orderResponse.id).isNotBlank() }
            )
            sleep(1000)
            val orderedTicket = api.getTicket(ticket.id).execute().body()!!
            assertThat(orderedTicket.reserved).isTrue()
            this.orderResponse = orderResponse
        }

        @Test
        @Order(7)
        @DisplayName("may get the order")
        internal fun `may get the order`() {
            assumeThat(this::orderResponse.isInitialized).isTrue()
            val response = api.getOrder(orderResponse.id, ticketBuyer.jwt).execute()
            assertThat(response.code()).isEqualTo(200)
            val gottenOrder = response.body() ?: fail("GET order failed")
            assertThat(gottenOrder.id).isEqualTo(this.orderResponse.id)
        }

        @Test
        @Order(8)
        @DisplayName("another user MAY NOT get the order")
        internal fun `another user MAY NOT get the order`() {
            assumeThat(this::orderResponse.isInitialized).isTrue()
            val response = api.getOrder(orderResponse.id, anotherUser.jwt).execute()
            assertThat(response.code()).isEqualTo(403)
        }

        @Test
        @Order(9)
        @DisplayName("may cancel the order")
        internal fun `may cancel the order`() {
            val order = createOrder(createTicket(ticketSeller), ticketBuyer)
            val response = api.cancelOrder(order.id, ticketBuyer.jwt).execute()
            assertThat(response.code()).isEqualTo(200)
            val cancelledOrder = response.body() ?: fail("DELETE order failed")
            assertThat(cancelledOrder.id).isEqualTo(order.id)
            assertThat(cancelledOrder.status).isEqualTo("Cancelled")
        }

        @Test
        @Order(10)
        @DisplayName("another user MAY NOT cancel the order")
        internal fun `another user MAY NOT cancel the order`() {
            val order = createOrder(createTicket(ticketSeller), ticketBuyer)
            val response = api.cancelOrder(order.id, anotherUser.jwt).execute()
            assertThat(response.code()).isEqualTo(403)
        }

        @Test
        @Order(11)
        @DisplayName("may get own orders only")
        internal fun `may get own orders only`() {
            val user = signupRandomUser()
            val user2 = signupRandomUser()
            createOrder(createTicket(ticketSeller), user)
            createOrder(createTicket(ticketSeller), user)
            createOrder(createTicket(ticketSeller), user)
            createOrder(createTicket(ticketSeller), user2)
            createOrder(createTicket(ticketSeller), user2)
            val response = api.getOrders(user.jwt).execute()
            assertThat(response.code()).isEqualTo(200)
            val orders = response.body()?.orders ?: fail("GET orders failed")
            assertThat(orders).allMatch { it.userId == user.currentUser.id }
        }

        @Test
        @Order(12)
        @DisplayName("may not order ticket already reserved")
        internal fun `may not order ticket already reserved`() {
            val ticket = createTicket(ticketSeller)
            createOrder(ticket, ticketBuyer)
            val response = api.postOrder(OrderRequest(ticket.id), anotherUser.jwt).execute()
            assertThat(response.code()).isEqualTo(400)
        }

        @Test
        @Order(13)
        @DisplayName("order and wait for it to expire")
        internal fun `order and wait for it to expire`() {
            val orderId = createOrder(createTicket(ticketSeller), ticketBuyer, "1").id
            println("waiting for order $orderId to expire")
            sleep(2000)
            val order = api.getOrder(orderId, ticketBuyer.jwt)
                    .execute()
                    .body()!!
            assertThat(order.status).isEqualTo("Cancelled")
        }

        @Test
        @Order(14)
        @DisplayName("make payment on order")
        internal fun `make payment on order`() {
            val paymentApi = Api.create("http://localhost:8080")
            val orderId = createOrder(createTicket(ticketSeller), ticketBuyer).id
            println("make payment for order $orderId")
            val paymentRequest = PaymentRequest("tok_visa", orderId)
            val response = paymentApi.postPayment(paymentRequest, ticketBuyer.jwt).execute()
            assertThat(response.code()).isEqualTo(201)
            val order = api.getOrder(orderId, ticketBuyer.jwt)
                    .execute()
                    .body()!!
            assertThat(order.status).isEqualTo("Completed")
        }
    }

    private fun randomUser(): UserRequest {
        val email = "${faker.name().username()}@${faker.internet().domainName()}"
        val password = faker.regexify("[A-Za-z1-9]{10}")
        return UserRequest(email, password)
    }

    private fun randomTicketRequest(): TicketRequest = TicketRequest(faker.rockBand().name(), faker.number().numberBetween(10, 1000))

    private fun signupRandomUser(): UserResponse {
        val userRequest = randomUser()
        val response = api.signup(userRequest).execute()
        return response.body() ?: fail("Signup failed")
    }

    private fun createTicket(user: UserResponse,
                             ticketRequest: TicketRequest = randomTicketRequest()): TicketResponse {
        val response = api.postTicket(ticketRequest, user.jwt).execute()
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

}