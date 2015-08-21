package com.indoorsy.frash;

/**
 * @author gll
 * 公共属性类
 */
public class Constants {
	
	/**
	 * 百度定位
	 */
	public static final String BAIDU_AK = "MhKbGxpGDB8zCoevvGTfVBqz"; //百度定位AK （测试）
	
	public static final String ANDROID_SHA1 = "CD:C9:2B:C5:CC:C6:4A:51:E4:69:D7:8C:4E:B2:38:CA:37:33:88:17"; //SHA1
	
	public static final String PACKAGE_NAME = "com.indoorsy.frash"; //程序包名
	
	public static final String LAT = "latitude"; //定位纬度
	
	public static final String LNG = "longitude"; //定位经度
	
	/**
	 * HOMEPAGE
	 */
	public static final String HOMEPAGE_PRODUCT_KEYWORDS = "keywords";
	
	public static final String HOMEPAGE_PRODUCT_DETAIL_GOODSID = "goodsid";
	
	public static final String HOMEPAGE_SECKILL_COUNTDOWN_TIME = "homepage_seckill_countdown_time";
	
	public static final String HOMEPAGE_RECIPE_SHARE_ID = "homepage_recipe_share_id";

	
	public static final String HOMEPAGE_PRODUCT_NO_COLLECTION = "collectgoodid";//取消收藏商品id
	
	public static final String HOMEPAGE_PRODUCT_NUM = "number";//购买商品数量 默认是1
	
	public static final String HOMEPAGE_PRODUCT_PRICE = "price";//购买商品单价
	
	public static final String HOMEPAGE_PRODUCT_DELID = "delid";//配送id
	
	public static final String HOMEPAGE_PRODUCT_UNIT = "unit";//购买商品单位
	
	public static final String HOMEPAGE_PRODUCT_COMMENT_LIST = "product_comment_list";//传递评论列表
	
	public static final String HOMEPAGE_PRODUCT_TYPE_SECKILL = "homepage_product_type_seckill";//秒杀价
	
	public static final String TYPE_SECKILL = "type_seckill";//秒杀价
	
	public static final String HOMEPAGE_PRODUCT_ID = "goodsid";//商品id
	
	public static final String HOMEPAGE_PRODUCT_NAME = "homepage_product_name";
	
	public static final String HOMEPAGE_PRODUCT_DESC = "homepage_product_desc";  //商品描述
	
	public static final String HOMEPAGE_PRODUCT_THUMB = "homepage_product_thumb"; //商品图片
	
	public static final String HOMEPAGE_PRODUCT_SIZE = "homepage_product_size"; //鱼类规格
	
//	public static final String HOMEPAGE_PRODUCT_HE = "homepage_product_thumb"; //盒装类规格
	
//	public static final String HOMEPAGE_PRODUCT = "homepage_product";//商品实体
	
//	public static final String HOMEPAGE_RECIPE = "homepage_recipe";//食谱实体
	
	public static final String HOMEPAGE_RECIPE_COMMENT_LIST = "recipe_comment_list";//食谱传递评论列表
	
	public static final String HOMEPAGE_RECIPE_RID = "rid";//食谱id
	
	public static final String HOMEPAGE_RECIPE_RECIPEID = "recipeid";//食谱收藏id
	
	public static final String HOMEPAGE_RECIPE_CORECIPEID = "corecipeid";//食谱取消收藏id
	
	public static final String HOMEPAGE_RECIPE_COMMCONTENT = "commcontent";//食谱添加评论
	
	public static final String HOMEPAGE_RECIPE_WHZRECIPEID = "whzrecipeid";//食谱添加评论食谱id
	
	public static final String HOMEPAGE_RECIPE_WHZUSERID = "whzuserid";//食谱添加评论用户id

	public static final String HOMEPAGE_TYPE = "homepage_type";//类别点击进入商品列表也的区别

	public static final String TYPEID = "typeid";//类别ID

	public static final String TYPE = "type";//个人到我的订单的类别

	public static final String ADVID = "advId";//首页广告ID

	public static final String HOMEPAGEADVID = "advid";//首页广告查询用到ID
	

	/**
	 * 下订单
	 * 
	 */
	public static final String COMMODITY_ORDERTOTAL = "ordertotal";//总计
	
	public static final String COMMODITY_ORDERAMOUNT = "orderamount";//支付金额

	public static final String COMMODITY_ADDID = "addid";//收货地址id
	
	public static final String COMMODITY_GOODSID = "goodsid";//商品id
	
	public static final String COMMODITY_ORDERNUMBER = "ordernumber";//购买数量
	
	public static final String COMMODITY_ORDERGOODSAMOUNT = "ordergoodsamount";//商品单价
	
	public static final String COMMODITY_UNIT = "unit";//商品单位
	
	public static final String COMMODITY_ORDERDELID = "orderdelid";//配送方式id
	
	public static final String COMMODITY_ORDERSHIPPINGFEE = "ordershippingfee";//邮费
	
	public static final String COMMODITY_ORDERBILL = "orderbill";//是否发票 1是要
	
	public static final String COMMODITY_INTEGRA = "integra";//使用积分
	
	public static final String COMMODITY_INTEGRAMONEY = "integramoney";//使用积分抵现
	
	public static final String COMMODITY_ORDERCONVERSION = "OrderConversion";//是否使用积分
	
	public static final String COMMODITY_FISH_SIZE = "fish_size";//鱼类特有 规格 ：1-2斤
	
	public static final String COMMODITY_ORDERRECEIVINGTIME = "orderreceivingtime";//收货时间
	
	public static final String COMMODITY_ORDERPOSTSCRIPT = "orderpostscript";//给卖家留言

	public static final String COMMODITY_AMOUNT = "amount";//转到第三方支付时-付款金额

	public static final String ORDERNUMBER = "orderno";//订单编号
	
	public static final String ORDERNUMBER_SN = "ordersn";//订单编号
	
	public static final String ORDERSTATE = "orderstate";//我的订单传参
	
	public static final String SHIPPINGSTATE = "shippingstate";//我的订单传参
	
	public static final String PAYSTATE = "paystate";//我的订单传参

	public static final String ORDERID = "orderid";//订单ID
	
	public static final String COMMODITY_LISTORDER = "listorder";//多个商品下订单	
	
	
	
	//--------------购物车------------------------
	
	public static final String CART_ID = "cartid";//购物车id
	
	public static final String CART_GOODSLSIT = "cart_goodslsit";//购物车多件商品的集合
	
	public static final String CART_TYPE_LIST_PRICE = "cart_type_list_price";//购物车多件商品合计价格
	
	public static final String CART_TYPE_LIST = "cart_type_list";//类型——购物车多件商品传递
	
	
	
	
	//--------------个人模块的------------------------
	//第三方登录
	public static final String OPENID = "openid";
	public static final String USERIMAGES = "userImages";
	public static final String PLATFORM = "platform";
	//注册
	public static final String TEL = "tel";
	public static final String PWD = "pwd";
	public static final String PHONE = "phone";
	public static final String CODE = "code";
	public static final String USERCODE = "usercode";
	//短信验证
	public static final String SMS_APPKEY = "77ff83b3a6b0";
	public static final String SMS_APPSECRET = "d372567fa7dfd6fb5b1727de42f8a97f";
	//用户id
	public static final String ULOGINID = "uloginid";
	//修改密码
	public static final String NEWPWD ="newpwd"; 
	//获取到登录用户的Id
	public static final String USERID = "loginId";
	public static final String LOGINID = "loginId";
	//头像
	public static final String IMAGE = "image";
	//退出登录
	public static final String EXIT_LOGIN = "exit_login";
	
	//修改个人资料用到的
	public static final String USID = "usid";
	public static final String SEX = "sex";
	public static final String USERSEX = "userSex";
	public static final String UNAME = "uname";
	public static final String AGE = "age";
	public static final String USERAGE = "userAge";
	public static final String USERNAMES = "userNames";
	public static final String PATH = "path";
	public static final String USERINVITATION = "userinvitation";

	//发表评论
	public static final String EVACONTENT = "evaContent";
	public static final String EVALOGISTICS = "evaLogistics";
	public static final String EVAQUALITY = "evaQuality";
	public static final String EVASERVE = "evaServe";
	public static final String EVASHIPMENTS = "evaShipments";
	public static final String LISTORDER = "listorder";
	public static final String IMAGES = "images";
	
	//修改收货地址
	public static final String ADDRESS = "address";//从订单跳转修改收货
	
	//删除收货地址
	public static final String ADDERSSID = "adderssid";
	
	//收货地址信息
	public static final String UPDATE_ID = "addAdderssid";
	public static final String UPDATE_ADDRESS = "addAdderss";
	public static final String UPDATE_NAME = "addConsignee";
	public static final String UPDATE_TEL = "addTel";
	
	// 地址实体
	public static final String PERSONAL_ADDRESS = "personal_address";
	public static final String PERSONAL_CHOOSE_ADDRESS = "personal_choose_address";
	
	
	
	//积分与等级
	public static final String INTEGRAL = "integer";
	public static final String CLASS = "lv";
	//-----------------------------------------

	
	public static final String OPINIONTEL = "opinionTel";  //意见反馈手机号
	public static final String OPINIONCONETNT = "opinionConetnt";  //意见反馈内容
	
	public static final String ID = "loginId";
	
	public static final String  ANDROIDURL = "VAdderss";  // ApkUrl
	
	public static final String  VERSIONNUMBER = "VNumber";  // 版本号
	
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	//环信客服
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CUSTOMERKEY = "802015";  //服务账号
	public static final String CUSTOMERAPPKEY = "whz2015#indoorsy";  //APPKEY

	public static final String UPDATE_MESSAGE = "update_message";  //更新聊天记录
}
