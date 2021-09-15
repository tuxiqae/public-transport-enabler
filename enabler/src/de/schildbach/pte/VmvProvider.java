/*
 * Copyright 2010-2015 the original author or authors.
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

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.annotation.Nullable;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;

import okhttp3.HttpUrl;

/**
 * @author Andreas Schildbach
 */
public class VmvProvider extends AbstractEfaProvider {
    private static final HttpUrl API_BASE = HttpUrl.parse("https://efa.vmv-mbh.de/vmv/");
    // http://80.146.180.107/delfi/

    public VmvProvider() {
        super(NetworkId.VMV, API_BASE);
        setIncludeRegionId(false);
        setUseRouteIndexAsTripId(false);
        httpClient.setSslAcceptAllHostnames(true);
    }

    @Override
    protected void appendXsltTripRequestParameters(final HttpUrl.Builder url, final Location from,
            final @Nullable Location via, final Location to, final Date time, final boolean dep,
            final @Nullable Collection<Product> products, final @Nullable Optimize optimize,
            final @Nullable WalkSpeed walkSpeed, final @Nullable Accessibility accessibility,
            final @Nullable Set<Option> options) {
        super.appendXsltTripRequestParameters(url, from, via, to, time, dep, products, optimize, walkSpeed,
                accessibility, options);
        url.addEncodedQueryParameter("inclMOT_11", "on");
    }
}
