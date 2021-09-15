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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;

import de.schildbach.pte.dto.Style;

/**
 * Has been renamed to PTV (Public Transport Vicoria).
 * 
 * @author Andreas Schildbach
 */
public class MetProvider extends AbstractEfaProvider
{
	public static final NetworkId NETWORK_ID = NetworkId.MET;
	private final static String API_BASE = "http://jp.ptv.vic.gov.au/ptv/";

	public MetProvider()
	{
		super(API_BASE);

		setLanguage("en");
		setTimeZone("Australia/Melbourne");
		setUseRouteIndexAsTripId(false);
		setStyles(STYLES);
		setSessionCookieName("BIGipServerpl_ptv_jp_lbvsvr");
	}

	public NetworkId id()
	{
		return NETWORK_ID;
	}

	@Override
	protected String parseLine(final String mot, final String symbol, final String name, final String longName, final String trainType,
			final String trainNum, final String trainName)
	{
		if ("0".equals(mot))
		{
			if ("Regional Train :".equals(longName))
				return 'R' + symbol;
			if ("Regional Train".equals(trainName))
				return "R";
			if ("vPK".equals(symbol) && "Regional Train Pakenham".equals(longName))
				return "RV/Line";
		}
		else if ("1".equals(mot))
		{
			if (trainType == null && trainNum != null)
				return 'S' + trainNum;
			if ("Metropolitan Train".equals(trainName) && trainNum == null)
				return 'S' + Strings.nullToEmpty(name);
		}

		return super.parseLine(mot, symbol, name, longName, trainType, trainNum, trainName);
	}

	private static final Map<String, Style> STYLES = new HashMap<String, Style>();

	static
	{
		STYLES.put("R", new Style(Style.parseColor("#a24ba3"), Style.WHITE));
		STYLES.put("S", new Style(Style.parseColor("#3a75c4"), Style.WHITE));
		STYLES.put("T", new Style(Style.parseColor("#5bbf21"), Style.WHITE));
		STYLES.put("B", new Style(Style.parseColor("#f77f00"), Style.WHITE));

		// TODO NightRider buses (buses with numbers > 940): #f26522
	}
}
