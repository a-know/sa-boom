package com.aknow.saboom.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.util.Consts;

public class DeleteMemcacheController extends Controller {

    @Override
    public Navigation run() throws Exception {
        //delete memcache
        Memcache.delete(Consts.ArtistRankingGraphData_KEY);

        Memcache.delete(Consts.Top10ArtistDataListOfTotalPlayCount_KEY);
        Memcache.delete(Consts.Top10ArtistPlayCountDataListOfTotalPlayCount_KEY);
        Memcache.delete(Consts.Top10ArtistUrlDataListOfTotalPlayCount_KEY);
        Memcache.delete(Consts.Top10ArtistImageUrlDataListOfTotalPlayCount_KEY);

        Memcache.delete(Consts.ArtistRankingData_first_KEY);
        Memcache.delete(Consts.ArtistRankingData_second_KEY);
        Memcache.delete(Consts.ArtistRankingData_third_KEY);
        Memcache.delete(Consts.ArtistRankingGraphData_first_KEY);
        Memcache.delete(Consts.ArtistRankingGraphData_second_KEY);
        Memcache.delete(Consts.ArtistRankingGraphData_third_KEY);
        return null;
    }
}
