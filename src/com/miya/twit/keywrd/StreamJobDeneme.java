package com.miya.twit.keywrd;

import com.miya.twit.pajo.TweetSentimentEntity;
import com.miya.twit.sentiment.ParseTweets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;

public class StreamJobDeneme {

    private TwitterStream myStream;
    // private long[] userIds = {188407220};
    private long[] userIds = {804542196};
    private TivitListener myTivitListener;
    private BaglantiListener myBaglantiListener;
    TurkishSentenceParser sentenceParser;

    public StreamJobDeneme(long[] userIds) throws IOException {
        //this.userIds = userIds;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("hxsrbUrkGeHrmQonlva2Vg");
        cb.setOAuthConsumerSecret("1lYxNAp5CyKQqFxuj2rfvZvrKhp1T6kuNrqxJr5Qyo");
        cb.setOAuthAccessToken("804542196-lmUMMiHvDGwAiXZ2A79HFtYK4GhP0Ziq9b3ULVbM");
        cb.setOAuthAccessTokenSecret("sWb5jtMlBIvLrIcxLfYnWvSvD0Ccu2hNWahw7fI");
        cb.setDebugEnabled(true);
        cb.setIncludeEntitiesEnabled(true);
        cb.setIncludeRTsEnabled(true);
        cb.setJSONStoreEnabled(true);
        myStream = new TwitterStreamFactory(cb.build()).getInstance();

        TurkishMorphParser morphParser = TurkishMorphParser.createWithDefaults();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        TurkishSentenceParser sentenceParser = new TurkishSentenceParser(
                morphParser,
                disambiguator
        );
        this.sentenceParser = sentenceParser;
    }
    private String[] keysGokhan = {"git", "ali", "bos", "kez", "icin", "daha", "gibi", "diye", "seni", "bana", "beni", "sana", "bile", "oldu", "olur", "oyle", "yeni", "olan", "yine",
        "yani", "iste", "adam", "evet", "once", "hala", "bunu", "ayni", "gece", "biri", "hadi", "saat", "olsa", "kotu", "bazi", "size", "bize", "gore", "niye", "sizi", "haha",
        "kisi", "tabi", "onun", "eger", "geri", "bizi", "dedi", "maci", "hava", "eski", "para", "uzun", "ders", "asik", "peki", "eden", "evde", "sene", "asla", "turk", "oysa", "öyle",
        "iyi", "içki", "kadar", "guzel", "değil", "olsun", "sonra", "benim", "zaman", "artik", "bugun", "nasil", "boyle", "allah", "simdi", "senin", "insan", "buyuk", "bende", "zaten",
        "yarin", "neden", "bence", "mutlu", "canim", "baska", "tamam", "biraz", "geldi", "hayat", "falan", "bazen", "olmak", "fazla", "butun", "kendi", "olmus", "lazim", "dogru", "olmaz",
        "sabah", "demek", "haber", "aksam", "devam", "yoksa", "dunya", "takip", "kadin", "bisey", "valla", "kimse", "acaba", "aynen", "cunku", "gerek", "super", "hafta", "belki", "dedim",
        "sende", "neyse", "diyen", "hemen", "arada", "erkek", "cocuk", "kanka", "varsa", "sanki", "yeter", "sizin", "merak", "vardi", "gelir", "cevap", "hayir", "gecen", "kaldi", "yalan",
        "belli", "keske", "misin", "hangi", "karsi", "nisan", "nedir", "oldum", "sarki", "onlar", "gelen", "kucuk", "askim", "soyle", "mesaj", "şimdi", "iste", "fakat", "çunku", "cemal",
        "sadece", "olarak", "herkes", "ederim", "lutfen", "olacak", "huysuz", "icinde", "oldugu", "vardir", "oluyor", "amk", "seyler", "gercek", "benden", "hicbir", "onemli", "yerine",
        "yanlis", "yalniz", "senden", "ediyor", "olmasi", "insan", "aslinda", "sanirim", "kendini", "hayirli", "herkese", "geceler", "geliyor", "bakalim", "arkadas", "diyorum", "kendimi",
        "insanlar", "gunaydin", "seviyorum", "kardesim", "istiyorum", "gercekten", "araciligiyla"};

    public boolean startJob() {
        String[] list = null;
        try {
//            List<String> words = DBManager.getKeywords();
//            list = new String[words.size()];
//            for (int i = 0; i < words.size(); i++) {
//                list[i] = words.get(i);
//            }
            list = keysGokhan;
        } catch (Exception e) {
            e.printStackTrace();
        }
        myTivitListener = new TivitListener();
        myStream.addListener(myTivitListener);
        myBaglantiListener = new BaglantiListener();
        myStream.addConnectionLifeCycleListener(myBaglantiListener);
        FilterQuery fq = new FilterQuery(userIds);
        System.out.println("sd:" + keysGokhan.length);
        fq.track(keysGokhan);
        myStream.filter(fq);
        return true;
    }

    class TivitListener implements StatusListener {

        List<TwitProfileWrapper> list = new ArrayList<TwitProfileWrapper>();

        @Override
        public void onStatus(Status status) {

            TwitProfileWrapper tp = null;
            try {
                if (status.getUser().getLang().equals("tr") && status.isRetweet()) {
                    tp = new TwitProfileWrapper();
                    tp.setUserId(status.getUser().getId());
                    tp.setUsername(status.getUser().getName());
                    tp.setScreenName(status.getUser().getScreenName());
                    tp.setUserDescription(status.getUser().getDescription());
                    tp.setUserTimeZone(status.getUser().getTimeZone());
                    tp.setUserLocation(status.getUser().getLocation());
                    tp.setUserLanguage(status.getUser().getLang());
                    tp.setText(status.getText());

                    if (tp.getUserLanguage().equals("tr")) {
                        System.out.println(tp.getText());
                        ParseTweets parseTweet = new ParseTweets(sentenceParser, null);
                        TweetSentimentEntity TSE = parseTweet.decompositionTweet(tp.getText());

                        TSE.getTweetPolarityValue();
                    }
                    if (list.size() == 20) {
                        System.out.println("******************:" + list.size());
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice sdn) {
            System.out.println("delete:" + sdn.toString());
        }

        @Override
        public void onTrackLimitationNotice(int i) {
            //System.out.println("dddf:" + i);
        }

        @Override
        public void onScrubGeo(long l, long l1) {
            System.out.println("lll ::" + l + " , l1 :" + l1);
        }

        @Override
        public void onException(Exception excptn) {
            excptn.printStackTrace();
        }

        @Override
        public void onStallWarning(StallWarning sw) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        }

        @Override
        public void onCleanUp() {
            System.out.println("Clean");
        }
    }
}
