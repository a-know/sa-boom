package com.aknow.saboom.service.graph;

import java.util.ArrayList;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.User;
import com.aknow.saboom.model.graph.TotalPlayCountByUser;


public class GetGraphDataForTotalPlayCountByUserService {

    static final UserMeta userMeta = UserMeta.get();

    @SuppressWarnings("static-method")
    public ArrayList<TotalPlayCountByUser> getData(String loginID){

        ArrayList<TotalPlayCountByUser> result = new ArrayList<TotalPlayCountByUser>();

        User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();

        if(user == null){
            return result;
        }

        ArrayList<PlayCountData> datas = (ArrayList<PlayCountData>) user.getPlayCountDataRefs().getModelList();

        for(PlayCountData e : datas){
            StringBuffer yyyymmdd = new StringBuffer("20");
            yyyymmdd.append(e.getYyMMddHHmmss().substring(0, 2));
            yyyymmdd.append("/");
            yyyymmdd.append(e.getYyMMddHHmmss().substring(2, 4));
            yyyymmdd.append("/");
            yyyymmdd.append(e.getYyMMddHHmmss().substring(4, 6));
            result.add(new TotalPlayCountByUser(yyyymmdd.toString(), e.getTotalPlayCount()));
        }

        if(result.size() > 5){
            int remove_cnt = result.size() - 5;
            for(int i = remove_cnt - 1; i >= 0; i--){
                result.remove(i);
            }
        }

        return result;

    }
}
