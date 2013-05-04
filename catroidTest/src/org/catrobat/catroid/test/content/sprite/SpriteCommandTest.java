package org.catrobat.catroid.test.content.sprite;

import java.io.File;
import java.io.IOException;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.BroadcastReceiverBrick;
import org.catrobat.catroid.content.bricks.ComeToFrontBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.ScriptBrick;
import org.catrobat.catroid.content.bricks.SetLookBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.test.utils.TestUtils;
import org.catrobat.catroid.utils.Utils;
import org.junit.Test;

import android.test.InstrumentationTestCase;

public class SpriteCommandTest extends InstrumentationTestCase {
	private Script testScript;
	private Script otherScript;

	@Test
	public void testAddCommandScript() {
		Sprite currentSprite = ProjectManager.getInstance().getCurrentProject().getSpriteList().get(0);
		assertEquals("Script choosen not cat sprite", currentSprite.getName(), "cat");
		int position = 0;
		ScriptBrick scriptBrick = new BroadcastReceiverBrick();
		ProjectManager.getInstance().getCurrentProject().getCommandManager()
				.executeCommand(new AddScriptCommand(currentSprite, scriptBrick, position));
		assertEquals("Script ", currentSprite.getNumberOfScripts(), 2);
	}

	public Project createTestProject(String projectName) throws IOException {
		StorageHandler storageHandler = StorageHandler.getInstance();

		int xPosition = 457;
		int yPosition = 598;
		double size = 0.8;

		Project project = new Project(getInstrumentation().getTargetContext(), projectName);
		storageHandler.saveProject(project);
		ProjectManager.getInstance().setProject(project);
		Sprite firstSprite = new Sprite("cat");
		Sprite secondSprite = new Sprite("dog");
		Sprite thirdSprite = new Sprite("horse");
		Sprite fourthSprite = new Sprite("pig");
		testScript = new StartScript(firstSprite);
		otherScript = new StartScript(secondSprite);
		HideBrick hideBrick = new HideBrick(firstSprite);
		ShowBrick showBrick = new ShowBrick(firstSprite);
		SetLookBrick lookBrick = new SetLookBrick(firstSprite);
		File image = TestUtils.saveFileToProject(projectName, "image.png", org.catrobat.catroid.test.R.raw.icon,
				getInstrumentation().getContext(), 0);
		LookData lookData = new LookData();
		lookData.setLookFilename(image.getName());
		lookData.setLookName("name");
		lookBrick.setLook(lookData);
		SetSizeToBrick setSizeToBrick = new SetSizeToBrick(secondSprite, size);
		ComeToFrontBrick comeToFrontBrick = new ComeToFrontBrick(firstSprite);
		PlaceAtBrick placeAtBrick = new PlaceAtBrick(secondSprite, xPosition, yPosition);

		// adding Bricks: ----------------
		testScript.addBrick(hideBrick);
		testScript.addBrick(showBrick);
		testScript.addBrick(setSizeToBrick);
		testScript.addBrick(comeToFrontBrick);

		otherScript.addBrick(placeAtBrick); // secondSprite
		otherScript.setPaused(true);
		// -------------------------------

		firstSprite.addScript(testScript);
		secondSprite.addScript(otherScript);

		project.addSprite(firstSprite);
		project.addSprite(secondSprite);
		project.addSprite(thirdSprite);
		project.addSprite(fourthSprite);

		ProjectManager.getInstance().getFileChecksumContainer()
				.addChecksum(Utils.md5Checksum(image), image.getAbsolutePath());

		storageHandler.saveProject(project);
		return project;
	}
}
