package com.aknow.saboom.service.graph;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.PlayCountByArtistMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.PlayCountByArtist;
import com.aknow.saboom.model.User;
import com.aknow.saboom.model.graph.RandomArtistRankingGraphData;


public class GetRandomArtistRankingGraphService {

    @SuppressWarnings("static-method")
    public List<RandomArtistRankingGraphData> getData(String artistName){

        List<RandomArtistRankingGraphData> returnList = new ArrayList<RandomArtistRankingGraphData>();

        PlayCountByArtistMeta pcbaMeta = new PlayCountByArtistMeta();
        List<PlayCountByArtist> list = Datastore.query(pcbaMeta).filter(pcbaMeta.artistName.equal(artistName)).sort(pcbaMeta.playCount.desc).asList();


        //そのユーザーがプライベートモードでなければ、表示情報として出力
        UserMeta userMeta = new UserMeta();
        User user = null;
        RandomArtistRankingGraphData data = null;

        for(PlayCountByArtist elm : list){
            user = Datastore.query(userMeta).filter(userMeta.loginId.equal(elm.getLoginID())).asSingle();
            if(!user.getIsPrivate().booleanValue()){
                data = new RandomArtistRankingGraphData();
                int rank = returnList.size() + 1;
                data.setLoginIDwithRank(rank + "位：" + user.getLoginId());
                data.setLoginID(user.getLoginId());
                data.setPlaycount(elm.getPlayCount());

                returnList.add(data);

            }
            if (returnList.size() == 5) break;
        }

        if(returnList.size() < 5){
            int count = 5 - returnList.size();
            for(int i = 0; i < count; i++){
                data = new RandomArtistRankingGraphData();
                data.setLoginIDwithRank("－");
                data.setPlaycount(Integer.valueOf(0));
                returnList.add(data);
            }
        }

        return returnList;
    }
}
