package com.aknow.saboom.service.graph;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.User;
import com.aknow.saboom.model.graph.UserRankingGraphData;


public class GetUserRankingGraphService {
    @SuppressWarnings("static-method")
    public List<UserRankingGraphData> getData(){

        List<UserRankingGraphData> returnList = new ArrayList<UserRankingGraphData>();

        UserMeta meta = new UserMeta();
        List<User> users = Datastore.query(meta).filter(meta.isPrivate.equal(Boolean.FALSE)).sort(meta.totalPlayCount.desc).limit(10).asList();

        for(User e : users){
            UserRankingGraphData data = new UserRankingGraphData();
            int rank = returnList.size() + 1;
            data.setLoginIDwithRank(rank + "位：" + e.getLoginId());
            data.setLoginID(e.getLoginId());
            data.setPlaycount(e.getTotalPlayCount());
            returnList.add(data);
        }

        return returnList;
    }
}
