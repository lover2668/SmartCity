package com.tourcool.ui.mvp.recharge;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.adapter.recharge.RechargeAmountAdapter;
import com.tourcool.bean.recharge.RechargeEntity;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.smartcity.R;
import com.tourcool.util.MoneyTextWatcher;

import java.util.ArrayList;
import java.util.List;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月29日13:47
 * @Email: 971613168@qq.com
 */
public class RechargeActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private RecyclerView rechargeRecyclerView;
    private RechargeAmountAdapter mRechargeAmountAdapter;
    private EditText etPhone;
    private List<RechargeEntity> mRechargeEntityList = new ArrayList<>();
    public static final int REQUEST_CODE_FOR_SINGLE_CUNTRACT_ONE = 9000;
    @Override
    protected void loadPresenter() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rechargeRecyclerView = findViewById(R.id.rechargeRecyclerView);
        findViewById(R.id.llSelectContact).setOnClickListener(this);
        rechargeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        etPhone = findViewById(R.id.etPhone);
        initData();
        setInputListener();
        KeyboardHelper.with(RechargeActivity.this).setDisable(SOFT_INPUT_STATE_UNCHANGED);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("话费充值");
    }


    private void setInputListener() {
        etPhone.addTextChangedListener(new MoneyTextWatcher(etPhone) {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                //如果长度不等于0
                if (s.length() != 0) {
                    for (RechargeEntity datum : mRechargeAmountAdapter.getData()) {
                        datum.selected = false;
                    }
                    mRechargeAmountAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void initData() {
        mRechargeEntityList.add(new RechargeEntity(100, true));
        mRechargeEntityList.add(new RechargeEntity(200));
        mRechargeEntityList.add(new RechargeEntity(300));
        mRechargeEntityList.add(new RechargeEntity(500));
        mRechargeEntityList.add(new RechargeEntity(800));
        mRechargeEntityList.add(new RechargeEntity(1000));
        mRechargeAmountAdapter = new RechargeAmountAdapter(mRechargeEntityList);
        mRechargeAmountAdapter.bindToRecyclerView(rechargeRecyclerView);
        mRechargeAmountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setSelect(position);
                adapter.notifyDataSetChanged();
                etPhone.setText("");
            }
        });
    }


    /**
     * 设置选中属性
     *
     * @param position
     */
    private void setSelect(int position) {
        if (position >= mRechargeEntityList.size()) {
            return;
        }
        RechargeEntity rechargeEntity;
        for (int i = 0; i < mRechargeEntityList.size(); i++) {
            rechargeEntity = mRechargeEntityList.get(i);
            if (i == position) {
                rechargeEntity.selected = true;
            } else {
                rechargeEntity.selected = false;
            }
        }
    }


    private void skipContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_FOR_SINGLE_CUNTRACT_ONE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_SINGLE_CUNTRACT_ONE) {
            if (data != null) {
                Uri uri = data.getData();
                String[] contact = getPhoneContacts(uri);
                if (contact != null) {
                    String name_one = contact[0];//姓名
                    String number_one = contact[1];//手机号
                    etPhone.setText(name_one+"---"+number_one);
                }
            }
        }
    }


    /**
     * 读取联系人信息
     *
     * @param uri
     * @return
     */
    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            contact[1] = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    .replaceAll("\\+86", "").replaceAll(" ", "");
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSelectContact:
                skipContact();
                break;
            default:
                break;
        }
    }
}
