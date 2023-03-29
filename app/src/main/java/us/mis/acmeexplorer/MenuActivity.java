package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import us.mis.acmeexplorer.entity.Link;

public class MenuActivity extends AppCompatActivity {
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        gridView = findViewById(R.id.gridView);
        MenuLinkAdapter linkAdapter = new MenuLinkAdapter(Link.createLinks(), this);
        gridView.setAdapter(linkAdapter);
    }
}

class MenuLinkAdapter extends BaseAdapter {
    private List<Link> links;
    private Context context;

    public MenuLinkAdapter(List<Link> links, Context context) {
        this.links = links;
        this.context = context;
    }

    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public Object getItem(int i) {
        return links.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_menu, viewGroup, false);
        }

        Link link = (Link) getItem(i);

        CardView cardView = view.findViewById(R.id.cardView);
        TextView textView = view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.imageView);

        cardView.setOnClickListener(menuItem -> context.startActivity(
                new Intent(context, link.getLinkToClass())
        ));
        textView.setText(link.getLinkName());
        imageView.setImageResource(link.getLinkImageResource());

        return view;
    }
}