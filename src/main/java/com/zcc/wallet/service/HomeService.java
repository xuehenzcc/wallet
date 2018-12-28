package com.zcc.wallet.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcc.wallet.mapper.HomeMapper;
import com.zcc.wallet.vo.Account;
import com.zcc.wallet.vo.Adress;
import com.zcc.wallet.vo.BusinessData;
import com.zcc.wallet.vo.Pos;
import com.zcc.wallet.vo.PosLog;
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
	
	@Transactional
	public int updatePos(Pos pos){
		PosLog data=new PosLog();
		data.setSn(pos.getSn());
		data.setStatus("0");
		if("1".equals(pos.getAction())){//下发
			data.setUserId(pos.getActiveUserId());
			homeMapper.addPosLog(data);
		}else if("3".equals(pos.getAction())){//同意召回
			Pos p=new Pos();
			p.setSn(pos.getSn());
			List<Pos> ps=homeMapper.getPosList(p);
			if(ps.size()>0){
				data.setUserId(ps.get(0).getUserId());
				homeMapper.addPosLog(data);
			}else{
				return 0;
			}
		}
		return homeMapper.updatePos(pos);
	}
	public List<BusinessData> getBusinessDataList(BusinessData data){
		return homeMapper.getBusinessDataList(data);
	}
	public List<Account> getImcomeList(Account data){
		return homeMapper.getImcomeList(data);
	}
	public Integer addPosLog(PosLog data){
		return homeMapper.addPosLog(data);
	}
	//计算pos是否达到刷满条件 
	public void getCompPos(){
		Pos pos=new Pos();
		pos.setStatus("0");//未激活
		Set<String> sns=new HashSet<String>();
		List<Pos> posList=homeMapper.getPosList(pos);
		for (Pos p:posList) {
			String sn = p.getSn();
			sns.add(sn);
			BusinessData data=new BusinessData();
			data.setSn(sn);
			homeMapper.getBusinessDataList(data);
		}
		
	}
	
}
