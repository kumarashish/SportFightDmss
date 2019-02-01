package sportsfight.com.s.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import sportsfight.com.s.R;
import sportsfight.com.s.common.AppController;
import sportsfight.com.s.interfaces.PlaceBidCallBack;
import sportsfight.com.s.interfaces.ViewAllCallBack;
import sportsfight.com.s.mainmodule.Dashboard;
import sportsfight.com.s.mainmodule.NewsViewAll;
import sportsfight.com.s.mainmodule.Profile;
import sportsfight.com.s.mainmodule.UpcomingMatchesViewAll;
import sportsfight.com.s.model.MatchesModel;
import sportsfight.com.s.util.Util;

/**
 * Created by Ashish.Kumar on 08-02-2018.
 */

public class DashboardItemAdapter extends RecyclerView.Adapter  {
    ArrayList<MatchesModel> myMatches=new ArrayList<>();
    ArrayList<MatchesModel> upComingMatches=new ArrayList<>();
    ArrayList<MatchesModel> bids=new ArrayList<>();
    ArrayList<MatchesModel> myMatchesDoubles=new ArrayList<>();
    ArrayList<MatchesModel>  upComingDoublesMatches=new ArrayList<>();
    ArrayList<Integer> viewCount=new ArrayList<>();
    ArrayList<MatchesModel> addedModel=new ArrayList<>();
    Activity act;
    AppController controller;
    ViewAllCallBack callBack;
    PlaceBidCallBack placeBidCallBack;
    MatchesModel model;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }

    public DashboardItemAdapter(Activity act, ArrayList<MatchesModel> bids, ArrayList<MatchesModel> myMatches, ArrayList<MatchesModel>upComingMatches, ArrayList<MatchesModel> myMatchesDoubles, ArrayList<MatchesModel> upComingDoublesMatches) {
       this.myMatches=myMatches;
       this.bids=bids;
       this.myMatchesDoubles=myMatchesDoubles;
       this.upComingMatches=upComingMatches;
       this.upComingDoublesMatches=upComingDoublesMatches;
       this.act=act;
       controller=(AppController)act.getApplicationContext();
        callBack=(ViewAllCallBack)act;
        placeBidCallBack=(PlaceBidCallBack)act;
        //callback=(PlaceBidCallBack)act;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if ((!viewCount.contains(0)) && (bids.size() > 0)) {
            MatchesModel model=bids.get(0);
            viewCount.add(0);
            addedModel.add(model);
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.congratulationcard, parent, false);
            return new Congratulation(itemView);
        } else if ((!viewCount.contains(1)) && (myMatches.size() > 0)) {
            MatchesModel model=myMatches.get(0);
            viewCount.add(1);
            addedModel.add(model);
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymatches, parent, false);
            return new MyMatches(itemView);
        } else if ((!viewCount.contains(2)) && (upComingMatches.size() > 0)) {
           final PlaceBidCallBack callback=(PlaceBidCallBack) act;
            final MatchesModel model=upComingMatches.get(0);
            viewCount.add(2);
            addedModel.add(model);
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcomingmatches, parent, false);
            return new UpComingMatches(itemView);
        } else if ((!viewCount.contains(3)) && (myMatchesDoubles.size() > 0)) {
            viewCount.add(3);
            final PlaceBidCallBack callback=(PlaceBidCallBack) act;
            final MatchesModel model=myMatchesDoubles.get(0);
            addedModel.add(model);
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymatches, parent, false);
            return new MyDoublesMatches(itemView);
        }
        else if ((!viewCount.contains(4)) && (upComingDoublesMatches.size() > 0)) {
            viewCount.add(4);
            final PlaceBidCallBack callback=(PlaceBidCallBack) act;
            final MatchesModel model=upComingDoublesMatches.get(0);
            addedModel.add(model);
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_doubles, parent, false);
            return new UpComingDoublesMatches(itemView);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (viewCount.get(position))
        {
            case 0:
               final MatchesModel model=addedModel.get(position);
               Congratulation c_holder=(Congratulation)holder;
                c_holder.heading.setText("Results(" + bids.size() + ")");
                ImageView img = new ImageView(act);
                Picasso.with(act).load(Util.getCardBgInt(model.getGameName(), act)).into(   c_holder.cardview);
                c_holder.tticon.setImageDrawable(Util.getIcon(model.getGameName(), act));
                c_holder.info_text.setText(model.getGameName().toUpperCase());
                c_holder.circleImageView.setText(Util.getInitial(model.getPlayer1Name()));
//                if (model.getPlayer1ImageUrl().length() > 0) {
//
//                    Picasso.with(act).load(model.getPlayer1ImageUrl()).resize(200, 200)
//                            .centerInside().placeholder(R.drawable.user_icon).into(   c_holder.circleImageView);
//                } else {
//                    c_holder.circleImageView.setImageResource(R.drawable.user_icon);
//                }
               // c_holder.circleImageView.setImageResource(R.drawable.user_icon);
                c_holder.player1Name.setText(model.getPlayer1Name());
                c_holder.player1Bid.setText("Bids: "+Integer.toString(model.getPlayer1Bids())+" pts");
//                if (model.getPlayer2ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer2ImageUrl()).placeholder(R.drawable.user_icon).into(   c_holder.circleImageView2);
//                } else {
//                    c_holder.circleImageView2.setImageResource(R.drawable.user_icon);
//                }
               // c_holder.circleImageView2.setImageResource(R.drawable.user_icon);
                c_holder.circleImageView2.setText(Util.getInitial(model.getPlayer2Name()));
                c_holder.Player2Name.setText(model.getPlayer2Name());
                c_holder.Player2Bid.setText("Bids : "+Integer.toString(model.getPlayer2Bids()));
                c_holder.yourBid.setText("My Bid :"+Integer.toString(model.getMyBid()));
               c_holder.result.setText(model.getMessage());
                if (model.getWinnerId() == model.getPlayer1Id()) {
                    c_holder.player1Won.setVisibility(View.VISIBLE);
                    c_holder.player2Won.setVisibility(View.GONE);

                } else {
                    c_holder. player1Won.setVisibility(View.GONE);
                    c_holder.player2Won.setVisibility(View.VISIBLE);
                }
                if(model.isWon())
                {
                    c_holder.congratulation.setText("Congratulation");
                  if  (Dashboard.isCongratulationShown==false) {
//                      Dashboard.isCongratulationShown=true;
//                      c_holder.congratulationView.build()
//                              .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
//                              .setDirection(0.0, 500.0)
//                              .setSpeed(1f, 5f)
//                              .setFadeOutEnabled(true)
//                              .setTimeToLive(2000L)
//                              .addShapes(Shape.RECT, Shape.CIRCLE)
//                              .addSizes(new Size(12, 5))
//                              .setPosition(-50f, c_holder.congratulationView.getWidth() + 50f, -50f, -50f)
//                              .stream(200, 5000L);
                  }
                }else{
                    c_holder.congratulation.setText("Sorry you loose");
                }
                c_holder.info_text.setTextColor(Util.getTextColor(model.getGameName(), act));
                c_holder.viewAllResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callBack.CongratulationViewALL();
                    }
                });
                break;
            case 1:
                MatchesModel  model2=addedModel.get(position);
                MyMatches holderr=(MyMatches)holder;
                holderr.heading.setText("My Matches(" + myMatches.size() + ")");
                int val=model2.getPlayer1Bids()+model2.getPlayer2Bids();
                holderr.bid_count.setText("");
                Picasso.with(act).load(Util.getCardBgInt(model2.getGameName(), act)).into(holderr.cardview);
                holderr.tticon.setImageDrawable(Util.getIcon(model2.getGameName(), act));
                holderr.info_text.setText(model2.getGameName().toUpperCase());
                holderr.info_text.setTextColor(Util.getTextColor(model2.getGameName(),act));

//                if (model.getPlayer1ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer1ImageUrl()).resize(200, 200).centerInside().placeholder(R.drawable.user_icon).into(holderr.circleImageView);
//                } else {
//                    holderr.circleImageView.setImageResource(R.drawable.user_icon);
//                }

                holderr.player1Name.setText(Util.getUpdatedName(model2.getPlayer1Name()));
                holderr.player1Bid.setText("");
//                if (model.getPlayer2ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer2ImageUrl()).resize(200, 200)
//                            .centerInside().placeholder(R.drawable.user_icon).into(holderr.circleImageView2);
//                } else {
//                    holderr. circleImageView2.setImageResource(R.drawable.user_icon);
//                }
                holderr.circleImageView.setText(Util.getInitial(model2.getPlayer1Name()));
                holderr.circleImageView2.setText(Util.getInitial(model2.getPlayer2Name()));
                holderr. Player2Name.setText(Util.getUpdatedName(model2.getPlayer2Name()));
                holderr.Player2Bid.setText("");

                holderr. viewAllResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callBack.MyMatchesViewALL();
                    }
                });
                holderr.date.setText(Util.getMulticolorTextView("Date : "+Util.getDateinMMDDYY(model2.getMatchDate()),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,model2.getMatchDate().length()+7}));
                holderr.time.setText(Util.getMulticolorTextView("Time : "+model2.getSlotTime(),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,model2.getSlotTime().length()+7}));

                break;
            case 2:
              final  MatchesModel model3=addedModel.get(position);
                UpComingMatches holderrr=(UpComingMatches)holder;
                int vall=model3.getPlayer1Bids()+model3.getPlayer2Bids();

                holderrr.bid_count.setText("Total bid (Current) "+Integer.toString(vall) +"pts");
                holderrr.date.setText(Util.getMulticolorTextView("Date : "+Util.getDateinMMDDYY(model3.getMatchDate()),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,model3.getMatchDate().length()+7}));
                holderrr.time.setText(Util.getMulticolorTextView("Time : "+model3.getSlotTime(),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,model3.getSlotTime().length()+7}));
                holderrr.heading.setText("Upcoming Matches(" + upComingMatches.size() + ")");
                Picasso.with(act).load(Util.getCardBgInt(model3.getGameName(), act)).into( holderrr.cardview);
                holderrr.tticon.setImageDrawable(Util.getIcon(model3.getGameName(), act));
                holderrr.info_text.setText(model3.getGameName().toUpperCase());
                holderrr.info_text.setTextColor(Util.getTextColor(model3.getGameName(),act));
               // holderrr.circleImageView.setImageResource(R.drawable.user_icon);
//                if (model.getPlayer1ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer1ImageUrl()).resize(200, 200)
//                            .centerInside().placeholder(R.drawable.user_icon).into( holderrr.circleImageView);
//                } else {
//                    holderrr.circleImageView.setImageResource(R.drawable.user_icon);
//                }
                holderrr.player1Name.setText(model3.getPlayer1Name());
                holderrr.player1Bid.setText("Bids : "+Integer.toString(model3.getPlayer1Bids())+" pts");
//                if (model.getPlayer2ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer2ImageUrl()).resize(200, 200)
//                            .centerInside().placeholder(R.drawable.user_icon).into( holderrr.circleImageView2);
//                } else {
//                    holderrr.circleImageView2.setImageResource(R.drawable.user_icon);
//                }
                holderrr.circleImageView.setText(Util.getInitial(model3.getPlayer1Name()));
                holderrr.circleImageView2.setText(Util.getInitial(model3.getPlayer2Name()));
                holderrr.Player2Name.setText(model3.getPlayer2Name());
                holderrr.Player2Bid.setText("Bids : "+Integer.toString(model3.getPlayer2Bids())+" pts");
                if(model3.getMyBidToId()==model3.getPlayer1Id()) {
                    holderrr.myBid.setText("My Bid on "+model3.getPlayer1Name()+"\n" + Integer.toString(model3.getMyBid()) + " pts" );
                }else if(model3.getMyBidToId()==model3.getPlayer2Id())
                {
                    holderrr.myBid.setText("My Bid on "+model3.getPlayer2Name()+"\n" + Integer.toString(model3.getMyBid()) + " pts" );
                }else{
                    holderrr.myBid.setText("My Bid  : " + Integer.toString(model3.getMyBid()) + " pts" );
                }
                holderrr.placeBid.setTypeface(controller.getDetailsFont());
                holderrr.placeBid.setVisibility(View.VISIBLE);
                holderrr.viewAllResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callBack.UpComingViewALL();
                    }
                });
                holderrr.placeBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     placeBidCallBack.onPlaceBidClick(model3);
                    }
                });
                break;
            case 3:

                MatchesModel model4=addedModel.get(position);
                MyDoublesMatches holderr3=(MyDoublesMatches)holder;
                holderr3.heading.setText("My Team Games(" + myMatchesDoubles.size() + ")");
                int val3=model4.getPlayer1Bids()+model4.getPlayer2Bids();
                holderr3.bid_count.setText("");
                Picasso.with(act).load(Util.getCardBgInt(model4.getGameName(), act)).into(holderr3.cardview);
                holderr3.tticon.setImageDrawable(Util.getIcon(model4.getGameName(), act));
                holderr3.info_text.setText((model4.getGameName()).toUpperCase());
                holderr3.info_text.setTextColor(Util.getTextColor(model4.getGameName(),act));
//                if (model.getPlayer1ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer1ImageUrl()).resize(200, 200).centerInside().placeholder(R.drawable.user_icon).into(holderr.circleImageView);
//                } else {
//                    holderr.circleImageView.setImageResource(R.drawable.user_icon);
//                }
              //  holderr3.circleImageView.setImageResource(R.drawable.user_icon);
                holderr3.player1Name.setText(Util.getUpdatedName(model4.getPlayer1Name()));
                holderr3.player1Bid.setText("");
                holderr3.circleImageView.setText(Util.getInitial(model4.getPlayer1Name()));
//                if (model.getPlayer2ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer2ImageUrl()).resize(200, 200)
//                            .centerInside().placeholder(R.drawable.user_icon).into(holderr.circleImageView2);
//                } else {
//                    holderr. circleImageView2.setImageResource(R.drawable.user_icon);
//                }
                holderr3.circleImageView2.setText(Util.getInitial(model4.getPlayer2Name()));
                holderr3.Player2Name.setText(Util.getUpdatedName(model4.getPlayer2Name()).toUpperCase());
                holderr3.Player2Bid.setText("");
               // holderr3.circleImageView2.setImageResource(R.drawable.user_icon);
                holderr3. viewAllResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callBack.MyMatchesDoublesViewAll();
                    }
                });
                holderr3.date.setText(Util.getMulticolorTextView("Date : "+Util.getDateinMMDDYY(model4.getMatchDate()),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,model4.getMatchDate().length()+7}));
                holderr3.time.setText(Util.getMulticolorTextView("Time : "+model4.getSlotTime(),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,model4.getSlotTime().length()+7}));

                break;
               case 4:

                   final MatchesModel modell=addedModel.get(position);
                   UpComingDoublesMatches holderrr4=( UpComingDoublesMatches)holder;
                   int vall4=modell.getPlayer1Bids()+modell.getPlayer2Bids();
                   holderrr4.bid_count.setText("Total bid (Current) "+Integer.toString(vall4) +"pts");
                   holderrr4.date.setText(Util.getMulticolorTextView("Date : "+Util.getDateinMMDDYY(modell.getMatchDate()),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,modell.getMatchDate().length()+7}));
                   holderrr4.time.setText(Util.getMulticolorTextView("Time : "+modell.getSlotTime(),new Integer[]{act.getResources().getColor(R.color.black_font),act.getResources().getColor(R.color.light_grey)},new Integer[]{0,5,7,modell.getSlotTime().length()+7}));
                   holderrr4.heading.setText("Upcoming Doubles Matches(" +upComingDoublesMatches.size() + ")");
                   Picasso.with(act).load(Util.getCardBgInt(modell.getGameName(), act)).into( holderrr4.cardview);
                   holderrr4.tticon.setImageDrawable(Util.getIcon(modell.getGameName(), act));
                   holderrr4.info_text.setText(modell.getGameName().toUpperCase());
                   holderrr4.info_text.setTextColor(Util.getTextColor(modell.getGameName(),act));
                   holderrr4.circleImageView.setText(Util.getInitial(modell.getPlayer1Name()));
//                if (model.getPlayer1ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer1ImageUrl()).resize(200, 200)
//                            .centerInside().placeholder(R.drawable.user_icon).into( holderrr.circleImageView);
//                } else {
//                    holderrr.circleImageView.setImageResource(R.drawable.user_icon);
//                }
                   holderrr4.player1Name.setText(Util.getUpdatedName(modell.getPlayer1Name()).toUpperCase());
                   holderrr4.player1Bid.setText("Bids : "+Integer.toString(modell.getPlayer1Bids())+" pts");
//                if (model.getPlayer2ImageUrl().length() > 0) {
//                    Picasso.with(act).load(model.getPlayer2ImageUrl()).resize(200, 200)
//                            .centerInside().placeholder(R.drawable.user_icon).into( holderrr.circleImageView2);
//                } else {
//                    holderrr.circleImageView2.setImageResource(R.drawable.user_icon);
//                }
                  // holderrr4.circleImageView2.setImageResource(R.drawable.user_icon);
                   holderrr4.circleImageView2.setText(Util.getInitial(modell.getPlayer2Name()));
                   holderrr4.Player2Name.setText(Util.getUpdatedName(modell.getPlayer2Name()).toUpperCase());
                   holderrr4.Player2Bid.setText("Bids : "+Integer.toString(modell.getPlayer2Bids())+" pts");
                   if(modell.getMyBidToId()==modell.getPlayer1Id()) {
                       holderrr4.myBid.setText("My Bid on "+modell.getPlayer1Name()+"\n" + Integer.toString(modell.getMyBid()) + " pts" );
                   }else if(modell.getMyBidToId()==modell.getPlayer2Id())
                   {
                       holderrr4.myBid.setText("My Bid on "+modell.getPlayer2Name()+"\n" + Integer.toString(modell.getMyBid()) + " pts" );
                   }else{
                       holderrr4.myBid.setText("My Bid  : " + Integer.toString(modell.getMyBid()) + " pts" );
                   }
                   holderrr4.placeBid.setTypeface(controller.getDetailsFont());
                   holderrr4.placeBid.setVisibility(View.VISIBLE);
                   holderrr4.viewAllResult.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           callBack.UpComingDoublesViewAll();
                       }
                   });
                   holderrr4.placeBid.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           placeBidCallBack.onPlaceBidClick(modell);
                       }
                   });
                break;

        }

    }

    @Override
    public int getItemViewType(int position) {
            return position;
    }
    @Override
    public int getItemCount() {
        return getCount();
    }
    public int getCount() {
        int size = 0;
        if ((bids.size() > 0)) {
            size = size + 1;
        }
        if ((myMatches.size() > 0)) {
            size = size + 1;
        }
        if ((upComingMatches.size() > 0)) {
            size = size + 1;
        }
        if ((myMatchesDoubles.size() > 0)) {
            size = size + 1;
        }
        if ((upComingDoublesMatches.size() > 0)) {
            size = size + 1;
        }
        return size;
    }

    public class Tournaments {

    }

    public class News extends RecyclerView.ViewHolder {
        TextView newsCount;
        TextView viewAllResult;
        TextView heading;
        ImageView image;
        TextView details;

        public News(View itemView) {
            super(itemView);
            newsCount = (TextView) itemView.findViewById(R.id.newsCount);
            heading = (TextView) itemView.findViewById(R.id.newsHeading);
            image = (ImageView) itemView.findViewById(R.id.image);
            details = (TextView) itemView.findViewById(R.id.details);
            viewAllResult = (TextView) itemView.findViewById(R.id.viewAllNews);


        }
    }

    public static class UpComingDoublesMatches extends RecyclerView.ViewHolder {

        TextView date;
        TextView time;
        TextView heading;
        TextView viewAllResult;
        TextView  bid_count;
        ImageView tticon;
        TextView info_text;
        TextView circleImageView;
        TextView player1Name;
        TextView player1Bid;
        TextView  circleImageView2;
        TextView Player2Name;
        TextView Player2Bid;
        TextView myBid;
        TextView bidCount;
        sportsfight.com.s.common.CustomLayout cardview;
        Button placeBid;

        public  UpComingDoublesMatches(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            bid_count = (TextView) itemView.findViewById(R.id.bid_count);
            heading = (TextView) itemView.findViewById(R.id.heading);
            viewAllResult = (TextView) itemView.findViewById(R.id.viewAllResult);
            cardview = (sportsfight.com.s.common.CustomLayout) itemView.findViewById(R.id.cardview);
            tticon = (ImageView) itemView.findViewById(R.id.tticon);
            info_text = (TextView) itemView.findViewById(R.id.info_text);
           circleImageView = (TextView) itemView.findViewById(R.id.circleImageView);
            player1Name = (TextView) itemView.findViewById(R.id.player1Name);
            player1Bid = (TextView) itemView.findViewById(R.id.player1Bid);
            circleImageView2 = (TextView) itemView.findViewById(R.id.circleImageView2);
            Player2Name = (TextView) itemView.findViewById(R.id.player2Name);
            Player2Bid = (TextView) itemView.findViewById(R.id.player2Bid);
            myBid = (TextView) itemView.findViewById(R.id.myBid);
            bidCount = (TextView) itemView.findViewById(R.id.bid_count);
            placeBid = (Button) itemView.findViewById(R.id.placeBid);

        }
    }

    public static class MyDoublesMatches extends RecyclerView.ViewHolder {

        TextView date;
        TextView time;
        TextView heading;
        TextView viewAllResult;
        sportsfight.com.s.common.CustomLayout  cardview;
        ImageView tticon;
        TextView info_text;
        TextView circleImageView;
        TextView player1Name;
        TextView player1Bid;
        TextView circleImageView2;
        TextView Player2Name;
        TextView Player2Bid;
        TextView bid_count;
        public MyDoublesMatches(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            heading = (TextView) itemView.findViewById(R.id.heading);
            viewAllResult = (TextView) itemView.findViewById(R.id.viewAllResult);
            cardview = (sportsfight.com.s.common.CustomLayout) itemView.findViewById(R.id.cardview);
            tticon = (ImageView) itemView.findViewById(R.id.tticon);
            info_text = (TextView) itemView.findViewById(R.id.info_text);
           circleImageView = (TextView) itemView.findViewById(R.id.circleImageView);
            player1Name = (TextView) itemView.findViewById(R.id.Player1Name);
            player1Bid = (TextView) itemView.findViewById(R.id.Player1Bid);
           circleImageView2 = (TextView) itemView.findViewById(R.id.circleImageView2);
            Player2Name = (TextView) itemView.findViewById(R.id.Player2Name);
            Player2Bid = (TextView) itemView.findViewById(R.id.Player2Bid);
            bid_count = (TextView) itemView.findViewById(R.id.bid_count);

        }
    }

    public static class UpComingMatches extends RecyclerView.ViewHolder {

        TextView date;
        TextView time;
        TextView heading;
        TextView viewAllResult;
        TextView  bid_count;
        ImageView tticon;
        TextView info_text;
      TextView circleImageView;
        TextView player1Name;
        TextView player1Bid;
       TextView circleImageView2;
        TextView Player2Name;
        TextView Player2Bid;
        TextView myBid;
        TextView bidCount;
        sportsfight.com.s.common.CustomLayout cardview;
        Button placeBid;

        public  UpComingMatches(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            bid_count = (TextView) itemView.findViewById(R.id.bid_count);
            heading = (TextView) itemView.findViewById(R.id.heading);
            viewAllResult = (TextView) itemView.findViewById(R.id.viewAllResult);
            cardview = (sportsfight.com.s.common.CustomLayout) itemView.findViewById(R.id.cardview);
            tticon = (ImageView) itemView.findViewById(R.id.tticon);
            info_text = (TextView) itemView.findViewById(R.id.info_text);
           circleImageView = (TextView) itemView.findViewById(R.id.circleImageView);
            player1Name = (TextView) itemView.findViewById(R.id.player1Name);
            player1Bid = (TextView) itemView.findViewById(R.id.player1Bid);
          circleImageView2 = (TextView) itemView.findViewById(R.id.circleImageView2);
            Player2Name = (TextView) itemView.findViewById(R.id.player2Name);
            Player2Bid = (TextView) itemView.findViewById(R.id.player2Bid);
            myBid = (TextView) itemView.findViewById(R.id.myBid);
            bidCount = (TextView) itemView.findViewById(R.id.bid_count);
            placeBid = (Button) itemView.findViewById(R.id.placeBid);

        }
    }

    public static class MyMatches extends RecyclerView.ViewHolder {

        TextView date;
        TextView time;
        TextView heading;
        TextView viewAllResult;
        sportsfight.com.s.common.CustomLayout  cardview;
        ImageView tticon;
        TextView info_text;
        TextView circleImageView;
        TextView player1Name;
        TextView player1Bid;
        TextView circleImageView2;
        TextView Player2Name;
        TextView Player2Bid;
        TextView bid_count;
        public MyMatches(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            heading = (TextView) itemView.findViewById(R.id.heading);
            viewAllResult = (TextView) itemView.findViewById(R.id.viewAllResult);
            cardview = (sportsfight.com.s.common.CustomLayout) itemView.findViewById(R.id.cardview);
            tticon = (ImageView) itemView.findViewById(R.id.tticon);
            info_text = (TextView) itemView.findViewById(R.id.info_text);
            circleImageView = (TextView) itemView.findViewById(R.id.circleImageView);
            player1Name = (TextView) itemView.findViewById(R.id.Player1Name);
            player1Bid = (TextView) itemView.findViewById(R.id.Player1Bid);
            circleImageView2 = (TextView) itemView.findViewById(R.id.circleImageView2);
            Player2Name = (TextView) itemView.findViewById(R.id.Player2Name);
            Player2Bid = (TextView) itemView.findViewById(R.id.Player2Bid);
            bid_count = (TextView) itemView.findViewById(R.id.bid_count);

        }
    }

    public static class Congratulation extends RecyclerView.ViewHolder {
        TextView heading;
        TextView viewAllResult;
        sportsfight.com.s.common.CustomLayout cardview;
        ImageView tticon;
        TextView info_text;
        TextView circleImageView;
        ImageView player1Won;
        TextView player1Name;
        TextView player1Bid;
        TextView result;
        TextView yourBid;
        TextView congratulation;
        TextView circleImageView2;
        ImageView player2Won;
        TextView Player2Name;
        TextView Player2Bid;
        nl.dionsegijn.konfetti.KonfettiView congratulationView;
        public Congratulation(View itemView) {
            super(itemView);
            heading = (TextView) itemView.findViewById(R.id.heading);
            viewAllResult = (TextView) itemView.findViewById(R.id.viewAllResult);
            cardview = (sportsfight.com.s.common.CustomLayout) itemView.findViewById(R.id.cardview);
            tticon = (ImageView) itemView.findViewById(R.id.tticon);
            info_text = (TextView) itemView.findViewById(R.id.info_text);
           circleImageView = (TextView) itemView.findViewById(R.id.circleImageView);
            player1Won = (ImageView) itemView.findViewById(R.id.player1Won);
            player1Name = (TextView) itemView.findViewById(R.id.Player1Name);
            player1Bid = (TextView) itemView.findViewById(R.id.Player1Bid);
            result = (TextView) itemView.findViewById(R.id.result);
            yourBid = (TextView) itemView.findViewById(R.id.yourBid);
            congratulation = (TextView) itemView.findViewById(R.id.congratulation);
            circleImageView2 = (TextView) itemView.findViewById(R.id.circleImageView2);
            player2Won = (ImageView) itemView.findViewById(R.id.player2Won);
            Player2Name = (TextView) itemView.findViewById(R.id.Player2Name);
            Player2Bid = (TextView) itemView.findViewById(R.id.Player2Bid);
            congratulationView=(nl.dionsegijn.konfetti.KonfettiView)itemView.findViewById(R.id.viewKonfetti);
        }
    }

}
