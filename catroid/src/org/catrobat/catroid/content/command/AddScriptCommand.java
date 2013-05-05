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
package org.catrobat.catroid.content.command;

import java.util.List;

import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.ScriptBrick;
import org.catrobat.catroid.ui.ScriptActivity;

/**
 * @author dieend
 * 
 */
public class AddScriptCommand extends Command {
	private static final long serialVersionUID = 1L;
	private Sprite targetSprite;
	private ScriptBrick scriptBrick;
	private int position;

	public AddScriptCommand(Sprite currentSprite, ScriptBrick scriptBrick, int position) {
		this.targetSprite = currentSprite;
		this.scriptBrick = scriptBrick;
		this.position = position;
		this.message = ScriptActivity.ACTION_BRICK_LIST_CHANGED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.catrobat.catroid.content.command.Command#isNeedMemento()
	 */
	@Override
	protected boolean isNeedMemento() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.catrobat.catroid.content.command.Command#execute()
	 */
	@Override
	protected String execute() {
		Sprite currentSprite = targetSprite;

		int[] temp = getScriptAndBrickIndexFromProject(position);

		int scriptPosition = temp[0];
		int brickPosition = temp[1];

		Script newScript = scriptBrick.initScript(currentSprite);
		if (currentSprite.getNumberOfBricks() > 0) {
			int addScriptTo = position == 0 ? 0 : scriptPosition + 1;
			currentSprite.addScript(addScriptTo, newScript);
		} else {
			currentSprite.addScript(newScript);
		}

		Script previousScript = currentSprite.getScript(scriptPosition);
		if (previousScript != null) {
			Brick brick;
			int size = previousScript.getBrickList().size();
			for (int i = brickPosition; i < size; i++) {
				brick = previousScript.getBrick(brickPosition);
				previousScript.removeBrick(brick);
				newScript.addBrick(brick);
			}
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.catrobat.catroid.content.command.Command#unexecute()
	 */
	@Override
	protected String unexecute() {
		int temp[] = getScriptAndBrickIndexFromProject(position);
		int scriptPosition = temp[0];
		Script script = targetSprite.getScript(scriptPosition + 1);
		Script previousScript = targetSprite.getScript(scriptPosition);
		if (script != null) {
			targetSprite.removeScript(script);
			if (previousScript != null) {
				Brick brick;
				int size = script.getBrickList().size();
				for (int i = 0; i < size; i++) {
					brick = script.getBrick(0);
					script.removeBrick(brick);
					previousScript.addBrick(brick);
				}
			}
			script = null;
			//			}
		}
		return message;
	}

	private int[] getScriptAndBrickIndexFromProject(int position) {
		int bricklistsize = targetSprite.getNumberOfBricks();
		int[] returnValue = new int[2];

		if (position >= bricklistsize) {

			returnValue[0] = targetSprite.getNumberOfScripts() - 1;
			if (returnValue[0] < 0) {
				returnValue[0] = 0;
				returnValue[1] = 0;
			} else {
				Script script = targetSprite.getScript(returnValue[0]);
				if (script != null) {
					returnValue[1] = script.getBrickList().size();
				} else {
					returnValue[1] = 0;
				}
			}

			return returnValue;
		}

		int scriptPosition = 0;
		int scriptOffset;
		for (scriptOffset = 0; scriptOffset < position;) {
			scriptOffset += targetSprite.getScript(scriptPosition).getBrickList().size() + 1;
			if (scriptOffset < position) {
				scriptPosition++;
			}
		}
		scriptOffset -= targetSprite.getScript(scriptPosition).getBrickList().size();

		returnValue[0] = scriptPosition;
		List<Brick> brickListFromProject = targetSprite.getScript(scriptPosition).getBrickList();
		int brickPosition = position;
		if (scriptOffset > 0) {
			brickPosition -= scriptOffset;
		}

		Brick brickFromProject;
		if (brickListFromProject.size() != 0 && brickPosition < brickListFromProject.size()) {
			brickFromProject = brickListFromProject.get(brickPosition);
		} else {
			brickFromProject = null;
		}

		returnValue[1] = targetSprite.getScript(scriptPosition).getBrickList().indexOf(brickFromProject);
		if (returnValue[1] < 0) {
			returnValue[1] = targetSprite.getScript(scriptPosition).getBrickList().size();
		}

		return returnValue;
	}
}
