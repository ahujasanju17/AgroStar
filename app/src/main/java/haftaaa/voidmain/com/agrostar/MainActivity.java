package haftaaa.voidmain.com.agrostar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private ListView myList;
    private SearchView searchView;
    private SearchHelper mDbHelper;
    private MyCustomAdapter defaultAdapter;
    private ArrayList<String> nameList;
    private String[] Titles;
    private String[] Prices;
    private TypedArray Icons;
    private TypedArray Icons_Image;
    private ArrayList<ListItem> items;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    private TextView text_featured;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        text_featured = (TextView) findViewById(R.id.text_featured);
        text_featured.setText("Featured Products");

        Titles = getResources().getStringArray(R.array.list_items);
        Prices= getResources().getStringArray(R.array.list_items_price);
        Icons = getResources().obtainTypedArray(R.array.listicons);
        items = new ArrayList<ListItem>();
        items.add(new ListItem(Titles[0], Icons.getResourceId(0, -1), Prices[0]));
        items.add(new ListItem(Titles[1], Icons.getResourceId(1, -1), Prices[1]));
        items.add(new ListItem(Titles[2], Icons.getResourceId(2, -1), Prices[2]));
        items.add(new ListItem(Titles[3], Icons.getResourceId(3, -1), Prices[3]));
        items.add(new ListItem(Titles[4], Icons.getResourceId(4, -1), Prices[4]));
        items.add(new ListItem(Titles[5], Icons.getResourceId(5, -1), Prices[5]));
        items.add(new ListItem(Titles[6], Icons.getResourceId(6, -1), Prices[6]));
        items.add(new ListItem(Titles[7], Icons.getResourceId(7, -1), Prices[7]));
        items.add(new ListItem(Titles[8], Icons.getResourceId(8, -1), Prices[8]));
        items.add(new ListItem(Titles[9], Icons.getResourceId(9, -1), Prices[9]));
        Icons.recycle();



        nameList = new ArrayList<String>();

        //for simplicity we will add the same name for 20 times to populate the nameList view
        for (int i = 0; i < 10; i++) {
            nameList.add(Titles[i]);
        }

        //relate the listView from java to the one created in xml
        myList = (ListView) findViewById(R.id.list);

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this nameList and for producing
        // a view to represent an item in that data set.
        defaultAdapter = new MyCustomAdapter(getApplicationContext(), items);
        myList.setAdapter(defaultAdapter);

        //prepare the SearchView
        searchView = (SearchView) toolbar.findViewById(R.id.search);

        //Sets the default or resting state of the search field. If true, a single search icon is shown by default and
        // expands to show the text field and other buttons when pressed. Also, if the default state is iconified, then it
        // collapses to that state when the close button is pressed. Changes to this property will take effect immediately.
        //The default value is true.
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        mDbHelper = new SearchHelper(this);
        mDbHelper.open();

        //Clear all names
        mDbHelper.deleteAllNames();


        // Create the list of names which will be displayed on search
        for (String name : nameList) {
            mDbHelper.createList(name);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {

            case R.id.action_eng:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.action_hin:
                Intent i1 = new Intent(this,MainActivityHindi.class);
                startActivity(i1);
                finish();
                return true;

        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDbHelper  != null) {
            mDbHelper.close();
        }
    }

    @Override
    public boolean onClose() {
        myList.setAdapter(defaultAdapter);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        displayResults(query + "*");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            displayResults(newText + "*");
        } else {
            myList.setAdapter(defaultAdapter);
        }

        return false;
    }

    /**
     * Method used for performing the search and displaying the results. This method is called every time a letter
     * is introduced in the search field.
     *
     * @param query Query used for performing the search
     */
    private void displayResults(String query) {

        Cursor cursor = mDbHelper.searchByInputText((query != null ? query : "@@@@"));

        if (cursor != null) {

            String[] from = new String[] {SearchHelper.COLUMN_NAME};

            // Specify the view where we want the results to go
            int[] to = new int[] {R.id.search_result_text_view};

            // Create a simple cursor adapter to keep the search data
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.result_search_item, cursor, from, to);
            myList.setAdapter(cursorAdapter);

            // Click listener for the searched item that was selected
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) myList.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    String selectedName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    Toast.makeText(MainActivity.this, selectedName, Toast.LENGTH_SHORT).show();

                    // Set the default adapter
                    myList.setAdapter(defaultAdapter);

                    // Find the position for the original list by the selected name from search
                    for (int pos = 0; pos < nameList.size(); pos++) {
                        if (nameList.get(pos).equals(selectedName)){
                            position = pos;
                            break;
                        }
                    }

                    // Create a handler. This is necessary because the adapter has just been set on the list again and
                    // the list might not be finished setting the adapter by the time we perform setSelection.
                    Handler handler = new Handler();
                    final int finalPosition = position;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            myList.setSelection(finalPosition);
                        }
                    });

                    searchView.setQuery("",true);
                }
            });

        }
    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }

    public static class SwipeFragment extends Fragment {
        private TypedArray Icons_Image;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            Icons_Image = getResources().obtainTypedArray(R.array.listicons);
            int imgResId = Icons_Image.getResourceId(position , -1);
            imageView.setImageResource(imgResId);
            return swipeView;
        }

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }

}