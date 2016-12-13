package edu.calvin.the_b_team.calvinassassingame;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class HelpActivity extends AppCompatActivity {

    private TextView help_text;
    private static final String TAG = "HelpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle(getResources().getText(R.string.help_title));

        String newString = "";
        help_text = (TextView)findViewById(R.id.help_text);

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("helpText");
                help_text.setText(newString);
            }
    }


}
