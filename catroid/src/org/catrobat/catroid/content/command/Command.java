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

/**
 * @author adinata
 * 
 */
public abstract class Command {
	private static class Manager {
		private static Manager instance;
		private List<Command> history;
		private int currentHistory;

		public static Manager instance() {
			if (instance == null) {
				instance = new Manager();
			}
			return instance;
		}

		private void cleanExpiredCommand() {

		}

		public void storeCommand(Command c) {
			cleanExpiredCommand();
			if (c.isNeedMemento()) {

			}
		}

		public Command getTopStack() {
			Command ret = history.get(currentHistory);
			currentHistory--;
			return ret;
		}

	}

	private static final void storeCommand(Command c) {
		// save command in memory / local storage
		Manager.instance().storeCommand(c);
	}

	public static final void undo() {
		// load latest undoable command
		Command c = Manager.instance().getTopStack();
		c.unexecute();
	}

	public static final void redo() {

	}

	public static final boolean isRedoable() {
		return false;
	}

	public static final boolean isUndoable() {
		return false;
	}

	public final void executeCommand() {
		Command.storeCommand(this);
		execute();
	}

	protected abstract boolean isNeedMemento();

	protected abstract void execute();

	protected abstract void unexecute();
}
