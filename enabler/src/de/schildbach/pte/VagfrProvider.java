/*
 * Copyright 2010-2014 the original author or authors.
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

import java.util.HashMap;
import java.util.Map;

import de.schildbach.pte.dto.Style;
import de.schildbach.pte.dto.Style.Shape;

/**
 * @author Andreas Schildbach
 */
public class VagfrProvider extends AbstractEfaProvider
{
	public static final NetworkId NETWORK_ID = NetworkId.VAGFR;
	private final static String API_BASE = "http://efa.vag-freiburg.de/vagfr/";

	public VagfrProvider()
	{
		super(API_BASE);

		setUseRouteIndexAsTripId(false);
		setStyles(STYLES);
	}

	public NetworkId id()
	{
		return NETWORK_ID;
	}

	public boolean hasCapabilities(final Capability... capabilities)
	{
		for (final Capability capability : capabilities)
			if (capability == Capability.AUTOCOMPLETE_ONE_LINE || capability == Capability.DEPARTURES || capability == Capability.TRIPS)
				return true;

		return false;
	}

	@Override
	protected String parseLine(final String mot, final String symbol, final String name, final String longName, final String trainType,
			final String trainNum, final String trainName)
	{
		if ("0".equals(mot))
		{
			if ("BSB-Zug".equals(longName))
				return "SBSB";
		}

		return super.parseLine(mot, symbol, name, longName, trainType, trainNum, trainName);
	}

	private static final Map<String, Style> STYLES = new HashMap<String, Style>();

	static
	{
		// Tram
		STYLES.put("T1", new Style(Shape.RECT, Style.parseColor("#ed1c24"), Style.WHITE));
		STYLES.put("T2", new Style(Shape.RECT, Style.parseColor("#33b540"), Style.WHITE));
		STYLES.put("T3", new Style(Shape.RECT, Style.parseColor("#f79210"), Style.WHITE));
		STYLES.put("T5", new Style(Shape.RECT, Style.parseColor("#0994ce"), Style.WHITE));

		// Nachtbus
		STYLES.put("BN42 Jupiter", new Style(Style.parseColor("#33b540"), Style.WHITE));
	}
}
