package com.miya.twit.keywrd;

import com.miya.twit.utils.Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class StreamJob {

    private TwitterStream myStream;
    private long[] userIds = {188407220};
    private TivitListener myTivitListener;
    private BaglantiListener myBaglantiListener;
    // private String[] keysGokhan = {"tayyip", "erdoğan", "rte", "recep"};

    public StreamJob(String userId, TwitterStream myStream) {
        //this.userIds = userIds;
        this.myStream = myStream;

    }
    List<TwitProfileWrapper> listTweetProfileWrapper = null;
    List<Integer> userIdList = new ArrayList<Integer>();

    public void startJob(String[] subject) {
        try {
            System.out.println("stream job start başladı");
            listTweetProfileWrapper = new ArrayList<TwitProfileWrapper>();
            myTivitListener = new TivitListener();
            myStream.addListener(myTivitListener);
            myBaglantiListener = new BaglantiListener();
            myStream.addConnectionLifeCycleListener(myBaglantiListener);
            FilterQuery fq = new FilterQuery(userIds);
            System.out.println("subject length :" + subject.length + " subject : " + subject);
            fq.track(subject);
            myStream.filter(fq);
            System.out.println("stream job start edildi");
        } catch (Exception ex) {
            System.out.println("Twitter statJob Hata : " + ex.getMessage());
        }
    }

    public void stopJob() {
        myStream.cleanUp();
    }

    public List<TwitProfileWrapper> getLast10OfList() {
        List<TwitProfileWrapper> newList = null;
        if (listTweetProfileWrapper != null && listTweetProfileWrapper.size() >= 10) {
            newList = listTweetProfileWrapper.subList(listTweetProfileWrapper.size() - 10, listTweetProfileWrapper.size());
            setList(listTweetProfileWrapper.subList(0, listTweetProfileWrapper.size() - 10));
            return newList;
        } else {
            return null;
        }
    }

    public List<TwitProfileWrapper> getLastSmallList() {
        if (listTweetProfileWrapper != null && listTweetProfileWrapper.size() < 10) {
            return listTweetProfileWrapper;
        } else {
            return null;
        }
    }

    public List<TwitProfileWrapper> getList() {
        return listTweetProfileWrapper;
    }

    public void setList(List<TwitProfileWrapper> listTweetProfileWrapper) {
        this.listTweetProfileWrapper = listTweetProfileWrapper;
    }

    public void clearList() {
        listTweetProfileWrapper.clear();
    }

    class TivitListener implements StatusListener {

        List<TwitProfileWrapper> list = new ArrayList<TwitProfileWrapper>();

        @Override
        public void onStatus(Status status) {
            TwitProfileWrapper tp = null;
            try {
                if (status.getUser().getLang().equals("tr") && !status.isRetweet()) {
                    if (list.size() < Utilities.totalTweetOfSession) {
                        if (status.getText() != null && !status.getText().equals("")) {
                            tp = new TwitProfileWrapper();

                            tp.setUserId(status.getUser().getId());
                            tp.setUsername(status.getUser().getName());
                            tp.setScreenName(status.getUser().getScreenName());
                            tp.setUserDescription(status.getUser().getDescription());
                            tp.setUserTimeZone(status.getUser().getTimeZone());
                            tp.setUserLocation(status.getUser().getLocation());
                            tp.setUserLanguage(status.getUser().getLang());
                            tp.setUserProfileImageUrl(status.getUser().getProfileImageURL().toString());
                            tp.setTextCreatedDate(status.getCreatedAt());
                            tp.setText(status.getText());
                            list.add(tp);
                            if (list.size() % 10 == 0) {
                                setList(list);
                                System.out.println("list set edildi size : " + list.size());
                            }
                        }
                    } else {
                        System.out.println("---------list doldu------------");
                        stopJob();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Twitter onStatus Hata : " + ex.getMessage());
                ex.printStackTrace();
                stopJob();
            }
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice sdn) {
            System.out.println("delete:" + sdn.toString());
        }

        @Override
        public void onTrackLimitationNotice(int i) {
            System.out.println("dddf:" + i);
        }

        @Override
        public void onScrubGeo(long l, long l1) {
            System.out.println("lll ::" + l + " , l1 :" + l1);
        }

        @Override
        public void onException(Exception excptn) {
            excptn.printStackTrace();
            myStream.shutdown();
        }
    }

    class BaglantiListener implements ConnectionLifeCycleListener {

        @Override
        public void onConnect() {
            System.out.println("Baglandi");
        }

        @Override
        public void onDisconnect() {
            System.out.println("kapandi");
            myStream.shutdown();
        }

        @Override
        public void onCleanUp() {
            System.out.println("Clean");
        }
    }
}
