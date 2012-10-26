/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.ui.ScriptTabActivity;
import org.catrobat.catroid.ui.dialogs.BrickTextDialog;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;

public class SetGravityBrick implements Brick, OnClickListener {
	private static final long serialVersionUID = 1L;

	private transient PhysicWorld physicWorld;
	private Sprite sprite;
	private Vector2 gravity;

	private transient View view;

	public SetGravityBrick() {
	}

	public SetGravityBrick(PhysicWorld physicWorld, Sprite sprite, Vector2 gravity) {
		this.physicWorld = physicWorld;
		this.sprite = sprite;
		this.gravity = gravity.cpy();
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public void execute() {
		physicWorld.setGravity(gravity);
	}

	public void setPhysicWorld(PhysicWorld physicWorld) {
		this.physicWorld = physicWorld;
	}

	@Override
	public Sprite getSprite() {
		return this.sprite;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter adapter) {
		view = View.inflate(context, R.layout.brick_set_gravity, null);

		EditText editX = (EditText) view.findViewById(R.id.brick_set_gravity_x_edit_text);
		editX.setText(String.valueOf(gravity.x));

		editX.setOnClickListener(this);

		EditText editY = (EditText) view.findViewById(R.id.brick_set_gravity_y_edit_text);
		editY.setText(String.valueOf(gravity.y));

		editY.setOnClickListener(this);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		return View.inflate(context, R.layout.brick_set_gravity, null);
	}

	@Override
	public Brick clone() {
		return new SetGravityBrick(physicWorld, sprite, gravity);
	}

	@Override
	public void onClick(final View view) {
		ScriptTabActivity activity = (ScriptTabActivity) view.getContext();

		BrickTextDialog editDialog = new BrickTextDialog() {
			@Override
			protected void initialize() {
				if (view.getId() == R.id.brick_set_gravity_x_edit_text) {
					input.setText(String.valueOf(gravity.x));
				} else if (view.getId() == R.id.brick_set_gravity_y_edit_text) {
					input.setText(String.valueOf(gravity.y));
				}
				input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
				input.setSelectAllOnFocus(true);
			}

			@Override
			protected boolean handleOkButton() {
				try {
					if (view.getId() == R.id.brick_set_gravity_x_edit_text) {
						gravity.x = Integer.parseInt(input.getText().toString());
					} else if (view.getId() == R.id.brick_set_gravity_y_edit_text) {
						gravity.y = Integer.parseInt(input.getText().toString());
					}
				} catch (NumberFormatException exception) {
					Toast.makeText(getActivity(), R.string.error_no_number_entered, Toast.LENGTH_SHORT).show();
				}

				return true;
			}
		};

		editDialog.show(activity.getSupportFragmentManager(), "dialog_set_gravity_brick");
	}
}