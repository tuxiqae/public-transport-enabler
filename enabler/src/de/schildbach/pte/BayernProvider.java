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

package de.schildbach.pte;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.EnumSet;

import javax.annotation.Nullable;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import de.schildbach.pte.dto.Line;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.NearbyLocationsResult;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.QueryDeparturesResult;
import de.schildbach.pte.dto.QueryTripsContext;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.SuggestLocationsResult;
import de.schildbach.pte.dto.TripOptions;

import okhttp3.HttpUrl;

/**
 * @author Andreas Schildbach
 */
public class BayernProvider extends AbstractEfaProvider {
    private static final HttpUrl API_BASE = HttpUrl.parse("https://mobile.defas-fgi.de/beg/");
    // http://mobile.defas-fgi.de/xml/

    private static final String DEPARTURE_MONITOR_ENDPOINT = "XML_DM_REQUEST";
    private static final String TRIP_ENDPOINT = "XML_TRIP_REQUEST2";
    private static final String STOP_FINDER_ENDPOINT = "XML_STOPFINDER_REQUEST";

    public BayernProvider() {
        super(NetworkId.BAYERN, API_BASE, DEPARTURE_MONITOR_ENDPOINT, TRIP_ENDPOINT, STOP_FINDER_ENDPOINT, null);

        setRequestUrlEncoding(Charsets.UTF_8);
        setIncludeRegionId(false);
        setNumTripsRequested(12);
    }

    @Override
    protected Line parseLine(final @Nullable String id, final @Nullable String network, final @Nullable String mot,
            final @Nullable String symbol, final @Nullable String name, final @Nullable String longName,
            final @Nullable String trainType, final @Nullable String trainNum, final @Nullable String trainName) {
        if ("0".equals(mot)) {
            if ("M".equals(trainType) && trainNum != null && trainName != null && trainName.endsWith("Meridian"))
                return new Line(id, network, Product.REGIONAL_TRAIN, "M" + trainNum);
            if ("ZUG".equals(trainType) && trainNum != null)
                return new Line(id, network, Product.REGIONAL_TRAIN, trainNum);
        } else if ("1".equals(mot)) {
            if ("ABR".equals(trainType) || "ABELLIO Rail NRW GmbH".equals(trainName))
                return new Line(id, network, Product.SUBURBAN_TRAIN, "ABR" + trainNum);
            if ("SBB".equals(trainType) || "SBB GmbH".equals(trainName))
                return new Line(id, network, Product.REGIONAL_TRAIN, "SBB" + Strings.nullToEmpty(trainNum));
        } else if ("5".equals(mot)) {
            if (name != null && name.startsWith("Stadtbus Linie ")) // Lindau
                return super.parseLine(id, network, mot, symbol, name.substring(15), longName, trainType, trainNum,
                        trainName);
            else
                return super.parseLine(id, network, mot, symbol, name, longName, trainType, trainNum, trainName);
        } else if ("16".equals(mot)) {
            if ("EC".equals(trainType) && trainNum != null)
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "EC" + trainNum);
            if ("IC".equals(trainType) && trainNum != null)
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "IC" + trainNum);
            if ("ICE".equals(trainType) && trainNum != null)
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "ICE" + trainNum);
            if ("CNL".equals(trainType) && trainNum != null)
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "CNL" + trainNum);
            if ("THA".equals(trainType) && trainNum != null) // Thalys
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "THA" + trainNum);
            if ("TGV".equals(trainType) && trainNum != null) // Train a grande Vitesse
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "TGV" + trainNum);
            if ("RJ".equals(trainType) && trainNum != null) // railjet
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "RJ" + trainNum);
            if ("WB".equals(trainType) && trainNum != null) // WESTbahn
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "WB" + trainNum);
            if ("HKX".equals(trainType) && trainNum != null) // Hamburg-Köln-Express
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "HKX" + trainNum);
            if ("D".equals(trainType) && trainNum != null) // Schnellzug
                return new Line(id, network, Product.HIGH_SPEED_TRAIN, "D" + trainNum);

            if ("IR".equals(trainType) && trainNum != null) // InterRegio
                return new Line(id, network, Product.REGIONAL_TRAIN, "IR" + trainNum);
        }

        return super.parseLine(id, network, mot, symbol, name, longName, trainType, trainNum, trainName);
    }

    @Override
    public NearbyLocationsResult queryNearbyLocations(final EnumSet<LocationType> types, final Location location,
            final int maxDistance, final int maxLocations) throws IOException {
        if (location.hasCoord())
            return mobileCoordRequest(types, location.coord, maxDistance, maxLocations);

        if (location.type != LocationType.STATION)
            throw new IllegalArgumentException("cannot handle: " + location.type);

        throw new IllegalArgumentException("station"); // TODO
    }

    @Override
    public QueryDeparturesResult queryDepartures(final String stationId, final @Nullable Date time,
            final int maxDepartures, final boolean equivs) throws IOException {
        checkNotNull(Strings.emptyToNull(stationId));

        return queryDeparturesMobile(stationId, time, maxDepartures, equivs);
    }

    @Override
    public SuggestLocationsResult suggestLocations(final CharSequence constraint) throws IOException {
        return mobileStopfinderRequest(new Location(LocationType.ANY, null, null, constraint.toString()));
    }

    @Override
    protected void appendXsltTripRequestParameters(final HttpUrl.Builder url, final Location from,
            final @Nullable Location via, final Location to, final Date time, final boolean dep,
            final @Nullable TripOptions options) {
        super.appendXsltTripRequestParameters(url, from, via, to, time, dep, options);
        if (options != null && options.products != null) {
            for (final Product p : options.products) {
                if (p == Product.HIGH_SPEED_TRAIN)
                    url.addEncodedQueryParameter("inclMOT_15", "on").addEncodedQueryParameter("inclMOT_16", "on");
                if (p == Product.REGIONAL_TRAIN)
                    url.addEncodedQueryParameter("inclMOT_13", "on");
            }
        }
        url.addEncodedQueryParameter("inclMOT_11", "on");
        url.addEncodedQueryParameter("inclMOT_14", "on");
        url.addEncodedQueryParameter("calcOneDirection", "1");
    }

    @Override
    public QueryTripsResult queryTrips(final Location from, final @Nullable Location via, final Location to,
            final Date date, final boolean dep, final @Nullable TripOptions options) throws IOException {
        return queryTripsMobile(from, via, to, date, dep, options);
    }

    @Override
    public QueryTripsResult queryMoreTrips(final QueryTripsContext contextObj, final boolean later) throws IOException {
        return queryMoreTripsMobile(contextObj, later);
    }
}
