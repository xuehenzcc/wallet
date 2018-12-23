/**
 * 版权：zcc
 * 作者：c0z00k8
 * @data 2018年8月17日
 */
package com.zcc.wallet.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zcc.wallet.vo.Adress;
import com.zcc.wallet.vo.Pos;
import com.zcc.wallet.vo.Shop;
import com.zcc.wallet.vo.ShopOrder;

/**
 * @author c0z00k8
 *
 */
@Component
public interface HomeMapper {
	
	//添加地址
	public int addAddress(Adress address);
	public int deleteAddress(Adress address);//删除地址
	public int updateAddress(Adress address);//修改地址
	public int updateAddressByMode(Adress address);//设置默认地址
	public List<Adress> getAddress(Adress address);//获取地址
	
	//获取shop
	public List<Shop> getShopList(Shop pos);
	//获取订单列表
	public List<ShopOrder> getShopOrderList(ShopOrder order);
	public int addShopOrder(ShopOrder order);//创建订单
	public int updateShopOrder(ShopOrder order);//更新订单
	
	//获取POS
	public List<Pos> getPosList(Pos pos);
	public int updatePos(Pos pos);
}
