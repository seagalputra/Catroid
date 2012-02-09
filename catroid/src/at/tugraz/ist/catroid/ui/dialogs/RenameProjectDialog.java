/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.ui.dialogs;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.io.StorageHandler;
import at.tugraz.ist.catroid.ui.MainMenuActivity;
import at.tugraz.ist.catroid.ui.MyProjectsActivity;
import at.tugraz.ist.catroid.utils.Utils;

public class RenameProjectDialog extends TextDialog {

    public RenameProjectDialog(MyProjectsActivity myProjectsActivity, String projectName) {
        super(myProjectsActivity, myProjectsActivity.getString(R.string.rename_project), projectName);
        initKeyListenerAndClickListener();
    }

    public void handleOkButton() {
        String newProjectName = (input.getText().toString()).trim();
        String oldProjectName = ((MyProjectsActivity) activity).projectToEdit;

        if (newProjectName.equalsIgnoreCase(oldProjectName)) {
            activity.dismissDialog(MyProjectsActivity.DIALOG_RENAME_PROJECT);
            return;
        }
        if (StorageHandler.getInstance().projectExists(newProjectName)) {
            Utils.displayErrorMessage(activity, activity.getString(R.string.error_project_exists));
            return;
        }
        if (newProjectName != null && !newProjectName.equalsIgnoreCase("")) {

            ProjectManager projectManager = ProjectManager.getInstance();
            String currentProjectName = projectManager.getCurrentProject().getName();
            // check if is current project
            if (oldProjectName.equalsIgnoreCase(currentProjectName)) {
                projectManager.renameProject(newProjectName, activity);
                ((MyProjectsActivity) activity).updateProjectTitle();
                Utils.saveToPreferences(activity, MainMenuActivity.PREF_PROJECTNAME_KEY, newProjectName);
            } else {
                projectManager.loadProject(oldProjectName, activity, false);
                projectManager.renameProject(newProjectName, activity);
                projectManager.loadProject(currentProjectName, activity, false);
            }
            ((MyProjectsActivity) activity).initAdapter();
        } else {
            Utils.displayErrorMessage(activity, activity.getString(R.string.notification_invalid_text_entered));
            return;
        }
        activity.dismissDialog(MyProjectsActivity.DIALOG_RENAME_PROJECT);
    }

    private void initKeyListenerAndClickListener() {
        dialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String newProjectName = (input.getText().toString()).trim();
                    String oldProjectName = ((MyProjectsActivity) activity).projectToEdit;
                    if (((MyProjectsActivity) activity).projectAlreadyExists(newProjectName)
                            && !newProjectName.equalsIgnoreCase(oldProjectName)) {
                        Utils.displayErrorMessage(activity, activity.getString(R.string.error_project_exists));
                    } else if (newProjectName.equalsIgnoreCase("")) {
                        Utils.displayErrorMessage(activity,
                                activity.getString(R.string.notification_invalid_text_entered));
                    } else {
                        handleOkButton();
                    }
                    return true;
                }
                return false;
            }
        });

        buttonPositive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOkButton();
            }
        });

        buttonNegative.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.dismissDialog(MyProjectsActivity.DIALOG_RENAME_PROJECT);
            }
        });
    }
}
