package com.tourcool.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.linkage.LinkageRecyclerView;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.frame.library.core.widget.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.frame.library.core.widget.linkage.bean.BaseGroupedItem;
import com.frame.library.core.widget.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.frame.library.core.widget.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.bean.ElemeGroupedItem;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.UserInfo;
import com.tourcool.bean.screen.Channel;
import com.tourcool.bean.screen.ChildNode;
import com.tourcool.bean.screen.ColumnItem;
import com.tourcool.bean.screen.ScreenPart;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.event.service.ServiceEvent;
import com.tourcool.smartcity.R;
import com.tourcool.ui.calender.YellowCalenderDetailActivity;
import com.tourcool.ui.certify.SelectCertifyActivity;
import com.tourcool.ui.constellation.ConstellationListActivity;
import com.tourcool.ui.driver.AgainstScoreQueryActivity;
import com.tourcool.ui.driver.DriverIllegalQueryActivity;
import com.tourcool.ui.express.ExpressQueryActivity;
import com.tourcool.ui.garbage.GarbageQueryActivity;
import com.tourcool.ui.kitchen.VideoListActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.mvp.service.SecondaryServiceActivity;
import com.tourcool.ui.parking.FastParkingActivity;
import com.tourcool.ui.social.SocialBaseInfoActivity;
import com.tourcool.ui.social.detail.SocialListDetailActivity;
import com.tourcool.widget.webview.CommonWebViewActivity;
import com.tourcool.widget.webview.WebViewConstant;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CERTIFY_NAME;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CONSTELLATION;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_DRIVER_AGAINST;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_DRIVER_SCORE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_EXPRESS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_GARBAGE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_KITCHEN;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_PARKING;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_BASE_INFO;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_BIRTH;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_GS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_YELLOW_CALENDER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_LINK_INNER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_LINK_OUTER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_NATIVE;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_NONE;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_WAITING;
import static com.tourcool.core.constant.ScreenConstant.SUB_CHANNEL;
import static com.tourcool.core.constant.ScreenConstant.SUB_COLUMN;
import static com.tourcool.core.constant.ScreenConstant.TIP_WAIT_DEV;
import static com.tourcool.core.constant.SocialConstant.EXTRA_SOCIAL_TYPE;
import static com.tourcool.core.constant.SocialConstant.TIP_GO_CERTIFY;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_RICH_TEXT_ENABLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_TITLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_URL;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月22日16:08
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public class ServiceFragment extends BaseTitleFragment implements OnRefreshListener {
    private static final int SPAN_COUNT_FOR_GRID_MODE = 3;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;
    private SmartRefreshLayout smartRefreshCommon;
    private LinkageRecyclerView recyclerView;
    /**
     * 是否需要请求
     */
    private boolean needRequest = false;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(ServiceFragment.this)) {
            EventBus.getDefault().register(ServiceFragment.this);
        }
        smartRefreshCommon = mContentView.findViewById(R.id.smartRefreshCommon);
        recyclerView = mContentView.findViewById(R.id.linkageView);
        smartRefreshCommon.setRefreshHeader(new ClassicsHeader(mContext));
        smartRefreshCommon.setEnableLoadMore(false);
        smartRefreshCommon.setOnRefreshListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBackgroundColor(TourCooUtil.getColor(R.color.blue4287FF));
        TextView mainText = titleBar.getMainTitleTextView();
        titleBar.setTitleMainText("智慧宜兴");
        mainText.setText("");
        mainText.setTextColor(TourCooUtil.getColor(R.color.white));
        mainText.setCompoundDrawablesWithIntrinsicBounds(null, null, TourCooUtil.getDrawable(R.mipmap.icon_title_name), null);
        titleBar.setBgResource(R.drawable.bg_gradient_title_common);
    }


    public static ServiceFragment newInstance() {
        Bundle args = new Bundle();
        ServiceFragment fragment = new ServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initLinkageData(LinkageRecyclerView linkage, List<ElemeGroupedItem> itemList) {
       /* Gson gson = new Gson();
        List<ElemeGroupedItem> itemList = gson.fromJson(getString(R.string.eleme_json),
                new TypeToken<List<ElemeGroupedItem>>() {
                }.getType());*/
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

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
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

    private class ServiceSecondaryAdapterConfig implements
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
                TourCooLogUtil.e(TAG, "onBindViewHolder---->item.info = null");
                return;
            }
            holder.getView(R.id.llMatrix).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClickEvent(item.info);
                }
            });
            tvMatrixIconName.setText(item.info.getTitle());
            GlideManager.loadCircleImg(TourCooUtil.getUrl(item.info.getImgUrl()), imageView, R.mipmap.ic_splash_logo);
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
        ApiRepository.getInstance().requestServiceList().compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        resetRequestStatus();
                        finishRefresh();
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
                        super.onRequestError(e);
                        resetRequestStatus();
                        finishRefresh();
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
                for (ScreenPart screenPart : screenPartList) {
                    if (screenPart == null) {
                        continue;
                    }
                    itemList.addAll(parseElemegroupItem(screenPart));
                }
                baseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initLinkageData(recyclerView, itemList);
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
            if (childNode == null || childNode.getDetail() == null || childNode.getType() == null || !childNode.isVisible()) {
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
                    item.info = new ElemeGroupedItem.ItemInfo(channel.getTitle(), screenPart.getColumnName(), channel.getDescription(), channel.getContent());
                    item.info.setType(SUB_CHANNEL);
                    item.info.setRichContent(channel.getContent());
                    TourCooLogUtil.i("富文本--->" + channel.getContent());
                    item.info.setJumpWay(channel.getJumpWay());
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
                    item.info = new ElemeGroupedItem.ItemInfo(columnItem.getName(), screenPart.getColumnName(), columnItem.getName(), columnItem.getLink());
                    item.info.setType(SUB_COLUMN);
                    item.info.setColumnName(screenPart.getColumnName());
                    item.info.setChildren(childNode.getChildren());
                    item.info.setJumpWay(columnItem.getJumpWay());
                    TourCooLogUtil.i("富文本--->呃呃呃呃呃");
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

    private void finishRefresh() {
        if (smartRefreshCommon != null) {
            smartRefreshCommon.finishRefresh();
        }
    }


    private void handleClickEvent(ElemeGroupedItem.ItemInfo item) {
        if (item == null) {
            ToastUtil.show("未匹配到对应栏目");
            return;
        }
        switch (item.getType()) {
            case SUB_CHANNEL:
                skipByJumpWay(item.getJumpWay(), item.getTitle(), item.getLink(), item.getRichContent());
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
                TourCooLogUtil.i("channelList=", channelList.size());
//                intent.putExtra("secondService", item.getChildren());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        super.onVisibleChanged(isVisibleToUser);
        TourCooLogUtil.i(TAG, "是否展示给客户：" + isVisibleToUser + "是否加载过数据:" + hasLoadData());
        if (isVisibleToUser && !hasLoadData() && needRequest) {
            requestServiceList();
        }
    }

    private boolean hasLoadData() {
        return recyclerView.getPrimaryAdapter() != null && !recyclerView.getPrimaryAdapter().getStrings().isEmpty();
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
        TourCooLogUtil.i(TAG, "收到消息 刷新请求状态");
        needRequest = true;
    }

    private void resetRequestStatus() {
        needRequest = false;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(ServiceFragment.this);
        super.onDestroy();
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
                TourCooLogUtil.e(TAG, "detail == null");
                continue;
            }
            Channel channel = JSON.parseObject(detail.toJSONString(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        return channelList;
    }


    private void skipBrightKitchen() {
        Intent intent = new Intent();
        intent.setClass(mContext, VideoListActivity.class);
        startActivity(intent);
    }


    private void skipSocialBase() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
            return;
        }
        if (!AccountHelper.getInstance().getUserInfo().isVerified()) {
            ToastUtil.show(TIP_GO_CERTIFY);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, SocialBaseInfoActivity.class);
        startActivity(intent);
    }

    private void skipSocialListDetail(String type) {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
            return;
        }
        if (!AccountHelper.getInstance().getUserInfo().isVerified()) {
            ToastUtil.show(TIP_GO_CERTIFY);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, SocialListDetailActivity.class);
        intent.putExtra(EXTRA_SOCIAL_TYPE, type);
        startActivity(intent);
    }

    private void skipParking() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, FastParkingActivity.class);
        startActivity(intent);
    }

    private void skipLogin() {
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        startActivity(intent);
    }

    private void skipNativeByCondition(String title, String link) {
        switch (StringUtil.getNotNullValue(title)) {
            case ITEM_TYPE_SOCIAL_BASE_INFO:
                skipSocialBase();
                break;
            case ITEM_TYPE_SOCIAL_QUERY_GS:
            case ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER:
            case ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK:
            case ITEM_TYPE_SOCIAL_QUERY_BIRTH:
                skipSocialListDetail(title);
                break;
            case ITEM_TYPE_KITCHEN:
                skipBrightKitchen();
                break;
            case ITEM_TYPE_PARKING:
                skipParking();
                break;
            case ITEM_TYPE_CONSTELLATION:
                skipConstellation();
                break;
            case ITEM_TYPE_EXPRESS:
                skipExpress();
                break;
            case ITEM_TYPE_GARBAGE:
                skipGarbage();
                break;
            case ITEM_TYPE_YELLOW_CALENDER:
                skipYellowCalender();
                break;
            case ITEM_TYPE_CERTIFY_NAME:
                skipCertify();
                break;
            case ITEM_TYPE_DRIVER_AGAINST:
                skipDriverAgainst();
                break;
            case ITEM_TYPE_DRIVER_SCORE:
                skipDriverScore();
                break;
            default:
                skipWebView(link, title);
                break;
        }
    }

    /**
     * 星座
     */
    private void skipConstellation() {
        Intent intent = new Intent();
        intent.setClass(mContext, ConstellationListActivity.class);
        startActivity(intent);
    }

    /**
     * 快递物流
     */
    private void skipExpress() {
        Intent intent = new Intent();
        intent.setClass(mContext, ExpressQueryActivity.class);
        startActivity(intent);
    }


    /**
     * 快递物流
     */
    private void skipGarbage() {
        Intent intent = new Intent();
        intent.setClass(mContext, GarbageQueryActivity.class);
        startActivity(intent);
    }


    /**
     * 查黄历
     */
    private void skipYellowCalender() {
        Intent intent = new Intent();
        intent.setClass(mContext, YellowCalenderDetailActivity.class);
        startActivity(intent);
    }

    private void skipWebView(String link, String title) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WEB_VIEW_URL, link);
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, false);
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, title);
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }

    private void skipWebViewRich(String richContent, String title) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, true);
        intent.putExtra(EXTRA_WEB_VIEW_URL, "");
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, title);
        WebViewConstant.richText = richContent;
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }


    private void skipByJumpWay(int jumpWay, String title, String link, String richText) {
        switch (jumpWay) {
            case CLICK_TYPE_LINK_OUTER:
                //展示外链
                skipWebView(StringUtil.getNotNullValue(link), title);
                break;
            case CLICK_TYPE_NONE:
//                        ToastUtil.show("什么也不做");
                break;
            case CLICK_TYPE_NATIVE:
                //展示原生
                skipNativeByCondition(title, link);
                break;
            case CLICK_TYPE_LINK_INNER:
                //展示外链
                skipWebViewRich(richText, title);
                break;

            case CLICK_TYPE_WAITING:
                //待开发
                ToastUtil.show(TIP_WAIT_DEV);
                break;
            default:
                break;
        }
    }

    private void skipCertify() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, SelectCertifyActivity.class);
            startActivity(intent);
        }
    }

    private void skipDriverAgainst() {
        Intent intent = new Intent();
        intent.setClass(mContext, DriverIllegalQueryActivity.class);
        startActivity(intent);
    }

    private void skipDriverScore() {
        Intent intent = new Intent();
        intent.setClass(mContext, AgainstScoreQueryActivity.class);
        startActivity(intent);
    }


    /**
     * 收到用户信息消息
     *
     * @param userInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoRefreshEvent(UserInfoEvent userInfoEvent) {
        if (userInfoEvent == null) {
            return;
        }
        TourCooLogUtil.i(TAG, "刷新用户信息");
        if (AccountHelper.getInstance().isLogin()) {
            refreshUserInfo();
        }

    }


    /**
     * 刷新用户信息
     */
    private void refreshUserInfo() {
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            UserInfo userInfo = parseJavaBean(entity.data, UserInfo.class);
                            if (userInfo == null) {
                                return;
                            }
                            AccountHelper.getInstance().saveUserInfoToDisk(userInfo);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {

                        TourCooLogUtil.e(TAG, e.toString());

                    }
                });
    }
}
