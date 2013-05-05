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

import java.util.ArrayList;
import java.util.List;

/**
 * @author adinata
 * 
 */
public class CommandManager {
	private List<Command> history;
	private int currentCommand;
	private List<Object> memento;

	public CommandManager() {
		history = new ArrayList<Command>();
		currentCommand = -1;
		memento = new ArrayList<Object>();
	}

	public boolean isRedoable() {
		return currentCommand + 1 < history.size();
	}

	public boolean isUndoable() {
		return currentCommand >= 0;
	}

	public final void executeCommand(Command c) {
		storeCommand(c);
		c.execute();
	}

	private Command getTopStack() {
		Command ret = history.get(currentCommand);
		currentCommand--;
		return ret;
	}

	public String undo() {
		assert currentCommand > 0;
		Command c = getTopStack();
		return c.unexecute();
	}

	public String redo() {
		assert currentCommand + 1 < history.size();
		currentCommand += 1;
		return history.get(currentCommand).execute();
	}

	private void cleanExpiredCommand() {

	}

	protected void storeCommand(Command c) {
		if (c.isNeedMemento()) {
			memento.add(c.getMemento());
		} else {
			memento.add(null);
		}
		currentCommand += 1;
		history.add(currentCommand, c);
		cleanExpiredCommand();
	}

}
