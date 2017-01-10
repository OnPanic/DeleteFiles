package org.onpanic.deletefiles;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.onpanic.deletefiles.constants.DeleteFilesConstants;
import org.onpanic.deletefiles.fragments.DeleteFilesSettings;
import org.onpanic.deletefiles.fragments.LockedByPermissions;
import org.onpanic.deletefiles.fragments.TriggerApps;
import org.onpanic.deletefiles.permissions.PermissionManager;

public class DeleteFilesActivity extends AppCompatActivity implements
        DeleteFilesSettings.OnTriggerAppsListener {

    private FragmentManager mFragmentManager;

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
            // TODO
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case DeleteFilesConstants.REQUEST_WRITE_STORAGE: {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO
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
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            mFragmentManager.popBackStack();
        }
    }

    @Override
    public void onTriggerAppsCallback() {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, new TriggerApps())
                .commit();
    }
}
