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

/**
 * Verkehrsverbund Vogtland
 * 
 * @author Andreas Schildbach
 */
public class VvvProvider extends AbstractEfaProvider
{
	public static final NetworkId NETWORK_ID = NetworkId.VVV;
	private final static String API_BASE = "http://195.30.98.162:8081/vvv2/";

	public VvvProvider()
	{
		super(API_BASE);
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
			if ("VL1".equals(trainNum))
				return "RVL1";
			if ("VL2".equals(trainNum))
				return "RVL2";
			if ("VL3".equals(trainNum))
				return "RVL3";
			if ("VL4".equals(trainNum))
				return "RVL4";
			if ("VL5".equals(trainNum))
				return "RVL5";
		}

		return super.parseLine(mot, symbol, name, longName, trainType, trainNum, trainName);
	}
}
