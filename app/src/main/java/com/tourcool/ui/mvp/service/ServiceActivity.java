package com.tourcool.ui.mvp.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.linkage.LinkageRecyclerView;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.frame.library.core.widget.linkage.bean.BaseGroupedItem;
import com.frame.library.core.widget.linkage.bean.SimpleServiceEntity;
import com.frame.library.core.widget.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.frame.library.core.widget.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.tourcool.bean.ElemeGroupedItem;
import com.tourcool.bean.screen.Channel;
import com.tourcool.bean.screen.ChildNode;
import com.tourcool.bean.screen.ColumnItem;
import com.tourcool.bean.screen.ScreenPart;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.event.service.ServiceEvent;
import com.tourcool.smartcity.R;
import com.tourcool.ui.main.TestFragment;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.constant.ScreenConsrant.SUB_CHANNEL;
import static com.tourcool.core.constant.ScreenConsrant.SUB_COLUMN;
import static com.tourcool.ui.main.MainHomeFragmentNew.EXTRA_CLASSIFY_NAME;
import static com.tourcool.ui.main.MainHomeFragmentNew.EXTRA_FIRST_CHILD_ID;

/**
 * @author :JenkinsZhou
 * @description :服务页
 * @company :途酷科技
 * @date 2019年10月30日14:14
 * @Email: 971613168@qq.com
 */

@SuppressWarnings("unchecked")
public class ServiceActivity extends BaseMvpTitleActivity {
    private static final int SPAN_COUNT_FOR_GRID_MODE = 3;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;
     public static final String TAG = "ServiceActivity";

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    private void initLinkageData(LinkageRecyclerView linkage, List<ElemeGroupedItem> itemList) {
       /* Gson gson = new Gson();
        List<ElemeGroupedItem> itemList = gson.fromJson(getString(R.string.eleme_json),
                new TypeToken<List<ElemeGroupedItem>>() {
                }.getType());*/
        TourCooLogUtil.i("服务数据", itemList);
        TourCooLogUtil.d("服务数据json", getString(R.string.eleme_json));
      /*  List<ElemeGroupedItem> items = new ArrayList<>();
        ElemeGroupedItem elemeGroupedItem = new ElemeGroupedItem(false, "交通");
        elemeGroupedItem.info = new ElemeGroupedItem.ItemInfo("智慧停车", "交通", "这是item内容");
        items.add(elemeGroupedItem);
        ElemeGroupedItem elemeGroupedItem1 = new ElemeGroupedItem(false, "测试");
        elemeGroupedItem1.info = new ElemeGroupedItem.ItemInfo("个人信用", "测试", "哈哈哈");
        items.add(elemeGroupedItem1);
        ElemeGroupedItem elemeGroupedItem2 = new ElemeGroupedItem(false, "诚信");
        elemeGroupedItem2.info = new ElemeGroupedItem.ItemInfo("阿斯达", "诚信", "电饭锅和地方");
        items.add(elemeGroupedItem1);
        items.add(elemeGroupedItem2);*/
        linkage.init(itemList, new ServiceAdapterConfig(), new ServiceSecondaryAdapterConfig());
        linkage.setGridMode(true);
    }

    @Override
    public void loadData() {
        requestServiceList();
    }

    private static class ServiceAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_category_layout;
        }

        @Override
        public int getGroupTitleViewId() {
            return R.id.tvCategoryName;
        }

        @Override
        public int getRootViewId() {
            return R.id.layout_group;
        }

        @Override
        public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
            TextView tvTitle = ((TextView) holder.mGroupTitle);
            tvTitle.setText(title);
            RelativeLayout layoutGroup = holder.getView(R.id.layout_group);
            tvTitle.setTextColor(ContextCompat.getColor(mContext,
                    selected ? R.color.black333333 : R.color.grayA2A2A2));
            layoutGroup.setBackground(selected ? TourCooUtil.getDrawable(R.drawable.tab) : TourCooUtil.getDrawable(R.color.grayF5F5F5));
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
        }
    }

    private  class ServiceSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getGridLayoutId() {
            return R.layout.item_matrix_layout;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.adapter_eleme_secondary_linear;
        }

        @Override
        public int getHeaderLayoutId() {
            return R.layout.default_adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return 0;
        }

        @Override
        public int getHeaderTextViewId() {
            return R.id.secondary_header;
        }

        @Override
        public int getSpanCountOfGridMode() {
            return SPAN_COUNT_FOR_GRID_MODE;
        }

        @Override
        public void onBindViewHolder(LinkageSecondaryViewHolder holder,
                                     BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
            ImageView imageView = holder.getView(R.id.ivMatrixIcon);
            TextView tvMatrixIconName = holder.getView(R.id.tvMatrixIconName);
//            ((TextView) holder.getView(R.id.iv_goods_name)).setText(item.info.getTitle());
           /* Glide.with(mContext).load(item.info.getImgUrl()).into((ImageView) holder.getView(R.id.iv_goods_img));
            holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                //TODO
            });*/

            /*holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                //TODO
            });*/
            if (item.info == null) {
                return;
            }
            holder.getView(R.id.llMatrix).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClickEvent(item.info);
                }
            });
            tvMatrixIconName.setText(item.info.getTitle());
              TourCooLogUtil.i(TAG,"图片链接1:"+item.info.getImgUrl());
            GlideManager.loadCircleImg(TourCooUtil.getUrl(item.info.getImgUrl()), imageView);
        }

        @Override
        public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
//            ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
           /* ImageView imageView = holder.getView(R.id.ivHeaderImage);
            GlideManager.loadImg(R.mipmap.icon_service_group, imageView);*/
            ((TextView) holder.getView(R.id.tvHeaderTitle)).setText(item.header);

        }

        @Override
        public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

        }
    }

    private void requestServiceList() {
        ApiRepository.getInstance().requestServiceList().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            TourCooLogUtil.i(TAG, entity);
                            //处理返回的实体数据
                            handleSuccessCallBack(entity);
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
//                        super.onRequestError(e);
                        if(e.toString().contains(RequestConfig.STRING_REQUEST_TOKEN_INVALID)){
                            ToastUtil.show("登录过期");
                        }else {
                            ToastUtil.show("服务器异常:" + e.toString());
                        }

                    }
                });
    }


    private void handleSuccessCallBack(BaseResult baseResult) {
        if (baseResult == null || baseResult.data == null) {
            return;
        }
        ThreadPoolManager.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                List<ScreenPart> screenPartList = parseJsonToBeanList(baseResult.data, ScreenPart.class);
                List<ElemeGroupedItem> itemList = new ArrayList<>();
                List<SimpleServiceEntity> simpleServiceEntityList = new ArrayList<>();
                for (ScreenPart screenPart : screenPartList) {
                    if (screenPart == null) {
                        continue;
                    }
                    itemList.addAll(parseElemegroupItem(screenPart));
                    //用来定位用户选择的是哪个分组下面的
                    SimpleServiceEntity serviceEntity = parseServiceEntity(screenPart);
                    if (serviceEntity != null) {
                        simpleServiceEntityList.add(serviceEntity);
                    }
                }
                LinkageRecyclerView recyclerView = mContentView.findViewById(R.id.linkageView);
                baseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initLinkageData(recyclerView, itemList);
                        int selesctPosition = getSelectPosition(simpleServiceEntityList);
                        if(selesctPosition>-1){
                            //定位到用户选择的分类
                            recyclerView.setCurrentSelectNotSmooth(selesctPosition);
                        }
                    }
                });
            }
        });
    }


    private List<ElemeGroupedItem> parseElemegroupItem(ScreenPart screenPart) {
        List<ElemeGroupedItem> elemeGroupedItemList = new ArrayList();
        if (screenPart == null || screenPart.getChildren() == null || TextUtils.isEmpty(screenPart.getColumnName())) {
            return elemeGroupedItemList;
        }
        ElemeGroupedItem header = new ElemeGroupedItem(true, screenPart.getColumnName());
        elemeGroupedItemList.add(header);
        List<ChildNode> childNodeList = screenPart.getChildren();
        for (ChildNode childNode : childNodeList) {
            if (childNode == null || childNode.getDetail() == null || childNode.getType() == null|| !childNode.isVisible() ) {
                continue;
            }
            ElemeGroupedItem item = null;
            switch (childNode.getType()) {
                case SUB_CHANNEL:
                    Channel channel = parseJavaBean(childNode.getDetail(), Channel.class);
                    if (channel == null) {
                        break;
                    }
                    item = new ElemeGroupedItem(false, null);
                    item.info = new ElemeGroupedItem.ItemInfo(channel.getTitle(), screenPart.getColumnName(), channel.getDescription());
                    item.info.setType(SUB_CHANNEL);
                    if (TextUtils.isEmpty(channel.getCircleIcon())) {
                        item.info.setImgUrl(TourCooUtil.getUrl(channel.getIcon()));
                    } else {
                        item.info.setImgUrl(TourCooUtil.getUrl(channel.getCircleIcon()));
                    }
                    item.info.setLink(TourCooUtil.getUrl(channel.getLink()));
                    item.info.setId(channel.getId());
                    break;
                case SUB_COLUMN:
                    ColumnItem columnItem = parseJavaBean(childNode.getDetail(), ColumnItem.class);
                    if (columnItem == null) {
                        break;
                    }
                    item = new ElemeGroupedItem(false, null);
                    item.info = new ElemeGroupedItem.ItemInfo(columnItem.getName(), screenPart.getColumnName(), columnItem.getName());
                    item.info.setType(SUB_COLUMN);
                    item.info.setColumnName(screenPart.getColumnName());
                    item.info.setChildren(childNode.getChildren());
                    item.info.setParentsName(columnItem.getName());
                    if (TextUtils.isEmpty(columnItem.getCircleIcon())) {
                        item.info.setImgUrl(TourCooUtil.getUrl(columnItem.getIcon()));
                    } else {
                        item.info.setImgUrl(TourCooUtil.getUrl(columnItem.getCircleIcon()));
                    }
                    item.info.setLink(TourCooUtil.getUrl(columnItem.getLink()));
                    item.info.setId(columnItem.getId());
                    break;
                default:
                    break;
            }
            if (item != null) {
                elemeGroupedItemList.add(item);
            }
        }
        return elemeGroupedItemList;
    }


    private SimpleServiceEntity parseServiceEntity(ScreenPart screenPart) {
        if (screenPart == null || screenPart.getChildren() == null || TextUtils.isEmpty(screenPart.getColumnName())) {
            return null;
        }
        SimpleServiceEntity serviceEntity = new SimpleServiceEntity();
        serviceEntity.setGroupName(screenPart.getColumnName());
        List<Integer> childItemList = new ArrayList<>();
        serviceEntity.setChildItemIdList(childItemList);
        List<ChildNode> childNodeList = screenPart.getChildren();
        for (ChildNode childNode : childNodeList) {
            if (childNode == null || childNode.getDetail() == null || childNode.getType() == null) {
                continue;
            }
            switch (childNode.getType()) {
                case SUB_CHANNEL:
                    Channel channel = parseJavaBean(childNode.getDetail(), Channel.class);
                    if (channel == null) {
                        break;
                    }
                    childItemList.add(channel.getId());
                    break;
                case SUB_COLUMN:
                    ColumnItem columnItem = parseJavaBean(childNode.getDetail(), ColumnItem.class);
                    if (columnItem == null) {
                        break;
                    }
                    childItemList.add(columnItem.getId());
                    break;
                default:
                    break;
            }
        }
        return serviceEntity;
    }

    /**
     * 获取用户点击的tab位置
     *
     * @param list
     * @return
     */
    private int getSelectPosition(List<SimpleServiceEntity> list) {
        if (list == null || list.isEmpty()) {
            return -1;
        }
        String classifyName = getIntent().getStringExtra(EXTRA_CLASSIFY_NAME);
        int firstChildItemId = getIntent().getIntExtra(EXTRA_FIRST_CHILD_ID, -1);
        SimpleServiceEntity serviceEntity ;
        List<Integer> childItemIdlist ;
        for (int i = 0; i < list.size(); i++) {
            serviceEntity = list.get(i);
            if (serviceEntity == null) {
                continue;
            }
            childItemIdlist = serviceEntity.getChildItemIdList();
            for (Integer id : childItemIdlist) {
                if (id == firstChildItemId && serviceEntity.getGroupName().equals(classifyName)) {
                    return i;
                }
            }
        }
        return -1;
    }


    private void handleClickEvent(ElemeGroupedItem.ItemInfo item) {
        if (item == null) {
            ToastUtil.show("未匹配到对应栏目");
            return;
        }
        switch (item.getType()) {
            case SUB_CHANNEL:
                WebViewActivity.start(mContext, TourCooUtil.getUrl(item.getLink()), true);
                break;
            case SUB_COLUMN:
                TourCooLogUtil.i("点击了栏目", item.getChildren());
                List<Channel> channelList = new ArrayList<>();
                if (item.getChildren() != null) {
                    channelList.addAll(parseChannelList(item.getChildren()));
                }
                Intent intent = new Intent();
                intent.setClass(mContext, SecondaryServiceActivity.class);
                intent.putExtra("columnName", item.getColumnName());
                intent.putExtra("groupName", item.getParentsName());
                intent.putExtra("channelList", (Serializable) channelList);
                TourCooLogUtil.i(TAG, "channelList=" + channelList.size());
//                intent.putExtra("secondService", item.getChildren());
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private List<Channel> parseChannelList(Object children) {
        List<Channel> channelList = new ArrayList<>();
        String jsonData = JSON.toJSONString(children);
        JSONArray jsonArray = JSON.parseArray(jsonData);
        TourCooLogUtil.i(TAG, jsonArray);
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = (JSONObject) jsonArray.get(i);
            JSONObject detail = (JSONObject) jsonObject.get("detail");
            if (detail == null) {
                continue;
            }
            Channel channel = JSON.parseObject(detail.toJSONString(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        return channelList;
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 收到消息
     *
     * @param serviceEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceRefreshEvent(ServiceEvent serviceEvent) {
        if (serviceEvent == null) {
            return;
        }
        baseHandler.postDelayed(() -> {
            TourCooLogUtil.i(TAG, "收到消息 刷新请求状态");
            requestServiceList();
        },500);
    }

}
