package com.zcc.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcc.wallet.mapper.HomeMapper;
import com.zcc.wallet.vo.Adress;
import com.zcc.wallet.vo.Pos;
import com.zcc.wallet.vo.Shop;
import com.zcc.wallet.vo.ShopOrder;

@Service
public class HomeService {

	@Autowired
	private HomeMapper homeMapper;

	
	//添加地址
	public Integer addAddress(Adress address){
		return homeMapper.addAddress(address);
	}
	public Integer deleteAddress(Adress address){
		return homeMapper.deleteAddress(address);
	}
	public Integer updateAddress(Adress address){
		return homeMapper.updateAddress(address);
	}
	public Integer updateAddressByMode(Adress address){
		return homeMapper.updateAddressByMode(address);
	}
	public List<Adress> getAddress(Adress address){
		return homeMapper.getAddress(address);
	}
	public List<Shop> getShopList(Shop pos){
		return homeMapper.getShopList(pos);
	}
	public List<ShopOrder> getShopOrderList(ShopOrder order){
		return homeMapper.getShopOrderList(order);
	}
	public Integer addShopOrder(ShopOrder order){
		return homeMapper.addShopOrder(order);
	}
	public Integer updateShopOrder(ShopOrder order){
		return homeMapper.updateShopOrder(order);
	}
	public List<Pos> getPosList(Pos pos){
		return homeMapper.getPosList(pos);
	}
	public int updatePos(Pos pos){
		return homeMapper.updatePos(pos);
	}
}
