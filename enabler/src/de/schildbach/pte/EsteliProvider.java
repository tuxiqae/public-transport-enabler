/*
 * Copyright 2015 the original author or authors.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.schildbach.pte;

import okhttp3.HttpUrl;

/**
 * @author Nico Alt
 */
public class EsteliProvider extends AbstractNavitiaProvider {
    private static String API_REGION = "ni-esteli";
    private static HttpUrl API_BASE = HttpUrl.parse("https://navitia.mapanica.net/").newBuilder().addPathSegment(SERVER_VERSION).build();

    public EsteliProvider(final String authorization) {
        super(NetworkId.ESTELI, API_BASE, authorization);

        setTimeZone("America/Managua");
    }

    @Override
    public String region() {
        return API_REGION;
    }
}
