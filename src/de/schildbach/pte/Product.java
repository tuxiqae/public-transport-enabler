/*
 * Copyright 2010 the original author or authors.
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
 * @author Andreas Schildbach
 */
public enum Product
{
	HIGH_SPEED_TRAIN, REGIONAL_TRAIN, SUBURBAN_TRAIN, SUBWAY, TRAM, BUS, FERRY, CABLECAR;

	public static Product fromCode(char code)
	{
		if (code == 'I')
			return Product.HIGH_SPEED_TRAIN;
		else if (code == 'R')
			return Product.REGIONAL_TRAIN;
		else if (code == 'S')
			return Product.SUBURBAN_TRAIN;
		else if (code == 'U')
			return Product.SUBWAY;
		else if (code == 'T')
			return Product.TRAM;
		else if (code == 'B')
			return Product.BUS;
		else if (code == 'F')
			return Product.FERRY;
		else if (code == 'C')
			return Product.CABLECAR;
		else
			throw new IllegalArgumentException(Character.toString(code));
	}
}
