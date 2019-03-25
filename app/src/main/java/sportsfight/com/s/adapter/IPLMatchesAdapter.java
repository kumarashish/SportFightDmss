package sportsfight.com.s.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.juspay.godel.ui.ACSOptionsFragment;
import sportsfight.com.s.R;
import sportsfight.com.s.common.AppController;
import sportsfight.com.s.interfaces.PlaceBidCallBack;
import sportsfight.com.s.model.MatchesModel;
import sportsfight.com.s.util.Util;

/**
 * Created by Ashish.Kumar on 12-04-2018.
 */

public class IPLMatchesAdapter extends BaseAdapter {
    ArrayList<MatchesModel> matchList;
    Activity act;
    LayoutInflater inflater;
    AppController controller;
    PlaceBidCallBack callBack;
    public static String []icons={"https://logosvector.net/wp-content/uploads/2014/01/chennai-super-kings-vector-logo.png",
            "https://upload.wikimedia.org/wikipedia/bn/thumb/c/cd/Mumbai_Indians_Logo.svg/1280px-Mumbai_Indians_Logo.svg.png",
            "https://i.pinimg.com/originals/f1/90/68/f190685a1bdc97d2a068e1af4bbe5f36.png",
            "https://timesofindia.indiatimes.com/thumb/msid-67149952,imgsize-367356,width-400,resizemode-4/67149952.jpg",
            "https://i.pinimg.com/originals/5b/a9/59/5ba9592c9608c9f627c6c2a2aeaaa516.jpg",
            "https://i.pinimg.com/originals/be/63/f9/be63f97f89eff774678695cb0a94e60c.png",
            "https://i.pinimg.com/236x/eb/5e/65/eb5e656dcb3411fd84853de8e2a9e346--cricket-score-live-cricket.jpg",
            "https://seeklogo.com/images/I/ipl-kings-xi-punjab-logo-6747D5C02B-seeklogo.com.png"};

    public IPLMatchesAdapter(ArrayList<MatchesModel> matchList, Activity act) {
        this.matchList = matchList;
        this.act = act;
        inflater = act.getLayoutInflater();
        controller=(AppController)act.getApplicationContext();
        callBack=(PlaceBidCallBack)act;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int i) {
        return matchList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final MatchesModel model = matchList.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.ipl_matches_row, null, true);
            holder.placeBid = (Button) view.findViewById(R.id.placeBid);
            holder.team1Icon = (sportsfight.com.s.common.CircleImageView) view.findViewById(R.id.team1Icon);
            holder.team2Icon = (sportsfight.com.s.common.CircleImageView) view.findViewById(R.id.team2Icon);
            holder.match_date = (TextView) view.findViewById(R.id.match_date);
            holder.venue = (TextView) view.findViewById(R.id.venue);
            holder.totalBid = (TextView) view.findViewById(R.id.totalBid);
            holder.player1Bid=(TextView) view.findViewById(R.id.player1Bid);
            holder.player2Bid=(TextView) view.findViewById(R.id.player2Bid);
            holder.myBid= (TextView) view.findViewById(R.id.myBid);
            holder.team1= (TextView) view.findViewById(R.id.team1);
            holder.team2= (TextView) view.findViewById(R.id.team2);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.placeBid.setTypeface(controller.getDetailsFont());
        holder.team1Icon = (sportsfight.com.s.common.CircleImageView) view.findViewById(R.id.team1Icon);
        holder.team2Icon = (sportsfight.com.s.common.CircleImageView) view.findViewById(R.id.team2Icon);
        Picasso.with(act).load(getimageUrl(model.getPlayer1Name())).resize(200, 200)
                .centerInside().placeholder(R.drawable.user_icon).into(holder.team1Icon);
        Picasso.with(act).load(getimageUrl(model.getPlayer2Name())).resize(200, 200)
                .centerInside().placeholder(R.drawable.user_icon).into(holder.team2Icon);
       holder.match_date.setText(Util. getDateinMMDDYY(model.getMatchDate()) +System.getProperty("line.separator")+" "+model.getSlotTime());
        holder.venue = (TextView) view.findViewById(R.id.venue);
        holder.venue.setText(model.getMatchPlace());
        holder.player1Bid.setText("Bid : "+model.getPlayer1Bids()+" coins.");
        holder.player2Bid.setText("Bid : "+model.getPlayer2Bids()+"  coins.");
        holder.totalBid.setText("Total Bid : "+model.getTotalBids()+"  coins.");
        holder.team1.setText(Util.getInitialsForName(model.getPlayer1Name()));
        holder.team2.setText(Util.getInitialsForName(model.getPlayer2Name()));
        if (model.getMyBid() > 0) {
            if (model.getMyBidToId() == model.getPlayer1Id()) {
                holder.myBid.setText("My Bid on " + Util.getInitialsForName(model.getPlayer1Name() )+ " : " + model.getMyBid() + " coins.");
            } else {
                holder.myBid.setText("My Bid on " + Util.getInitialsForName( model.getPlayer2Name()) + " : " + model.getMyBid() + " coins.");
            }
        } else {
            holder.myBid.setText("My Bid : 0 coins.");
        }
        view.setTag(holder);
        holder.placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onPlaceBidClick(model);
            }
        });
        return view;
    }

    public class ViewHolder {
        sportsfight.com.s.common.CircleImageView team1Icon;
        sportsfight.com.s.common.CircleImageView team2Icon;
        TextView match_date, totalBid, venue,myBid,player1Bid,player2Bid,team1,team2;
        Button placeBid;
    }


    public static String getimageUrl(String teamName)
    {
        switch (teamName)
        {
            case "Chennai Super Kings":
                return icons[0];

            case "Mumbai Indians":
                return icons[1];

            case "Kolkata Knight Riders":
                return icons[2];

            case "Delhi Capitans":
                return icons[3];

            case "Sunrisers Hyderabad":
                return icons[4];

            case "Rajasthan Royals":
                return icons[5];

            case "Royal Challengers Bangalore":
                return icons[6];

            case "Kings XI Punjab":
                return icons[7];

                default:
                    return "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";

        }

    }
}
