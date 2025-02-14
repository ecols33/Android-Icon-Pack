package com.jahirfiquitiva.paperboard.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.materialos.icons.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DonateFragment extends Fragment {

    private List<Level> mLevels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_cards_recycler, container, false);

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(R.string.donate);

        mLevels = new ArrayList<>();

        for (int i = 1; ; i++) {
            int id = getResources().getIdentifier("level" + i + "_donators", "array", getActivity().getPackageName());
            if (id != 0) {
                Level level = new Level();
                String[] array = getResources().getStringArray(id);
                level.donators = Arrays.asList(array);

                int name = getCreditsStringResource(i + "");
                level.name = getString(name);

                int amount = getCreditsStringResource(i + "_amount");
                level.amount = getString(amount);

                int linkId = getCreditsStringResource(i + "_link");
                level.donateLink = getString(linkId);

                mLevels.add(level);
            } else {
                break;
            }
        }


        recyclerView.setAdapter(new DonateRecyclerAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        return recyclerView;
    }

    private int getCreditsStringResource(String i) {
        return getResources().getIdentifier("level" + i, "string", getActivity().getPackageName());
    }

    private class Level {
        String name;
        List<String> donators;
        String donateLink;
        String amount;
    }

    private class DonateRecyclerAdapter extends RecyclerView.Adapter<DonateRecyclerAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.list_item_donate, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Level item = mLevels.get(position);

            holder.name.setText(item.name);
            holder.subtitle.setText(item.amount);

            holder.desc.removeAllViews();
            for (String donator : item.donators) {
                View chip = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_chip, holder.desc, false);
                TextView view = (TextView) chip.findViewById(R.id.list_item_chip_text);
                view.setText(donator);
                TextView initial = (TextView) chip.findViewById(R.id.list_item_chip_image_initial);
                initial.setText(donator.substring(0, 1).toUpperCase());

                holder.desc.addView(chip);
            }

            holder.website.setText("Donate This Level");

        }

        @Override
        public int getItemCount() {
            return mLevels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView subtitle;
            private FlowLayout desc;
            private Button website;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.list_item_donate_title);

                subtitle = (TextView) itemView.findViewById(R.id.list_item_donate_subtitle);
                desc = (FlowLayout) itemView.findViewById(R.id.list_item_donate_donators_flow);

                website = (Button) itemView.findViewById(R.id.list_item_donate_button);

                View.OnClickListener l = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String link = mLevels.get(getLayoutPosition()).donateLink;
                        if (link != null) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            startActivity(i);
                        }
                    }
                };
                itemView.setOnClickListener(l);
                website.setOnClickListener(l);
            }
        }
    }

}
