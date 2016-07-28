/*
 * Copyright 2016 the original author or authors.
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

import javax.annotation.Nullable;

import de.schildbach.pte.dto.Line;
import de.schildbach.pte.dto.Product;

/**
 * @author Andreas Schildbach
 */
public class RtaChicagoProvider extends AbstractEfaProvider
{
	private final static String API_BASE = "http://tripplanner.rtachicago.com/ccg3/";
	// "http://elb-jpinstances-1463028547.us-east-1.elb.amazonaws.com/ccg3/";

	public RtaChicagoProvider()
	{
		super(NetworkId.RTACHICAGO, API_BASE);

		setLanguage("en");
		setTimeZone("America/Chicago");
	}

	@Override
	protected Line parseLine(final @Nullable String id, final @Nullable String network, final @Nullable String mot, final @Nullable String symbol,
			final @Nullable String name, final @Nullable String longName, final @Nullable String trainType, final @Nullable String trainNum,
			final @Nullable String trainName)
	{
		if ("0".equals(mot))
		{
			if ("UP-N".equals(symbol)) // Union Pacific North Line
				return new Line(id, network, Product.SUBURBAN_TRAIN, "UP-N");
			if ("UP-NW".equals(symbol)) // Union Pacific Northwest Line
				return new Line(id, network, Product.SUBURBAN_TRAIN, "UP-N");
			if ("UP-W".equals(symbol)) // Union Pacific West Line
				return new Line(id, network, Product.SUBURBAN_TRAIN, "UP-NW");
		}

		return super.parseLine(id, network, mot, symbol, name, longName, trainType, trainNum, trainName);
	}
}
