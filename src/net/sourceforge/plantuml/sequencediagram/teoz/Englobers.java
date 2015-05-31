/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.Englober;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Englobers {

	private final List<Englober> englobers = new ArrayList<Englober>();

	public Englobers(TileArguments tileArguments) {
		Englober pending = null;
		for (Participant p : tileArguments.getLivingSpaces().participants()) {
			final ParticipantEnglober englober = tileArguments.getLivingSpaces().get(p).getEnglober();
			if (englober == null) {
				pending = null;
				continue;
			}
			assert englober != null;
			if (pending != null && englober == pending.getParticipantEnglober()) {
				pending.add(p);
				continue;
			}
			pending = new Englober(englober, p, tileArguments);
			englobers.add(pending);
		}
	}

	public double getOffsetForEnglobers(StringBounder stringBounder) {
		double result = 0;
		for (Englober englober : englobers) {
			final double height = englober.getPreferredHeight();
			if (height > result) {
				result = height;
			}
		}
		return result;
	}

	public void addConstraints(StringBounder stringBounder) {
		Englober last = null;
		for (Englober current : englobers) {
			current.addInternalConstraints();

			if (last != null) {
				last.addConstraintAfter(current);
			}
			last = current;
		}
	}

	public void drawEnglobers(UGraphic ug, double height, Context2D context) {
		for (Englober englober : englobers) {
			englober.drawEnglober(ug, height, context);
		}
	}

}