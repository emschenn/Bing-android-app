package emschenn.csie.ncku.webparsetest;

import android.os.StrictMode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class crawl {

    public static String crawl_func(int i,String search){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        switch (i){
            //1 moviesun; 2 VM美劇; 3 157; 4 anime1.me; 5 58
            case 1 :
                String url1 = "http://moviesun101.com/";
                try {
                    Document doc = Jsoup.connect(url1 + search).get();
                    Elements a = doc.select("div.yarpp-related");// .select("strong");
                    String result = "";
                    Pattern season = Pattern.compile("第[^\\s]+季");
                    Pattern episode = Pattern.compile("第[^\\s]+集");

                    StringTokenizer word = new StringTokenizer(a.text());
                    while (word.hasMoreTokens()) {
                        Matcher mseason = season.matcher(word.nextToken());
                        if (mseason.find()) {
                            result = result + (mseason.group(0));
                            break;
                        }
                    }
                    while (word.hasMoreTokens()) {
                        Matcher mepisode = episode.matcher(word.nextToken());
                        if (mepisode.find()) {
                            result = result + " " + (mepisode.group(0));
                            break;
                        }
                    }
                    return result;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 2 :
                String url2 = "https://vmus.cc/?s=";
                try {
                    Document doc = Jsoup.connect(url2 + search).get();
                    Elements a = doc.select(".post-title.entry-title").select("a");
                    String newurl = a.attr("href");

                    Document doc2 = Jsoup.connect(newurl).get();
                    a = doc2.select(".entry.clearfix");
                    String result = "";
                    Pattern season = Pattern.compile("第[^\\s]+季");
                    Pattern episode = Pattern.compile("(EP[0-9]+)|(Ep[0-9]+)|(ep[0-9]+)");

                    String[] word = a.toString().split("<hr>");
                    String ans1 = "", ans2 = "";
                    for (String w : word) {
                        Matcher mseason = season.matcher(w);
                        if (mseason.find()) {
                            Matcher mepisode = episode.matcher(w);
                            if (mepisode.find()) {
                                ans1 = mseason.group();
                                ans2 = w;
                            }
                        }
                    }
                    Document doc3 = Jsoup.parse(ans2);

                    String[] word2 = doc3.select("a").text().split(" ");
                    for (String w : word2) {
                        Matcher mepisode = episode.matcher(w);
                        if (mepisode.find()) {
                            ans2 = mepisode.group();
                        }
                    }
                    result = result + ans1 + " " + ans2;
                    return result;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 3 :
                String url3 = "http://www.157mov.com/index.php?s=vod-search-wd-";
                try {
                    // 影集
                    Document doc = Jsoup.connect(url3 + search + ".html").get();
                    Element a = doc.select("div.con > a").get(1);
                    String newurl = a.attr("href");
                    Document doc2 = Jsoup.connect("http://www.157mov.com" + newurl).get();
                    Element b = doc2.select("ul.dramaNumList").select("li").get(0);
                    String result = "";

                    result = result + b.text();
                    return result;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 4 :

                try {
                    Document xmlDoc = Jsoup.connect("https://anime1.me/").get(); //使用Jsoup jar 去解析網頁
                    String x = xmlDoc.toString();//.select("ul").select("li");
                    Pattern pattern = Pattern.compile(search+"</a></td>[\\s\\S]*?</td>");

                    Matcher matcher = pattern.matcher(x);
                    while (matcher.find()) {
                        return matcher.group().split("\">")[1].split("</td>")[0];
                    }
                }catch(Exception e){
                    System.out.println("aaa");
                }
                break;

            case 5 :
                String mQueryString = "https://www.58b.tv/index.php?s=home-Vod-innersearch-q-"+search+"-order-undefined";
                try{
                    Document doc = Jsoup.connect(mQueryString).get();
                    Element c = doc.select("li.g").first().select("h3.r").first();
                    String result;
                    String start = "連載至";
                    String end = "集";
                    if(c.text().indexOf(start) > -1)
                        result = c.text().substring(c.text().indexOf(start)+start.length(),c.text().indexOf(end));
                    else
                        result = "已完結";
                    return result;
                }catch (Exception e){
                    System.out.println("aa");
                }

        }
        return "error";
    }

}
