package com.ybb.alipay.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.ybb.alipay.service.AliPayContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/alipay")
@Controller
@ResponseBody
public class AliPayController {

    @Autowired
    private AliPayContentService aliPayContentService;

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) throws Exception {
        System.out.println("支付宝回调 notify");
        String tradeStatus = request.getParameter("trade_status");

        if (tradeStatus.trim().equals("TRADE_SUCCESS")){
            Map<String,String> param = new HashMap<>();

            Map<String, String[]> parameterMap = request.getParameterMap();
            for (String name: parameterMap.keySet()) {
                param.put(name,request.getParameter(name));
            }

            if (Factory.Payment.Common().verifyNotify(param)){
                System.out.println("通过支付宝的验证");

                String out_trade_no = param.get("out_trade_no");
                Long orderId = Long.parseLong(out_trade_no);

                aliPayContentService.pay(orderId);

            }else {
                System.out.println("支付宝验证 不通过！");
            }

        }

        return "success";
    }
    /**
     * @param subject 支付主题
     * @param outTradeNo 交易流水
     * @param totalAmount 总金额
     * @return
     */
    @GetMapping("/pay")
    public String pay(String subject, String outTradeNo, String totalAmount) {
        AlipayTradePagePayResponse response = null;
        try {
            response = Factory.Payment.Page().pay(subject, outTradeNo, totalAmount, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }
}
