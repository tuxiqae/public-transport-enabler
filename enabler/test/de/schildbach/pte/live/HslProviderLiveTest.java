/*
 * Copyright the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.schildbach.pte.live;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.schildbach.pte.HslProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.NearbyLocationsResult;
import de.schildbach.pte.dto.QueryDeparturesResult;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.SuggestLocationsResult;
import de.schildbach.pte.dto.Trip;

/**
 * @author Adrian Perez de Castro <aperez@igalia.com>
 */
public class HslProviderLiveTest extends AbstractNavitiaProviderLiveTest {
    public HslProviderLiveTest() {
        super(new HslProvider(secretProperty("navitia.authorization")));
    }

	@Test
	public void nearbyStationsAddress() throws Exception {
		nearbyStationsAddress(60160920, 24941870);
	}

	@Test
	public void nearbyStationsStation() throws Exception {
		nearbyStationsStation("stop_point:OFI:SP:1050412");
	}

	@Test
	public void nearbyStationsPoi() throws Exception {
		nearbyStationsPoi("poi:osm:way:29071686");
	}

    @Test
    public void nearbyStationsAny() throws Exception {
        nearbyStationsAny(60160920, 24941870);
    }

	@Test
	public void nearbyStationsInvalidStation() throws Exception {
		nearbyStationsInvalidStation("stop_point:9999999999");
	}

	@Test
	public void queryDeparturesStopArea() throws Exception {
		queryDeparturesStopArea("stop_area:OFI:SA:1000201");
	}

    @Test
    public void queryDeparturesEquivsFalse() throws Exception {
        queryDeparturesEquivsFalse("stop_point:OFI:SP:1050412");
    }

	@Test
	public void queryDeparturesEquivsTrue() throws Exception {
		queryDeparturesEquivsTrue("stop_area:OFI:SA:1000201");
	}

    @Test
    public void shortTrip() throws Exception {
        final QueryTripsResult result = queryTrips(
                new Location(LocationType.STATION, null, "", "Gustaf Hällströmin katu 1"), null,
                new Location(LocationType.STATION, null, "", "Tyynenmerenkatu 11"), new Date(), true, null);
        print(result);
        assertTimesInSequence(result.trips);

        assertEquals(QueryTripsResult.Status.OK, result.status);
        assertTrue(result.trips.size() > 0);

        assertTrue(result.context.canQueryLater());

        final QueryTripsResult laterResult = queryMoreTrips(result.context, true);
        print(laterResult);
        assertEquals(QueryTripsResult.Status.OK, laterResult.status);
        assertTrue(laterResult.trips.size() > result.trips.size());
        assertTimesInSequence(laterResult.trips);

        assertTrue(laterResult.context.canQueryLater());

        final QueryTripsResult later2Result = queryMoreTrips(laterResult.context, true);
        print(later2Result);
        for (Trip trip : later2Result.trips) {
            System.out.println("LATER2 " + trip.getFirstDepartureTime() + " " + trip.getId());
        }
        assertEquals(QueryTripsResult.Status.OK, later2Result.status);
        assertTrue(later2Result.trips.size() > laterResult.trips.size());
        assertTimesInSequence(later2Result.trips);

        assertTrue(later2Result.context.canQueryEarlier());

        final QueryTripsResult earlierResult = queryMoreTrips(later2Result.context, false);
        print(earlierResult);
        for (Trip trip : earlierResult.trips) {
            System.out.println("EARLIER " + trip.getFirstDepartureTime() + " " + trip.getId());
        }
        assertEquals(QueryTripsResult.Status.OK, earlierResult.status);
        assertTrue(earlierResult.trips.size() > later2Result.trips.size());
        assertTimesInSequence(earlierResult.trips);
    }

	@Test
	public void suggestLocations() throws Exception {
		suggestLocationsFromName("postitalo");
	}

	@Test
	public void suggestLocationsFromAddress() throws Exception {
		suggestLocationsFromAddress("10 yrjönkatu");
	}

	@Test
	public void suggestLocationsNoLocation() throws Exception {
		suggestLocationsNoLocation("fontana di trevi blah blah");
	}

	@Test
	public void queryTripAddresses() throws Exception {
		queryTrip("Yrjönkatu, 10, Helsinki", "Kolmas Linja, 5, Helsinki");
	}

	@Test
	public void queryTripAddressStation() throws Exception {
		queryTrip("Viides Linja, 3, Helsinki", "Kapylän asema");
	}

	@Test
	public void queryTripStations() throws Exception {
		queryTrip("Kapylän asema", "Päärautatieasema");
	}

	@Test
	public void queryTripNoSolution() throws Exception {
		queryTripNoSolution("Steissi, Helsinki", "Keskuskatu 1, Kuopio");
	}

	@Test
	public void queryTripUnknownFrom() throws Exception {
		queryTripUnknownFrom("Rautatieasema");
	}

	@Test
	public void queryTripUnknownTo() throws Exception {
		queryTripUnknownTo("Rautatieasema");
	}

	@Test
	public void queryTripSlowWalk() throws Exception {
		queryTripSlowWalk("Rautatieasema", "Postitalo");
	}

	@Test
	public void queryTripFastWalk() throws Exception {
		queryTripFastWalk("Rautatieasema", "Postitalo");
	}

	@Test
	public void queryMoreTrips() throws Exception {
		queryMoreTrips("Steissi", "Töölöntori");
	}

	@Test
	public void getArea() throws Exception {
		final Point[] polygon = provider.getArea();
		assertTrue(polygon.length > 0);
	}
}
