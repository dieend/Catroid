package org.catrobat.catroid.uitest.content.command;

import java.io.File;
import java.util.ArrayList;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.adapter.BrickAdapter;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;

public class TimeCapsuleTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	private Solo solo;

	public TimeCapsuleTest() {
		super(MainMenuActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		UiTestUtils.prepareStageForTest();
		solo = new Solo(getInstrumentation(), getActivity());
		createProject();
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		UiTestUtils.clearAllUtilTestProjects();
		super.tearDown();
		solo = null;
	}

	public void testAddNewScript() {
		UiTestUtils.getIntoScriptActivityFromMainMenu(solo);
		ListView view = solo.getCurrentListViews().get(1);
		BrickAdapter adapter = (BrickAdapter) view.getAdapter();
		int brickNumber = adapter.getCount();

		UiTestUtils.addNewBrick(solo, R.string.brick_when_started);
		solo.clickOnScreen(200, 200);

		assertEquals("Wrong number of Brick", brickNumber + 1, adapter.getCount());

		solo.goBack();
		solo.goBack();
		solo.clickOnMenuItem(solo.getString(R.string.undo));
		adapter.updateProjectBrickList();
		assertEquals("Wrong number of Brick", brickNumber, adapter.getCount());

		solo.clickOnMenuItem(solo.getString(R.string.redo));
		adapter.updateProjectBrickList();
		assertEquals("Wrong number of Brick", brickNumber + 1, adapter.getCount());
	}

	private void createProject() {
		Project project = new Project(null, UiTestUtils.PROJECTNAME1);

		Sprite spriteCat = new Sprite("Background");
		Script startScriptCat = new StartScript(spriteCat);
		Script scriptTappedCat = new WhenScript(spriteCat);
		Brick setXBrick = new SetXBrick(spriteCat, 50);
		Brick setYBrick = new SetYBrick(spriteCat, 50);
		Brick changeXBrick = new ChangeXByNBrick(spriteCat, 50);
		startScriptCat.addBrick(setYBrick);
		startScriptCat.addBrick(setXBrick);
		scriptTappedCat.addBrick(changeXBrick);

		spriteCat.addScript(startScriptCat);
		spriteCat.addScript(scriptTappedCat);
		project.addSprite(spriteCat);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(spriteCat);
		ProjectManager.getInstance().setCurrentScript(startScriptCat);

		File imageFile = UiTestUtils.saveFileToProject(project.getName(), "catroid_sunglasses.png",
				org.catrobat.catroid.uitest.R.drawable.catroid_sunglasses, getActivity(), UiTestUtils.FileTypes.IMAGE);

		ProjectManager projectManager = ProjectManager.getInstance();
		ArrayList<LookData> lookDataList = projectManager.getCurrentSprite().getLookDataList();
		LookData lookData = new LookData();
		lookData.setLookFilename(imageFile.getName());
		lookData.setLookName("Catroid sun");
		lookDataList.add(lookData);
		projectManager.getFileChecksumContainer().addChecksum(lookData.getChecksum(), lookData.getAbsolutePath());

		File soundFile = UiTestUtils.saveFileToProject(project.getName(), "longsound.mp3",
				org.catrobat.catroid.uitest.R.raw.longsound, getInstrumentation().getContext(),
				UiTestUtils.FileTypes.SOUND);
		SoundInfo soundInfo = new SoundInfo();
		soundInfo.setSoundFileName(soundFile.getName());
		soundInfo.setTitle("longsound");

		ArrayList<SoundInfo> soundInfoList = ProjectManager.getInstance().getCurrentSprite().getSoundList();
		soundInfoList.add(soundInfo);
		ProjectManager.getInstance().getFileChecksumContainer()
				.addChecksum(soundInfo.getChecksum(), soundInfo.getAbsolutePath());
	}

	private void addNewSprite(String spriteName) {
		solo.sleep(500);
		UiTestUtils.clickOnBottomBar(solo, R.id.button_add);
		solo.waitForText(solo.getString(R.string.new_sprite_dialog_title));

		EditText addNewSpriteEditText = solo.getEditText(0);
		//check if hint is set
		assertEquals("Not the proper hint set", solo.getString(R.string.new_sprite_dialog_default_sprite_name),
				addNewSpriteEditText.getHint());
		assertEquals("There should no text be set", "", addNewSpriteEditText.getText().toString());
		solo.enterText(0, spriteName);
		solo.clickOnButton(solo.getString(R.string.ok));
		solo.sleep(200);
	}
}
