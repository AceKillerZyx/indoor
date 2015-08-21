/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indoorsy.frash.easemob.helper;

import java.util.Map;

import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.indoorsy.frash.R;
import com.indoorsy.frash.easemob.activity.ChatActivity;
import com.indoorsy.frash.easemob.domain.User;
import com.indoorsy.frash.easemob.model.HXSDKModel;
import com.indoorsy.frash.easemob.model.IndoorsyHXSDKModel;
import com.indoorsy.frash.easemob.util.CommonUtils;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * @author easemob
 *
 */
public class IndoorsyHXSDKHelper extends HXSDKHelper{

    /**
     * contact list in cache
     */
    private Map<String, User> contactList;
    
    @Override
    protected void initHXOptions(){
        super.initHXOptions();
        // you can also get EMChatOptions to set related SDK options
        // EMChatOptions options = EMChatManager.getInstance().getChatOptions();
    }

    @Override
    protected OnMessageNotifyListener getMessageNotifyListener(){
        // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
      return new OnMessageNotifyListener() {

          @Override
          public String onNewMessageNotify(EMMessage message) {
              // 设置状态栏的消息提示，可以根据message的类型做相应提示
              String ticker = CommonUtils.getMessageDigest(message, appContext);
              if(message.getType() == Type.TXT)
                  ticker = ticker.replaceAll("\\[.{2,3}\\]", "["+R.string.attach_smile+"]");
              return message.getFrom() + ": " + ticker;
          }

          @Override
          public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
              return null;
             // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
          }

          @Override
          public String onSetNotificationTitle(EMMessage message) {
              //修改标题,这里使用默认
              return null;
          }

          @Override
          public int onSetSmallIcon(EMMessage message) {
              //设置小图标
              return 0;
          }
      };
    }
    
    @Override
    protected OnNotificationClickListener getNotificationClickListener(){
        return new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(appContext, ChatActivity.class);
                ChatType chatType = message.getChatType();
                if (chatType == ChatType.Chat) { // 单聊信息
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                } else { // 群聊信息
                            // message.getTo()为群聊id
                    intent.putExtra("groupId", message.getTo());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                }
                return intent;
            }
        };
    }
    
    
    
    @Override
    protected void initListener(){
        super.initListener();
        @SuppressWarnings("unused")
		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingVoiceCallBroadcastAction());
//        appContext.registerReceiver(new VoiceCallReceiver(), callFilter);    
    }

    @Override
    protected HXSDKModel createModel() {
        return new IndoorsyHXSDKModel(appContext);
    }
    
    /**
     * get demo HX SDK Model
     */
    public IndoorsyHXSDKModel getModel(){
        return (IndoorsyHXSDKModel) hxModel;
    }
    
    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        if (getHXId() != null && contactList == null) {
            contactList = ((IndoorsyHXSDKModel) getModel()).getContactList();
        }
        
        return contactList;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }
    
    @Override
    public void logout(final EMCallBack callback){
        super.logout(new EMCallBack(){

            @Override
            public void onSuccess() {
                setContactList(null);
                getModel().closeDB();
                if(callback != null){
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String message) {
                
            }

            @Override
            public void onProgress(int progress, String status) {
                if(callback != null){
                    callback.onProgress(progress, status);
                }
            }
            
        });
    }
}
