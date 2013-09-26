package com.blogspot.tonyatkins.pigskin.activity;

import java.io.File;

import com.blogspot.tonyatkins.pigskin.Constants;
import com.blogspot.tonyatkins.pigskin.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ToolsActivity extends Activity {
	public static final int REQUEST_CODE = -9753;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tools);
		
		Button deleteIndexButton = (Button) findViewById(R.id.toolsDictionaryButton);
		deleteIndexButton.setOnClickListener(new DeleteIndexListener());
	}
	
	private class DeleteIndexListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			File indexDirectory = new File(Constants.INDEX_DIRECTORY);
			if (indexDirectory.exists() && indexDirectory.isDirectory()) {
				deleteRecursively(indexDirectory);
			}
		}

		private void deleteRecursively(File file) {
			if (!file.exists()) return;
			if (file.isDirectory()) {
				for (File childFile : file.listFiles()) {
					deleteRecursively(childFile);
				}
				file.delete();
			}
			else {
				file.delete();
			}
		}
	}
}
