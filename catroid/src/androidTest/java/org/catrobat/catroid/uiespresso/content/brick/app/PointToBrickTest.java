/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.uiespresso.content.brick.app;

import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.uiespresso.content.brick.utils.BrickTestUtils;
import org.catrobat.catroid.uiespresso.testsuites.Cat;
import org.catrobat.catroid.uiespresso.testsuites.Level;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickDataInteractionWrapper.onBrickAtPosition;

@RunWith(AndroidJUnit4.class)
public class PointToBrickTest {
	private int brickPosition;

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(SpriteActivity.class, SpriteActivity.EXTRA_FRAGMENT_POSITION, SpriteActivity.FRAGMENT_SCRIPTS);

	@Before
	public void setUp() throws Exception {
		brickPosition = 1;
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void pointToBrickOneSpriteTest() {
		BrickTestUtils.createProjectAndGetStartScript("PointToBrickTest")
				.addBrick(new PointToBrick());
		baseActivityTestRule.launchActivity();

		onBrickAtPosition(brickPosition).checkShowsText(R.string.brick_point_to);

		onBrickAtPosition(brickPosition).onVariableSpinner(R.id.brick_point_to_spinner)
				.checkShowsText(R.string.brick_variable_spinner_create_new_variable)
				.performSelect(R.string.brick_variable_spinner_create_new_variable);

		onView(withText(R.string.new_look_dialog_title))
				.check(matches(isDisplayed()));
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void pointToBrickTwoSpritesTest() {
		BrickTestUtils.createProjectAndGetStartScript("PointToBrickTest")
				.addBrick(new PointToBrick());
		String secondSpriteName = "secondTestSprite";
		Sprite sprite = new Sprite(secondSpriteName);
		ProjectManager.getInstance().getCurrentProject().getDefaultScene().addSprite(sprite);
		baseActivityTestRule.launchActivity();

		onBrickAtPosition(brickPosition).checkShowsText(R.string.brick_point_to);

		onBrickAtPosition(brickPosition).onVariableSpinner(R.id.brick_point_to_spinner)
				.checkShowsText(secondSpriteName);
	}
}
