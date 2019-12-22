package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.Result;
import com.taotao.pojo.order.Order;
import com.taotao.pojo.user.Address;
import com.taotao.service.order.CartService;
import com.taotao.service.order.OrderService;
import com.taotao.service.user.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference
    private CartService cartService;
    @Reference
    private AddressService addressService;
    @Reference
    private OrderService orderService;
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

    @GetMapping("/findNewOrderItemList")
    public List<Map<String,Object>> findNewOrderItemList(){
        return cartService.findNewOrderItemList(this.getUserName());
    }

    @GetMapping("/findAddressByUsername")
    public List<Address> findAddressByUsername(){
        return addressService.findAddressByUserName(this.getUserName());
    }

    @PostMapping("/saveOrder")
    public Map<String,Object> saveOrder(@RequestBody Order order){
        order.setUsername(this.getUserName());
        return orderService.add(order);
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
