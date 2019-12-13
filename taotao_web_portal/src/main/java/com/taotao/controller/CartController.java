package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.Result;
import com.taotao.service.order.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference
    private CartService cartService;


    @GetMapping("/findCartList")
    public List<Map<String, Object>> findCartList() {
        return cartService.findCartList(this.getUserName());
    }

    @GetMapping("/addItem")
    public Result addItem(String skuId, Integer num) {
        cartService.addItem(this.getUserName(), skuId, num);
        return new Result();
    }

    @RequestMapping("/buy")
    public void buy(HttpServletResponse response, String skuId, Integer num) throws Exception {
        cartService.addItem(this.getUserName(), skuId, num);
        response.sendRedirect("/cart.html");
    }

    @GetMapping("/updateChecked")
    public Result updateChecked(String skuId, boolean checked) {
        cartService.updateChecked(this.getUserName(), skuId, checked);
        return new Result();
    }

    @GetMapping("/checkedAll")
    public boolean checkedAll() {
        return cartService.checkedAll(this.getUserName());
    }

    @GetMapping("/deleteCheckedGoods")
    public Result deleteCheckedGoods() {
        cartService.deleteCheckedGoods(this.getUserName());
        return new Result();
    }

    @GetMapping("/preferential")
    public int preferential() {
        return cartService.preferential(this.getUserName());
    }

    /**
     * 获取当前登录用户名
     *
     * @return
     */
    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
