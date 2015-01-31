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

import de.schildbach.pte.dto.Line;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;

/**
 * @author Andreas Schildbach
 */
public class VgnProvider extends AbstractEfaProvider
{
	public static final NetworkId NETWORK_ID = NetworkId.VGN;
	private static final String DEPARTURE_MONITOR_ENDPOINT = "XML_DM_REQUEST";
	private static final String TRIP_ENDPOINT = "XML_TRIP_REQUEST2";

	public VgnProvider(final String apiBase)
	{
		super(apiBase, DEPARTURE_MONITOR_ENDPOINT, TRIP_ENDPOINT, null, null);
	}

	public NetworkId id()
	{
		return NETWORK_ID;
	}

	@Override
	protected Line parseLine(final String id, final String mot, final String symbol, final String name, final String longName,
			final String trainType, final String trainNum, final String trainName)
	{
		if ("0".equals(mot))
		{
			if ("R5(z)".equals(trainNum))
				return new Line(id, Product.REGIONAL_TRAIN, "R5(z)");
			if ("R7(z)".equals(trainNum))
				return new Line(id, Product.REGIONAL_TRAIN, "R7(z)");
			if ("R8(z)".equals(trainNum))
				return new Line(id, Product.REGIONAL_TRAIN, "R8(z)");
		}

		return super.parseLine(id, mot, symbol, name, longName, trainType, trainNum, trainName);
	}

	@Override
	protected String xsltTripRequestParameters(final Location from, final Location via, final Location to, final Date date, final boolean dep,
			final Collection<Product> products, final WalkSpeed walkSpeed, final Accessibility accessibility, final Set<Option> options)
	{
		return super.xsltTripRequestParameters(from, via, to, date, dep, products, walkSpeed, accessibility, options) + "&itdLPxx_showTariffLevel=1";
	}
}
