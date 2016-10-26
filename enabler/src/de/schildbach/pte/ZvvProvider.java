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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import de.schildbach.pte.dto.Line;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.Style;
import de.schildbach.pte.dto.Style.Shape;

/**
 * @author Andreas Schildbach
 */
public class ZvvProvider extends AbstractHafasProvider {
    private static final String API_BASE = "https://online.fahrplan.zvv.ch/bin/";
    private static final Product[] PRODUCTS_MAP = { Product.HIGH_SPEED_TRAIN, Product.HIGH_SPEED_TRAIN,
            Product.REGIONAL_TRAIN, Product.REGIONAL_TRAIN, Product.FERRY, Product.SUBURBAN_TRAIN, Product.BUS,
            Product.CABLECAR, Product.SUBWAY, Product.TRAM };

    public ZvvProvider() {
        super(NetworkId.ZVV, API_BASE, "dn", PRODUCTS_MAP);

        setJsonGetStopsEncoding(Charsets.UTF_8);
        setJsonNearbyLocationsEncoding(Charsets.UTF_8);
        setStyles(STYLES);
    }

    @Override
    public Set<Product> defaultProducts() {
        return Product.ALL;
    }

    private static final String[] OPERATORS = { "SBB", "SZU" };
    private static final String[] PLACES = { "Zürich", "Winterthur" };

    @Override
    protected String[] splitStationName(String name) {
        for (final String operator : OPERATORS) {
            if (name.endsWith(" " + operator)) {
                name = name.substring(0, name.length() - operator.length() - 1);
                break;
            }

            if (name.endsWith(" (" + operator + ")")) {
                name = name.substring(0, name.length() - operator.length() - 3);
                break;
            }
        }

        final Matcher m = P_SPLIT_NAME_FIRST_COMMA.matcher(name);
        if (m.matches())
            return new String[] { m.group(1), m.group(2) };

        for (final String place : PLACES)
            if (name.startsWith(place + " ") || name.startsWith(place + ","))
                return new String[] { place, name.substring(place.length() + 1) };

        return super.splitStationName(name);
    }

    @Override
    protected String[] splitPOI(final String poi) {
        final Matcher m = P_SPLIT_NAME_FIRST_COMMA.matcher(poi);
        if (m.matches())
            return new String[] { m.group(1), m.group(2) };

        return super.splitStationName(poi);
    }

    @Override
    protected String[] splitAddress(final String address) {
        final Matcher m = P_SPLIT_NAME_FIRST_COMMA.matcher(address);
        if (m.matches())
            return new String[] { m.group(1), m.group(2) };

        return super.splitStationName(address);
    }

    private static final Pattern P_NUMBER = Pattern.compile("\\d{2,5}");

    @Override
    protected Line parseLineAndType(final String lineAndType) {
        final Matcher m = P_NORMALIZE_LINE_AND_TYPE.matcher(lineAndType);
        if (m.matches()) {
            final String number = Strings.emptyToNull(m.group(1).replaceAll("\\s+", ""));
            final String type = Strings.emptyToNull(m.group(2));

            if ("Bus".equals(type))
                return newLine(Product.BUS, stripPrefix(number, "Bus"), null);
            if ("Bus-NF".equals(type))
                return newLine(Product.BUS, stripPrefix(number, "Bus", "Bus-NF"), null, Line.Attr.WHEEL_CHAIR_ACCESS);
            if ("Tro".equals(type) || "Trolley".equals(type))
                return newLine(Product.BUS, stripPrefix(number, "Tro"), null);
            if ("Tro-NF".equals(type))
                return newLine(Product.BUS, stripPrefix(number, "Tro", "Tro-NF"), null, Line.Attr.WHEEL_CHAIR_ACCESS);
            if ("Trm".equals(type))
                return newLine(Product.TRAM, stripPrefix(number, "Trm"), null);
            if ("Trm-NF".equals(type))
                return newLine(Product.TRAM, stripPrefix(number, "Trm", "Trm-NF"), null, Line.Attr.WHEEL_CHAIR_ACCESS);

            if ("S18".equals(number))
                return newLine(Product.SUBURBAN_TRAIN, "S18", null);

            if (number != null && P_NUMBER.matcher(number).matches())
                return newLine(null, number, null);

            if (type != null) {
                final Product product = normalizeType(type);
                if (product != null)
                    return newLine(product, number, null);
            }

            throw new IllegalStateException(
                    "cannot normalize type " + type + " number " + number + " line#type " + lineAndType);
        }

        throw new IllegalStateException("cannot normalize line#type " + lineAndType);
    }

    private String stripPrefix(final String str, final String... prefixes) {
        for (final String prefix : prefixes)
            if (str.startsWith(prefix))
                return str.substring(prefix.length());

        return str;
    }

    @Override
    protected Product normalizeType(final String type) {
        final String ucType = type.toUpperCase();

        if ("N".equals(ucType)) // Nachtbus
            return Product.BUS;
        if ("TX".equals(ucType))
            return Product.BUS;
        if ("KB".equals(ucType)) // Kleinbus?
            return Product.BUS;

        if ("TE2".equals(ucType)) // Basel - Strasbourg
            return Product.REGIONAL_TRAIN;

        if ("D-SCHIFF".equals(ucType))
            return Product.FERRY;
        if ("DAMPFSCH".equals(ucType))
            return Product.FERRY;

        if ("BERGBAHN".equals(ucType))
            return Product.CABLECAR;
        if ("LSB".equals(ucType)) // Luftseilbahn
            return Product.CABLECAR;
        if ("SLB".equals(ucType)) // Sesselliftbahn
            return Product.CABLECAR;

        return super.normalizeType(type);
    }

    private static final Map<String, Style> STYLES = new HashMap<String, Style>();

    static {
        // S-Bahn
        STYLES.put("SS2", new Style(Shape.RECT, Style.parseColor("#70c82c"), Style.WHITE));
        STYLES.put("SS3", new Style(Shape.RECT, Style.parseColor("#587AC2"), Style.WHITE));
        STYLES.put("SS4", new Style(Shape.RECT, Style.parseColor("#EE7267"), Style.WHITE));
        STYLES.put("SS5", new Style(Shape.RECT, Style.parseColor("#6aadc3"), Style.WHITE));
        STYLES.put("SS6", new Style(Shape.RECT, Style.parseColor("#6f41a4"), Style.WHITE));
        STYLES.put("SS7", new Style(Shape.RECT, Style.parseColor("#fbb809"), Style.BLACK));
        STYLES.put("SS8", new Style(Shape.RECT, Style.parseColor("#562691"), Style.WHITE));
        STYLES.put("SS9", new Style(Shape.RECT, Style.parseColor("#069A5D"), Style.WHITE));
        STYLES.put("SS10", new Style(Shape.RECT, Style.parseColor("#fbc434"), Style.BLACK));
        STYLES.put("SS11", new Style(Shape.RECT, Style.parseColor("#ae90cf"), Style.WHITE));
        STYLES.put("SS12", new Style(Shape.RECT, Style.parseColor("#ed1c24"), Style.WHITE));
        STYLES.put("SS13", new Style(Shape.RECT, Style.parseColor("#905723"), Style.WHITE));
        STYLES.put("SS14", new Style(Shape.RECT, Style.parseColor("#753c0c"), Style.WHITE));
        STYLES.put("SS15", new Style(Shape.RECT, Style.parseColor("#c79f73"), Style.WHITE));
        STYLES.put("SS16", new Style(Shape.RECT, Style.parseColor("#68c971"), Style.WHITE));
        STYLES.put("SS17", new Style(Shape.RECT, Style.parseColor("#3b99b5"), Style.WHITE));
        STYLES.put("SS18", new Style(Shape.RECT, Style.parseColor("#f14337"), Style.WHITE));
        STYLES.put("SS21", new Style(Shape.RECT, Style.parseColor("#9acaee"), Style.WHITE));
        STYLES.put("SS22", new Style(Shape.RECT, Style.parseColor("#8dd24e"), Style.WHITE));
        STYLES.put("SS24", new Style(Shape.RECT, Style.parseColor("#ab7745"), Style.WHITE));
        STYLES.put("SS26", new Style(Shape.RECT, Style.parseColor("#0e87aa"), Style.WHITE));
        STYLES.put("SS29", new Style(Shape.RECT, Style.parseColor("#3dba56"), Style.WHITE));
        STYLES.put("SS30", new Style(Shape.RECT, Style.parseColor("#0b8ed8"), Style.WHITE));
        STYLES.put("SS33", new Style(Shape.RECT, Style.parseColor("#51aae3"), Style.WHITE));
        STYLES.put("SS35", new Style(Shape.RECT, Style.parseColor("#81c0eb"), Style.WHITE));
        STYLES.put("SS40", new Style(Shape.RECT, Style.parseColor("#ae90cf"), Style.WHITE));
        STYLES.put("SS41", new Style(Shape.RECT, Style.parseColor("#f89a83"), Style.WHITE));
        STYLES.put("SS55", new Style(Shape.RECT, Style.parseColor("#905723"), Style.WHITE));

        // Tram
        STYLES.put("T2", new Style(Shape.RECT, Style.parseColor("#ed1c24"), Style.WHITE));
        STYLES.put("T3", new Style(Shape.RECT, Style.parseColor("#19ae48"), Style.WHITE));
        STYLES.put("T4", new Style(Shape.RECT, Style.parseColor("#453fa0"), Style.WHITE));
        STYLES.put("T5", new Style(Shape.RECT, Style.parseColor("#8c5a2c"), Style.WHITE));
        STYLES.put("T6", new Style(Shape.RECT, Style.parseColor("#d6973c"), Style.WHITE));
        STYLES.put("T7", new Style(Shape.RECT, Style.parseColor("#231f20"), Style.WHITE));
        STYLES.put("T8", new Style(Shape.RECT, Style.parseColor("#99d420"), Style.BLACK));
        STYLES.put("T9", new Style(Shape.RECT, Style.parseColor("#453fa0"), Style.WHITE));
        STYLES.put("T10", new Style(Shape.RECT, Style.parseColor("#ee1998"), Style.WHITE));
        STYLES.put("T11", new Style(Shape.RECT, Style.parseColor("#19ae48"), Style.WHITE));
        STYLES.put("T12", new Style(Shape.RECT, Style.parseColor("#85d7e3"), Style.BLACK));
        STYLES.put("T13", new Style(Shape.RECT, Style.parseColor("#fdd205"), Style.BLACK));
        STYLES.put("T14", new Style(Shape.RECT, Style.parseColor("#2cbbf2"), Style.WHITE));
        STYLES.put("T15", new Style(Shape.RECT, Style.parseColor("#ed1c24"), Style.WHITE));
        STYLES.put("T17", new Style(Shape.RECT, Style.parseColor("#9e1a6e"), Style.WHITE));

        // Bus/Trolley
        STYLES.put("B31", new Style(Shape.RECT, Style.parseColor("#999bd3"), Style.WHITE));
        STYLES.put("B32", new Style(Shape.RECT, Style.parseColor("#d8a1d6"), Style.BLACK));
        STYLES.put("B33", new Style(Shape.RECT, Style.parseColor("#e4e793"), Style.BLACK));
    }
}
