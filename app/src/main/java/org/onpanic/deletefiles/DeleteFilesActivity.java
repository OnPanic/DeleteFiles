package org.onpanic.deletefiles;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.onpanic.deletefiles.constants.DeleteFilesConstants;
import org.onpanic.deletefiles.dialogs.DeleteFMItemDialog;
import org.onpanic.deletefiles.fragments.DeleteFilesSettings;
import org.onpanic.deletefiles.fragments.FileManagerFragment;
import org.onpanic.deletefiles.fragments.LockedByPermissions;
import org.onpanic.deletefiles.fragments.PathsListFragment;
import org.onpanic.deletefiles.fragments.TriggerApps;
import org.onpanic.deletefiles.permissions.PermissionManager;
import org.onpanic.deletefiles.providers.PathsProvider;

import java.util.ArrayList;

public class DeleteFilesActivity extends AppCompatActivity implements
        DeleteFilesSettings.OnTriggerAppsListener,
        PathsListFragment.OnPathListener,
        FileManagerFragment.OnSavePaths {

    private FragmentManager mFragmentManager;
    private MenuItem settingsIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();

        // Do not overlapping fragments.
        if (savedInstanceState != null) return;

        if (PermissionManager.isLollipopOrHigher() && !PermissionManager.hasExternalWritePermission(this)) {
            PermissionManager.requestExternalWritePermissions(this, DeleteFilesConstants.REQUEST_WRITE_STORAGE);
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new PathsListFragment())
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case DeleteFilesConstants.REQUEST_WRITE_STORAGE: {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    transaction.replace(R.id.fragment_container, new PathsListFragment());
                } else {
                    transaction.replace(R.id.fragment_container, new LockedByPermissions());
                }

                transaction.commit();

                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_files, menu);
        settingsIcon = menu.findItem(R.id.action_settings);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new DeleteFilesSettings())
                    .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        switch (mFragmentManager.getBackStackEntryCount()) {
            case 0:
                super.onBackPressed();
                return;
            case 1:
                settingsIcon.setVisible(true);
                break;
        }

        mFragmentManager.popBackStack();
    }

    @Override
    public void onTriggerAppsCallback() {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, new TriggerApps())
                .commit();
    }

    @Override
    public void onPathListenerCallback(int id) {
        DeleteFMItemDialog dialog = new DeleteFMItemDialog();
        Bundle arguments = new Bundle();
        arguments.putInt(PathsProvider.Path._ID, id);
        dialog.setArguments(arguments);
        dialog.show(getSupportFragmentManager(), "DeleteFMItemDialog");
    }

    @Override
    public void onFabClickCallback() {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, new FileManagerFragment())
                .commit();
    }

    @Override
    public void save(ArrayList<String> files) {
        mFragmentManager.popBackStack();
        ContentResolver cr = getContentResolver();
        for (String file : files) {
            ContentValues values = new ContentValues();
            values.put(PathsProvider.Path.PATH, file);
            cr.insert(PathsProvider.CONTENT_URI, values);
        }
    }
}
