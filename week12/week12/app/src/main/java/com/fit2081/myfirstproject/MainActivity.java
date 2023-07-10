package com.fit2081.myfirstproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fit2081.myfirstproject.provider.Book;
import com.fit2081.myfirstproject.provider.BookRepo;
import com.fit2081.myfirstproject.provider.BookViewModel;
import com.fit2081.myfirstproject.provider.RecycleViewFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private DatabaseReference databaseRef;
    GestureDetectorCompat mDetector;
    int counter = 0;
    int X; int Y;
    View gestureView;
    DatabaseReference cloudDB;
    DatabaseReference bookTable;

    public Boolean ifExisted = false;
    public static final String TAG="week3tag";
    public static final String title_Key = "title_key";
    public static final String ISBN_Key = "ISBN_key";
    public static final String price_Key = "price_key";
    public static final String bookID_key = "bookId_key";
    public static final String desc_key = "desc_key";
    public static final String author_key = "author_key";

    public static final String open ="open_key";
    private EditText Title;
    private EditText ISBN;
    private  EditText Price;
    private  EditText BookId;
    private EditText Desc;
    private EditText Author;

    private String lastTitle;
    private String lastISBN;
    private  String lastPrice;
    private  String lastBookId;
    private String lastDesc;
    private String lastAuthor;

    private ArrayList<String> datasource;
    private ArrayAdapter<String> adapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BookAdapter adapter1;

    private ArrayList<Book> db;

    private BookViewModel bookViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
            Title = findViewById(R.id.Title);
            ISBN = findViewById(R.id.ISBN);
            Price = findViewById(R.id.Price);
            BookId = findViewById(R.id.bookID);
            Desc = findViewById(R.id.Description);
            Author = findViewById(R.id.Author);

            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            toolbar = findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.open_nav);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

        // find adapter by ID, setting up an adapter in listview
//        lv = findViewById(R.id.list_view);
        datasource = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datasource);
//        lv.setAdapter(adapter);
        // set navigation view function

        navigationView.setNavigationItemSelectedListener(new Navhandler());

        // set up fab

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view);
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        //init array db for book
        db = new ArrayList<>();

//        recyclerView = findViewById(R.id.recycle_view_book);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        adapter1 = new BookAdapter();
//        recyclerView.setAdapter(adapter1);


        SharedPreferences sp = getSharedPreferences("bookIfor.ext",MODE_PRIVATE);
        lastTitle = sp.getString(title_Key, lastTitle);
        lastISBN = sp.getString(ISBN_Key, lastISBN);
        lastPrice = sp.getString(price_Key, lastPrice);
        lastBookId = sp.getString(bookID_key,lastBookId);
        lastDesc = sp.getString(desc_key, lastDesc);
        lastAuthor = sp.getString(author_key,lastAuthor);

        Log.d(TAG,"OnCreate");
        if (savedInstanceState == null) {
            Desc.setText(sp.getString(desc_key, lastDesc));
            Title.setText(sp.getString(title_Key, lastTitle));
            Price.setText(sp.getString(price_Key,lastPrice));
            Author.setText(sp.getString(author_key,lastAuthor));
            ISBN.setText(sp.getString(ISBN_Key, lastISBN));
            BookId.setText(sp.getString(bookID_key,lastBookId));
            lastTitle = Title.getText().toString();
            lastISBN = ISBN.getText().toString();
            lastPrice = Price.getText().toString();
            lastBookId = BookId.getText().toString();
            lastDesc = Desc.getText().toString();
            lastAuthor = Author.getText().toString();
            Log.d(TAG,"onNull");
        }


        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        cloudDB = FirebaseDatabase.getInstance().getReference();
        bookTable = cloudDB.child("week8/book");

        gestureView = findViewById(R.id.gesture_view);
        myGeustureDetector myGeustureDetector = new myGeustureDetector();
        mDetector = new GestureDetectorCompat(this,myGeustureDetector);
        gestureView.setOnTouchListener(this);
//        gestureView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = (int) motionEvent.getAction();
//                switch (action){
//                    case (MotionEvent.ACTION_DOWN):
//                        if (counter == 0){
//                            int secondX = (int) motionEvent.getX();
//                            int secondY = (int) motionEvent.getY();
//                            if (X == secondX & Y == secondY){
//                                int price = Integer.parseInt(Price.getText().toString());
//                                price = price -1;
//                                Price.setText(String.valueOf(price));
//                                X = 0;
//                                Y = 0;
//                                counter += 1;
//                            }
//                        }
//                        else{
//                            counter = 0;
//                        }
//                        X = (int) motionEvent.getX();
//                        Y = (int) motionEvent.getY();
//                    case (MotionEvent.ACTION_MOVE):
//                        int initX = (int) motionEvent.getX();
//                        int initY = (int) motionEvent.getY();
//                        int deltaX = initX - X;
//                        int deltaY = initY - Y;
//                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
//                            // Horizontal swipe gesture
//                            if (deltaX > 0 && Math.abs(deltaX) > 0.25 * gestureView.getWidth()) {
//                                if (!Price.getText().toString().isEmpty()){
//                                    int price = Integer.parseInt(Price.getText().toString());
//                                    price = price +1;
//                                    Price.setText(String.valueOf(price));
//                                }
//                                else {
//                                    int price = 1;
//                                    Price.setText(String.valueOf(price));
//
//                        }}}
//                        break;
//
//                    case (MotionEvent.ACTION_UP):
//                        initX = (int) motionEvent.getX();
//                        initY = (int) motionEvent.getY();
//                        deltaX = initX - X;
//                        deltaY = initY - Y;
//                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
//                            // Horizontal swipe gesture
//                            if (deltaX < 0 && Math.abs(deltaX) > 0.25 * gestureView.getWidth()) {
//                                // Swipe from right to left (Add new Book)
//                                onButtonClick(view);
//                            }
//                        } else {
//                            // Vertical swipe gesture
//                            if (deltaY < 0 && Math.abs(deltaY) > 0.25 * gestureView.getHeight()) {
//                                // Swipe from bottom to top (Clear all fields)
//                                Author.getText().clear();
//                                Title.getText().clear();
//                                Price.getText().clear();
//                                BookId.getText().clear();
//                                ISBN.getText().clear();
//                                Desc.getText().clear();
//                            }
//                        }
//                        break;
//
//                }
//                return true;
//            }
//        });
        bookTable.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                bookTable = cloudDB.child("week8/book");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void onButtonClick(View view){
        String title = Title.getText().toString();
        String price = Price.getText().toString();
        if (Price.length() != 0)
        {int num =  Integer.parseInt(Price.getText().toString());
            price = num + "";
        }
        String mess = "Book ("+ title + ") and the price (" + price+")";
        Toast.makeText(this,mess,Toast.LENGTH_SHORT).show();
        lastTitle = Title.getText().toString();
        lastISBN = ISBN.getText().toString();
        lastPrice = Price.getText().toString();
        lastBookId = BookId.getText().toString();
        lastDesc = Desc.getText().toString();
        lastAuthor = Author.getText().toString();
        datasource.add(mess);
        adapter.notifyDataSetChanged();

        Book newbook = new Book(lastBookId,lastTitle,lastISBN,lastAuthor,lastDesc,lastPrice);

        bookViewModel.addBook(newbook);

        RecycleViewFragment recycleViewFragment = new RecycleViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.Framelayout_id,recycleViewFragment).commit();
        bookTable.push().setValue(newbook);

        RecyclerView recyclerViewBooks = findViewById(R.id.rv1);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewBooks.setLayoutManager(layoutManager);
        List<Book> bookList = bookViewModel.getFilteredBooks("hai","hai",0);
        adapter1 = new BookAdapter();
        adapter1.setDp(bookList);
        recyclerViewBooks.setAdapter(adapter1);


    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
        outState.putString(title_Key,Title.getText().toString());
        outState.putString(ISBN_Key,ISBN.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        Title.setText(savedInstanceState.getString(title_Key));
        ISBN.setText(savedInstanceState.getString(ISBN_Key));
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("bookIfor.ext",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(title_Key, lastTitle);
        editor.putString(author_key, lastAuthor);
        editor.putString(bookID_key, lastBookId);
        editor.putString(desc_key, lastDesc);
        editor.putString(ISBN_Key, lastISBN);
        editor.putString(price_Key, lastPrice);
        editor.apply();
        Log.d(TAG,"onStop");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mDetector.onTouchEvent(motionEvent);
        return true;
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * Retrieve the message from the intent
             * */
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            /*
             * String Tokenizer is used to parse the incoming message
             * The protocol is to have the account holder name and account number separate by a semicolon
             * */

            StringTokenizer sT = new StringTokenizer(msg, "|");
            if(sT.countTokens()==6){
            String title = sT.nextToken();
            String bookId = sT.nextToken();
            String iSBN = sT.nextToken();
            String author = sT.nextToken();
            String desc = sT.nextToken();
            String price= sT.nextToken();



            Title.setText(title);
            Author.setText(author);
            ISBN.setText(iSBN);
            Price.setText(price);
            BookId.setText(bookId);
            Desc.setText(desc);
            Toast.makeText(context, title+'|'+iSBN+'|'+price+'|'+bookId+'|'+desc+'|'+author, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onReceive: message");}
            /*
             * Now, its time to update the UI
             * */
//            accountNumberTv.setText(accountNumber);
//            accountNameTv.setText(accountName);
            if(sT.countTokens()==7){
                String title = sT.nextToken();
                String bookId = sT.nextToken();
                String iSBN = sT.nextToken();
                String author = sT.nextToken();
                String desc = sT.nextToken();
                String price= sT.nextToken();
                String price2 = sT.nextToken();

                String newPrice = String.valueOf(Integer.parseInt(price)+Integer.parseInt(price2));

                Title.setText(title);
                Author.setText(author);
                ISBN.setText(iSBN);
                Price.setText(newPrice);
                BookId.setText(bookId);
                Desc.setText(desc);
                Toast.makeText(context, title+'|'+iSBN+'|'+newPrice+'|'+bookId+'|'+desc+'|'+author, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onReceive: message");}

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_fields_options_menu){
            Author.getText().clear();
            Title.getText().clear();
            Price.getText().clear();
            BookId.getText().clear();
            ISBN.getText().clear();
            Desc.getText().clear();
        } else if (id == R.id.load_data_options_menu) {
            Desc.setText(lastDesc);
            Title.setText(lastTitle);
            Price.setText(lastPrice);
            Author.setText(lastAuthor);
            ISBN.setText(lastISBN);
            BookId.setText(lastBookId);
        }
        return true;
    }
    class Navhandler implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.add_book_nav_menu){
                String title = Title.getText().toString();
                String price = Price.getText().toString();
                if (Price.length() != 0)
                {int num =  Integer.parseInt(Price.getText().toString());
                    price = num + "";
                }
                String mess = "Book ("+ title + ") and the price (" + price+")";
                Toast.makeText(MainActivity.this,mess,Toast.LENGTH_SHORT).show();
                lastTitle = Title.getText().toString();
                lastISBN = ISBN.getText().toString();
                lastPrice = Price.getText().toString();
                lastBookId = BookId.getText().toString();
                lastDesc = Desc.getText().toString();
                lastAuthor = Author.getText().toString();
                int newprice;
                // add new book to db
                if (Price.getText().toString().length() != 0){
                    newprice = Integer.parseInt(Price.getText().toString());
                }
                else{
                    newprice = 0;
                }
                Book newbook = new Book(lastBookId,lastTitle,lastISBN,lastAuthor,lastDesc,lastPrice);
                bookViewModel.addBook(newbook);



            } else if (id == R.id.remove_last_nav_menu) {
//                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
//                startActivity(intent);
//                List<Book> bookList = bookViewModel.getFilteredBooks("hai","hai",0);
//                Log.d("week4tag",bookList.size()+"");
//                bookTable.removeValue();

            } else if (id == R.id.show_number_of_item) {
                Toast.makeText(MainActivity.this, "Number of Item in the list is: "+datasource.size()+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent);
            } else {
                if (datasource.size()>0){
                    datasource.clear();
                    adapter.notifyDataSetChanged();
                    bookViewModel.deleteAll();
                    bookTable.removeValue();
                }
            }
            drawerLayout.closeDrawers();
            return true;
        }
    }
private class myGeustureDetector extends GestureDetector.SimpleOnGestureListener{

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        String randomISBN = RandomString.generateNewRandomString(5);
        ISBN.setText(randomISBN);
        return true;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Author.getText().clear();
        Title.getText().clear();
        Price.getText().clear();
        BookId.getText().clear();
        ISBN.getText().clear();
        Desc.getText().clear();
        return true;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(Math.abs(distanceX) > Math.abs(distanceY)) { // Checking if scroll was more horizontal than vertical
            if(distanceX > 0) {
                // Scroll from right to left, increment price
                if (!Price.getText().toString().isEmpty()){
                    int price = Integer.parseInt(Price.getText().toString());
                    price = price + 1;
                    Price.setText(String.valueOf(price));
                } else {
                    int price = 1;
                    Price.setText(String.valueOf(price));}

            } else if(distanceX < 0) {
                // Scroll from left to right, decrement price
                if (!Price.getText().toString().isEmpty()){
                    int price = Integer.parseInt(Price.getText().toString());
                    price = price -1;
                    Price.setText(String.valueOf(price));
                } else {
                    int price = 0;
                    Price.setText(String.valueOf(price));}
            }
        } else if (Math.abs(distanceX) < Math.abs(distanceY)) {
            {Author.setText("Unknown");}

        }
        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(Math.abs(velocityX) > 1000) {
            moveTaskToBack(true);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Desc.setText(lastDesc);
        Title.setText(lastTitle);
        Price.setText(lastPrice);
        Author.setText(lastAuthor);
        ISBN.setText(lastISBN);
        BookId.setText(lastBookId);
    }

}
}