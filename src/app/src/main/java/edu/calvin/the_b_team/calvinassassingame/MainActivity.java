package edu.calvin.the_b_team.calvinassassingame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the text of the prototype label
        TextView tv = (TextView) findViewById (R.id.main_page_text_label);
        tv.setText(getResources().getString(R.string.main_page_text));
    }
}
