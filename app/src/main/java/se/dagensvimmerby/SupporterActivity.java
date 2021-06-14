package se.dagensvimmerby;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.limerse.iap.IapConnector;
import com.limerse.iap.SubscriptionServiceListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crm.agile.agilecrm.BuildConfig;
import crm.agile.agilecrm.R;

public class SupporterActivity extends AppCompatActivity implements SupporterAdapter.ItemClickListener, SubscriptionServiceListener {

    private IapConnector connector;
    private SupporterAdapter adapter;
    private TextView supporterTitle;
    private RecyclerView recyclerView;
    private Button btnPay;

    private String selectedId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter);

        supporterTitle = findViewById(R.id.supporter_title);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        toolbar.inflateMenu(R.menu.supporter_menu);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.restore) {
//                    Log.d("test", "asdf");
//                    return true;
//                }
//                return false;
//            }
//        });


        final List<String> keys = new ArrayList<>();
        keys.add(BuildConfig.APPLICATION_ID+".tier.1");
        keys.add(BuildConfig.APPLICATION_ID+".tier.2");

        connector = new IapConnector(this,
                new ArrayList<String>(),
                new ArrayList<String>(),
                keys,
                getString(R.string.billing_license),
                true);

        connector.addSubscriptionListener(this);

        btnPay = findViewById(R.id.btn_pay);
        btnPay.setEnabled(false);

        ArrayList<SupporterAdapter.Row> rows = new ArrayList<>();

        // set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SupporterAdapter(this, rows);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connector.subscribe(SupporterActivity.this, selectedId);
            }
        });

    }

    @Override
    public void onDestroy() {
        connector.destroy();
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {

        List<SupporterAdapter.Row> data = adapter.getData();

        for (int i = 0; i < data.size(); i++) {
            SupporterAdapter.Row row = data.get(i);
            if (i == position) {
                row.isChecked = true;
                selectedId = row.key;
            } else {
                row.isChecked = false;
            }
        }
        btnPay.setEnabled(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSubscriptionPurchased(String s) {
        //Log.d("SupporterActivity", String.format("onSubscriptionPurchased: %s", s != null ? s : ""));
        onSubscribed();
    }

    @Override
    public void onSubscriptionRestored(String s) {
        //Log.d("SupporterActivity", String.format("onSubscriptionPurchased: %s", s != null ? s : ""));
        onSubscribed();
    }

    @Override
    public void onPricesUpdated(Map<String, String> map) {
        //Log.d("SupporterActivity", "onPricesUpdated");

        ArrayList<SupporterAdapter.Row> rows = new ArrayList<>();
        for (String key : map.keySet()) {
            boolean selected = false;
            if (key.equals(selectedId)) {
                selected = true;
            }
            rows.add(new SupporterAdapter.Row(key, map.get(key), selected));
        }
        adapter.setData(rows);
        adapter.notifyDataSetChanged();
    }

    private void onSubscribed() {
        recyclerView.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
        btnPay.setEnabled(false);
        supporterTitle.setText(R.string.supporter_title_text_done);
    }

}
