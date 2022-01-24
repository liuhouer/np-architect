package com.example.tccdemo.service;

import com.example.tccdemo.db131.dao.PaymentMsgMapper;
import com.example.tccdemo.db131.model.PaymentMsg;
import com.example.tccdemo.db131.model.PaymentMsgExample;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderScheduler {
    @Resource
    private PaymentMsgMapper paymentMsgMapper;

    @Scheduled(cron = "0/10 * * * * ?")
    public void orderNotify() throws IOException {

        PaymentMsgExample paymentMsgExample = new PaymentMsgExample();
        paymentMsgExample.createCriteria().andStatusEqualTo(0);//未发送
        List<PaymentMsg> paymentMsgs = paymentMsgMapper.selectByExample(paymentMsgExample);
        if (paymentMsgs==null || paymentMsgs.size() ==0) return;

        for (PaymentMsg paymentMsg : paymentMsgs) {
            int order = paymentMsg.getOrderId();

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("http://localhost:8080/handleOrder");
            NameValuePair orderIdPair = new BasicNameValuePair("orderId",order+"");
            List<NameValuePair> list = new ArrayList<>();
            list.add(orderIdPair);
            HttpEntity httpEntity = new UrlEncodedFormEntity(list);
            httpPost.setEntity(httpEntity);


            CloseableHttpResponse response = httpClient.execute(httpPost);
            String s = EntityUtils.toString(response.getEntity());

            if ("success".equals(s)){
                paymentMsg.setStatus(1);//发送成功
                paymentMsg.setUpdateTime(new Date());
                paymentMsg.setUpdateUser(0);//系统更新
                paymentMsgMapper.updateByPrimaryKey(paymentMsg);
            }else {
                Integer falureCnt = paymentMsg.getFalureCnt();
                falureCnt++;
                paymentMsg.setFalureCnt(falureCnt);
                if (falureCnt > 5){
                    paymentMsg.setStatus(2);//失败
                }
                paymentMsg.setUpdateTime(new Date());
                paymentMsg.setUpdateUser(0);//系统更新
                paymentMsgMapper.updateByPrimaryKey(paymentMsg);
            }
        }
    }

}
