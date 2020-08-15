package ticketing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordJsonTest {
    static final String json = """
            {
                "country": "United States",
                "country abbreviation": "US",
                "places": [
                    {
                        "latitude": "44.8162",
                        "longitude": "-85.5751",
                        "place name": "Traverse City",
                        "state": "Michigan",
                        "state abbreviation": "MI"
                    }
                ],
                "post code": "49685"
            }
            """;

    @Test
    void jsonCodec() throws JsonProcessingException {
        var expected = new Location("49685", "United States", "US",
                List.of(new Place("Traverse City", "-85.5751",
                        "Michigan", "MI", "44.8162")));
        ObjectMapper om = new ObjectMapper();
        Location actual = om.readValue(json, Location.class);
        assertAll(
                () -> assertEquals(expected.country(), actual.country()),
                () -> assertEquals(expected.postCode(), actual.postCode()),
                () -> assertEquals(expected.country(), actual.country()),
                () -> assertEquals(expected.countryAbbreviation(), actual.countryAbbreviation()),
                () -> assertAll(() -> {
                    Place p1 = expected.places().get(0);
                    Place p2 = actual.places().get(0);
                    assertAll(
                            () -> assertEquals(p1.latitude(), p2.latitude()),
                            () -> assertEquals(p1.longitude(), p2.longitude()),
                            () -> assertEquals(p1.state(), p2.state()),
                            () -> assertEquals(p1.stateAbbreviation(), p2.stateAbbreviation()),
                            () -> assertEquals(p1.placeName(), p2.placeName())
                    );
                })
        );
        System.out.println(om.writeValueAsString(actual));
    }

    public static record Location(
            @JsonProperty("post code")
            String postCode,
            @JsonProperty("country")
            String country,
            @JsonProperty("country abbreviation")
            String countryAbbreviation,
            @JsonProperty("places")
            List<Place> places
    ) {
    }

    public static record Place(
            @JsonProperty("place name")
            String placeName,
            @JsonProperty("longitude")
            String longitude,
            @JsonProperty("state")
            String state,
            @JsonProperty("state abbreviation")
            String stateAbbreviation,
            @JsonProperty("latitude")
            String latitude
    ) {
    }
}

