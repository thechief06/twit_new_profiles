package com.miya.twit.keywrd;

import com.miya.twit.mongodb.DBConnect;
import com.miya.twit.utils.DataInitializer;
import com.mongodb.DBCollection;
import java.io.IOException;
import java.util.ArrayList;
import twitter4j.*;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;

/**
 *
 * @author ertugrula
 */
public class Main {

    public TurkishSentenceParser sentenceParser;
    public TwitterStream myStream;

    public Main() throws IOException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("fO4dl6RWZ5TI4X452EUA");
        cb.setOAuthConsumerSecret("rXw4RrLKGJZHnDMcb001MrndkMA5YhSMSCo5G1QNc");
        cb.setOAuthAccessToken("711975678-S8Rx4dOCdI4NcsWvrIo4NFAM76K21vWWm04xOZ6J");
        cb.setOAuthAccessTokenSecret("LtiL9eynQKZKuRlyufN3tVAYLQQJzdSRzvqmQF1AJ3I");
        cb.setDebugEnabled(true);
        cb.setIncludeEntitiesEnabled(true);
        cb.setIncludeRTsEnabled(true);

        cb.setJSONStoreEnabled(true);
        cb.setUseSSL(false); 
        System.setProperty("twitter4j.http.useSSL","false");
        myStream = new TwitterStreamFactory(cb.build()).getInstance();
        TurkishMorphParser morphParser = TurkishMorphParser.createWithDefaults();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        sentenceParser = new TurkishSentenceParser(
                morphParser,
                disambiguator
        );
    }

    public static void main(String[] args) throws IOException {

        new Main().metod();
    }

    public void metod() {
        String[] subject = new String[]{"ok"};
        DataInitializer dIni = new DataInitializer();
        new StreamJob("1242", myStream).startJob(subject);
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
}
