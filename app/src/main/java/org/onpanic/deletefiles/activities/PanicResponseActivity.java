package org.onpanic.deletefiles.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.onpanic.deletefiles.constants.DeleteFilesConstants;
import org.onpanic.deletefiles.services.DeleteFilesService;

import info.guardianproject.panic.PanicResponder;

public class PanicResponseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PanicResponder.receivedTriggerFromConnectedApp(this)) {
            // File deletion can take a while
            Intent intent = new Intent(this, DeleteFilesService.class);
            intent.setAction(DeleteFilesConstants.DELETE_FILES);

            startService(intent);
        }

        finish();
    }
}
