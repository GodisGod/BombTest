package com.example.bombtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.bombtest.R;
import com.example.bombtest.constant.Constant;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class ChooseScrip extends AppCompatActivity implements View.OnClickListener {
    private Context ctx;
    private ImageView choose1;
    private ImageView choose2;
    private ImageView choose3;
    private Intent intent;
    private String img_url;
    private String text;
    private String objectId;
    private String userId;
    private String userName;
    private String userIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_scrip);
        ctx = this;
        initView();
        intent = getIntent();
        img_url = intent.getStringExtra("imgurl");
        text = intent.getStringExtra("text");
        objectId = intent.getStringExtra("objectid");
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userIcon = intent.getStringExtra("userIcon");
    }

    private void initView() {
        choose1 = (ImageView) findViewById(R.id.scrip1);
        choose2 = (ImageView) findViewById(R.id.scrip2);
        choose3 = (ImageView) findViewById(R.id.scrip3);

        choose1.setOnClickListener(this);
        choose2.setOnClickListener(this);
        choose3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ctx, Scrip.class);
        intent.putExtra("imgurl", img_url);
        intent.putExtra("text", text);
        intent.putExtra("objectid", objectId);
        intent.putExtra("userId", userId);
        intent.putExtra("userIcon", userIcon);
        intent.putExtra("userName", userName);
        startActivity(intent);
        finish();
//        //todo 开启私聊页面
//        if (RongIM.getInstance() != null) {
//            HD.LOG("开启私聊页面");
//            RongIM.getInstance().startPrivateChat(ctx, userId, userName);
//            //todo 接收预置消息
////            getMessages();
//        }

    }

    //接收捡到的纸片的文本、语音、图片消息,这里将获取到的纸片信息发给自己
    private void getMessages() {
        // 构造 TextMessage 实例
        TextMessage myTextMessage = TextMessage.obtain(text);

/* 生成 Message 对象。
 * "7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
 * Conversation.ConversationType.PRIVATE 为私聊会话类型，根据需要，也可以传入其它会话类型，如群组，讨论组等。
 */
        Message myMessage = Message.obtain(Constant.userId, Conversation.ConversationType.PRIVATE, myTextMessage);

/**
 * <p>发送消息。
 * 通过 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}
 * 中的方法回调发送的消息状态及消息体。</p>
 *
 * @param message     将要发送的消息体。
 * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
 *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
 *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
 * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
 * @param callback    发送消息的回调，参考 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}。
 */
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });
    }
}
