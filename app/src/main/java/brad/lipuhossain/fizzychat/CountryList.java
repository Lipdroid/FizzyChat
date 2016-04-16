package brad.lipuhossain.fizzychat;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Lipu Hossain on 16/04/2016.
 */
public class CountryList extends ListActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView l = this.getListView();
        l.setVerticalScrollBarEnabled(false);
        setListAdapter(new ArrayAdapter<String>(CountryList.this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.CountryName)));

    }

    @Override

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent returnIntent = new Intent();
        String value = (String)l.getItemAtPosition(position);
        returnIntent.putExtra("result",value );
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
