package com.tourcool.bean.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年11月27日13:59
 * @Email: 971613168@qq.com
 */
public class GreenDaoHelper {
    private DaoMaster.DevOpenHelper dbHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DaoSession getDaoSession() {
        return mDaoSession;
    }



    private static class SingletonInstance {
        private static final GreenDaoHelper INSTANCE = new GreenDaoHelper();
    }

    private GreenDaoHelper() {
    }

    public static GreenDaoHelper getInstance() {
        return GreenDaoHelper.SingletonInstance.INSTANCE;
    }

    public void initDatabase(Context context) {
        //这里之后会修改，关于升级数据库
        dbHelper = new DaoMaster.DevOpenHelper(context, "smart_city_yixing", null);
        db = dbHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
}
