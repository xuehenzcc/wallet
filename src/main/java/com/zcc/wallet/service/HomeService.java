package com.zcc.wallet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcc.wallet.mapper.HomeMapper;
import com.zcc.wallet.util.DateUtil;
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
	@Autowired
	private ImageService iMageService;
	
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
		List<Shop> shopList = homeMapper.getShopList(pos);
		for (Shop shop:shopList) {
			String image=shop.getImage();
			String content=shop.getContent();
			if(null!=image){
				System.out.println(image);
				List<String> images=iMageService.convertImageUrlHtml2(image);
				System.out.println(images.size());
				shop.setImages(images);
			}
			if(null!=content){
				System.out.println(content);
				String newcontent = iMageService.convertImageUrlHtml(content);
				System.out.println(newcontent);
				shop.setContent(newcontent);
			}
		}
		return shopList;
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
	@Transactional
	public void getCompPos(){
		Pos pos=new Pos();
		pos.setStatus("0");//未激活
		pos.setType("3");//小POS
//		Map<String,List<BusinessData>> sns=new HashMap<String,List<BusinessData>>();
		List<Pos> activePosList=new ArrayList<Pos>();
		List<Pos> posList=homeMapper.getPosList(pos);
		for (Pos p:posList) {
			String sn = p.getSn();
			BigDecimal compAmt=p.getCompAmt();
			BigDecimal totalAmt=BigDecimal.ZERO;
			BusinessData data=new BusinessData();
			data.setSn(sn);
			List<BusinessData> businessList=homeMapper.getBusinessDataList(data);
			if(businessList!=null && businessList.size()>0){
				for (int i = 0; i < businessList.size(); i++) {
					data=businessList.get(i);
					BigDecimal amt=data.getAmt();
					totalAmt = totalAmt.add(amt);
				}
				if(compAmt.compareTo(totalAmt)<=0){//满足条件
					p.setStatus("1");
					p.setActiveTime(new Date());
					activePosList.add(p);//要激活的POS
				}
			}
		}
		//获取未返现(大POS,智能POS)
		List<Pos> POSList=new ArrayList<Pos>();
		Pos bigPos=new Pos();
		bigPos.setReach(0);
		List<Pos> bigPosList=homeMapper.getPosList(bigPos);
		for (int i = 0; i < bigPosList.size(); i++) {
			Pos p=bigPosList.get(i);
			if("3".equals(bigPosList.get(i).getType())){
				continue;
			}
			POSList.add(bigPosList.get(i));
			Date activeTime=p.getActiveTime();
			int limit=p.getLimitnum();
			Date endDate=DateUtil.addMonth(activeTime, limit);
			String sn=p.getSn();
			BigDecimal compAmt=p.getCompAmt();//达标金额
			BigDecimal totalAmt=BigDecimal.ZERO;
			
			BusinessData data=new BusinessData();
			data.setSn(sn);
			List<BusinessData> businessList=homeMapper.getBusinessDataList(data);
			if(businessList!=null && businessList.size()>0){
				for (int j = 0; j < businessList.size(); j++) {
					data=businessList.get(j);
					BigDecimal amt=data.getAmt();
					totalAmt = totalAmt.add(amt);
				}
				if(compAmt.compareTo(totalAmt)<=0){//满足条件
					p.setReach(1);
					activePosList.add(p);//要激活的POS
				}else{//不满足
					if(endDate.getTime()<new Date().getTime()){//已经超过期限了
						p.setReach(2);//未达标
					}
				}
			}
		}
		//更新数据
		if(activePosList.size()>0){
			updateReachData(activePosList);
		}
	}
	
	//创建分润记录（待开发：余额变动）
	@Transactional
	public void updateReachData(List<Pos> activePosList){
		List<Account> accountList=new ArrayList<Account>();
		for (int i = 0; i < activePosList.size(); i++) {
			Pos pos=activePosList.get(i);
			String type=pos.getType();
			Account account=new Account();
			account.setApp("2");
			account.setAmount(pos.getAmt());
			account.setPosType(pos.getType());
			account.setUserId(pos.getUserId());
			account.setUserName("退还保证金");
			account.setSn(pos.getSn());
			accountList.add(account);
			if("3".equals(type)){//小POS（返激活保证金+返现）
				Account acc=new Account();
				acc.setApp("2");
				acc.setAmount(pos.getBackAmt());//小POS激活返现
				acc.setPosType(pos.getType());
				acc.setUserId(pos.getUserId());
				acc.setUserName("激活返现");
				acc.setSn(pos.getSn());
				accountList.add(acc);
			}
		}
		homeMapper.addAccountList(accountList);
		homeMapper.updatePosList(activePosList);
	}
	
}
