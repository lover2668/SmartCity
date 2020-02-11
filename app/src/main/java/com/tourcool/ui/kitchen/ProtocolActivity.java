package com.tourcool.ui.kitchen;

import android.os.Bundle;
import android.widget.TextView;

import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.core.widget.HtmlWebView;
import com.tourcool.smartcity.R;
import com.tourcool.ui.base.BaseCommonTitleActivity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月11日9:47
 * @Email: 971613168@qq.com
 */
public class ProtocolActivity extends BaseCommonTitleActivity {
    private TextView htmlWebView;
    @Override
    public int getContentLayout() {
        return R.layout.activity_webview_protocol_layout;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("用户服务协议");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        htmlWebView = findViewById(R.id.htmlWebView);
        String content = "一、服务条款确认\n" +
                "用户应当同意本协议的条款并按照页面上的提示完成全部的注册程序。用户在进行注册程序过程中点击\"同意\"按钮即表示用户与本单位达成协议，完全接受本协议项下的全部条款。\n" +
                "二、服务条款修改\n" +
                "本系统在必要时可修改服务条款，并在系统上进行更新，一经发布，立即生效。如您继续使用服务，则视为您已接受修订的服务条款。\n" +
                "三、用户注册\n" +
                "考虑到本系统用户服务的重要性，您同意在注册时提供真实、完整及准确的个人资料，并及时更新。盗用他人身份信息注册本系统所引发起的一切不当后果与本系统无关，本系统不承担因此造成的任何法律责任。以此身份信息办理的一切事务与本系统无关，本系统不承担因此造成的任何法律责任。\n" +
                "用户注册成功后，本系统将给予每个用户一个用户帐号及相应的密码，该用户帐号和密码由用户负责保管；用户应当对以其用户帐号进行的所有活动和事件负法律责任。\n" +
                "本系统有权对您提供的资料进行核验，核验结果仅适用于您在本系统办理注册以及查询本人相关信息业务；如您提供的资料不准确，或无法通过本系统核验，或本系统有合理的理由认为该资料不真实、不完整、不准确，本系统有权暂停或终止您的注册身份及资料，并拒绝您使用本系统的服务。\n" +
                "四、用户资料及保密\n" +
                "注册时，请您按页面提示提交相关信息。您负有对用户名和密码保密的义务，并对该用户名和密码下发生的所有活动承担责任。\n" +
                "实名认证就等于授权智慧宜兴显示个人的相关信息，但是个人信息只有登录后个人才能看到。如果不想让智慧宜兴显示个人信息，请自行选择是否使用智慧宜兴。\n" +
                "为更为有效地向您提供服务，您同意，本单位有权将您注册及使用本服务过程中所提供、形成的信息提供给本系统对接的相关政府部门或公用事业单位等合作单位，本系统不会向您所使用服务所涉及合作单位之外的其他方公开或透露您的个人资料，法律法规规定除外。\n" +
                "五、责任范围及责任限制\n" +
                "为本系统提供服务的相关政府或公共事业单位等合作单位，所提供之服务品质及内容由该合作单位自行负责，本系统不保证该信息之准确、及时和完整。\n" +
                "六、\n外部链接本系统含有到其他网站的链接，但本系统对其他网站的隐私保护措施不负任何责任。本系统可能在任何需要的时候增加商业伙伴或共用品牌的网站。\n" +
                "七、保障\n" +
                "您同意保障和维护本系统的利益，并承担您或其他人使用您的用户资料造成本系统或任何第三方的损失或损害的赔偿责任。";
        loadRichText(content);
    }

    private void loadRichText(String richText){
        htmlWebView.setText(richText);
    }
     /*   htmlWebView.imageClick((imageUrls, position) -> {
            //imageUrls是所有图片地址的list
            ///position是点击位置
        })
                .urlClick(url -> {
                    //url为链接跳转地址
                    //返回true为自己处理跳转，false为webview自行跳转
                    return false;
                })
                .imageLongClick(imageUrl->{
                    //imageUrl为长按图片的地址
                })
                .setHtml(richText);//html富文本的字符串
    }*/
}
