/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.content.actions;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;
import org.catrobat.catroid.content.actions.GlideToAction;
import org.catrobat.catroid.formulaeditor.Formula;

import android.test.AndroidTestCase;

public class PlaceAtBrickTest extends AndroidTestCase {

	private Formula xPosition = new Formula(100);
	private Formula yPosition = new Formula(200);

	public void testNormalBehavior() {
		Sprite sprite = new Sprite("testSprite");
		assertEquals("Unexpected initial sprite x position", 0f, sprite.look.getXPosition());
		assertEquals("Unexpected initial sprite y position", 0f, sprite.look.getYPosition());

		GlideToAction action = ExtendedActions.placeAt(sprite, xPosition, yPosition);
		sprite.look.addAction(action);
		action.act(1.0f);

		assertEquals("Incorrect sprite x position after PlaceAtBrick executed", xPosition,
				(int) sprite.look.getXPosition());
		assertEquals("Incorrect sprite y position after PlaceAtBrick executed", yPosition,
				(int) sprite.look.getYPosition());
	}

	public void testNullSprite() {
		GlideToAction action = ExtendedActions.placeAt(sprite, xPosition, yPosition);
		try {
			action.act(1.0f);
			fail("Execution of PlaceAtBrick with null Sprite did not cause a " + "NullPointerException to be thrown");
		} catch (NullPointerException e) {
			// expected behavior
		}
	}

	public void testBoundaryPositions() {
		Sprite sprite = new Sprite("testSprite");

		GlideToAction action = ExtendedActions.placeAt(sprite, new Formula(Integer.MAX_VALUE), new Formula(
				Integer.MAX_VALUE));
		sprite.look.addAction(action);
		action.act(1.0f);

		assertEquals("PlaceAtBrick failed to place Sprite at maximum x integer value", Integer.MAX_VALUE,
				(int) sprite.look.getXPosition());
		assertEquals("PlaceAtBrick failed to place Sprite at maximum y integer value", Integer.MAX_VALUE,
				(int) sprite.look.getYPosition());

		action = ExtendedActions.placeAt(sprite, new Formula(Integer.MIN_VALUE), new Formula(Integer.MIN_VALUE));
		sprite.look.addAction(action);
		action.act(1.0f);

		assertEquals("PlaceAtBrick failed to place Sprite at minimum x integer value", Integer.MIN_VALUE,
				(int) sprite.look.getXPosition());
		assertEquals("PlaceAtBrick failed to place Sprite at minimum y integer value", Integer.MIN_VALUE,
				(int) sprite.look.getYPosition());
	}
}
