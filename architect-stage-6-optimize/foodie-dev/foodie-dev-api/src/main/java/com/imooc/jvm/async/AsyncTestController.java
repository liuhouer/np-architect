package com.imooc.jvm.async;

import com.imooc.pojo.bo.AddressBO;
import com.imooc.utils.IMOOCJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@RestController
public class AsyncTestController {
    public static final Logger LOGGER = LoggerFactory.getLogger(AsyncTestController.class);
    @Autowired
    private ASyncDemoAsyncAnnotation aSyncDemoAsyncAnnotation;

    @GetMapping("/async-test")
    public String asyncTest() throws InterruptedException, ExecutionException {
        aSyncDemoAsyncAnnotation.biz();
        return "success";
    }

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @GetMapping("/test-async-rest-template")
    public String testAsyncRestTemplate() throws ExecutionException, InterruptedException {
        ListenableFuture<ResponseEntity<IMOOCJSONResult>> future = this.asyncRestTemplate.getForEntity(
            "http://localhost:8088/index/subCat/{rootCatId}",
            IMOOCJSONResult.class,
            1
        );
//        ResponseEntity<IMOOCJSONResult> entity = future.get();
//        IMOOCJSONResult body = entity.getBody();

        future.addCallback(new ListenableFutureCallback<ResponseEntity<IMOOCJSONResult>>() {
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("请求失败", ex);
            }

            @Override
            public void onSuccess(ResponseEntity<IMOOCJSONResult> result) {
                LOGGER.info("调用成功,body = {}", result.getBody().getData());
            }
        });
        return "success";
    }


    @GetMapping("/test-async-rest-template-post")
    public IMOOCJSONResult testAsyncRestTemplatePost() throws ExecutionException, InterruptedException {
        AddressBO addressBO = new AddressBO();
        addressBO.setAddressId("111");
        addressBO.setUserId("111");
        addressBO.setReceiver("111");
        addressBO.setMobile("15151816012");
        addressBO.setProvince("aaa");
        addressBO.setDistrict("aaa");
        addressBO.setDetail("aaa");
        addressBO.setCity("nj");

        ListenableFuture<ResponseEntity<IMOOCJSONResult>> future =
            this.asyncRestTemplate.postForEntity(
                "http://localhost:8088/address/update",
                new HttpEntity<>(addressBO),
                IMOOCJSONResult.class
            );
        ResponseEntity<IMOOCJSONResult> entity = future.get();
        return entity.getBody();
    }

    @Autowired
    private WebClient webClient;

    @GetMapping("/test-web-client")
    public IMOOCJSONResult testWebClient() {
        Mono<IMOOCJSONResult> mono = this.webClient.get()
            .uri("http://localhost:8088/index/subCat/{rootCatId}", 1)
            .retrieve()
            .bodyToMono(IMOOCJSONResult.class);

        return mono.block();
    }


    @GetMapping("/test-web-client-post")
    public IMOOCJSONResult testWebClientPost() throws ExecutionException, InterruptedException {
        AddressBO addressBO = new AddressBO();
        addressBO.setAddressId("111");
        addressBO.setUserId("111");
        addressBO.setReceiver("111");
        addressBO.setMobile("15151816012");
        addressBO.setProvince("aaa");
        addressBO.setDistrict("aaa");
        addressBO.setDetail("aaa");
        addressBO.setCity("nj");

        return this.webClient.post()
            .uri("http://localhost:8088/address/update")
            // 指定发送请求时候的content-type 这个header的值
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(addressBO))
            .retrieve()
            .bodyToMono(IMOOCJSONResult.class)
            .block();
    }

    @Autowired
    private AsyncJob asyncJob;

    @GetMapping("test-async")
    public String testAsync() throws InterruptedException {
        this.asyncJob.test();
        return "success";
    }
}
