package com.triangl.dashboard

import com.triangl.dashboard.dto.VisitorAverageTimeframeDto
import com.triangl.dashboard.dto.VisitorCountTimeframeDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DashboardIntegrationTest {

    @Value("\${local.server.port}")
    private val serverPort: Int = 0

    @Before
    fun setUp() {
        RestAssured.port = serverPort
    }

    @Test
    fun `should return a list of timeframe's with their visitorCount`() {
        val jsonPayload = "{ " +
            "\"customerId\": \"customer1\"," +
            "\"from\": \"2018-10-10T09:00:00Z\"," +
            "\"to\": \"2018-10-10T14:00:00Z\"," +
            "\"dataPointCount\": \"5\"" +
        "}"

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(jsonPayload)
            .post("/visitors/count")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.OK.value())
            .body("total", `is`(3))
            .body("data", hasSize<VisitorCountTimeframeDto>(5))
    }

    @Test
    fun `should return visitor's average dwell time per given are`() {
        val jsonPayload = "{ " +
            "\"mapId\": \"map1\"," +
            "\"from\": \"2018-10-10T09:00:00Z\"," +
            "\"to\": \"2018-10-10T14:00:00Z\"," +
            "\"areaDtos\": [" +
                "{ " +
                    "\"corner1\": { \"x\": \"0\", \"y\": \"0\" }," +
                    "\"corner2\": { \"x\": \"100\", \"y\": \"100\" }" +
                "},{" +
                    "\"corner1\": { \"x\": \"101\", \"y\": \"101\" }," +
                    "\"corner2\": { \"x\": \"300\", \"y\": \"300\" }" +
                "}" +
            "]" +
        "}"

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(jsonPayload)
            .post("/visitors/areas/duration")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.OK.value())
            .body("size()", `is`(2))
            .body("[0].corner1.x", `is`(0f))
            .body("[0].corner1.y", `is`(0f))
            .body("[0].corner2.x", `is`(100f))
            .body("[0].corner2.y", `is`(100f))
            .body("[0].dwellTime", isA(Int::class.java))
            .body("[1].corner1.x", `is`(101f))
            .body("[1].corner1.y", `is`(101f))
            .body("[1].corner2.x", `is`(300f))
            .body("[1].corner2.y", `is`(300f))
            .body("[1].dwellTime", isA(Int::class.java))
    }

    @Test
    fun `should return average visitor count per weekday per hour`() {
        val jsonPayload = "{ " +
            "\"customerId\": \"customer1\"," +
            "\"from\": \"2018-10-10T09:00:00Z\"," +
            "\"to\": \"2018-10-10T14:00:00Z\"" +
        "}"

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(jsonPayload)
            .post("/visitors/byTimeOfDay/average")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.OK.value())
            .body("size()", `is`(7))
            .body("[0].values", hasSize<VisitorAverageTimeframeDto>(24))
            .body("[1].values", hasSize<VisitorAverageTimeframeDto>(24))
            .body("[2].values", hasSize<VisitorAverageTimeframeDto>(24))
            .body("[3].values", hasSize<VisitorAverageTimeframeDto>(24))
            .body("[4].values", hasSize<VisitorAverageTimeframeDto>(24))
            .body("[5].values", hasSize<VisitorAverageTimeframeDto>(24))
            .body("[6].values", hasSize<VisitorAverageTimeframeDto>(24))
    }
}