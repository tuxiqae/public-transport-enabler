/*
 * Copyright 2010-2013 the original author or authors.
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

import java.util.TimeZone;

/**
 * @author Andreas Schildbach
 */
public class SydneyProvider extends AbstractEfaProvider
{
	public static final NetworkId NETWORK_ID = NetworkId.SYDNEY;
	private final static String API_BASE = "http://tp.transportnsw.info/nsw/";

	public SydneyProvider()
	{
		super(API_BASE);

		setUseRouteIndexAsTripId(false);
	}

	public NetworkId id()
	{
		return NETWORK_ID;
	}

	@Override
	protected TimeZone timeZone()
	{
		return TimeZone.getTimeZone("Australia/Sydney");
	}

	public boolean hasCapabilities(final Capability... capabilities)
	{
		for (final Capability capability : capabilities)
			if (capability == Capability.AUTOCOMPLETE_ONE_LINE || capability == Capability.DEPARTURES || capability == Capability.TRIPS)
				return true;

		return false;
	}

	@Override
	protected String normalizeLocationName(final String name)
	{
		if (name == null || name.length() == 0)
			return null;

		return super.normalizeLocationName(name).replace("$XINT$", "&");
	}
}
