package pers.tpec.games.colorbeans2.objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import pers.tpec.tpecview.utils.SharedPreferencesUtil;

public class Statistics {
    private long total_score = 0;
    private long total_time_sec = 0;
    private long total_game_cnt = 0;
    private long total_step_cnt = 0;
    private long max_time_sec_one_game = 0;

    public void load(Context context) {
        SharedPreferences sp = SharedPreferencesUtil.getSP(context, "statistics");
        total_score = sp.getLong("total_score", 0);
        total_time_sec = sp.getLong("total_time_sec", 0);
        total_game_cnt = sp.getLong("total_game_cnt", 0);
        total_step_cnt = sp.getLong("total_step_cnt", 0);
        max_time_sec_one_game = sp.getLong("max_time_sec_one_game", 0);
    }

    public void save(Context context) {
        java.util.Map<String, Object> m = new HashMap<>();
        m.put("total_score", total_score);
        m.put("total_time_sec", total_time_sec);
        m.put("total_game_cnt", total_game_cnt);
        m.put("total_step_cnt", total_step_cnt);
        m.put("max_time_sec_one_game", max_time_sec_one_game);
        SharedPreferencesUtil.save(context, "statistics", m);
    }

    public void update_gameover(long score, long time_sec, long step_cnt) {
        total_score += score;
        total_time_sec += time_sec;
        total_game_cnt++;
        total_step_cnt += step_cnt;
        max_time_sec_one_game = time_sec > max_time_sec_one_game ? time_sec : max_time_sec_one_game;
    }


    private static final Statistics instance = new Statistics();

    public static Statistics getInstance() {
        return instance;
    }

    private Statistics() {

    }
}
