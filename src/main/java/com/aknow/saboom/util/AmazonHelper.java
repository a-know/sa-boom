package com.aknow.saboom.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * AmazonHelper
 * @author a-know
 * Amazon Product Advertising APIを介して商品情報を取得する為のHelperクラス。
 */
public final class AmazonHelper {

    private int preIndex;
    private String[] divideArtist;

    private final String separator = "@@##@@";

    /**
     * AmazonHelper コンストラクタ
     * @param artist 検索条件としてのアーティスト名。区切り文字で複数の条件が渡される。区切り文字は「@@##@@」とする。
     * @param cd_or_dvd 検索条件としての、CD/DVD区分。区切り文字で複数の条件が渡される。区切り文字は「@@##@@」とする。
     */
    public AmazonHelper(String artist){

        //区切り文字で文字列を分割。
        this.divideArtist = artist.split(this.separator);

    }

    /**
     * getFreshPubs()
     * コンストラクタでセットした検索条件を元に新譜情報を検索、結果をリストで返す。
     *
     * @return List<FreshPub> 検索結果としての新譜情報のList
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public List<FreshPub> getFreshPubs() throws InvalidKeyException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException{

        List<FreshPub> freshPubs = new ArrayList<FreshPub>();

        for(int i = 1; i < this.divideArtist.length; i++){//指定された条件の数だけ繰り返す。区切り文字の位置の関係で、index=0のときの文字列は空文字なので、indexは1スタート
            Map<String, String> params = prepareParameters(i);
            freshPubs.add(search(params, i));
        }

        return freshPubs;
    }

    /**
     *
     * @param i 何番目の検索条件を用いるか。
     * @return Map<String, String> 検索条件をセットしたMap。
     */
    private Map<String, String> prepareParameters(int i){

        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2011-08-01");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "Music");
        params.put("Sort", "-releasedate");//発売日が新しいものから
        params.put("ItemPage", "1");
        params.put("AssociateTag", "aknow-22");
        params.put("ResponseGroup", "Medium");//参考　http://www.ajaxtower.jp/ecs/responsegroup/
        if(this.divideArtist[i] != null && !"".equals(this.divideArtist[i])){
            params.put("Artist", this.divideArtist[i]);//i番目のアーティスト名を検索条件としてセット
        }
        return params;
    }

    /**
     * search(Map<String, String> params, int i)
     * 検索を実施。
     *
     * @param params Map<String, String> 検索条件をセットしたMap。
     * @param i 何番目の検索条件を用いるか。
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws ParserConfigurationException
     */
    private FreshPub search(Map<String, String> params, int i) throws SAXException, IOException, InvalidKeyException, NoSuchAlgorithmException, ParserConfigurationException{

        SignedRequestsHelper helper = new SignedRequestsHelper();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //リクエストの同期を取る
        NodeList items = null;
        try{
            waitAccess();

            String requestUrl = helper.sign(params);
            Document document = builder.parse(requestUrl);
            items = document.getElementsByTagName("Item");
        }catch(Exception e){
            return new FreshPub(params.get("Artist"), "", "../images/Music.png");
        }

        //検索結果はリリース日時で降順に返ってきているため、無条件に先頭の要素を取得でおｋ
        return createFreshPub(items, 0, i);
    }

    /**
     * waitAccess()
     * アプリケーション全体を通して、処理が1秒以上の間隔で行われるようにする。
     */
    @SuppressWarnings("static-method")
    private void waitAccess(){

        MemcacheService service = MemcacheServiceFactory.getMemcacheService();
        while(true){
            Date lastAccess = (Date) service.get("lastAccess");
            if(lastAccess == null){
                lastAccess = new Date(0);
            }
            Date current = new Date();

            //最終アクセスから1秒以上経過していなければsleep
            if(lastAccess.getTime() + 1000 <= current.getTime()){
                service.put("lastAccess", current);
                break;
            }
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                //no operation
            }
        }
    }

    /**
     * createFreshPub(NodeList items, int index, int i)
     * 新譜情報を返す
     *
     * @param items APIによる検索結果のうち、"Item"ノードのリスト
     * @param index NodeListの走査開始インデックス
     * @param i 何番目の検索条件を用いるか。
     * @return
     */
    private FreshPub createFreshPub(NodeList items, int index, int i){

        String url = "";
        String artist = this.divideArtist[i];
        NodeList imageData = null;
        String imageUrl = "../images/Music.png";

        while(true){
            Element item  = (Element) items.item(index);
            if(this.divideArtist[i].equals(getTextContent(item, "Artist"))){//アーティスト名が完全一致でなければ許さない
                url = getTextContent(item, "DetailPageURL");
                //imageは別NodeList
                imageData = item.getElementsByTagName("SmallImage");
                imageUrl = getTextContent(((Element) imageData.item(0)),"URL");
                if(imageUrl == null || "".equals(imageUrl)) imageUrl = "../images/Music.png";
                break;
            }else{
                index++;
            }
            if(index > items.getLength()) break;
        }
        return new FreshPub(artist, url, imageUrl);
    }

    /**
     * getTextContent(Element item, String name)
     * NodeListから取り出されたElementから、指定された属性の値を取り出す。
     *
     * @param item NodeListから取り出されたElement
     * @param name 属性名称
     * @return 指定された属性の値
     */
    @SuppressWarnings("static-method")
    private String getTextContent(Element item, String name){
        try{
            return item.getElementsByTagName(name).item(0).getTextContent();
        }catch(Exception e){
            return "";
        }
    }

    /**
     * getFreshPubsForRandomRanking()
     * コンストラクタでセットした検索条件を元に新譜情報を検索、結果をリストで返す。ランダムアーティストランキング専用。
     *
     * @return List<FreshPub> 検索結果としての新譜情報のList
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public List<FreshPub> getFreshPubsForRandomRanking(int needCount) throws InvalidKeyException, NoSuchAlgorithmException, SAXException, IOException, ParserConfigurationException{

        List<FreshPub> freshPubs = new ArrayList<FreshPub>();
        this.preIndex = 0;

        for(int i = 1; i <= needCount; i++){
            Map<String, String> params = prepareParametersForRandomRanking();
            freshPubs.add(searchForRandomRanking(params));
        }

        return freshPubs;
    }

    /**
     *
     * @param i 何番目の検索条件を用いるか。
     * @return Map<String, String> 検索条件をセットしたMap。
     */
    private Map<String, String> prepareParametersForRandomRanking(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2010-11-01");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "Music");
        params.put("Sort", "-releasedate");//発売日が新しいものから
        params.put("ItemPage", "1");
        params.put("AssociateTag", "aknow-22");
        params.put("ResponseGroup", "Medium");//参考　http://www.ajaxtower.jp/ecs/responsegroup/
        params.put("Artist", this.divideArtist[1]);
        return params;
    }

    /**
     * searchForRandomRanking(Map<String, String> params, int i)
     * 検索を実施。ランダムアーティストランキング専用。
     *
     * @param params Map<String, String> 検索条件をセットしたMap。
     * @param i 何番目の検索条件を用いるか。
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws ParserConfigurationException
     */
    private FreshPub searchForRandomRanking(Map<String, String> params) throws SAXException, IOException, InvalidKeyException, NoSuchAlgorithmException, ParserConfigurationException{

        SignedRequestsHelper helper = new SignedRequestsHelper();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //リクエストの同期を取る
        NodeList items = null;
        try{
            waitAccess();

            String requestUrl = helper.sign(params);
            Document document = builder.parse(requestUrl);
            items = document.getElementsByTagName("Item");
        }catch(Exception e){
            return new FreshPub(params.get("Artist"), "", "../images/Music.png");
        }

        //検索結果はリリース日時で降順に返ってきているため、無条件に先頭の要素を取得でおｋ
        return createFreshPubForRandomRanking(items);
    }

    /**
     * createFreshPubForRandomRanking(NodeList items, int index, int i)
     * 新譜情報を返す
     *
     * @param items APIによる検索結果のうち、"Item"ノードのリスト
     * @param index NodeListの走査開始インデックス
     * @param i 何番目の検索条件を用いるか。
     * @return
     */
    private FreshPub createFreshPubForRandomRanking(NodeList items){

        String url = "";
        String artist = this.divideArtist[1];
        NodeList imageData = null;
        String imageUrl = "../images/fail.png";
        int index = this.preIndex;

        while(true){
            if(index > items.getLength()) break;
            try{
                Element item  = (Element) items.item(index);
                if(this.divideArtist[1].equals(getTextContent(item, "Artist"))){//アーティスト名が完全一致であれば取得
                    url = getTextContent(item, "DetailPageURL");
                    //imageは別NodeList
                    imageData = item.getElementsByTagName("SmallImage");
                    imageUrl = getTextContent(((Element) imageData.item(0)),"URL");
                    if(imageUrl == null || "".equals(imageUrl)) imageUrl = "../images/Music.png";
                    index++;
                    break;
                }else if(this.divideArtist[1].toLowerCase().equals(getTextContent(item, "Artist").toLowerCase())){//どちらも全てを小文字に変換した状態で比較
                    url = getTextContent(item, "DetailPageURL");
                    //imageは別NodeList
                    imageData = item.getElementsByTagName("SmallImage");
                    imageUrl = getTextContent(((Element) imageData.item(0)),"URL");
                    if(imageUrl == null || "".equals(imageUrl)) imageUrl = "../images/Music.png";
                    index++;
                    break;
                }else{
                    int halfLength = this.divideArtist[1].length() / 2;
                    if(halfLength > getTextContent(item, "Artist").length()) halfLength = getTextContent(item, "Artist").length();
                    if(this.divideArtist[1].substring(0, halfLength).toLowerCase().equals(getTextContent(item, "Artist").substring(0, halfLength).toLowerCase())){//比較文字列の先頭が一致していれば可とする
                        url = getTextContent(item, "DetailPageURL");
                        //imageは別NodeList
                        imageData = item.getElementsByTagName("SmallImage");
                        imageUrl = getTextContent(((Element) imageData.item(0)),"URL");
                        if(imageUrl == null || "".equals(imageUrl)) imageUrl = "../images/Music.png";
                        index++;
                        break;
                    }
                    index++;
                }
            }catch(NullPointerException e){
                index++;
            }
        }

        this.preIndex = index;

        return new FreshPub(artist, url, imageUrl);
    }
}
